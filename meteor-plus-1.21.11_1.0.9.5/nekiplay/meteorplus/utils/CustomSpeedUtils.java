package nekiplay.meteorplus.utils;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.movement.Anchor;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.class_1294;
import net.minecraft.class_243;

public class CustomSpeedUtils {
   public static void applySpeed(PlayerMoveEvent event, double speed) {
      class_243 vel = PlayerUtils.getHorizontalVelocity(speed);
      double velX = vel.method_10216();
      double velZ = vel.method_10215();
      if (MeteorClient.mc.field_1724.method_6059(class_1294.field_5904)) {
         double value = (double)(MeteorClient.mc.field_1724.method_6112(class_1294.field_5904).method_5578() + 1) * 0.205;
         velX += velX * value;
         velZ += velZ * value;
      }

      Anchor anchor = (Anchor)Modules.get().get(Anchor.class);
      if (anchor.isActive() && anchor.controlMovement) {
         velX = anchor.deltaX;
         velZ = anchor.deltaZ;
      }

      ((IVec3d)event.movement).meteor$set(velX, event.movement.field_1351, velZ);
   }
}
