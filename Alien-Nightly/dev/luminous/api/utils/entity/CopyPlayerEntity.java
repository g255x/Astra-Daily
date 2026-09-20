package dev.luminous.api.utils.entity;

import com.mojang.authlib.GameProfile;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.PredictUtil;
import dev.luminous.asm.accessors.ILivingEntity;
import java.util.ArrayList;
import net.minecraft.class_1293;
import net.minecraft.class_1657;

public class CopyPlayerEntity extends class_1657 {
   private final boolean onGround;

   public CopyPlayerEntity(class_1657 player) {
      this(player, false, (double)0.0F, 0, 0, false, false, false, false);
   }

   public CopyPlayerEntity(class_1657 player, boolean effect) {
      super(Wrapper.mc.field_1687, player.method_24515(), player.method_36454(), new GameProfile(player.method_7334().getId(), player.method_7334().getName()));
      this.method_5719(player);
      this.field_6014 = player.field_6014;
      this.field_5969 = player.field_5969;
      this.field_6036 = player.field_6036;
      this.field_6283 = player.field_6283;
      this.field_6241 = player.field_6241;
      this.field_6251 = player.field_6251;
      this.field_6279 = player.field_6279;
      this.field_42108.method_48567(player.field_42108.method_48566());
      this.field_42108.field_42111 = player.field_42108.method_48569();
      ((ILivingEntity)this).setLeaningPitch(((ILivingEntity)player).getLeaningPitch());
      ((ILivingEntity)this).setLastLeaningPitch(((ILivingEntity)player).getLeaningPitch());
      this.field_5957 = player.method_5799();
      this.method_5660(player.method_5715());
      this.method_18380(player.method_18376());
      this.method_5729(7, player.method_6128());
      this.onGround = player.method_24828();
      this.method_24830(this.onGround);
      this.method_18799(player.method_18798());
      this.method_31548().method_7377(player.method_31548());
      if (effect) {
         for(class_1293 se : new ArrayList(player.method_6026())) {
            this.method_6092(se);
         }
      }

      this.method_52544(player.method_6067());
      this.method_6033(player.method_6032());
      this.method_5857(player.method_5829());
   }

   public CopyPlayerEntity(class_1657 player, boolean effect, double maxMotionY, int ticks, int simulation, boolean step, boolean doubleStep, boolean jump, boolean inBlockPause) {
      this(player, effect);
      if (ticks > 0) {
         this.method_33574(PredictUtil.getPos(player, maxMotionY, ticks, simulation, step, doubleStep, jump, inBlockPause));
      }

   }

   public boolean method_24828() {
      return this.onGround;
   }

   public boolean method_7325() {
      return false;
   }

   public boolean method_7337() {
      return false;
   }
}
