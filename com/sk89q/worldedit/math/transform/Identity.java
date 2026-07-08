package com.sk89q.worldedit.math.transform;

import com.sk89q.worldedit.Vector;

public class Identity implements Transform {
   @Override
   public boolean isIdentity() {
      return true;
   }

   @Override
   public Vector apply(Vector vector) {
      return vector;
   }

   @Override
   public Transform inverse() {
      return this;
   }

   @Override
   public Transform combine(Transform other) {
      return (Transform)(other instanceof Identity ? this : other);
   }
}
