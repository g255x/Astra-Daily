package dev.Astra.core.impl;

import dev.Astra.core.Manager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import net.minecraft.class_1802;
import org.apache.commons.io.IOUtils;

public class HudItemManager extends Manager {
   private final ArrayList<String> list = new ArrayList();

   public HudItemManager() {
      this.read();
   }

   public ArrayList<String> getList() {
      return this.list;
   }

   public boolean inList(String name) {
      return this.list.contains(name) || this.list.contains(name.replace("block.minecraft.", "").replace("item.minecraft.", ""));
   }

   public void clear() {
      this.list.clear();
   }

   public void remove(String name) {
      name = name.replace("block.minecraft.", "").replace("item.minecraft.", "");
      this.list.remove(name);
   }

   public void add(String name) {
      if (!this.list.contains(name = name.replace("block.minecraft.", "").replace("item.minecraft.", ""))) {
         this.list.add(name);
      }

   }

   public void read() {
      try {
         File file = getFile("hud_items.txt");
         if (!file.exists()) {
            this.add(class_1802.field_8301.method_7876());
            this.add(class_1802.field_8287.method_7876());
            this.add(class_1802.field_8634.method_7876());
            this.add(class_1802.field_8281.method_7876());
            this.add(class_1802.field_8367.method_7876());
            this.add(class_1802.field_8463.method_7876());
            this.add(class_1802.field_8288.method_7876());
            this.add(class_1802.field_8786.method_7876());
            this.add(class_1802.field_23141.method_7876());
            this.add(class_1802.field_8801.method_7876());
            this.add(class_1802.field_8249.method_7876());
            this.add(class_1802.field_8105.method_7876());
            this.add(class_1802.field_8793.method_7876());
            this.add(class_1802.field_8466.method_7876());
            this.add(class_1802.field_8639.method_7876());
            return;
         }

         for(String s : IOUtils.readLines(new FileInputStream(file), StandardCharsets.UTF_8)) {
            this.add(s);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public void save() {
      try {
         File file = getFile("hud_items.txt");
         PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

         for(String str : this.list) {
            printwriter.println(str);
         }

         printwriter.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}
