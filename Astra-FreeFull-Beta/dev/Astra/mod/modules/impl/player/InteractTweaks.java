package dev.Astra.mod.modules.impl.player;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.asm.accessors.ILivingEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_2199;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2336;
import net.minecraft.class_2596;
import net.minecraft.class_2885;
import net.minecraft.class_418;
import net.minecraft.class_437;

public class InteractTweaks extends Module {
   public static InteractTweaks INSTANCE;
   private final BooleanSetting noJumpDelay = this.add(new BooleanSetting("NoJumpDelay", true));
   public final BooleanSetting noEntityTrace = this.add((new BooleanSetting("NoEntityTrace", true)).setParent());
   public final BooleanSetting onlyPickaxe;
   public final BooleanSetting multiTask;
   public final BooleanSetting respawn;
   private final BooleanSetting noDelay;
   private final BooleanSetting noInteract;
   private final BooleanSetting reach;
   public final SliderSetting blockRange;
   public final SliderSetting entityRange;
   private final SliderSetting delay;
   public boolean isActive;

   public InteractTweaks() {
      super("InteractTweaks", Module.Category.Player);
      BooleanSetting var10006 = this.noEntityTrace;
      Objects.requireNonNull(var10006);
      this.onlyPickaxe = this.add(new BooleanSetting("OnlyPickaxe", true, var10006::isOpen));
      this.multiTask = this.add(new BooleanSetting("MultiTask", true));
      this.respawn = this.add(new BooleanSetting("Respawn", true));
      this.noDelay = this.add(new BooleanSetting("NoMineDelay", false));
      this.noInteract = this.add(new BooleanSetting("NoInteract", false));
      this.reach = this.add(new BooleanSetting("Reach", false));
      BooleanSetting var10009 = this.reach;
      Objects.requireNonNull(var10009);
      this.blockRange = this.add(new SliderSetting("BlockRange", (double)5.0F, (double)0.0F, (double)15.0F, 0.1, var10009::getValue));
      var10009 = this.reach;
      Objects.requireNonNull(var10009);
      this.entityRange = this.add(new SliderSetting("EntityRange", (double)5.0F, (double)0.0F, (double)15.0F, 0.1, var10009::getValue));
      this.delay = this.add(new SliderSetting("UseDelay", (double)4.0F, (double)0.0F, (double)4.0F, (double)1.0F));
      this.setChinese("交互调整");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.respawn.getValue() && mc.field_1755 instanceof class_418) {
         mc.field_1724.method_7331();
         mc.method_1507((class_437)null);
      }

      if (mc.field_1752 <= 4 - this.delay.getValueInt()) {
         mc.field_1752 = 0;
      }

      if (this.noJumpDelay.getValue() && mc.field_1724 != null) {
         ((ILivingEntity)mc.field_1724).setLastJumpCooldown(0);
      }

   }

   @EventListener
   public void onPacket(PacketEvent.Send event) {
      class_2596<?> packet;
      if (!nullCheck() && this.noInteract.getValue() && (packet = event.getPacket()) instanceof class_2885) {
         class_2885 packet2 = (class_2885)packet;
         class_2248 block = mc.field_1687.method_8320(packet2.method_12543().method_17777()).method_26204();
         if (!mc.field_1724.method_5715() && (block instanceof class_2281 || block instanceof class_2336 || block instanceof class_2199)) {
            event.cancel();
         }

      }
   }

   public void onDisable() {
      this.isActive = false;
   }

   public boolean reach() {
      return this.isOn() && this.reach.getValue();
   }

   public boolean noDelay() {
      return this.isOn() && this.noDelay.getValue();
   }

   public boolean multiTask() {
      return this.isOn() && this.multiTask.getValue();
   }

   public boolean noEntityTrace() {
      if (!this.isOff() && this.noEntityTrace.getValue()) {
         if (!this.onlyPickaxe.getValue()) {
            return true;
         } else {
            return mc.field_1724.method_6047().method_7909() instanceof class_1810 || mc.field_1724.method_6115() && !(mc.field_1724.method_6047().method_7909() instanceof class_1829);
         }
      } else {
         return false;
      }
   }
}
