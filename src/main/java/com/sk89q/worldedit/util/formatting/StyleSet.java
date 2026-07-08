package com.sk89q.worldedit.util.formatting;

public class StyleSet {
   private Boolean bold;
   private Boolean italic;
   private Boolean underline;
   private Boolean strikethrough;
   private Style color;

   public StyleSet() {
   }

   public StyleSet(Style... styles) {
      for (Style style : styles) {
         if (style.isColor()) {
            this.color = style;
         } else if (style == Style.BOLD) {
            this.bold = true;
         } else if (style == Style.ITALIC) {
            this.italic = true;
         } else if (style == Style.UNDERLINE) {
            this.underline = true;
         } else if (style == Style.STRIKETHROUGH) {
            this.strikethrough = true;
         }
      }
   }

   public Boolean getBold() {
      return this.bold;
   }

   public boolean isBold() {
      return this.getBold() != null && this.getBold();
   }

   public void setBold(Boolean bold) {
      this.bold = bold;
   }

   public Boolean getItalic() {
      return this.italic;
   }

   public boolean isItalic() {
      return this.getItalic() != null && this.getItalic();
   }

   public void setItalic(Boolean italic) {
      this.italic = italic;
   }

   public Boolean getUnderline() {
      return this.underline;
   }

   public boolean isUnderline() {
      return this.getUnderline() != null && this.getUnderline();
   }

   public void setUnderline(Boolean underline) {
      this.underline = underline;
   }

   public Boolean getStrikethrough() {
      return this.strikethrough;
   }

   public boolean isStrikethrough() {
      return this.getStrikethrough() != null && this.getStrikethrough();
   }

   public void setStrikethrough(Boolean strikethrough) {
      this.strikethrough = strikethrough;
   }

   public Style getColor() {
      return this.color;
   }

   public void setColor(Style color) {
      this.color = color;
   }

   public boolean hasFormatting() {
      return this.getBold() != null || this.getItalic() != null || this.getUnderline() != null || this.getStrikethrough() != null;
   }

   public boolean hasEqualFormatting(StyleSet other) {
      return this.getBold() == other.getBold()
         && this.getItalic() == other.getItalic()
         && this.getUnderline() == other.getUnderline()
         && this.getStrikethrough() == other.getStrikethrough();
   }

   public StyleSet extend(StyleSet style) {
      StyleSet newStyle = this.clone();
      if (style.getBold() != null) {
         newStyle.setBold(style.getBold());
      }

      if (style.getItalic() != null) {
         newStyle.setItalic(style.getItalic());
      }

      if (style.getUnderline() != null) {
         newStyle.setUnderline(style.getUnderline());
      }

      if (style.getStrikethrough() != null) {
         newStyle.setStrikethrough(style.getStrikethrough());
      }

      if (style.getColor() != null) {
         newStyle.setColor(style.getColor());
      }

      return newStyle;
   }

   public StyleSet clone() {
      StyleSet style = new StyleSet();
      style.setBold(this.getBold());
      style.setItalic(this.getItalic());
      style.setUnderline(this.getUnderline());
      style.setStrikethrough(this.getStrikethrough());
      style.setColor(this.getColor());
      return style;
   }
}
