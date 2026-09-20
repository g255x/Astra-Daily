package nekiplay.meteorplus.utils;

import meteordevelopment.meteorclient.MeteorClient;

public class GameSensitivityUtils {
   public static float getSensitivity(float rot) {
      return getDeltaMouse(rot) * getGCDValue();
   }

   public static float getGCDValue() {
      return (float)((double)getGCD() * 0.15);
   }

   public static float getGCD() {
      float f1;
      return (f1 = (float)((Double)MeteorClient.mc.field_1690.method_42495().method_41753() * 0.6 + 0.2)) * f1 * f1 * 8.0F;
   }

   public static float getDeltaMouse(float delta) {
      return (float)Math.round(delta / getGCDValue());
   }
}
