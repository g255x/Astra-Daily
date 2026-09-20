package dev.luminous.core.impl;

import dev.luminous.api.interfaces.IClientPlayNetworkHandler;
import dev.luminous.asm.accessors.AccessorClientWorld;
import dev.luminous.core.Manager;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_2596;
import net.minecraft.class_2792;
import net.minecraft.class_7202;
import net.minecraft.class_7204;

public class NetworkManager extends Manager {
   private static final Set<class_2596<?>> PACKET_CACHE = new HashSet();

   public void sendPacket(class_2596<?> p) {
      if (mc.method_1562() != null) {
         PACKET_CACHE.add(p);
         mc.method_1562().method_52787(p);
      }

   }

   public void sendQuietPacket(class_2596<?> p) {
      if (mc.method_1562() != null) {
         PACKET_CACHE.add(p);
         ((IClientPlayNetworkHandler)mc.method_1562()).sendQuietPacket(p);
      }

   }

   public void sendSequencedPacket(class_7204 p) {
      if (mc.field_1687 != null) {
         class_7202 updater = ((AccessorClientWorld)mc.field_1687).hookGetPendingUpdateManager().method_41937();

         try {
            int i = updater.method_41942();
            class_2596<class_2792> packet = p.predict(i);
            this.sendPacket(packet);
         } catch (Throwable var6) {
            var6.printStackTrace();
            if (updater != null) {
               try {
                  updater.close();
               } catch (Throwable e1) {
                  e1.printStackTrace();
                  var6.addSuppressed(e1);
               }
            }

            throw var6;
         }

         if (updater != null) {
            updater.close();
         }
      }

   }
}
