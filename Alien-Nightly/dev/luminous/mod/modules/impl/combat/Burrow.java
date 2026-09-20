package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2828;
import net.minecraft.class_2885;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3532;
import net.minecraft.class_3965;

public class Burrow extends Module {
   public static Burrow INSTANCE;
   private final EnumSetting<RotateMode> rotate;
   private final EnumSetting<LagBackMode> lagMode;
   private final EnumSetting<LagBackMode> aboveLagMode;
   private final List<class_2338> placePos;
   private final Timer timer;
   private final Timer webTimer;
   private final BooleanSetting disable;
   private final BooleanSetting jumpDisable;
   private final SliderSetting delay;
   private final SliderSetting webTime;
   private final BooleanSetting enderChest;
   private final BooleanSetting antiLag;
   private final BooleanSetting single;
   private final BooleanSetting detectMine;
   private final BooleanSetting headFill;
   private final BooleanSetting usingPause;
   private final BooleanSetting down;
   private final BooleanSetting noSelfPos;
   private final BooleanSetting packetPlace;
   private final BooleanSetting sound;
   private final SliderSetting blocksPer;
   private final BooleanSetting breakCrystal;
   private final BooleanSetting wait;
   private final BooleanSetting fakeMove;
   private final BooleanSetting center;
   private final SliderSetting preCorrect;
   private final SliderSetting moveDis;
   private final SliderSetting moveDis2;
   private final SliderSetting moveCorrect2;
   private final SliderSetting yOffset;
   private final BooleanSetting inventory;
   private final SliderSetting smartX;
   private final SliderSetting smartUp;
   private final SliderSetting smartDown;
   private final SliderSetting smartDistance;
   private int progress;
   private class_243 currentPos;

   public Burrow() {
      super("Burrow", Module.Category.Combat);
      this.rotate = this.add(new EnumSetting("RotateMode", Burrow.RotateMode.Bypass));
      this.lagMode = this.add(new EnumSetting("LagMode", Burrow.LagBackMode.TrollHack));
      this.aboveLagMode = this.add(new EnumSetting("MoveLagMode", Burrow.LagBackMode.Smart));
      this.placePos = new ArrayList();
      this.timer = new Timer();
      this.webTimer = new Timer();
      this.disable = this.add(new BooleanSetting("Disable", true));
      this.jumpDisable = this.add(new BooleanSetting("JumpDisable", true, () -> !this.disable.getValue()));
      this.delay = this.add(new SliderSetting("Delay", 500, 0, 1000, () -> !this.disable.getValue()));
      this.webTime = this.add(new SliderSetting("WebTime", 0, 0, 500));
      this.enderChest = this.add(new BooleanSetting("EnderChest", true));
      this.antiLag = this.add(new BooleanSetting("AntiLag", false));
      this.single = this.add(new BooleanSetting("SingleBlock", false));
      this.detectMine = this.add(new BooleanSetting("DetectMining", false));
      this.headFill = this.add(new BooleanSetting("HeadFill", false));
      this.usingPause = this.add(new BooleanSetting("UsingPause", false));
      this.down = this.add(new BooleanSetting("Down", true));
      this.noSelfPos = this.add(new BooleanSetting("NoSelfPos", false));
      this.packetPlace = this.add(new BooleanSetting("PacketPlace", true));
      this.sound = this.add(new BooleanSetting("Sound", true));
      this.blocksPer = this.add(new SliderSetting("BlocksPer", (double)4.0F, (double)1.0F, (double)4.0F, (double)1.0F));
      this.breakCrystal = this.add(new BooleanSetting("Break", true));
      BooleanSetting var10006 = this.disable;
      Objects.requireNonNull(var10006);
      this.wait = this.add(new BooleanSetting("Wait", true, var10006::getValue));
      this.fakeMove = this.add((new BooleanSetting("FakeMove", true)).setParent());
      var10006 = this.fakeMove;
      Objects.requireNonNull(var10006);
      this.center = this.add(new BooleanSetting("AllowCenter", true, var10006::isOpen));
      BooleanSetting var10009 = this.fakeMove;
      Objects.requireNonNull(var10009);
      this.preCorrect = this.add(new SliderSetting("PreCorrect", (double)0.25F, (double)0.0F, (double)1.0F, 0.001, var10009::isOpen));
      var10009 = this.fakeMove;
      Objects.requireNonNull(var10009);
      this.moveDis = this.add(new SliderSetting("MoveDis", (double)0.25F, (double)0.0F, (double)1.0F, 0.001, var10009::isOpen));
      var10009 = this.fakeMove;
      Objects.requireNonNull(var10009);
      this.moveDis2 = this.add(new SliderSetting("MoveDis2", (double)0.25F, (double)0.0F, (double)1.0F, 0.001, var10009::isOpen));
      var10009 = this.fakeMove;
      Objects.requireNonNull(var10009);
      this.moveCorrect2 = this.add(new SliderSetting("Correct", (double)0.25F, (double)0.0F, (double)1.0F, 0.001, var10009::isOpen));
      var10009 = this.fakeMove;
      Objects.requireNonNull(var10009);
      this.yOffset = this.add(new SliderSetting("YOffset", 0.01, (double)0.0F, (double)1.0F, 0.001, var10009::isOpen));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true));
      this.smartX = this.add(new SliderSetting("SmartXZ", (double)3.0F, (double)0.0F, (double)10.0F, 0.1, () -> this.lagMode.getValue() == Burrow.LagBackMode.Smart || this.aboveLagMode.getValue() == Burrow.LagBackMode.Smart));
      this.smartUp = this.add(new SliderSetting("SmartUp", (double)3.0F, (double)0.0F, (double)10.0F, 0.1, () -> this.lagMode.getValue() == Burrow.LagBackMode.Smart || this.aboveLagMode.getValue() == Burrow.LagBackMode.Smart));
      this.smartDown = this.add(new SliderSetting("SmartDown", (double)3.0F, (double)0.0F, (double)10.0F, 0.1, () -> this.lagMode.getValue() == Burrow.LagBackMode.Smart || this.aboveLagMode.getValue() == Burrow.LagBackMode.Smart));
      this.smartDistance = this.add(new SliderSetting("SmartDistance", (double)2.0F, (double)0.0F, (double)10.0F, 0.1, () -> this.lagMode.getValue() == Burrow.LagBackMode.Smart || this.aboveLagMode.getValue() == Burrow.LagBackMode.Smart));
      this.progress = 0;
      this.setChinese("卡黑曜石");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(ClientTickEvent event) {
      if ((!this.inventory.getValue() || EntityUtil.inInventory()) && event.isPost()) {
         if (!this.disable.getValue() && this.jumpDisable.getValue() && mc.field_1724.field_3913.field_3904) {
            this.disable();
            return;
         }

         if (Alien.PLAYER.isInWeb(mc.field_1724)) {
            this.webTimer.reset();
            return;
         }

         if (this.usingPause.getValue() && mc.field_1724.method_6115()) {
            return;
         }

         if (!this.webTimer.passedMs(this.webTime.getValue())) {
            return;
         }

         if (!this.disable.getValue() && !this.timer.passedMs(this.delay.getValue())) {
            return;
         }

         if (!mc.field_1724.method_24828()) {
            return;
         }

         if (this.antiLag.getValue() && !BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10074()))) {
            return;
         }

         if (this.single.getValue() && EntityUtil.isInsideBlock()) {
            if (this.disable.getValue()) {
               this.disable();
            }

            return;
         }

         if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
            return;
         }

         int oldSlot = mc.field_1724.method_31548().field_7545;
         int block;
         if ((block = this.getBlock()) == -1) {
            CommandManager.sendMessageId("§4No block found.", this.hashCode() - 1);
            this.disable();
            return;
         }

         this.progress = 0;
         this.placePos.clear();
         double offset = this.single.getValue() ? (double)0.0F : AntiCheat.getOffset();
         class_2338 pos1 = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + offset);
         class_2338 pos2 = new BlockPosX(mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + offset);
         class_2338 pos3 = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() - offset);
         class_2338 pos4 = new BlockPosX(mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() - offset);
         class_2338 pos5 = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + (double)1.5F, mc.field_1724.method_23321() + offset);
         class_2338 pos6 = new BlockPosX(mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + (double)1.5F, mc.field_1724.method_23321() + offset);
         class_2338 pos7 = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + (double)1.5F, mc.field_1724.method_23321() - offset);
         class_2338 pos8 = new BlockPosX(mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() + (double)1.5F, mc.field_1724.method_23321() - offset);
         class_2338 pos9 = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() - (double)1.0F, mc.field_1724.method_23321() + offset);
         class_2338 pos10 = new BlockPosX(mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() - (double)1.0F, mc.field_1724.method_23321() + offset);
         class_2338 pos11 = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() - (double)1.0F, mc.field_1724.method_23321() - offset);
         class_2338 pos12 = new BlockPosX(mc.field_1724.method_23317() - offset, mc.field_1724.method_23318() - (double)1.0F, mc.field_1724.method_23321() - offset);
         class_2338 playerPos = EntityUtil.getPlayerPos(true);
         boolean headFill = false;
         if (!this.canPlace(pos1) && !this.canPlace(pos2) && !this.canPlace(pos3) && !this.canPlace(pos4)) {
            boolean cantHeadFill = !this.headFill.getValue() || !this.canPlace(pos5) && !this.canPlace(pos6) && !this.canPlace(pos7) && !this.canPlace(pos8);
            boolean cantDown = !this.down.getValue() || !this.canPlace(pos9) && !this.canPlace(pos10) && !this.canPlace(pos11) && !this.canPlace(pos12);
            if (cantHeadFill) {
               if (cantDown) {
                  if (!this.wait.getValue() && this.disable.getValue()) {
                     this.disable();
                  }

                  return;
               }
            } else {
               headFill = true;
            }
         }

         boolean above = false;
         class_2338 headPos = EntityUtil.getPlayerPos(true).method_10086(2);
         boolean rotate = this.rotate.getValue() == Burrow.RotateMode.Normal;
         CombatUtil.attackCrystal(pos1, rotate, false);
         CombatUtil.attackCrystal(pos2, rotate, false);
         CombatUtil.attackCrystal(pos3, rotate, false);
         CombatUtil.attackCrystal(pos4, rotate, false);
         if (!headFill && !mc.field_1724.method_20448() && !this.trapped(headPos) && !this.trapped(headPos.method_10069(1, 0, 0)) && !this.trapped(headPos.method_10069(-1, 0, 0)) && !this.trapped(headPos.method_10069(0, 0, 1)) && !this.trapped(headPos.method_10069(0, 0, -1)) && !this.trapped(headPos.method_10069(1, 0, -1)) && !this.trapped(headPos.method_10069(-1, 0, -1)) && !this.trapped(headPos.method_10069(1, 0, 1)) && !this.trapped(headPos.method_10069(-1, 0, 1))) {
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.4199999868869781, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.7531999805212017, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.9999957640154541, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.1661092609382138, mc.field_1724.method_23321(), false));
            this.currentPos = new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.1661092609382138, mc.field_1724.method_23321());
         } else {
            above = true;
            if (!this.fakeMove.getValue()) {
               if (!this.wait.getValue() && this.disable.getValue()) {
                  this.disable();
               }

               return;
            }

            if (!this.fakeMove()) {
               return;
            }
         }

         this.timer.reset();
         this.doSwap(block);
         if (this.rotate.getValue() == Burrow.RotateMode.Bypass) {
            if (above) {
               float[] angle = RotationManager.getRotation(this.currentPos.method_1031((double)0.0F, (double)mc.field_1724.method_18381(mc.field_1724.method_18376()), (double)0.0F), mc.field_1724.method_19538());
               Alien.ROTATION.snapAt(angle[0], angle[1]);
            } else {
               Alien.ROTATION.snapAt(Alien.ROTATION.rotationYaw, 90.0F);
            }
         }

         this.placeBlock(playerPos, rotate);
         this.placeBlock(pos1, rotate);
         this.placeBlock(pos2, rotate);
         this.placeBlock(pos3, rotate);
         this.placeBlock(pos4, rotate);
         if (this.down.getValue()) {
            this.placeBlock(pos9, rotate);
            this.placeBlock(pos10, rotate);
            this.placeBlock(pos11, rotate);
            this.placeBlock(pos12, rotate);
         }

         if (this.headFill.getValue() && above) {
            this.placeBlock(pos5, rotate);
            this.placeBlock(pos6, rotate);
            this.placeBlock(pos7, rotate);
            this.placeBlock(pos8, rotate);
         }

         if (this.inventory.getValue()) {
            this.doSwap(block);
            EntityUtil.syncInventory();
         } else {
            this.doSwap(oldSlot);
         }

         switch ((above ? (LagBackMode)this.aboveLagMode.getValue() : (LagBackMode)this.lagMode.getValue()).ordinal()) {
            case 0:
               ArrayList<class_2338> list = new ArrayList();

               for(double x = mc.field_1724.method_19538().method_10216() - this.smartX.getValue(); x < mc.field_1724.method_19538().method_10216() + this.smartX.getValue(); ++x) {
                  for(double z = mc.field_1724.method_19538().method_10215() - this.smartX.getValue(); z < mc.field_1724.method_19538().method_10215() + this.smartX.getValue(); ++z) {
                     for(double y = mc.field_1724.method_19538().method_10214() - this.smartDown.getValue(); y < mc.field_1724.method_19538().method_10214() + this.smartUp.getValue(); ++y) {
                        list.add(new BlockPosX(x, y, z));
                     }
                  }
               }

               double distance = (double)0.0F;
               class_2338 bestPos = null;

               for(class_2338 pos : list) {
                  if (this.canMove(pos) && !((double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558().method_1031((double)0.0F, (double)-0.5F, (double)0.0F))) < this.smartDistance.getValue()) && (bestPos == null || mc.field_1724.method_5707(pos.method_46558()) < distance)) {
                     bestPos = pos;
                     distance = mc.field_1724.method_5707(pos.method_46558());
                  }
               }

               if (bestPos != null) {
                  mc.method_1562().method_52787(new class_2828.class_2829((double)bestPos.method_10263() + (double)0.5F, (double)bestPos.method_10264(), (double)bestPos.method_10260() + (double)0.5F, false));
               }
               break;
            case 1:
               for(int i = 0; i < 20; ++i) {
                  mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)1337.0F, mc.field_1724.method_23321(), false));
               }
               break;
            case 2:
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.3400880035762786, mc.field_1724.method_23321(), false));
               break;
            case 3:
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), (double)-70.0F, mc.field_1724.method_23321(), false));
               break;
            case 4:
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), (double)-7.0F, mc.field_1724.method_23321(), false));
               break;
            case 5:
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.9, mc.field_1724.method_23321(), false));
               break;
            case 6:
               mc.method_1562().method_52787(new class_2828.class_2831(-180.0F, -90.0F, false));
               mc.method_1562().method_52787(new class_2828.class_2831(180.0F, 90.0F, false));
               break;
            case 7:
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.16610926093821, mc.field_1724.method_23321(), false));
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.170005801788139, mc.field_1724.method_23321(), false));
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.2426308013947485, mc.field_1724.method_23321(), false));
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.3400880035762786, mc.field_1724.method_23321(), false));
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.640088003576279, mc.field_1724.method_23321(), false));
               break;
            case 8:
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.1001, mc.field_1724.method_23321(), false));
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0605, mc.field_1724.method_23321(), false));
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0802, mc.field_1724.method_23321(), false));
               mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.1127, mc.field_1724.method_23321(), false));
         }

         if (this.disable.getValue()) {
            this.disable();
         }
      }

   }

   private boolean fakeMove() {
      double[] offsets = new double[]{(double)1.0F, (double)0.0F, (double)-1.0F};
      List<class_2338> offList = new ArrayList();
      class_2338 playerPos = mc.field_1724.method_24515();

      for(double x : offsets) {
         for(double z : offsets) {
            offList.add(new BlockPosX(mc.field_1724.method_23317() + x, mc.field_1724.method_23318(), mc.field_1724.method_23321() + z));
         }
      }

      for(class_2338 offPos : offList) {
         if (this.checkSelf(offPos) && !BlockUtil.canReplace(offPos) && (!this.headFill.getValue() || !BlockUtil.canReplace(offPos.method_10084()))) {
            this.gotoPos(offPos);
            return true;
         }
      }

      List<class_2338> pos = new ArrayList();

      for(class_2338 offPosx : offList) {
         if (!playerPos.equals(offPosx) && this.checkSelf(offPosx) && this.canMove(offPosx)) {
            pos.add(offPosx);
         }
      }

      if (!pos.isEmpty()) {
         double dis = (double)10.0F;
         class_2338 target = null;

         for(class_2338 p : pos) {
            double distance = mc.field_1724.method_19538().method_1022(p.method_46558().method_1031((double)0.0F, (double)-0.5F, (double)0.0F));
            if (distance < dis || target == null) {
               target = p;
               dis = distance;
            }
         }

         this.gotoPos(target);
         return true;
      } else {
         for(class_2338 offPosxx : offList) {
            if (!playerPos.equals(offPosxx) && this.checkSelf(offPosxx)) {
               pos.add(offPosxx);
            }
         }

         if (pos.isEmpty()) {
            if (!this.center.getValue()) {
               return false;
            } else {
               offList.clear();
               offList.add(new BlockPosX(mc.field_1724.method_23317() + (double)1.0F, mc.field_1724.method_23318(), mc.field_1724.method_23321()));
               offList.add(new BlockPosX(mc.field_1724.method_23317() - (double)1.0F, mc.field_1724.method_23318(), mc.field_1724.method_23321()));
               offList.add(new BlockPosX(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321() - (double)1.0F));
               offList.add(new BlockPosX(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321() + (double)1.0F));

               for(class_2338 offPosxxx : offList) {
                  if (this.canMove(offPosxxx)) {
                     this.gotoPos(offPosxxx);
                     return true;
                  }
               }

               if (!this.wait.getValue() && this.disable.getValue()) {
                  this.disable();
               }

               return false;
            }
         } else {
            double dis = (double)10.0F;
            class_2338 target = null;

            for(class_2338 px : pos) {
               double distance = mc.field_1724.method_19538().method_1022(px.method_46558().method_1031((double)0.0F, (double)-0.5F, (double)0.0F));
               if (distance < dis || target == null) {
                  target = px;
                  dis = distance;
               }
            }

            this.gotoPos(target);
            return true;
         }
      }
   }

   private void placeBlock(class_2338 pos, boolean rotate) {
      if (this.canPlace(pos) && !this.placePos.contains(pos) && this.progress < this.blocksPer.getValueInt()) {
         this.placePos.add(pos);
         if (BlockUtil.allowAirPlace()) {
            ++this.progress;
            BlockUtil.placedPos.add(pos);
            if (this.sound.getValue()) {
               mc.field_1687.method_8396(mc.field_1724, pos, class_3417.field_14574, class_3419.field_15245, 1.0F, 0.8F);
            }

            this.clickBlock(pos, class_2350.field_11033, rotate, class_1268.field_5808, this.packetPlace.getValue());
         }

         class_2350 side;
         if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return;
         }

         ++this.progress;
         BlockUtil.placedPos.add(pos);
         if (this.sound.getValue()) {
            mc.field_1687.method_8396(mc.field_1724, pos, class_3417.field_14574, class_3419.field_15245, 1.0F, 0.8F);
         }

         this.clickBlock(pos.method_10093(side), side.method_10153(), rotate, class_1268.field_5808, this.packetPlace.getValue());
      }

   }

   public void clickBlock(class_2338 pos, class_2350 side, boolean rotate, class_1268 hand, boolean packet) {
      class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
      if (rotate) {
         float[] angle = RotationManager.getRotation(this.currentPos.method_1031((double)0.0F, (double)mc.field_1724.method_18381(mc.field_1724.method_18376()), (double)0.0F), directionVec);
         Alien.ROTATION.snapAt(angle[0], angle[1]);
      }

      EntityUtil.swingHand(hand, AntiCheat.INSTANCE.interactSwing.getValue());
      class_3965 result = new class_3965(directionVec, side, pos, false);
      if (packet) {
         Module.sendSequencedPacket((id) -> new class_2885(hand, result, id));
      } else {
         mc.field_1761.method_2896(mc.field_1724, hand, result);
      }

      if (rotate) {
         Alien.ROTATION.snapBack();
      }

   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private void gotoPos(class_2338 offPos) {
      double targetX = (double)offPos.method_10263() + (double)0.5F;
      double targetZ = (double)offPos.method_10260() + (double)0.5F;
      double x = mc.field_1724.method_23317();
      double z = mc.field_1724.method_23321();
      double y = mc.field_1724.method_23318() + this.yOffset.getValue();
      double xDiff = Math.abs(x - targetX);
      double zDiff = Math.abs(z - targetZ);
      double moveDis = this.preCorrect.getValue();
      if (moveDis > (double)0.0F) {
         if (xDiff >= moveDis) {
            if (x > targetX) {
               x -= moveDis;
            } else {
               x += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
         }

         if (zDiff >= moveDis) {
            if (z > targetZ) {
               z -= moveDis;
            } else {
               z += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
         }
      }

      xDiff = Math.abs(x - targetX);
      zDiff = Math.abs(z - targetZ);
      moveDis = this.moveDis.getValue();
      if (moveDis > (double)0.0F) {
         while(xDiff > moveDis) {
            if (x > targetX) {
               x -= moveDis;
            } else {
               x += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
            xDiff = Math.abs(x - targetX);
         }

         while(zDiff > moveDis) {
            if (z > targetZ) {
               z -= moveDis;
            } else {
               z += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
            zDiff = Math.abs(z - targetZ);
         }
      }

      moveDis = this.moveDis2.getValue();
      if (moveDis > (double)0.0F) {
         while(xDiff > moveDis) {
            if (x > targetX) {
               x -= moveDis;
            } else {
               x += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
            xDiff = Math.abs(x - targetX);
         }

         while(zDiff > moveDis) {
            if (z > targetZ) {
               z -= moveDis;
            } else {
               z += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
            zDiff = Math.abs(z - targetZ);
         }
      }

      moveDis = this.moveCorrect2.getValue();
      if (moveDis > (double)0.0F) {
         if (xDiff >= moveDis) {
            if (x > targetX) {
               x -= moveDis;
            } else {
               x += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
         }

         if (zDiff >= moveDis) {
            if (z > targetZ) {
               z -= moveDis;
            } else {
               z += moveDis;
            }

            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, y, z, false));
         }
      }

      this.currentPos = new class_243(x, y, z);
   }

   private boolean canMove(class_2338 pos) {
      return mc.field_1687.method_22347(pos) && mc.field_1687.method_22347(pos.method_10084());
   }

   private boolean canPlace(class_2338 pos) {
      if (this.noSelfPos.getValue() && pos.equals(EntityUtil.getPlayerPos(true))) {
         return false;
      } else if (!BlockUtil.allowAirPlace() && BlockUtil.getPlaceSide(pos) == null) {
         return false;
      } else if (!BlockUtil.canReplace(pos)) {
         return false;
      } else {
         return this.detectMine.getValue() && Alien.BREAK.isMining(pos) ? false : !this.hasEntity(pos);
      }
   }

   private boolean hasEntity(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity != mc.field_1724 && entity.method_5805() && !(entity instanceof class_1542) && !(entity instanceof class_1303) && !(entity instanceof class_1683) && !(entity instanceof class_1667) && (!(entity instanceof class_1511) || !this.breakCrystal.getValue())) {
            return true;
         }
      }

      return false;
   }

   private boolean checkSelf(class_2338 pos) {
      return mc.field_1724.method_5829().method_994(new class_238(pos));
   }

   private boolean trapped(class_2338 pos) {
      return (BlockUtil.canCollide(mc.field_1724, new class_238(pos)) || BlockUtil.getBlock(pos) == class_2246.field_10343) && this.checkSelf(pos.method_10087(2));
   }

   private int getBlock() {
      if (this.inventory.getValue()) {
         return InventoryUtil.findBlockInventorySlot(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10443) : InventoryUtil.findBlockInventorySlot(class_2246.field_10540);
      } else {
         return InventoryUtil.findBlock(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlock(class_2246.field_10443) : InventoryUtil.findBlock(class_2246.field_10540);
      }
   }

   private static enum LagBackMode {
      Smart,
      Invalid,
      TrollHack,
      ToVoid,
      ToVoid2,
      Normal,
      Rotation,
      Fly,
      Glide;

      // $FF: synthetic method
      private static LagBackMode[] $values() {
         return new LagBackMode[]{Smart, Invalid, TrollHack, ToVoid, ToVoid2, Normal, Rotation, Fly, Glide};
      }
   }

   private static enum RotateMode {
      Bypass,
      Normal,
      None;

      // $FF: synthetic method
      private static RotateMode[] $values() {
         return new RotateMode[]{Bypass, Normal, None};
      }
   }
}
