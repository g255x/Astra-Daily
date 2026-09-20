package dev.Astra.core.impl;

import dev.Astra.core.Manager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import net.minecraft.class_1657;
import org.apache.commons.io.IOUtils;

public class FriendManager extends Manager {
   public final ArrayList<String> friendList = new ArrayList();

   public FriendManager() {
      this.read();
   }

   public boolean isFriend(String name) {
      return this.friendList.contains(name);
   }

   public boolean isFriend(class_1657 entity) {
      return this.isFriend(entity.method_7334().getName());
   }

   public void remove(String name) {
      this.friendList.remove(name);
   }

   public void add(String name) {
      if (!this.friendList.contains(name)) {
         this.friendList.add(name);
      }

   }

   public void friend(class_1657 entity) {
      this.friend(entity.method_7334().getName());
   }

   public void friend(String name) {
      if (this.friendList.contains(name)) {
         this.friendList.remove(name);
      } else {
         this.friendList.add(name);
      }

   }

   public void read() {
      try {
         File friendFile = getFile("friends.txt");
         if (!friendFile.exists()) {
            return;
         }

         for(String s : IOUtils.readLines(new FileInputStream(friendFile), StandardCharsets.UTF_8)) {
            this.add(s);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public void save() {
      try {
         File friendFile = getFile("friends.txt");
         PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(friendFile), StandardCharsets.UTF_8));

         try {
            for(String str : this.friendList) {
               printwriter.println(str);
            }
         } catch (Throwable var6) {
            try {
               printwriter.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         printwriter.close();
      } catch (Exception exception) {
         exception.printStackTrace();
         System.out.println("[Astra Client] Failed to save friends");
      }

   }
}
