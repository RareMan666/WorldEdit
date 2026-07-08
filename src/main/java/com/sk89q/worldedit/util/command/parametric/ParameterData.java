package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.worldedit.util.command.SimpleParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ParameterData extends SimpleParameter {
   private Binding binding;
   private Annotation classifier;
   private Annotation[] modifiers;
   private Type type;

   public Binding getBinding() {
      return this.binding;
   }

   void setBinding(Binding binding) {
      this.binding = binding;
   }

   public Type getType() {
      return this.type;
   }

   void setType(Type type) {
      this.type = type;
   }

   public Annotation getClassifier() {
      return this.classifier;
   }

   void setClassifier(Annotation classifier) {
      this.classifier = classifier;
   }

   public Annotation[] getModifiers() {
      return this.modifiers;
   }

   void setModifiers(Annotation[] modifiers) {
      this.modifiers = modifiers;
   }

   int getConsumedCount() {
      return this.getBinding().getConsumedCount(this);
   }

   boolean isUserInput() {
      return this.getBinding().getBehavior(this) != BindingBehavior.PROVIDES;
   }

   boolean isNonFlagConsumer() {
      return this.getBinding().getBehavior(this) != BindingBehavior.PROVIDES && !this.isValueFlag();
   }

   void validate(Method method, int parameterIndex) throws ParametricException {
      BindingBehavior behavior = this.getBinding().getBehavior(this);
      boolean indeterminate = behavior == BindingBehavior.INDETERMINATE;
      if (!this.isValueFlag() && indeterminate) {
         throw new ParametricException(
            "@Switch missing for indeterminate consumer\n\nNotably:\nFor the type "
               + this.type
               + ", the binding "
               + this.getBinding().getClass().getCanonicalName()
               + "\nmay or may not consume parameters (isIndeterminateConsumer("
               + this.type
               + ") = true)\nand therefore @Switch(flag) is required for parameter #"
               + parameterIndex
               + " of \n"
               + method.toGenericString()
         );
      } else if (behavior != BindingBehavior.CONSUMES && this.binding.getConsumedCount(this) != -1) {
         throw new ParametricException(
            "getConsumedCount() does not return -1 for binding "
               + this.getBinding().getClass().getCanonicalName()
               + "\neven though its behavior type is "
               + behavior.name()
               + "\nfor parameter #"
               + parameterIndex
               + " of \n"
               + method.toGenericString()
         );
      } else if (behavior != BindingBehavior.PROVIDES && this.binding.getConsumedCount(this) == 0) {
         throw new ParametricException(
            "getConsumedCount() must not return 0 for binding "
               + this.getBinding().getClass().getCanonicalName()
               + "\nwhen its behavior type is "
               + behavior.name()
               + " and not PROVIDES \nfor parameter #"
               + parameterIndex
               + " of \n"
               + method.toGenericString()
         );
      }
   }
}
