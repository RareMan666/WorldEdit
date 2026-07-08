package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.session.request.Request;
import javax.annotation.Nullable;

public final class Masks {
   private static final Masks.AlwaysTrue ALWAYS_TRUE = new Masks.AlwaysTrue();
   private static final Masks.AlwaysFalse ALWAYS_FALSE = new Masks.AlwaysFalse();

   private Masks() {
   }

   public static Mask alwaysTrue() {
      return ALWAYS_TRUE;
   }

   public static Mask2D alwaysTrue2D() {
      return ALWAYS_TRUE;
   }

   public static Mask negate(final Mask mask) {
      if (mask instanceof Masks.AlwaysTrue) {
         return ALWAYS_FALSE;
      } else if (mask instanceof Masks.AlwaysFalse) {
         return ALWAYS_TRUE;
      } else {
         Preconditions.checkNotNull(mask);
         return new AbstractMask() {
            @Override
            public boolean test(Vector vector) {
               return !mask.test(vector);
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
               Mask2D mask2d = mask.toMask2D();
               return mask2d != null ? Masks.negate(mask2d) : null;
            }
         };
      }
   }

   public static Mask2D negate(final Mask2D mask) {
      if (mask instanceof Masks.AlwaysTrue) {
         return ALWAYS_FALSE;
      } else if (mask instanceof Masks.AlwaysFalse) {
         return ALWAYS_TRUE;
      } else {
         Preconditions.checkNotNull(mask);
         return new AbstractMask2D() {
            @Override
            public boolean test(Vector2D vector) {
               return !mask.test(vector);
            }
         };
      }
   }

   public static Mask asMask(final Mask2D mask) {
      return new AbstractMask() {
         @Override
         public boolean test(Vector vector) {
            return mask.test(vector.toVector2D());
         }

         @Nullable
         @Override
         public Mask2D toMask2D() {
            return mask;
         }
      };
   }

   @Deprecated
   public static Mask wrap(final com.sk89q.worldedit.masks.Mask mask, final EditSession editSession) {
      Preconditions.checkNotNull(mask);
      return new AbstractMask() {
         @Override
         public boolean test(Vector vector) {
            return mask.matches(editSession, vector);
         }

         @Nullable
         @Override
         public Mask2D toMask2D() {
            return null;
         }
      };
   }

   public static Mask wrap(final com.sk89q.worldedit.masks.Mask mask) {
      Preconditions.checkNotNull(mask);
      return new AbstractMask() {
         @Override
         public boolean test(Vector vector) {
            EditSession editSession = Request.request().getEditSession();
            return editSession != null && mask.matches(editSession, vector);
         }

         @Nullable
         @Override
         public Mask2D toMask2D() {
            return null;
         }
      };
   }

   public static com.sk89q.worldedit.masks.Mask wrap(final Mask mask) {
      Preconditions.checkNotNull(mask);
      return new com.sk89q.worldedit.masks.AbstractMask() {
         @Override
         public boolean matches(EditSession editSession, Vector position) {
            Request.request().setEditSession(editSession);
            return mask.test(position);
         }
      };
   }

   private static class AlwaysFalse implements Mask, Mask2D {
      private AlwaysFalse() {
      }

      @Override
      public boolean test(Vector vector) {
         return false;
      }

      @Override
      public boolean test(Vector2D vector) {
         return false;
      }

      @Nullable
      @Override
      public Mask2D toMask2D() {
         return this;
      }
   }

   private static class AlwaysTrue implements Mask, Mask2D {
      private AlwaysTrue() {
      }

      @Override
      public boolean test(Vector vector) {
         return true;
      }

      @Override
      public boolean test(Vector2D vector) {
         return true;
      }

      @Nullable
      @Override
      public Mask2D toMask2D() {
         return this;
      }
   }
}
