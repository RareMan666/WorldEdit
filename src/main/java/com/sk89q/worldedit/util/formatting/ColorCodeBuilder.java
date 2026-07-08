package com.sk89q.worldedit.util.formatting;

import com.google.common.base.Joiner;
import java.util.LinkedList;
import java.util.List;

public class ColorCodeBuilder {
   private static final ColorCodeBuilder instance = new ColorCodeBuilder();
   private static final Joiner newLineJoiner = Joiner.on("\n");
   public static final int GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH = 47;

   public String[] build(StyledFragment message) {
      StringBuilder builder = new StringBuilder();
      this.buildFragment(builder, message, message.getStyle(), new StyleSet());
      return builder.toString().split("\r?\n");
   }

   private StyleSet buildFragment(StringBuilder builder, StyledFragment message, StyleSet parentStyle, StyleSet lastStyle) {
      for (Fragment node : message.getChildren()) {
         if (node instanceof StyledFragment) {
            StyledFragment fragment = (StyledFragment)node;
            lastStyle = this.buildFragment(builder, fragment, parentStyle.extend(message.getStyle()), lastStyle);
         } else {
            StyleSet style = parentStyle.extend(message.getStyle());
            builder.append(getAdditive(style, lastStyle));
            builder.append(node);
            lastStyle = style;
         }
      }

      return lastStyle;
   }

   public static String getFormattingCode(StyleSet style) {
      StringBuilder builder = new StringBuilder();
      if (style.isBold()) {
         builder.append(Style.BOLD);
      }

      if (style.isItalic()) {
         builder.append(Style.ITALIC);
      }

      if (style.isUnderline()) {
         builder.append(Style.UNDERLINE);
      }

      if (style.isStrikethrough()) {
         builder.append(Style.STRIKETHROUGH);
      }

      return builder.toString();
   }

   public static String getCode(StyleSet style) {
      StringBuilder builder = new StringBuilder();
      builder.append(getFormattingCode(style));
      if (style.getColor() != null) {
         builder.append(style.getColor());
      }

      return builder.toString();
   }

   public static String getAdditive(StyleSet resetTo, StyleSet resetFrom) {
      if (!resetFrom.hasFormatting() && resetTo.hasFormatting()) {
         StringBuilder builder = new StringBuilder();
         builder.append(getFormattingCode(resetTo));
         if (resetFrom.getColor() != resetTo.getColor()) {
            builder.append(resetTo.getColor());
         }

         return builder.toString();
      } else if (resetFrom.hasEqualFormatting(resetTo) && (resetFrom.getColor() == null || resetTo.getColor() != null)) {
         return resetFrom.getColor() != resetTo.getColor() ? String.valueOf(resetTo.getColor()) : "";
      } else {
         StringBuilder builder = new StringBuilder();
         builder.append(Style.RESET);
         builder.append(getCode(resetTo));
         return builder.toString();
      }
   }

   private String[] wordWrap(String rawString, int lineLength) {
      if (rawString == null) {
         return new String[]{""};
      } else if (rawString.length() <= lineLength && !rawString.contains("\n")) {
         return new String[]{rawString};
      } else {
         char[] rawChars = (rawString + ' ').toCharArray();
         StringBuilder word = new StringBuilder();
         StringBuilder line = new StringBuilder();
         List<String> lines = new LinkedList<>();
         int lineColorChars = 0;

         for (int i = 0; i < rawChars.length; i++) {
            char c = rawChars[i];
            if (c == 167) {
               word.append(Style.getByChar(rawChars[i + 1]));
               lineColorChars += 2;
               i++;
            } else if (c != ' ' && c != '\n') {
               word.append(c);
            } else {
               if (line.length() == 0 && word.length() > lineLength) {
                  String wordStr = word.toString();
                  String transformed;
                  if ((transformed = this.transform(wordStr)) != null) {
                     line.append(transformed);
                  } else {
                     for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
                        lines.add(partialWord);
                     }
                  }
               } else if (line.length() + word.length() - lineColorChars == lineLength) {
                  line.append(' ');
                  line.append((CharSequence)word);
                  lines.add(line.toString());
                  line = new StringBuilder();
                  lineColorChars = 0;
               } else if (line.length() + 1 + word.length() - lineColorChars > lineLength) {
                  String wordStr = word.toString();
                  String transformed;
                  if (word.length() <= lineLength || (transformed = this.transform(wordStr)) == null) {
                     for (String partialWord : wordStr.split("(?<=\\G.{" + lineLength + "})")) {
                        lines.add(line.toString());
                        line = new StringBuilder(partialWord);
                     }

                     lineColorChars = 0;
                  } else if (line.length() + 1 + transformed.length() - lineColorChars > lineLength) {
                     lines.add(line.toString());
                     line = new StringBuilder(transformed);
                     lineColorChars = 0;
                  } else {
                     if (line.length() > 0) {
                        line.append(' ');
                     }

                     line.append(transformed);
                  }
               } else {
                  if (line.length() > 0) {
                     line.append(' ');
                  }

                  line.append((CharSequence)word);
               }

               word = new StringBuilder();
               if (c == '\n') {
                  lines.add(line.toString());
                  line = new StringBuilder();
               }
            }
         }

         if (line.length() > 0) {
            lines.add(line.toString());
         }

         if (lines.get(0).isEmpty() || lines.get(0).charAt(0) != 167) {
            lines.set(0, Style.WHITE + lines.get(0));
         }

         for (int ix = 1; ix < lines.size(); ix++) {
            String pLine = lines.get(ix - 1);
            String subLine = lines.get(ix);
            char color = pLine.charAt(pLine.lastIndexOf(167) + 1);
            if (subLine.isEmpty() || subLine.charAt(0) != 167) {
               lines.set(ix, Style.getByChar(color) + subLine);
            }
         }

         return lines.toArray(new String[lines.size()]);
      }
   }

   protected String transform(String word) {
      return null;
   }

   public static String asColorCodes(StyledFragment fragment) {
      return newLineJoiner.join(instance.build(fragment));
   }
}
