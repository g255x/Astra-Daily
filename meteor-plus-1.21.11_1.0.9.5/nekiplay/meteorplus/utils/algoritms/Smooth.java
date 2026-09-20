package nekiplay.meteorplus.utils.algoritms;

public class Smooth {
   public static double getDouble(SmoothType type, double diffAngle, double minRotation, double maxRotation) {
      double speeds = (double)180.0F;
      if (type == Smooth.SmoothType.Linear) {
         speeds = diffAngle / (double)180.0F * maxRotation + ((double)1.0F - diffAngle / (double)180.0F) * minRotation;
      } else if (type == Smooth.SmoothType.Quad) {
         speeds = Math.pow(diffAngle / (double)180.0F, (double)2.0F) * maxRotation + ((double)1.0F - Math.pow(diffAngle / (double)180.0F, (double)2.0F)) * minRotation;
      } else if (type != Smooth.SmoothType.Sine && type != Smooth.SmoothType.QuadSine) {
         if (type == Smooth.SmoothType.Perlin) {
            int noice = PerlinNoice.PerlinNoice(maxRotation);
            if (noice > 0) {
               speeds = (double)noice;
            } else {
               speeds = maxRotation;
            }
         } else if (type == Smooth.SmoothType.PerlinRandom) {
            double random = RandomUtils.nextDouble(minRotation, maxRotation);
            int noice = PerlinNoice.PerlinNoice(random);
            if (noice > 0) {
               speeds = (double)noice;
            } else {
               speeds = random;
            }
         }
      } else {
         double v = -Math.cos(diffAngle / (double)180.0F * Math.PI) * (double)0.5F + (double)0.5F;
         if (type == Smooth.SmoothType.Sine) {
            speeds = v * maxRotation + (Math.cos(diffAngle / (double)180.0F * Math.PI * (double)0.5F + (double)0.5F) * (double)0.5F + (double)0.5F) * minRotation;
         } else {
            speeds = Math.pow(v, (double)2.0F) * maxRotation + ((double)1.0F - Math.pow(v, (double)2.0F)) * minRotation;
         }
      }

      return speeds;
   }

   public static enum SmoothType {
      None,
      Linear,
      Quad,
      Sine,
      QuadSine,
      Perlin,
      PerlinRandom;

      // $FF: synthetic method
      private static SmoothType[] $values() {
         return new SmoothType[]{None, Linear, Quad, Sine, QuadSine, Perlin, PerlinRandom};
      }
   }
}
