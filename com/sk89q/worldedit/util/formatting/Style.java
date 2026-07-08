package com.sk89q.worldedit.util.formatting;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;

public enum Style {
   BLACK('0', 0),
   BLUE_DARK('1', 1),
   GREEN_DARK('2', 2),
   CYAN_DARK('3', 3),
   RED_DARK('4', 4),
   PURPLE_DARK('5', 5),
   YELLOW_DARK('6', 6),
   GRAY('7', 7),
   GRAY_DARK('8', 8),
   BLUE('9', 9),
   GREEN('a', 10),
   CYAN('b', 11),
   RED('c', 12),
   PURPLE('d', 13),
   YELLOW('e', 14),
   WHITE('f', 15),
   RANDOMIZE('k', 16, true),
   BOLD('l', 17, true),
   STRIKETHROUGH('m', 18, true),
   UNDERLINE('n', 19, true),
   ITALIC('o', 20, true),
   RESET('r', 21);

   public static final char COLOR_CHAR = '§';
   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
   private final int intCode;
   private final char code;
   private final boolean isFormat;
   private final String toString;
   private static final Map<Integer, Style> BY_ID = Maps.newHashMap();
   private static final Map<Character, Style> BY_CHAR = Maps.newHashMap();

   private Style(char code, int intCode) {
      this(code, intCode, false);
   }

   private Style(char code, int intCode, boolean isFormat) {
      this.code = code;
      this.intCode = intCode;
      this.isFormat = isFormat;
      this.toString = new String(new char[]{'§', code});
   }

   public char getChar() {
      return this.code;
   }

   @Override
   public String toString() {
      return this.toString;
   }

   public boolean isFormat() {
      return this.isFormat;
   }

   public boolean isColor() {
      return !this.isFormat && this != RESET;
   }

   public static Style getByChar(char code) {
      return BY_CHAR.get(code);
   }

   public static Style getByChar(String code) {
      Preconditions.checkNotNull(code);
      Preconditions.checkArgument(!code.isEmpty(), "Code must have at least one character");
      return BY_CHAR.get(code.charAt(0));
   }

   public static String stripColor(String input) {
      return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
   }

   public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
      char[] b = textToTranslate.toCharArray();

      for (int i = 0; i < b.length - 1; i++) {
         if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
            b[i] = 167;
            b[i + 1] = Character.toLowerCase(b[i + 1]);
         }
      }

      return new String(b);
   }

   public static String getLastColors(String input) {
      String result = "";
      int length = input.length();

      for (int index = length - 1; index > -1; index--) {
         char section = input.charAt(index);
         if (section == 167 && index < length - 1) {
            char c = input.charAt(index + 1);
            Style color = getByChar(c);
            if (color != null) {
               result = color + result;
               if (color.isColor() || color == RESET) {
                  break;
               }
            }
         }
      }

      return result;
   }

   static {
      for (Style color : values()) {
         BY_ID.put(color.intCode, color);
         BY_CHAR.put(color.code, color);
      }
   }
}
