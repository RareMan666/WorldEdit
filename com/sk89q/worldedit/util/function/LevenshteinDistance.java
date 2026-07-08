package com.sk89q.worldedit.util.function;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class LevenshteinDistance implements Function<String, Integer> {
   public static final Pattern STANDARD_CHARS = Pattern.compile("[ _\\-]");
   private final String baseString;
   private final boolean caseSensitive;
   private final Pattern replacePattern;

   public LevenshteinDistance(String baseString, boolean caseSensitive) {
      this(baseString, caseSensitive, null);
   }

   public LevenshteinDistance(String baseString, boolean caseSensitive, @Nullable Pattern replacePattern) {
      Preconditions.checkNotNull(baseString);
      this.caseSensitive = caseSensitive;
      this.replacePattern = replacePattern;
      baseString = caseSensitive ? baseString : baseString.toLowerCase();
      baseString = replacePattern != null ? replacePattern.matcher(baseString).replaceAll("") : baseString;
      this.baseString = baseString;
   }

   @Nullable
   public Integer apply(String input) {
      if (input == null) {
         return null;
      } else {
         if (this.replacePattern != null) {
            input = this.replacePattern.matcher(input).replaceAll("");
         }

         return this.caseSensitive ? distance(this.baseString, input) : distance(this.baseString, input.toLowerCase());
      }
   }

   public static int distance(String s, String t) {
      if (s != null && t != null) {
         int n = s.length();
         int m = t.length();
         if (n == 0) {
            return m;
         } else if (m == 0) {
            return n;
         } else {
            int[] p = new int[n + 1];
            int[] d = new int[n + 1];
            int i = 0;

            while (i <= n) {
               p[i] = i++;
            }

            for (int j = 1; j <= m; j++) {
               char tj = t.charAt(j - 1);
               d[0] = j;

               for (int var11 = 1; var11 <= n; var11++) {
                  int cost = s.charAt(var11 - 1) == tj ? 0 : 1;
                  d[var11] = Math.min(Math.min(d[var11 - 1] + 1, p[var11] + 1), p[var11 - 1] + cost);
               }

               int[] _d = p;
               p = d;
               d = _d;
            }

            return p[n];
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }
}
