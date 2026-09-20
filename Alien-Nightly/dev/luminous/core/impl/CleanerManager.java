package dev.luminous.core.impl;

import dev.luminous.core.Manager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import net.minecraft.class_1802;
import org.apache.commons.io.IOUtils;

public class CleanerManager extends Manager {
   private final ArrayList<String> list = new ArrayList();

   public CleanerManager() {
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
      name = name.replace("block.minecraft.", "").replace("item.minecraft.", "");
      if (!this.list.contains(name)) {
         this.list.add(name);
      }

   }

   public void read() {
      try {
         File friendFile = getFile("cleaner.txt");
         if (!friendFile.exists()) {
            this.add(class_1802.field_22022.method_7876());
            this.add(class_1802.field_22024.method_7876());
            this.add(class_1802.field_22027.method_7876());
            this.add(class_1802.field_22028.method_7876());
            this.add(class_1802.field_22029.method_7876());
            this.add(class_1802.field_22030.method_7876());
            this.add(class_1802.field_8281.method_7876());
            this.add(class_1802.field_8466.method_7876());
            this.add(class_1802.field_8634.method_7876());
            this.add(class_1802.field_8367.method_7876());
            this.add(class_1802.field_8287.method_7876());
            this.add(class_1802.field_8786.method_7876());
            this.add(class_1802.field_8574.method_7876());
            this.add(class_1802.field_8436.method_7876());
            this.add(class_1802.field_8288.method_7876());
            this.add(class_1802.field_8301.method_7876());
            this.add(class_1802.field_8833.method_7876());
            this.add(class_1802.field_8884.method_7876());
            this.add(class_1802.field_8249.method_7876());
            this.add(class_1802.field_8105.method_7876());
            this.add(class_1802.field_8793.method_7876());
            this.add(class_1802.field_8801.method_7876());
            this.add(class_1802.field_23141.method_7876());
            this.add(class_1802.field_8782.method_7876());
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
         File friendFile = getFile("cleaner.txt");
         PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(friendFile), StandardCharsets.UTF_8));

         for(String str : this.list) {
            printwriter.println(str);
         }

         printwriter.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
