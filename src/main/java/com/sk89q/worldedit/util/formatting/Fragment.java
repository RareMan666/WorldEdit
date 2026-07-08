package com.sk89q.worldedit.util.formatting;

public class Fragment {
   private final StringBuilder builder = new StringBuilder();

   Fragment() {
   }

   public Fragment append(String str) {
      this.builder.append(Style.stripColor(str));
      return this;
   }

   public Fragment append(Object obj) {
      this.append(String.valueOf(obj));
      return this;
   }

   public Fragment append(StringBuffer sb) {
      this.append(String.valueOf(sb));
      return this;
   }

   public Fragment append(CharSequence s) {
      this.append(String.valueOf(s));
      return this;
   }

   public Fragment append(boolean b) {
      this.append(String.valueOf(b));
      return this;
   }

   public Fragment append(char c) {
      this.append(String.valueOf(c));
      return this;
   }

   public Fragment append(int i) {
      this.append(String.valueOf(i));
      return this;
   }

   public Fragment append(long lng) {
      this.append(String.valueOf(lng));
      return this;
   }

   public Fragment append(float f) {
      this.append(String.valueOf(f));
      return this;
   }

   public Fragment append(double d) {
      this.append(String.valueOf(d));
      return this;
   }

   public Fragment newLine() {
      this.append("\n");
      return this;
   }

   @Override
   public String toString() {
      return this.builder.toString();
   }
}
