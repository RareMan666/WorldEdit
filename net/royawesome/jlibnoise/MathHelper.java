package net.royawesome.jlibnoise;

import java.awt.Color;

public class MathHelper {
   public static final double DBL_EPSILON = Double.longBitsToDouble(4372995238176751616L);
   public static final float FLT_EPSILON = Float.intBitsToFloat(872415232);
   public static final double PI = Math.PI;
   public static final double SQUARED_PI = 9.869604401089358;
   public static final double HALF_PI = Math.PI / 2;
   public static final double QUARTER_PI = Math.PI / 4;
   public static final double TWO_PI = Math.PI * 2;
   public static final double THREE_PI_HALVES = Math.PI * 3.0 / 2.0;
   public static final double DEGTORAD = Math.PI / 180.0;
   public static final double RADTODEG = 180.0 / Math.PI;
   public static final double SQRTOFTWO = Math.sqrt(2.0);
   public static final double HALF_SQRTOFTWO = 0.5 * SQRTOFTWO;
   private static final double sin_a = -0.4052847345693511;
   private static final double sin_b = 1.2732395447351628;
   private static final double sin_p = 0.225;
   private static final double asin_a = -0.048129527683101345;
   private static final double asin_b = -0.3438359939479152;
   private static final double asin_c = 0.9627618484259132;
   private static final double asin_d = 1.0013894086010704;
   private static final double atan_a = 0.280872;

   public static double lengthSquared(double... values) {
      double rval = 0.0;

      for (double value : values) {
         rval += value * value;
      }

      return rval;
   }

   public static double length(double... values) {
      return Math.sqrt(lengthSquared(values));
   }

   public static float getAngleDifference(float angle1, float angle2) {
      return Math.abs(wrapAngle(angle1 - angle2));
   }

   public static double getRadianDifference(double radian1, double radian2) {
      return Math.abs(wrapRadian(radian1 - radian2));
   }

   public static float wrapAngle(float angle) {
      angle %= 360.0F;
      if (angle <= -180.0F) {
         return angle + 360.0F;
      } else {
         return angle > 180.0F ? angle - 360.0F : angle;
      }
   }

   public static byte wrapByte(int value) {
      value %= 256;
      if (value < 0) {
         value += 256;
      }

      return (byte)value;
   }

   public static double wrapRadian(double radian) {
      radian %= Math.PI * 2;
      if (radian <= -Math.PI) {
         return radian + (Math.PI * 2);
      } else {
         return radian > Math.PI ? radian - (Math.PI * 2) : radian;
      }
   }

   public static double round(double input, int decimals) {
      double p = Math.pow(10.0, decimals);
      return Math.round(input * p) / p;
   }

   public static double lerp(double a, double b, double percent) {
      return (1.0 - percent) * a + percent * b;
   }

   public static float lerp(float a, float b, float percent) {
      return (1.0F - percent) * a + percent * b;
   }

   public static int lerp(int a, int b, double percent) {
      return (int)((1.0 - percent) * a + percent * b);
   }

   public static Color lerp(Color a, Color b, double percent) {
      int red = lerp(a.getRed(), b.getRed(), percent);
      int blue = lerp(a.getBlue(), b.getBlue(), percent);
      int green = lerp(a.getGreen(), b.getGreen(), percent);
      int alpha = lerp(a.getAlpha(), b.getAlpha(), percent);
      return new Color(red, green, blue, alpha);
   }

   public static Color blend(Color a, Color b) {
      int red = lerp(a.getRed(), b.getRed(), a.getAlpha() / 255.0);
      int blue = lerp(a.getBlue(), b.getBlue(), a.getAlpha() / 255.0);
      int green = lerp(a.getGreen(), b.getGreen(), a.getAlpha() / 255.0);
      int alpha = lerp(a.getAlpha(), b.getAlpha(), a.getAlpha() / 255.0);
      return new Color(red, green, blue, alpha);
   }

   public static double clamp(double value, double low, double high) {
      if (value < low) {
         return low;
      } else {
         return value > high ? high : value;
      }
   }

   public static int clamp(int value, int low, int high) {
      if (value < low) {
         return low;
      } else {
         return value > high ? high : value;
      }
   }

   public static final double cos(double x) {
      return sin(x + (x > Math.PI / 2 ? -Math.PI * 3.0 / 2.0 : Math.PI / 2));
   }

   public static final double sin(double x) {
      x = -0.4052847345693511 * x * Math.abs(x) + 1.2732395447351628 * x;
      return 0.225 * (x * Math.abs(x) - x) + x;
   }

   public static final double tan(double x) {
      return sin(x) / cos(x);
   }

   public static final double asin(double x) {
      return x * (Math.abs(x) * (Math.abs(x) * -0.048129527683101345 + -0.3438359939479152) + 0.9627618484259132)
         + Math.signum(x) * (1.0013894086010704 - Math.sqrt(1.0 - x * x));
   }

   public static final double acos(double x) {
      return (Math.PI / 2) - asin(x);
   }

   public static final double atan(double x) {
      return Math.abs(x) < 1.0 ? x / (1.0 + 0.280872 * x * x) : Math.signum(x) * (Math.PI / 2) - x / (x * x + 0.280872);
   }

   public static final double inverseSqrt(double x) {
      double xhalves = 0.5 * x;
      x = Double.longBitsToDouble(6910469410427058090L - (Double.doubleToRawLongBits(x) >> 1));
      return x * (1.5 - xhalves * x * x);
   }

   public static final double sqrt(double x) {
      return x * inverseSqrt(x);
   }

   public static int floor(double x) {
      int y = (int)x;
      return x < y ? y - 1 : y;
   }

   public static int floor(float x) {
      int y = (int)x;
      return x < y ? y - 1 : y;
   }

   public static byte max(byte value1, byte value2) {
      return value1 > value2 ? value1 : value2;
   }

   public static int roundUpPow2(int x) {
      if (x <= 0) {
         return 1;
      } else if (x > 1073741824) {
         throw new IllegalArgumentException("Rounding " + x + " to the next highest power of two would exceed the int range");
      } else {
         x = --x | x >> 1;
         x |= x >> 2;
         x |= x >> 4;
         x |= x >> 8;
         x |= x >> 16;
         return x + 1;
      }
   }

   public static Float castFloat(Object o) {
      if (o == null) {
         return null;
      } else if (o instanceof Number) {
         return ((Number)o).floatValue();
      } else {
         try {
            return Float.valueOf(o.toString());
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Byte castByte(Object o) {
      if (o == null) {
         return null;
      } else if (o instanceof Number) {
         return ((Number)o).byteValue();
      } else {
         try {
            return Byte.valueOf(o.toString());
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Short castShort(Object o) {
      if (o == null) {
         return null;
      } else if (o instanceof Number) {
         return ((Number)o).shortValue();
      } else {
         try {
            return Short.valueOf(o.toString());
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Integer castInt(Object o) {
      if (o == null) {
         return null;
      } else if (o instanceof Number) {
         return ((Number)o).intValue();
      } else {
         try {
            return Integer.valueOf(o.toString());
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Double castDouble(Object o) {
      if (o == null) {
         return null;
      } else if (o instanceof Number) {
         return ((Number)o).doubleValue();
      } else {
         try {
            return Double.valueOf(o.toString());
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Long castLong(Object o) {
      if (o == null) {
         return null;
      } else if (o instanceof Number) {
         return ((Number)o).longValue();
      } else {
         try {
            return Long.valueOf(o.toString());
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Boolean castBoolean(Object o) {
      if (o == null) {
         return null;
      } else if (o instanceof Boolean) {
         return (Boolean)o;
      } else if (o instanceof String) {
         try {
            return Boolean.parseBoolean((String)o);
         } catch (IllegalArgumentException var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   public static int mean(int... values) {
      int sum = 0;

      for (int v : values) {
         sum += v;
      }

      return sum / values.length;
   }

   public static double mean(double... values) {
      double sum = 0.0;

      for (double v : values) {
         sum += v;
      }

      return sum / values.length;
   }
}
