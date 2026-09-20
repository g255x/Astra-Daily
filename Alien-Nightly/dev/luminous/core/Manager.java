package dev.luminous.core;

import java.io.File;
import net.minecraft.class_310;

public class Manager {
   public static final class_310 mc = class_310.method_1551();

   public static File getFile(String s) {
      File folder = getFolder();
      return new File(folder, s);
   }

   public static File getFolder() {
      String var10002 = mc.field_1697.getPath();
      File folder = new File(var10002 + File.separator + "Alien".toLowerCase());
      if (!folder.exists()) {
         folder.mkdirs();
      }

      return folder;
   }
}
