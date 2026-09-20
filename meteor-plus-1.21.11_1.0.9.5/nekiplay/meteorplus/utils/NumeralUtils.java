package nekiplay.meteorplus.utils;

public class NumeralUtils {
   public static String FormatNumber(long count) {
      if (count < 1000L) {
         return "" + count;
      } else {
         int exp = (int)(Math.log((double)count) / Math.log((double)1000.0F));
         return String.format("%.1f %c", (double)count / Math.pow((double)1000.0F, (double)exp), "kMGTPE".charAt(exp - 1));
      }
   }
}
