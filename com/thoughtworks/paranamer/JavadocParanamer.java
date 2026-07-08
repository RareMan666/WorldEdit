package com.thoughtworks.paranamer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JavadocParanamer implements Paranamer {
   private final JavadocParanamer.JavadocProvider provider;
   public static final String __PARANAMER_DATA = "<init> java.io.File archiveOrDirectory \n<init> java.net.URL url \nurlToStream java.net.URL url \ngetCanonicalName java.lang.Class klass \ngetJavadocFilename java.lang.reflect.Member member \nstreamToString java.io.InputStream input \nlookupParameterNames java.lang.reflect.AccessibleObject accessible \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean accessible,throwExceptionIfMissing \n";

   public JavadocParanamer(File archiveOrDirectory) throws IOException {
      if (!archiveOrDirectory.exists()) {
         throw new FileNotFoundException(archiveOrDirectory.getAbsolutePath());
      } else {
         if (archiveOrDirectory.isDirectory()) {
            this.provider = new JavadocParanamer.DirJavadocProvider(archiveOrDirectory);
         } else {
            if (!archiveOrDirectory.isFile()) {
               throw new IllegalArgumentException("neither file nor directory: " + archiveOrDirectory);
            }

            this.provider = new JavadocParanamer.ZipJavadocProvider(archiveOrDirectory);
         }
      }
   }

   public JavadocParanamer(URL url) throws IOException {
      this.provider = new JavadocParanamer.UrlJavadocProvider(url);
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject accessible) {
      return this.lookupParameterNames(accessible, true);
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject accessible, boolean throwExceptionIfMissing) {
      if (!(accessible instanceof Member)) {
         throw new IllegalArgumentException(accessible.getClass().getCanonicalName());
      } else {
         try {
            String javadocFilename = getJavadocFilename((Member)accessible);
            InputStream stream = this.provider.getRawJavadoc(javadocFilename);
            String raw = streamToString(stream);
            if (accessible instanceof Method) {
               return this.getMethodParameterNames((Method)accessible, raw);
            } else if (accessible instanceof Constructor) {
               return this.getConstructorParameterNames((Constructor<?>)accessible, raw);
            } else {
               throw new IllegalArgumentException(accessible.getClass().getCanonicalName());
            }
         } catch (IOException var6) {
            if (throwExceptionIfMissing) {
               throw new ParameterNamesNotFoundException(accessible.toString(), var6);
            } else {
               return Paranamer.EMPTY_NAMES;
            }
         } catch (ParameterNamesNotFoundException var7) {
            if (throwExceptionIfMissing) {
               throw var7;
            } else {
               return Paranamer.EMPTY_NAMES;
            }
         }
      }
   }

   private String[] getConstructorParameterNames(Constructor<?> cons, String raw) {
      return this.getParameterNames(cons, cons.getDeclaringClass().getSimpleName(), cons.getParameterTypes(), raw);
   }

   private String[] getMethodParameterNames(Method method, String raw) {
      return this.getParameterNames(method, method.getName(), method.getParameterTypes(), raw);
   }

   private String[] getParameterNames(AccessibleObject a, String name, Class<?>[] types, String raw) {
      if (types.length == 0) {
         return new String[0];
      } else {
         StringBuilder regex = new StringBuilder();
         regex.append(String.format(">\\Q%s\\E</A></(?:B|strong)>\\(", name));

         for (Class klass : types) {
            regex.append(String.format(",?\\s*(?:<A[^>]+>)?[\\w.]*\\Q%s\\E(?:</A>)?(?:&lt;[^&]+&gt;)?&nbsp;([^),\\s]+)", klass.getSimpleName()));
         }

         regex.append(String.format("\\)</CODE>"));
         Pattern pattern = Pattern.compile(regex.toString(), 10);
         Matcher matcher = pattern.matcher(raw);
         if (!matcher.find()) {
            throw new ParameterNamesNotFoundException(a + ", " + regex);
         } else {
            String[] names = new String[types.length];

            for (int i = 0; i < names.length; i++) {
               names[i] = matcher.group(1 + i).trim();
            }

            return names;
         }
      }
   }

   protected static String getJavadocFilename(Member member) {
      return getCanonicalName(member.getDeclaringClass()).replace('.', '/') + ".html";
   }

   protected static String getCanonicalName(Class<?> klass) {
      return klass.isArray() ? getCanonicalName(klass.getComponentType()) + "[]" : klass.getName();
   }

   protected static String streamToString(InputStream input) throws IOException {
      InputStreamReader reader = new InputStreamReader(input, "UTF-8");
      BufferedReader buffered = new BufferedReader(reader);

      String var5;
      try {
         StringBuilder builder = new StringBuilder();

         String line;
         while ((line = buffered.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
         }

         var5 = builder.toString();
      } finally {
         buffered.close();
      }

      return var5;
   }

   protected static InputStream urlToStream(URL url) throws IOException {
      URLConnection conn = url.openConnection();
      conn.connect();
      return conn.getInputStream();
   }

   protected static class DirJavadocProvider implements JavadocParanamer.JavadocProvider {
      private final File dir;
      public static final String __PARANAMER_DATA = "<init> java.io.File dir \ngetRawJavadoc java.lang.String fqn \n";

      public DirJavadocProvider(File dir) throws IOException {
         this.dir = dir;
         if (!new File(dir, "package-list").exists()) {
            throw new FileNotFoundException("package-list");
         }
      }

      @Override
      public InputStream getRawJavadoc(String fqn) throws IOException {
         File file = new File(this.dir, fqn);
         return new FileInputStream(file);
      }
   }

   protected interface JavadocProvider {
      String __PARANAMER_DATA = "getRawJavadoc java.lang.String canonicalClassName \n";

      InputStream getRawJavadoc(String var1) throws IOException;
   }

   protected static class UrlJavadocProvider implements JavadocParanamer.JavadocProvider {
      private final URL base;
      public static final String __PARANAMER_DATA = "<init> java.net.URL base \ngetRawJavadoc java.lang.String fqn \n";

      public UrlJavadocProvider(URL base) throws IOException {
         this.base = base;
         JavadocParanamer.streamToString(JavadocParanamer.urlToStream(new URL(base + "/package-list")));
      }

      @Override
      public InputStream getRawJavadoc(String fqn) throws IOException {
         return JavadocParanamer.urlToStream(new URL(this.base + "/" + fqn));
      }
   }

   protected static class ZipJavadocProvider implements JavadocParanamer.JavadocProvider {
      private final ZipFile zip;
      public static final String __PARANAMER_DATA = "<init> java.io.File file \ngetRawJavadoc java.lang.String fqn \n";

      public ZipJavadocProvider(File file) throws IOException {
         this.zip = new ZipFile(file);
         this.find("package-list");
      }

      private ZipEntry find(String postfix) throws FileNotFoundException {
         Enumeration<? extends ZipEntry> entries = this.zip.entries();

         while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(postfix)) {
               return entry;
            }
         }

         throw new FileNotFoundException(postfix);
      }

      @Override
      public InputStream getRawJavadoc(String fqn) throws IOException {
         ZipEntry entry = this.find(fqn);
         return this.zip.getInputStream(entry);
      }
   }
}
