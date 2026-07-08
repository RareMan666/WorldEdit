package com.sk89q.worldedit.util.command;

public class SimpleParameter implements Parameter {
   private String name;
   private Character flag;
   private boolean isValue;
   private boolean isOptional;
   private String[] defaultValue;

   public SimpleParameter() {
   }

   public SimpleParameter(String name) {
      this.name = name;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public SimpleParameter setName(String name) {
      this.name = name;
      return this;
   }

   @Override
   public Character getFlag() {
      return this.flag;
   }

   @Override
   public boolean isValueFlag() {
      return this.flag != null && this.isValue;
   }

   public SimpleParameter setFlag(Character flag, boolean isValue) {
      this.flag = flag;
      this.isValue = isValue;
      return this;
   }

   @Override
   public boolean isOptional() {
      return this.isOptional || this.getFlag() != null;
   }

   public SimpleParameter setOptional(boolean isOptional) {
      this.isOptional = isOptional;
      return this;
   }

   @Override
   public String[] getDefaultValue() {
      return this.defaultValue;
   }

   public SimpleParameter setDefaultValue(String[] defaultValue) {
      this.defaultValue = defaultValue;
      return this;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      if (this.getFlag() != null) {
         if (this.isValueFlag()) {
            builder.append("[-").append(this.getFlag()).append(" <").append(this.getName()).append(">]");
         } else {
            builder.append("[-").append(this.getFlag()).append("]");
         }
      } else if (this.isOptional()) {
         builder.append("[<").append(this.getName()).append(">]");
      } else {
         builder.append("<").append(this.getName()).append(">");
      }

      return builder.toString();
   }
}
