package com.sk89q.worldedit.math.transform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombinedTransform implements Transform {
   private final Transform[] transforms;

   public CombinedTransform(Transform... transforms) {
      Preconditions.checkNotNull(transforms);
      this.transforms = Arrays.copyOf(transforms, transforms.length);
   }

   public CombinedTransform(Collection<Transform> transforms) {
      this(transforms.toArray(new Transform[((Collection)Preconditions.checkNotNull(transforms)).size()]));
   }

   @Override
   public boolean isIdentity() {
      for (Transform transform : this.transforms) {
         if (!transform.isIdentity()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public Vector apply(Vector vector) {
      for (Transform transform : this.transforms) {
         vector = transform.apply(vector);
      }

      return vector;
   }

   @Override
   public Transform inverse() {
      List<Transform> list = new ArrayList<>();

      for (int i = this.transforms.length - 1; i >= 0; i--) {
         list.add(this.transforms[i].inverse());
      }

      return new CombinedTransform(list);
   }

   @Override
   public Transform combine(Transform other) {
      Preconditions.checkNotNull(other);
      if (other instanceof CombinedTransform) {
         CombinedTransform combinedOther = (CombinedTransform)other;
         Transform[] newTransforms = new Transform[this.transforms.length + combinedOther.transforms.length];
         System.arraycopy(this.transforms, 0, newTransforms, 0, this.transforms.length);
         System.arraycopy(combinedOther.transforms, 0, newTransforms, this.transforms.length, combinedOther.transforms.length);
         return new CombinedTransform(newTransforms);
      } else {
         return new CombinedTransform(this, other);
      }
   }
}
