package dev.Astra.core.impl;

import com.google.common.base.Splitter;
import dev.Astra.Astra;
import dev.Astra.core.Manager;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.Fonts;
import dev.Astra.mod.modules.settings.Setting;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public class ConfigManager extends Manager {
   public static File options = getFile("options.txt");
   private final Hashtable<String, String> settings = new Hashtable();

   public void load() {
      this.settings.clear();
      Splitter COLON_SPLITTER = Splitter.on(':');

      try {
         if (options.exists()) {
            InputStream fis = new FileInputStream(options);

            try {
               for(String s : IOUtils.readLines(fis, StandardCharsets.UTF_8)) {
                  try {
                     Iterator iterator = COLON_SPLITTER.limit(2).split(s).iterator();
                     this.settings.put((String)iterator.next(), (String)iterator.next());
                  } catch (Exception var10) {
                     System.out.println("Skipping bad option: " + s);
                  }
               }
            } catch (Throwable var11) {
               try {
                  fis.close();
               } catch (Throwable var9) {
                  var11.addSuppressed(var9);
               }

               throw var11;
            }

            fis.close();
         }
      } catch (Exception exception) {
         exception.printStackTrace();
         System.out.println("[Astra Client] Failed to load settings");
      }

      for(Module module : Astra.MODULE.getModules()) {
         for(Setting setting : module.getSettings()) {
            String var10000 = module.getName();
            String line = var10000 + "_" + setting.getName();
            Objects.requireNonNull(setting);
            if (setting instanceof BooleanSetting s) {
               s.setValueWithoutTask(Astra.CONFIG.getBoolean(line, s.getDefaultValue()));
            } else if (setting instanceof SliderSetting s) {
               s.setValue((double)Astra.CONFIG.getFloat(line, (float)s.getDefaultValue()));
            } else if (setting instanceof BindSetting s) {
               s.setValue(Astra.CONFIG.getInt(line, s.getDefaultValue()));
               s.setHoldEnable(Astra.CONFIG.getBoolean(line + "_hold"));
            } else if (setting instanceof EnumSetting s) {
               s.loadSetting(Astra.CONFIG.getString(line));
            } else if (setting instanceof ColorSetting s) {
               s.setValue(new Color(Astra.CONFIG.getInt(line, s.getDefaultValue().getRGB()), true));
               boolean rainbow;
               if (Astra.CONFIG.getString(line + "Rainbow") != null) {
                  rainbow = Astra.CONFIG.getBoolean(line + "Rainbow", s.getDefaultRainbow());
               } else {
                  rainbow = Astra.CONFIG.getBoolean(line + "Sync", s.getDefaultRainbow());
               }

               s.setRainbow(rainbow);
               if (s.injectBoolean) {
                  s.booleanValue = Astra.CONFIG.getBoolean(line + "Boolean", s.getDefaultBooleanValue());
               }
            } else if (setting instanceof StringSetting s) {
               s.setValue(Astra.CONFIG.getString(line, s.getDefaultValue()));
            }
         }

         module.setState(Astra.CONFIG.getBoolean(module.getName() + "_state", module.getName().equals("Info")));
      }

   }

   public void read() {
      this.load();
   }

   public void save() {
      PrintWriter printwriter = null;

      try {
         printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(options), StandardCharsets.UTF_8));

         for(Module module : Astra.MODULE.getModules()) {
            for(Setting setting : module.getSettings()) {
               String var10000 = module.getName();
               String line = var10000 + "_" + setting.getName();
               Objects.requireNonNull(setting);
               if (setting instanceof BooleanSetting s) {
                  printwriter.println(line + ":" + s.getValue());
               } else if (setting instanceof SliderSetting s) {
                  printwriter.println(line + ":" + s.getValue());
               } else if (setting instanceof BindSetting s) {
                  printwriter.println(line + ":" + s.getValue());
                  printwriter.println(line + "_hold:" + s.isHoldEnable());
               } else if (setting instanceof EnumSetting s) {
                  printwriter.println(line + ":" + s.getValue().name());
               } else if (setting instanceof ColorSetting s) {
                  printwriter.println(line + ":" + s.getValue().getRGB());
                  printwriter.println(line + "Rainbow:" + s.rainbow);
                  if (s.injectBoolean) {
                     printwriter.println(line + "Boolean:" + s.booleanValue);
                  }
               } else if (setting instanceof StringSetting s) {
                  printwriter.println(line + ":" + s.getValue());
               }
            }

            String var10001 = module.getName();
            printwriter.println(var10001 + "_state:" + module.isOn());
         }

         IOUtils.closeQuietly(printwriter);
      } catch (Exception exception) {
         exception.printStackTrace();
         System.out.println("[Astra Client] Failed to save settings");
      } finally {
         IOUtils.closeQuietly(printwriter);
      }

   }

   public static File getCfgFolder() {
      File base = getFolder();
      File folder = new File(base, "cfg");
      if (!folder.exists()) {
         folder.mkdirs();
      }

      return folder;
   }

   public static File getCfgFile(String name) {
      if (name != null && !name.isEmpty()) {
         File folder = getCfgFolder();
         return new File(folder, name + ".cfg");
      } else {
         return null;
      }
   }

   public static ArrayList<String> listCfgNames() {
      ArrayList<String> out = new ArrayList();
      File folder = getCfgFolder();
      if (folder == null) {
         return out;
      } else {
         File[] files = folder.listFiles();
         if (files == null) {
            return out;
         } else {
            for(File f : files) {
               if (f != null && f.isFile()) {
                  String n = f.getName();
                  if (n != null) {
                     String ln = n.toLowerCase();
                     if (ln.endsWith(".cfg")) {
                        String base = n.substring(0, n.length() - 4);
                        if (!base.isEmpty()) {
                           out.add(base);
                        }
                     }
                  }
               }
            }

            out.sort(String::compareToIgnoreCase);
            return out;
         }
      }
   }

   public static String sanitizeCfgName(String name) {
      if (name == null) {
         return "";
      } else {
         String s = name.trim();
         if (s.isEmpty()) {
            return "";
         } else {
            StringBuilder out = new StringBuilder();

            for(int i = 0; i < s.length(); ++i) {
               char c = s.charAt(i);
               if (!Character.isLetterOrDigit(c) && c != '_' && c != '-' && c != ' ') {
                  out.append('_');
               } else {
                  out.append(c);
               }
            }

            String r = out.toString().trim();
            if (r.length() > 32) {
               r = r.substring(0, 32);
            }

            return r;
         }
      }
   }

   public static String uniqueCfgName(String base) {
      String n = sanitizeCfgName(base);
      if (n.isEmpty()) {
         return "";
      } else {
         File f = getCfgFile(n);
         if (f != null && !f.exists()) {
            return n;
         } else {
            for(int i = 1; i < 1000; ++i) {
               String nn = n + "_" + i;
               File ff = getCfgFile(nn);
               if (ff != null && !ff.exists()) {
                  return nn;
               }
            }

            return n;
         }
      }
   }

   public static void writeDefaultCfg(File file) {
      if (file != null) {
         PrintWriter out = null;

         try {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

            for(Module module : Astra.MODULE.getModules()) {
               for(Setting setting : module.getSettings()) {
                  String var10000 = module.getName();
                  String line = var10000 + "_" + setting.getName();
                  if (setting instanceof BooleanSetting) {
                     BooleanSetting s = (BooleanSetting)setting;
                     out.println(line + ":" + s.getDefaultValue());
                  } else if (setting instanceof SliderSetting) {
                     SliderSetting s = (SliderSetting)setting;
                     out.println(line + ":" + s.getDefaultValue());
                  } else if (setting instanceof BindSetting) {
                     BindSetting s = (BindSetting)setting;
                     out.println(line + ":" + s.getDefaultValue());
                     out.println(line + "_hold:false");
                  } else if (setting instanceof EnumSetting) {
                     EnumSetting s = (EnumSetting)setting;
                     Enum dv = s.getDefaultValue();
                     out.println(line + ":" + dv.name());
                  } else if (setting instanceof ColorSetting) {
                     ColorSetting s = (ColorSetting)setting;
                     out.println(line + ":" + s.getDefaultValue().getRGB());
                     out.println(line + "Rainbow:" + s.getDefaultRainbow());
                     if (s.injectBoolean) {
                        out.println(line + "Boolean:" + s.getDefaultBooleanValue());
                     }
                  } else if (setting instanceof StringSetting) {
                     StringSetting s = (StringSetting)setting;
                     out.println(line + ":" + s.getDefaultValue());
                  }
               }

               String var10001 = module.getName();
               out.println(var10001 + "_state:" + module.getName().equals("Info"));
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            if (out != null) {
               out.close();
            }

         }

      }
   }

   public static String createDefaultCfg(String nameInput) {
      String base = sanitizeCfgName(nameInput);
      if (base.isEmpty()) {
         return null;
      } else {
         String name = uniqueCfgName(base);
         File file = getCfgFile(name);
         if (file == null) {
            return null;
         } else {
            writeDefaultCfg(file);
            return name;
         }
      }
   }

   public static String backupCfg(String fromName, String toNameInput) {
      if (fromName != null && !fromName.isEmpty()) {
         File src = getCfgFile(fromName);
         if (src != null && src.exists()) {
            String baseName = sanitizeCfgName(toNameInput);
            if (baseName.isEmpty()) {
               baseName = sanitizeCfgName(fromName);
            }

            if (baseName.isEmpty()) {
               return null;
            } else {
               LocalDate d = LocalDate.now();
               String prefix = baseName + "_" + d.getYear() + "_" + d.getMonthValue() + "_" + d.getDayOfMonth();
               String name = null;

               for(int i = 1; i < 100000; ++i) {
                  String candidate = prefix + "_[" + i + "]";
                  File f = getCfgFile(candidate);
                  if (f != null && !f.exists()) {
                     name = candidate;
                     break;
                  }
               }

               if (name == null) {
                  return null;
               } else {
                  File dst = getCfgFile(name);
                  if (dst == null) {
                     return null;
                  } else {
                     try {
                        Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        return name;
                     } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                     }
                  }
               }
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void deleteCfg(String name) {
      File f = getCfgFile(name);
      if (f != null && f.exists()) {
         try {
            f.delete();
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }

   public static String saveCfg(String nameInput) {
      String n = sanitizeCfgName(nameInput);
      if (n.isEmpty()) {
         return null;
      } else {
         getCfgFolder();

         Object var3;
         try {
            options = Manager.getFile("cfg" + File.separator + n + ".cfg");
            Astra.save();
            String var2 = n;
            return var2;
         } catch (Exception e) {
            e.printStackTrace();
            var3 = null;
         } finally {
            options = Manager.getFile("options.txt");
         }

         return (String)var3;
      }
   }

   public static boolean loadCfg(String name) {
      if (name != null && !name.isEmpty()) {
         boolean var2;
         try {
            options = Manager.getFile("cfg" + File.separator + name + ".cfg");
            Astra.CONFIG = new ConfigManager();
            Astra.CONFIG.load();
            boolean var1 = true;
            return var1;
         } catch (Exception e) {
            e.printStackTrace();
            var2 = false;
         } finally {
            options = Manager.getFile("options.txt");
            Astra.save();
            if (Fonts.INSTANCE != null) {
               Fonts.INSTANCE.refresh();
            }

         }

         return var2;
      } else {
         return false;
      }
   }

   public int getInt(String setting, int defaultValue) {
      String s = (String)this.settings.get(setting);
      return s != null && this.isInteger(s) ? Integer.parseInt(s) : defaultValue;
   }

   public float getFloat(String setting, float defaultValue) {
      String s = (String)this.settings.get(setting);
      return s != null && this.isFloat(s) ? Float.parseFloat(s) : defaultValue;
   }

   public boolean getBoolean(String setting) {
      String s = (String)this.settings.get(setting);
      return Boolean.parseBoolean(s);
   }

   public boolean getBoolean(String setting, boolean defaultValue) {
      if (this.settings.get(setting) != null) {
         String s = (String)this.settings.get(setting);
         return Boolean.parseBoolean(s);
      } else {
         return defaultValue;
      }
   }

   public String getString(String setting) {
      return (String)this.settings.get(setting);
   }

   public String getString(String setting, String defaultValue) {
      return this.settings.get(setting) == null ? defaultValue : (String)this.settings.get(setting);
   }

   public boolean isInteger(String str) {
      Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
      return pattern.matcher(str).matches();
   }

   public boolean isFloat(String str) {
      String pattern = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
      return str.matches(pattern);
   }
}
