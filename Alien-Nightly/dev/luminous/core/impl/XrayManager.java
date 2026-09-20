package dev.luminous.core.impl;

import dev.luminous.core.Manager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.render.Xray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import net.minecraft.class_2246;
import org.apache.commons.io.IOUtils;

public class XrayManager extends Manager {
   private final ArrayList<String> list = new ArrayList();

   public XrayManager() {
      this.read();
   }

   public ArrayList<String> getList() {
      return this.list;
   }

   public boolean inWhitelist(String name) {
      return this.list.contains(name) || this.list.contains(name.replace("block.minecraft.", "").replace("item.minecraft.", ""));
   }

   public void clear() {
      this.list.clear();
   }

   public void remove(String name) {
      name = name.replace("block.minecraft.", "").replace("item.minecraft.", "");
      if (this.list.remove(name) && !Module.nullCheck() && Xray.INSTANCE.isOn()) {
         mc.field_1769.method_3279();
      }

   }

   public void add(String name) {
      name = name.replace("block.minecraft.", "").replace("item.minecraft.", "");
      if (!this.list.contains(name)) {
         this.list.add(name);
         if (!Module.nullCheck() && Xray.INSTANCE.isOn()) {
            mc.field_1769.method_3279();
         }
      }

   }

   public void read() {
      try {
         File friendFile = getFile("xrays.txt");
         if (!friendFile.exists()) {
            this.add(class_2246.field_10442.method_9539());
            this.add(class_2246.field_29029.method_9539());
            this.add(class_2246.field_10571.method_9539());
            this.add(class_2246.field_23077.method_9539());
            this.add(class_2246.field_10212.method_9539());
            this.add(class_2246.field_29027.method_9539());
            this.add(class_2246.field_10080.method_9539());
            this.add(class_2246.field_10013.method_9539());
            this.add(class_2246.field_29220.method_9539());
            this.add(class_2246.field_29030.method_9539());
            this.add(class_2246.field_10418.method_9539());
            this.add(class_2246.field_29219.method_9539());
            this.add(class_2246.field_22109.method_9539());
            this.add(class_2246.field_10213.method_9539());
            this.add(class_2246.field_10090.method_9539());
            this.add(class_2246.field_29028.method_9539());
            return;
         }

         for(String s : IOUtils.readLines(new FileInputStream(friendFile), StandardCharsets.UTF_8)) {
            this.add(s);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void save() {
      try {
         File friendFile = getFile("xrays.txt");
         PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(friendFile), StandardCharsets.UTF_8));

         for(String str : this.list) {
            printwriter.println(str);
         }

         printwriter.close();
      } catch (Exception var5) {
         System.out.println("[Alien] Failed to save xrays");
      }

   }
}
