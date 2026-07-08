package com.thoughtworks.paranamer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class BytecodeReadingParanamer implements Paranamer {
   private static final Map<String, String> primitives = new HashMap<String, String>() {
      {
         this.put("int", "I");
         this.put("boolean", "Z");
         this.put("byte", "B");
         this.put("char", "C");
         this.put("short", "S");
         this.put("float", "F");
         this.put("long", "J");
         this.put("double", "D");
      }
   };
   public static final String __PARANAMER_DATA = "lookupParameterNames java.lang.reflect.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \n";

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
      return this.lookupParameterNames(methodOrConstructor, true);
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
      Class<?>[] types = null;
      Class<?> declaringClass = null;
      String name = null;
      if (methodOrCtor instanceof Method) {
         Method method = (Method)methodOrCtor;
         types = method.getParameterTypes();
         name = method.getName();
         declaringClass = method.getDeclaringClass();
      } else {
         Constructor<?> constructor = (Constructor<?>)methodOrCtor;
         types = constructor.getParameterTypes();
         declaringClass = constructor.getDeclaringClass();
         name = "<init>";
      }

      if (types.length == 0) {
         return EMPTY_NAMES;
      } else {
         InputStream byteCodeStream = this.getClassAsStream(declaringClass);
         if (byteCodeStream == null) {
            if (throwExceptionIfMissing) {
               throw new ParameterNamesNotFoundException("Unable to get class bytes");
            } else {
               return Paranamer.EMPTY_NAMES;
            }
         } else {
            try {
               BytecodeReadingParanamer.ClassReader reader = new BytecodeReadingParanamer.ClassReader(byteCodeStream);
               BytecodeReadingParanamer.TypeCollector visitor = new BytecodeReadingParanamer.TypeCollector(name, types, throwExceptionIfMissing);
               reader.accept(visitor);
               String[] parameterNamesForMethod = visitor.getParameterNamesForMethod();

               try {
                  byteCodeStream.close();
               } catch (IOException var11) {
               }

               return parameterNamesForMethod;
            } catch (IOException var12) {
               if (throwExceptionIfMissing) {
                  throw new ParameterNamesNotFoundException("IoException while reading class bytes", var12);
               } else {
                  return Paranamer.EMPTY_NAMES;
               }
            }
         }
      }
   }

   private InputStream getClassAsStream(Class<?> clazz) {
      ClassLoader classLoader = clazz.getClassLoader();
      if (classLoader == null) {
         classLoader = ClassLoader.getSystemClassLoader();
      }

      return this.getClassAsStream(classLoader, clazz.getName());
   }

   private InputStream getClassAsStream(ClassLoader classLoader, String className) {
      String name = className.replace('.', '/') + ".class";
      InputStream asStream = classLoader.getResourceAsStream(name);
      if (asStream == null) {
         asStream = BytecodeReadingParanamer.class.getResourceAsStream(name);
      }

      return asStream;
   }

   private static class ClassReader {
      public final byte[] b;
      private final int[] items;
      private final String[] strings;
      private final int maxStringLength;
      public final int header;
      static final int FIELD = 9;
      static final int METH = 10;
      static final int IMETH = 11;
      static final int INT = 3;
      static final int FLOAT = 4;
      static final int LONG = 5;
      static final int DOUBLE = 6;
      static final int NAME_TYPE = 12;
      static final int UTF8 = 1;
      public static final String __PARANAMER_DATA = "";

      private ClassReader(byte[] b) {
         this(b, 0);
      }

      private ClassReader(byte[] b, int off) {
         this.b = b;
         this.items = new int[this.readUnsignedShort(off + 8)];
         int n = this.items.length;
         this.strings = new String[n];
         int max = 0;
         int index = off + 10;

         for (int i = 1; i < n; i++) {
            this.items[i] = index + 1;
            int size;
            switch (b[index]) {
               case 1:
                  size = 3 + this.readUnsignedShort(index + 1);
                  if (size > max) {
                     max = size;
                  }
                  break;
               case 2:
               case 7:
               case 8:
               default:
                  size = 3;
                  break;
               case 3:
               case 4:
               case 9:
               case 10:
               case 11:
               case 12:
                  size = 5;
                  break;
               case 5:
               case 6:
                  size = 9;
                  i++;
            }

            index += size;
         }

         this.maxStringLength = max;
         this.header = index;
      }

      private ClassReader(InputStream is) throws IOException {
         this(readClass(is));
      }

      private static byte[] readClass(InputStream is) throws IOException {
         if (is == null) {
            throw new IOException("Class not found");
         } else {
            try {
               byte[] b = new byte[is.available()];
               int len = 0;

               while (true) {
                  int n = is.read(b, len, b.length - len);
                  if (n == -1) {
                     if (len < b.length) {
                        byte[] c = new byte[len];
                        System.arraycopy(b, 0, c, 0, len);
                        b = c;
                     }

                     return b;
                  }

                  len += n;
                  if (len == b.length) {
                     int last = is.read();
                     if (last < 0) {
                        return b;
                     }

                     byte[] c = new byte[b.length + 1000];
                     System.arraycopy(b, 0, c, 0, len);
                     c[len++] = (byte)last;
                     b = c;
                  }
               }
            } finally {
               try {
                  is.close();
               } catch (IOException var13) {
               }
            }
         }
      }

      private void accept(BytecodeReadingParanamer.TypeCollector classVisitor) {
         char[] c = new char[this.maxStringLength];
         int anns = 0;
         int ianns = 0;
         int u = this.header;
         int v = this.items[this.readUnsignedShort(u + 4)];
         int len = this.readUnsignedShort(u + 6);
         int w = 0;
         u += 8;

         for (int i = 0; i < len; i++) {
            u += 2;
         }

         int var17 = this.readUnsignedShort(u);

         for (v = u + 2; var17 > 0; var17--) {
            int j = this.readUnsignedShort(v + 6);

            for (v += 8; j > 0; j--) {
               v += 6 + this.readInt(v + 2);
            }
         }

         var17 = this.readUnsignedShort(v);

         for (v += 2; var17 > 0; var17--) {
            int j = this.readUnsignedShort(v + 6);

            for (v += 8; j > 0; j--) {
               v += 6 + this.readInt(v + 2);
            }
         }

         var17 = this.readUnsignedShort(v);

         for (int var16 = v + 2; var17 > 0; var17--) {
            var16 += 6 + this.readInt(var16 + 2);
         }

         var17 = this.readUnsignedShort(u);

         for (u += 2; var17 > 0; var17--) {
            int j = this.readUnsignedShort(u + 6);

            for (u += 8; j > 0; j--) {
               u += 6 + this.readInt(u + 2);
            }
         }

         var17 = this.readUnsignedShort(u);

         for (int var13 = u + 2; var17 > 0; var17--) {
            var13 = this.readMethod(classVisitor, c, var13);
         }
      }

      private int readMethod(BytecodeReadingParanamer.TypeCollector classVisitor, char[] c, int u) {
         int access = this.readUnsignedShort(u);
         String name = this.readUTF8(u + 2, c);
         String desc = this.readUTF8(u + 4, c);
         int v = 0;
         int w = 0;
         int j = this.readUnsignedShort(u + 6);

         for (u += 8; j > 0; j--) {
            String attrName = this.readUTF8(u, c);
            int attrSize = this.readInt(u + 2);
            u += 6;
            if (attrName.equals("Code")) {
               v = u;
            }

            u += attrSize;
         }

         if (w != 0) {
            w += 2;

            for (int var27 = 0; var27 < this.readUnsignedShort(w); var27++) {
               w += 2;
            }
         }

         BytecodeReadingParanamer.MethodCollector mv = classVisitor.visitMethod(access, name, desc);
         if (mv != null && v != 0) {
            int codeLength = this.readInt(v + 4);
            v += 8;
            int codeEnd = v + codeLength;
            j = this.readUnsignedShort(codeEnd);

            for (v = codeEnd + 2; j > 0; j--) {
               v += 8;
            }

            int varTable = 0;
            int varTypeTable = 0;
            j = this.readUnsignedShort(v);

            for (int var23 = v + 2; j > 0; j--) {
               String attrName = this.readUTF8(var23, c);
               if (attrName.equals("LocalVariableTable")) {
                  varTable = var23 + 6;
               } else if (attrName.equals("LocalVariableTypeTable")) {
                  varTypeTable = var23 + 6;
               }

               var23 += 6 + this.readInt(var23 + 2);
            }

            if (varTable != 0) {
               if (varTypeTable != 0) {
                  int k = this.readUnsignedShort(varTypeTable) * 3;
                  w = varTypeTable + 2;

                  for (int[] typeTable = new int[k]; k > 0; w += 10) {
                     typeTable[--k] = w + 6;
                     typeTable[--k] = this.readUnsignedShort(w + 8);
                     typeTable[--k] = this.readUnsignedShort(w);
                  }
               }

               int k = this.readUnsignedShort(varTable);

               for (int var26 = varTable + 2; k > 0; k--) {
                  int index = this.readUnsignedShort(var26 + 8);
                  mv.visitLocalVariable(this.readUTF8(var26 + 4, c), index);
                  var26 += 10;
               }
            }
         }

         return u;
      }

      private int readUnsignedShort(int index) {
         byte[] b = this.b;
         return (b[index] & 0xFF) << 8 | b[index + 1] & 0xFF;
      }

      private int readInt(int index) {
         byte[] b = this.b;
         return (b[index] & 0xFF) << 24 | (b[index + 1] & 0xFF) << 16 | (b[index + 2] & 0xFF) << 8 | b[index + 3] & 0xFF;
      }

      private String readUTF8(int index, char[] buf) {
         int item = this.readUnsignedShort(index);
         String s = this.strings[item];
         if (s != null) {
            return s;
         } else {
            index = this.items[item];
            return this.strings[item] = this.readUTF(index + 2, this.readUnsignedShort(index), buf);
         }
      }

      private String readUTF(int index, int utfLen, char[] buf) {
         int endIndex = index + utfLen;
         byte[] b = this.b;
         int strLen = 0;
         int st = 0;
         char cc = 0;

         while (index < endIndex) {
            int c = b[index++];
            switch (st) {
               case 0:
                  c &= 255;
                  if (c < 128) {
                     buf[strLen++] = (char)c;
                  } else {
                     if (c < 224 && c > 191) {
                        cc = (char)(c & 31);
                        st = 1;
                        continue;
                     }

                     cc = (char)(c & 15);
                     st = 2;
                  }
                  break;
               case 1:
                  buf[strLen++] = (char)(cc << 6 | c & 63);
                  st = 0;
                  break;
               case 2:
                  cc = (char)(cc << 6 | c & 63);
                  st = 1;
            }
         }

         return new String(buf, 0, strLen);
      }
   }

   private static class MethodCollector {
      private final int paramCount;
      private final int ignoreCount;
      private int currentParameter;
      private final StringBuffer result;
      private boolean debugInfoPresent;
      public static final String __PARANAMER_DATA = "visitLocalVariable java.lang.String,int name,index \n";

      private MethodCollector(int ignoreCount, int paramCount) {
         this.ignoreCount = ignoreCount;
         this.paramCount = paramCount;
         this.result = new StringBuffer();
         this.currentParameter = 0;
         this.debugInfoPresent = paramCount == 0;
      }

      public void visitLocalVariable(String name, int index) {
         if (index >= this.ignoreCount && index < this.ignoreCount + this.paramCount) {
            if (!name.equals("arg" + this.currentParameter)) {
               this.debugInfoPresent = true;
            }

            this.result.append(',');
            this.result.append(name);
            this.currentParameter++;
         }
      }

      private String getResult() {
         return this.result.length() != 0 ? this.result.substring(1) : "";
      }

      private boolean isDebugInfoPresent() {
         return this.debugInfoPresent;
      }
   }

   private static class Type {
      private static final int VOID = 0;
      private static final int BOOLEAN = 1;
      private static final int CHAR = 2;
      private static final int BYTE = 3;
      private static final int SHORT = 4;
      private static final int INT = 5;
      private static final int FLOAT = 6;
      private static final int LONG = 7;
      private static final int DOUBLE = 8;
      private static final int ARRAY = 9;
      private static final int OBJECT = 10;
      private static final BytecodeReadingParanamer.Type VOID_TYPE = new BytecodeReadingParanamer.Type(0, null, 1443168256, 1);
      private static final BytecodeReadingParanamer.Type BOOLEAN_TYPE = new BytecodeReadingParanamer.Type(1, null, 1509950721, 1);
      private static final BytecodeReadingParanamer.Type CHAR_TYPE = new BytecodeReadingParanamer.Type(2, null, 1124075009, 1);
      private static final BytecodeReadingParanamer.Type BYTE_TYPE = new BytecodeReadingParanamer.Type(3, null, 1107297537, 1);
      private static final BytecodeReadingParanamer.Type SHORT_TYPE = new BytecodeReadingParanamer.Type(4, null, 1392510721, 1);
      private static final BytecodeReadingParanamer.Type INT_TYPE = new BytecodeReadingParanamer.Type(5, null, 1224736769, 1);
      private static final BytecodeReadingParanamer.Type FLOAT_TYPE = new BytecodeReadingParanamer.Type(6, null, 1174536705, 1);
      private static final BytecodeReadingParanamer.Type LONG_TYPE = new BytecodeReadingParanamer.Type(7, null, 1241579778, 1);
      private static final BytecodeReadingParanamer.Type DOUBLE_TYPE = new BytecodeReadingParanamer.Type(8, null, 1141048066, 1);
      private final int sort;
      private char[] buf;
      private int off;
      private final int len;
      public static final String __PARANAMER_DATA = "";

      private Type(int sort) {
         this.sort = sort;
         this.len = 1;
      }

      private Type(int sort, char[] buf, int off, int len) {
         this.sort = sort;
         this.buf = buf;
         this.off = off;
         this.len = len;
      }

      private static BytecodeReadingParanamer.Type[] getArgumentTypes(String methodDescriptor) {
         char[] buf = methodDescriptor.toCharArray();
         int off = 1;
         int size = 0;

         while (true) {
            char car = buf[off++];
            if (car == ')') {
               BytecodeReadingParanamer.Type[] args = new BytecodeReadingParanamer.Type[size];
               off = 1;

               for (int var6 = 0; buf[off] != ')'; var6++) {
                  args[var6] = getType(buf, off);
                  off += args[var6].len + (args[var6].sort == 10 ? 2 : 0);
               }

               return args;
            }

            if (car == 'L') {
               while (buf[off++] != ';') {
               }

               size++;
            } else if (car != '[') {
               size++;
            }
         }
      }

      private static BytecodeReadingParanamer.Type getType(char[] buf, int off) {
         switch (buf[off]) {
            case 'B':
               return BYTE_TYPE;
            case 'C':
               return CHAR_TYPE;
            case 'D':
               return DOUBLE_TYPE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
               int var3 = 1;

               while (buf[off + var3] != ';') {
                  var3++;
               }

               return new BytecodeReadingParanamer.Type(10, buf, off + 1, var3 - 1);
            case 'F':
               return FLOAT_TYPE;
            case 'I':
               return INT_TYPE;
            case 'J':
               return LONG_TYPE;
            case 'S':
               return SHORT_TYPE;
            case 'V':
               return VOID_TYPE;
            case 'Z':
               return BOOLEAN_TYPE;
            case '[':
               int len = 1;

               while (buf[off + len] == '[') {
                  len++;
               }

               if (buf[off + len] == 'L') {
                  len++;

                  while (buf[off + len] != ';') {
                     len++;
                  }
               }

               return new BytecodeReadingParanamer.Type(9, buf, off, len + 1);
         }
      }

      private int getDimensions() {
         int i = 1;

         while (this.buf[this.off + i] == '[') {
            i++;
         }

         return i;
      }

      private BytecodeReadingParanamer.Type getElementType() {
         return getType(this.buf, this.off + this.getDimensions());
      }

      private String getClassName() {
         switch (this.sort) {
            case 0:
               return "void";
            case 1:
               return "boolean";
            case 2:
               return "char";
            case 3:
               return "byte";
            case 4:
               return "short";
            case 5:
               return "int";
            case 6:
               return "float";
            case 7:
               return "long";
            case 8:
               return "double";
            case 9:
               StringBuffer b = new StringBuffer(this.getElementType().getClassName());

               for (int i = this.getDimensions(); i > 0; i--) {
                  b.append("[]");
               }

               return b.toString();
            default:
               return new String(this.buf, this.off, this.len).replace('/', '.');
         }
      }
   }

   private static class TypeCollector {
      private static final String COMMA = ",";
      private final String methodName;
      private final Class<?>[] parameterTypes;
      private final boolean throwExceptionIfMissing;
      private BytecodeReadingParanamer.MethodCollector collector;
      public static final String __PARANAMER_DATA = "visitMethod int,java.lang.String,java.lang.String access,name,desc \n";

      private TypeCollector(String methodName, Class<?>[] parameterTypes, boolean throwExceptionIfMissing) {
         this.methodName = methodName;
         this.parameterTypes = parameterTypes;
         this.throwExceptionIfMissing = throwExceptionIfMissing;
         this.collector = null;
      }

      public BytecodeReadingParanamer.MethodCollector visitMethod(int access, String name, String desc) {
         if (this.collector != null) {
            return null;
         } else if (!name.equals(this.methodName)) {
            return null;
         } else {
            BytecodeReadingParanamer.Type[] argumentTypes = BytecodeReadingParanamer.Type.getArgumentTypes(desc);
            int longOrDoubleQuantity = 0;

            for (BytecodeReadingParanamer.Type t : argumentTypes) {
               if (t.getClassName().equals("long") || t.getClassName().equals("double")) {
                  longOrDoubleQuantity++;
               }
            }

            int paramCount = argumentTypes.length;
            if (paramCount != this.parameterTypes.length) {
               return null;
            } else {
               for (int i = 0; i < argumentTypes.length; i++) {
                  if (!this.correctTypeName(argumentTypes, i).equals(this.parameterTypes[i].getName())) {
                     return null;
                  }
               }

               this.collector = new BytecodeReadingParanamer.MethodCollector(Modifier.isStatic(access) ? 0 : 1, argumentTypes.length + longOrDoubleQuantity);
               return this.collector;
            }
         }
      }

      private String correctTypeName(BytecodeReadingParanamer.Type[] argumentTypes, int i) {
         String s = argumentTypes[i].getClassName();

         String braces;
         for (braces = ""; s.endsWith("[]"); s = s.substring(0, s.length() - 2)) {
            braces = braces + "[";
         }

         if (!braces.equals("")) {
            if (BytecodeReadingParanamer.primitives.containsKey(s)) {
               s = braces + BytecodeReadingParanamer.primitives.get(s);
            } else {
               s = braces + "L" + s + ";";
            }
         }

         return s;
      }

      private String[] getParameterNamesForMethod() {
         if (this.collector == null) {
            return Paranamer.EMPTY_NAMES;
         } else if (!this.collector.isDebugInfoPresent()) {
            if (this.throwExceptionIfMissing) {
               throw new ParameterNamesNotFoundException("Parameter names not found for " + this.methodName);
            } else {
               return Paranamer.EMPTY_NAMES;
            }
         } else {
            return this.collector.getResult().split(",");
         }
      }
   }
}
