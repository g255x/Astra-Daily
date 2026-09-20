package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.ILivingEntity;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.gui.windows.WindowBase;
import dev.luminous.mod.gui.windows.WindowsScreen;
import dev.luminous.mod.gui.windows.impl.ItemSelectWindow;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.combat.Aura;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.impl.render.PlaceRender;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1481;
import net.minecraft.class_1501;
import net.minecraft.class_1533;
import net.minecraft.class_1542;
import net.minecraft.class_1646;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1714;
import net.minecraft.class_1724;
import net.minecraft.class_1728;
import net.minecraft.class_1733;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1821;
import net.minecraft.class_1829;
import net.minecraft.class_1914;
import net.minecraft.class_1916;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2482;
import net.minecraft.class_2596;
import net.minecraft.class_2627;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2851;
import net.minecraft.class_2863;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_3545;
import net.minecraft.class_465;
import net.minecraft.class_490;
import net.minecraft.class_516;
import net.minecraft.class_7439;
import net.minecraft.class_8786;
import net.minecraft.class_2846.class_2847;

public class Bot extends Module {
   public static Bot INSTANCE;
   public final EnumSetting<Mode> mode;
   private final BooleanSetting autoEat;
   private final SliderSetting hunger;
   private final SliderSetting health;
   private final BooleanSetting anyFood;
   private final BooleanSetting rotate;
   private final SliderSetting delay;
   private final BooleanSetting dropTridents;
   private final BooleanSetting durabilityManagement;
   public final BooleanSetting edit;
   public final SliderSetting repeatSetting;
   public final BooleanSetting autoCloseSetting;
   public final BooleanSetting timeoutCloseSetting;
   public final SliderSetting timeOutSetting;
   public final BooleanSetting autoOpenSetting;
   private final SliderSetting range;
   private final BooleanSetting inventory;
   private final BooleanSetting ai;
   private final BooleanSetting nuker;
   private final BooleanSetting redSand;
   private final SliderSetting breaks;
   private final SliderSetting maxTime;
   public final SliderSetting placeDelay;
   private final SliderSetting blocksPer;
   private final BooleanSetting usingPause;
   private final StringSetting name;
   private final BooleanSetting getScore;
   private final Timer commandTimer;
   private final Timer duelItemTimer;
   final List<class_2338> emptyBox;
   final List<class_1481> inLove;
   final Timer timeOut;
   final Timer closeScreen;
   final Timer openTimeOut;
   final Timer putTimer;
   final Timer ominousTimer;
   Stage stage;
   class_2338 boxPos;
   boolean closeToBox;
   boolean putIn;
   class_1501 llama;
   boolean storageSand;
   boolean endEat;
   final Timer craftTimer;
   final Timer screenTimeout;
   int lastSlot;
   int tick;
   private final List<class_1646> tradedVillager;
   private final Timer timeoutTimer;
   int placeProgress;
   private final Timer slabPlacerDelay;
   private boolean cancel;
   private final List<class_3545<Long, Runnable>> scheduledTasks;
   private final List<class_3545<Long, Runnable>> scheduledTasks2;

   public Bot() {
      super("Bot", Module.Category.Misc);
      this.mode = this.add(new EnumSetting("Mode", Bot.Mode.AutoTrade));
      this.autoEat = this.add((new BooleanSetting("AutoEat", true)).setParent());
      BooleanSetting var10009 = this.autoEat;
      Objects.requireNonNull(var10009);
      this.hunger = this.add(new SliderSetting("Hunger", (double)10.0F, (double)0.0F, (double)20.0F, (double)1.0F, var10009::isOpen));
      var10009 = this.autoEat;
      Objects.requireNonNull(var10009);
      this.health = this.add(new SliderSetting("Health", (double)20.0F, (double)0.0F, (double)36.0F, 0.1, var10009::isOpen));
      BooleanSetting var10006 = this.autoEat;
      Objects.requireNonNull(var10006);
      this.anyFood = this.add(new BooleanSetting("AnyFood", false, var10006::isOpen));
      this.rotate = this.add(new BooleanSetting("Rotate", true));
      this.delay = this.add(new SliderSetting("Delay", 5, 0, 100, () -> this.mode.is(Bot.Mode.TridentDupe)));
      this.dropTridents = this.add(new BooleanSetting("DropTridents", true, () -> this.mode.is(Bot.Mode.TridentDupe)));
      this.durabilityManagement = this.add(new BooleanSetting("DurabilityManagement", true, () -> this.mode.is(Bot.Mode.TridentDupe)));
      this.edit = this.add((new BooleanSetting("Edit", false, () -> this.mode.is(Bot.Mode.AutoTrade))).injectTask(this::openAutoTradeEdit));
      this.repeatSetting = this.add(new SliderSetting("Repeat", (double)2.0F, (double)1.0F, (double)15.0F, (double)1.0F, () -> this.mode.is(Bot.Mode.AutoTrade)));
      this.autoCloseSetting = this.add(new BooleanSetting("AutoClose", true, () -> this.mode.is(Bot.Mode.AutoTrade)));
      this.timeoutCloseSetting = this.add(new BooleanSetting("TimeoutClose", true, () -> this.mode.is(Bot.Mode.AutoTrade)));
      this.timeOutSetting = this.add(new SliderSetting("Timeout", (double)1.0F, (double)0.0F, (double)15.0F, 0.1, () -> this.mode.is(Bot.Mode.AutoTrade)));
      this.autoOpenSetting = this.add(new BooleanSetting("AutoOpen", true, () -> this.mode.is(Bot.Mode.AutoTrade)));
      this.range = this.add(new SliderSetting("Range", (double)4.0F, (double)0.0F, (double)8.0F, 0.1, () -> this.mode.is(Bot.Mode.SlabPlacer) || this.mode.is(Bot.Mode.AutoTrade)));
      this.inventory = this.add(new BooleanSetting("Inventory", true, () -> this.mode.is(Bot.Mode.NPlusOneDupe)));
      this.ai = this.add(new BooleanSetting("AI", true, () -> this.mode.is(Bot.Mode.SandMiner)));
      this.nuker = this.add(new BooleanSetting("Nuker", true, () -> this.mode.is(Bot.Mode.SandMiner)));
      this.redSand = this.add(new BooleanSetting("RedSand", false, () -> this.mode.is(Bot.Mode.SandMiner)));
      this.breaks = this.add(new SliderSetting("Breaks", 10, 0, 20, () -> this.mode.is(Bot.Mode.SandMiner)));
      this.maxTime = this.add(new SliderSetting("MaxTime", 60, 0, 100, () -> this.mode.is(Bot.Mode.Ominous)));
      this.placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> this.mode.is(Bot.Mode.SlabPlacer)));
      this.blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, () -> this.mode.is(Bot.Mode.SlabPlacer)));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.mode.is(Bot.Mode.SlabPlacer)));
      this.name = this.add(new StringSetting("Name", "KizuatoResu", () -> this.mode.is(Bot.Mode.ScoreFarmer)));
      this.getScore = this.add(new BooleanSetting("GetScore", false, () -> this.mode.is(Bot.Mode.ScoreFarmer)));
      this.commandTimer = new Timer();
      this.duelItemTimer = new Timer();
      this.emptyBox = new ArrayList();
      this.inLove = new ArrayList();
      this.timeOut = new Timer();
      this.closeScreen = new Timer();
      this.openTimeOut = new Timer();
      this.putTimer = new Timer();
      this.ominousTimer = new Timer();
      this.stage = Bot.Stage.Open;
      this.storageSand = false;
      this.endEat = false;
      this.craftTimer = new Timer();
      this.screenTimeout = new Timer();
      this.lastSlot = -1;
      this.tick = 0;
      this.tradedVillager = new ArrayList();
      this.timeoutTimer = new Timer();
      this.placeProgress = 0;
      this.slabPlacerDelay = new Timer();
      this.cancel = true;
      this.scheduledTasks = new ArrayList();
      this.scheduledTasks2 = new ArrayList();
      this.setChinese("机器人");
      INSTANCE = this;
   }

   private void openAutoTradeEdit() {
      this.edit.setValueWithoutTask(false);
      if (!nullCheck()) {
         mc.method_1507(new WindowsScreen(new WindowBase[]{new ItemSelectWindow(Alien.TRADE)}));
      }

   }

   private void placeBlock(class_2338 pos) {
      int block;
      if ((block = this.getBlock()) != -1) {
         int oldSlot = mc.field_1724.method_31548().field_7545;
         if (BlockUtil.canPlace(pos)) {
            if (BlockUtil.allowAirPlace()) {
               this.doSwap(block);
               BlockUtil.placedPos.add(pos);
               BlockUtil.airPlace(pos, this.rotate.getValue());
               if (this.inventory.getValue()) {
                  this.doSwap(block);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(oldSlot);
               }

               return;
            }

            class_2350 side = BlockUtil.getPlaceSide(pos);
            if (side == null) {
               return;
            }

            this.doSwap(block);
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), this.rotate.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(block);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(oldSlot);
            }
         }
      }

   }

   private void tryPlaceBlock(class_2338 pos) {
      if (pos != null && (double)this.placeProgress < this.blocksPer.getValue()) {
         int block;
         if (this.inventory.getValue()) {
            block = InventoryUtil.findClassInventorySlot(class_2482.class);
         } else {
            block = InventoryUtil.findClass(class_2482.class);
         }

         if (block != -1) {
            class_2350 side = BlockUtil.getPlaceSide(pos);
            if (side != null) {
               class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
               if (BlockUtil.canPlace(pos, (double)6.0F, true)) {
                  if (this.rotate.getValue()) {
                     Alien.ROTATION.lookAt(directionVec);
                  }

                  if (!BlockUtil.hasEntity(pos, false)) {
                     int old = mc.field_1724.method_31548().field_7545;
                     this.doSwap(block);
                     BlockUtil.placedPos.add(pos);
                     if (BlockUtil.allowAirPlace()) {
                        BlockUtil.airPlace(pos, false, class_1268.field_5808, true);
                     } else {
                        BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), false, class_1268.field_5808);
                     }

                     if (this.inventory.getValue()) {
                        this.doSwap(block);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old);
                     }

                     if (this.rotate.getValue()) {
                        Alien.ROTATION.snapBack();
                     }

                     ++this.placeProgress;
                     this.slabPlacerDelay.reset();
                  }
               }
            }
         }
      }

   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private int getBlock() {
      return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2480.class) : InventoryUtil.findClass(class_2480.class);
   }

   public String getInfo() {
      return this.mode.is(Bot.Mode.XinDupe) ? "Stage:" + this.stage.name() + ", Riding:" + mc.field_1724.method_5765() : ((Mode)this.mode.getValue()).name();
   }

   public boolean onEnable() {
      this.emptyBox.clear();
      this.storageSand = false;
      this.stage = Bot.Stage.Summon;
      this.boxPos = null;
      this.closeToBox = false;
      this.llama = null;
      this.putIn = false;
      this.tick = 0;
      this.scheduledTasks.clear();
      this.scheduledTasks2.clear();
      if (this.mode.is(Bot.Mode.TridentDupe)) {
         this.tridentDupe();
      }

      return false;
   }

   @EventListener(
      priority = 201
   )
   private void onSendPacket(PacketEvent.Send event) {
      if (this.mode.is(Bot.Mode.TridentDupe)) {
         if (!this.cancel) {
            return;
         }

         if (event.getPacket() instanceof class_2828 || event.getPacket() instanceof class_2815) {
            return;
         }

         if (!(event.getPacket() instanceof class_2813) && !(event.getPacket() instanceof class_2846)) {
            return;
         }

         event.cancel();
      }

   }

   private void tridentDupe() {
      int delayInt = this.delay.getValueInt() * 100;
      int lowestHotbarSlot = 0;
      int lowestHotbarDamage = 1000;

      for(int i = 0; i < 9; ++i) {
         if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8547) {
            int currentHotbarDamage = mc.field_1724.method_31548().method_5438(i).method_7919();
            if (lowestHotbarDamage > currentHotbarDamage) {
               lowestHotbarSlot = i;
               lowestHotbarDamage = currentHotbarDamage;
            }
         }
      }

      mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
      this.cancel = true;
      this.scheduleTask(() -> {
         this.cancel = false;
         if (this.durabilityManagement.getValue() && lowestHotbarSlot != 0) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7791, mc.field_1724);
            if (this.dropTridents.getValue()) {
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7795, mc.field_1724);
            }

            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 36 + lowestHotbarSlot, 0, class_1713.field_7791, mc.field_1724);
         }

         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 3, 0, class_1713.field_7791, mc.field_1724);
         class_2846 packet2 = new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033, 0);
         mc.method_1562().method_52787(packet2);
         if (this.dropTridents.getValue()) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7795, mc.field_1724);
         }

         this.cancel = true;
         this.scheduleTask2(this::tridentDupe, (long)delayInt);
      }, (long)delayInt);
   }

   public void scheduleTask(Runnable task, long delayMillis) {
      long executeTime = System.currentTimeMillis() + delayMillis;
      this.scheduledTasks.add(new class_3545(executeTime, task));
   }

   public void scheduleTask2(Runnable task, long delayMillis) {
      long executeTime = System.currentTimeMillis() + delayMillis;
      this.scheduledTasks2.add(new class_3545(executeTime, task));
   }

   public void onDisable() {
      BaritoneUtil.cancelEverything();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (nullCheck()) {
         this.emptyBox.clear();
         this.stage = Bot.Stage.Summon;
         this.boxPos = null;
         this.closeToBox = false;
         this.llama = null;
         this.putIn = false;
      } else if (!this.autoEat.getValue() || !(mc.field_1724.method_6032() + mc.field_1724.method_6067() < this.health.getValueFloat()) && mc.field_1724.method_7344().method_7586() >= this.hunger.getValueInt()) {
         if (this.endEat) {
            this.endEat = false;
            mc.field_1690.field_1904.method_23481(false);
         }

         switch (((Mode)this.mode.getValue()).ordinal()) {
            case 0:
               class_1703 var24 = mc.field_1724.field_7512;
               if (var24 instanceof class_1728) {
                  class_1728 handler = (class_1728)var24;
                  if (this.timeoutCloseSetting.getValue() && this.timeoutTimer.passedS(this.timeOutSetting.getValue())) {
                     mc.method_1562().method_52787(new class_2815(mc.field_1724.field_7512.field_7763));
                     mc.field_1755.method_25419();
                     return;
                  }

                  int i = 0;
                  class_1916 list = handler.method_17438();

                  for(int size = 0; size < list.size(); ++size) {
                     if ((double)i >= this.repeatSetting.getValue()) {
                        return;
                     }

                     class_1914 tradeOffer = (class_1914)list.get(size);
                     if (!tradeOffer.method_8255() && Alien.TRADE.inWhitelist(tradeOffer.method_8250().method_7909().method_7876())) {
                        while((double)i < this.repeatSetting.getValue()) {
                           if (!tradeOffer.method_19272().method_7960()) {
                              int count = InventoryUtil.getItemCount(tradeOffer.method_19272().method_7909());
                              if (handler.method_7611(0).method_7677().method_7909() == tradeOffer.method_19272().method_7909()) {
                                 count += handler.method_7611(0).method_7677().method_7947();
                              }

                              if (count < tradeOffer.method_19272().method_7947()) {
                                 break;
                              }
                           }

                           if (!tradeOffer.method_8247().method_7960()) {
                              int countx = InventoryUtil.getItemCount(tradeOffer.method_8247().method_7909());
                              if (handler.method_7611(1).method_7677().method_7909() == tradeOffer.method_8247().method_7909()) {
                                 countx += handler.method_7611(1).method_7677().method_7947();
                              }

                              if (countx < tradeOffer.method_8247().method_7947()) {
                                 break;
                              }
                           }

                           mc.method_1562().method_52787(new class_2863(size));
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 2, 1, class_1713.field_7794, mc.field_1724);
                           ++i;
                        }
                     }
                  }

                  if (this.autoCloseSetting.getValue() && (double)i < this.repeatSetting.getValue()) {
                     mc.method_1562().method_52787(new class_2815(mc.field_1724.field_7512.field_7763));
                     mc.field_1755.method_25419();
                  }
               } else {
                  this.timeoutTimer.reset();
                  if (this.autoOpenSetting.getValue()) {
                     for(class_1297 entityxxxx : Alien.THREAD.getEntities()) {
                        if (entityxxxx instanceof class_1646) {
                           class_1646 villager = (class_1646)entityxxxx;
                           if ((double)mc.field_1724.method_5739(villager) <= this.range.getValue()) {
                              if (!this.tradedVillager.contains(villager)) {
                                 this.tradedVillager.add(villager);
                                 if (this.rotate.getValue()) {
                                    Alien.ROTATION.snapAt(villager.method_33571());
                                 }

                                 mc.method_1562().method_52787(class_2824.method_34207(villager, mc.field_1724.method_5715(), class_1268.field_5808));
                                 if (this.rotate.getValue()) {
                                    Alien.ROTATION.snapBack();
                                 }

                                 return;
                              }
                           } else {
                              this.tradedVillager.remove(villager);
                           }
                        }
                     }
                  }
               }
               break;
            case 1:
               ++this.tick;
               int shulkerx = InventoryUtil.findClass(class_2480.class);
               if (shulkerx != -1) {
                  for(class_1297 entityxxx : Alien.THREAD.getEntities()) {
                     if (entityxxx instanceof class_1533) {
                        class_1533 itemFrameEntity = (class_1533)entityxxx;
                        if (!(entityxxx.method_5739(mc.field_1724) > 3.0F)) {
                           InventoryUtil.switchToSlot(shulkerx);
                           mc.method_1562().method_52787(class_2824.method_34207(itemFrameEntity, false, class_1268.field_5808));
                           if (this.tick >= 2) {
                              mc.method_1562().method_52787(class_2824.method_34206(itemFrameEntity, false));
                              mc.field_1724.method_6104(class_1268.field_5808);
                              this.tick = 0;
                           }

                           return;
                        }
                     }
                  }
               }

               this.tick = 0;
               break;
            case 2:
               if (this.llama != null && (this.llama.method_29504() || this.llama.method_5739(mc.field_1724) > 20.0F)) {
                  this.llama = null;
               }

               int chestSlot = InventoryUtil.findBlockInventorySlot(class_2246.field_10034);
               int swordSlot = InventoryUtil.findClass(class_1829.class);
               if (chestSlot == -1) {
                  this.emptyBox.clear();
                  this.stage = Bot.Stage.Open;
                  this.boxPos = null;
                  this.closeToBox = false;
                  this.llama = null;
                  this.putIn = false;
                  return;
               }

               int shulkers = 0;

               for(Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                  class_1792 var62 = ((class_1799)entry.getValue()).method_7909();
                  if (var62 instanceof class_1747) {
                     class_1747 blockItem = (class_1747)var62;
                     if (blockItem.method_7711() instanceof class_2480) {
                        ++shulkers;
                     }
                  }
               }

               if (shulkers > 18) {
                  if (mc.field_1755 != null) {
                     mc.field_1755.method_25419();
                  }

                  for(int slot1x = 9; slot1x < 36; ++slot1x) {
                     class_1799 stack = mc.field_1724.method_31548().method_5438(slot1x);
                     if (!stack.method_7960()) {
                        class_1792 var71 = stack.method_7909();
                        if (var71 instanceof class_1747) {
                           class_1747 blockItem = (class_1747)var71;
                           if (blockItem.method_7711() instanceof class_2480) {
                              --shulkers;
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1x, 1, class_1713.field_7795, mc.field_1724);
                              if (shulkers <= 18) {
                                 return;
                              }
                           }
                        }
                     }
                  }

                  return;
               }

               if (this.closeToBox && this.boxPos != null) {
                  this.closeTo(this.boxPos);
               }

               if (!this.openTimeOut.passed(100L)) {
                  return;
               }

               switch (this.stage.ordinal()) {
                  case 0:
                     if (!this.closeScreen.passed(250L)) {
                        if (mc.field_1755 != null) {
                           mc.field_1755.method_25419();
                        }

                        return;
                     }

                     if (mc.field_1724.method_5765()) {
                        mc.method_1562().method_52787(new class_2851(0.0F, 0.0F, false, true));
                        return;
                     }

                     if (this.boxPos == null || this.emptyBox.contains(this.boxPos)) {
                        for(class_2338 posxxxx : BlockUtil.getSphere(3.0F)) {
                           if (!this.emptyBox.contains(posxxxx) && mc.field_1687.method_8321(posxxxx) instanceof class_2627 && BlockUtil.getClickSideStrict(posxxxx) != null) {
                              this.closeToBox = false;
                              this.boxPos = posxxxx;
                              break;
                           }
                        }
                     }

                     if (this.boxPos != null && !this.emptyBox.contains(this.boxPos)) {
                        if (mc.field_1724.method_33571().method_1022(this.boxPos.method_46558()) < (double)4.0F) {
                           if (this.openTimeOut.passedS((double)1.0F)) {
                              this.closeToBox = false;
                              this.openTimeOut.reset();
                              BlockUtil.clickBlock(this.boxPos, BlockUtil.getClickSide(this.boxPos), true);
                              this.stage = Bot.Stage.Take;
                              return;
                           }
                        } else {
                           this.closeToBox = true;
                        }

                        return;
                     }

                     return;
                  case 1:
                     if (mc.field_1724.method_5765()) {
                        mc.method_1562().method_52787(new class_2851(0.0F, 0.0F, false, true));
                        return;
                     }

                     if (this.boxPos != null && !this.emptyBox.contains(this.boxPos)) {
                        class_1703 hays = mc.field_1724.field_7512;
                        if (!(hays instanceof class_1733)) {
                           if (this.openTimeOut.passedS((double)1.0F)) {
                              this.closeScreen.reset();
                              this.stage = Bot.Stage.Open;
                              return;
                           }

                           return;
                        }

                        class_1733 shulkerx = (class_1733)hays;
                        boolean egg = false;
                        boolean hay = false;

                        for(class_1735 slotxxx : shulkerx.field_7761) {
                           if (slotxxx.field_7874 < 27 && !slotxxx.method_7677().method_7960()) {
                              if (slotxxx.method_7677().method_7909() == class_1802.field_8803) {
                                 egg = true;
                              }

                              if (slotxxx.method_7677().method_7909() == class_2246.field_10359.method_8389()) {
                                 hay = true;
                              }
                           }
                        }

                        if (egg && hay) {
                           int eggs = 0;
                           int hays = 0;

                           for(Map.Entry<Integer, class_1799> entryx : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                              if (((class_1799)entryx.getValue()).method_7909() == class_1802.field_8803) {
                                 ++eggs;
                              }

                              if (((class_1799)entryx.getValue()).method_7909() == class_2246.field_10359.method_8389()) {
                                 ++hays;
                              }
                           }

                           for(class_1735 slotxxxx : shulkerx.field_7761) {
                              if (!slotxxxx.method_7677().method_7960()) {
                                 if (slotxxxx.field_7874 < 27) {
                                    if (slotxxxx.method_7677().method_7909() == class_1802.field_8803 && eggs < 2) {
                                       ++eggs;
                                       mc.field_1761.method_2906(shulkerx.field_7763, slotxxxx.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                    }

                                    if (slotxxxx.method_7677().method_7909() == class_2246.field_10359.method_8389() && hays < 2) {
                                       ++hays;
                                       mc.field_1761.method_2906(shulkerx.field_7763, slotxxxx.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                    }
                                 } else if (slotxxxx.method_7677().method_7909() == class_1802.field_8745) {
                                    mc.field_1761.method_2906(shulkerx.field_7763, slotxxxx.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                 }
                              }
                           }

                           if (hays < 1 || eggs < 1) {
                              this.emptyBox.add(this.boxPos);
                           }

                           if (mc.field_1755 != null) {
                              mc.field_1755.method_25419();
                           }

                           this.stage = Bot.Stage.Summon;
                        } else {
                           this.closeScreen.reset();
                           this.emptyBox.add(this.boxPos);
                           this.stage = Bot.Stage.Open;
                        }

                        return;
                     }

                     this.closeScreen.reset();
                     this.stage = Bot.Stage.Open;
                     return;
                  case 2:
                     int eggs = 0;
                     int hays = 0;

                     for(Map.Entry<Integer, class_1799> entryx : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                        if (((class_1799)entryx.getValue()).method_7909() == class_1802.field_8803) {
                           ++eggs;
                        }

                        if (((class_1799)entryx.getValue()).method_7909() == class_2246.field_10359.method_8389()) {
                           ++hays;
                        }
                     }

                     if (eggs > 1 && hays > 1) {
                        for(class_1297 entityxxxxx : Alien.THREAD.getEntities()) {
                           if (entityxxxxx instanceof class_1501) {
                              class_1501 llamaEntity = (class_1501)entityxxxxx;
                              if (mc.field_1724.method_33571().method_1022(entityxxxxx.method_19538()) < (double)10.0F && entityxxxxx.method_5805()) {
                                 if (mc.field_1724.method_33571().method_1022(entityxxxxx.method_19538()) < (double)5.0F) {
                                    this.llama = llamaEntity;
                                    this.stage = Bot.Stage.Tame;
                                 } else {
                                    this.closeTo(entityxxxxx.method_24515());
                                 }

                                 return;
                              }
                           }
                        }

                        if (mc.field_1755 != null) {
                           mc.field_1755.method_25419();
                        }

                        int slotxxx = InventoryUtil.findItemInventorySlot(class_1802.field_8803);
                        InventoryUtil.inventorySwap(slotxxx, mc.field_1724.method_31548().field_7545);
                        Alien.ROTATION.snapAt(Alien.ROTATION.getLastYaw(), 89.0F);
                        sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                        InventoryUtil.inventorySwap(slotxxx, mc.field_1724.method_31548().field_7545);
                        return;
                     }

                     this.closeScreen.reset();
                     this.stage = Bot.Stage.Open;
                     return;
                  case 3:
                     if (this.llama != null && !this.llama.method_29504()) {
                        int eggs = 0;
                        int hays = 0;

                        for(Map.Entry<Integer, class_1799> entryx : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                           if (((class_1799)entryx.getValue()).method_7909() == class_1802.field_8803) {
                              ++eggs;
                           }

                           if (((class_1799)entryx.getValue()).method_7909() == class_2246.field_10359.method_8389()) {
                              ++hays;
                           }
                        }

                        if (eggs > 1 && hays > 1) {
                           if (mc.field_1724.method_5765()) {
                              if (this.llama.method_6727()) {
                                 if (!this.llama.method_6703()) {
                                    mc.method_1562().method_52787(new class_2851(0.0F, 0.0F, false, true));
                                    return;
                                 }

                                 int moves = 0;
                                 class_1703 var78 = mc.field_1724.field_7512;
                                 if (var78 instanceof class_1724) {
                                    class_1724 shulkerx = (class_1724)var78;
                                    if (this.putTimer.passed(250L)) {
                                       if (!this.putIn) {
                                          for(class_1735 slotxxxxx : shulkerx.field_7761) {
                                             class_1792 var106 = slotxxxxx.method_7677().method_7909();
                                             if (var106 instanceof class_1747) {
                                                class_1747 blockItemx = (class_1747)var106;
                                                if (blockItemx.method_7711() instanceof class_2480) {
                                                   mc.field_1761.method_2906(shulkerx.field_7763, slotxxxxx.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                                   ++moves;
                                                   this.putTimer.reset();
                                                   if (moves >= 15) {
                                                      break;
                                                   }
                                                }
                                             }
                                          }

                                          this.putIn = true;
                                       } else {
                                          this.stage = Bot.Stage.Kill;
                                          mc.method_1562().method_52787(new class_2851(0.0F, 0.0F, false, true));
                                       }

                                       return;
                                    }
                                 } else {
                                    this.putIn = false;
                                    this.putTimer.reset();
                                    mc.field_1724.method_3132();
                                 }

                                 return;
                              }

                              return;
                           }

                           if (mc.field_1724.method_33571().method_1022(this.llama.method_19538()) > (double)5.0F) {
                              this.closeTo(this.llama.method_24515());
                              return;
                           }

                           if (this.llama.method_6109()) {
                              if (mc.field_1755 != null) {
                                 mc.field_1755.method_25419();
                              }

                              int slotxxxxxx = InventoryUtil.findBlockInventorySlot(class_2246.field_10359);
                              InventoryUtil.inventorySwap(slotxxxxxx, mc.field_1724.method_31548().field_7545);
                              Alien.ROTATION.lookAt(this.llama.method_19538());
                              mc.field_1761.method_2905(mc.field_1724, this.llama, class_1268.field_5808);
                              InventoryUtil.inventorySwap(slotxxxxxx, mc.field_1724.method_31548().field_7545);
                           } else {
                              if (!this.llama.method_6727()) {
                                 if (mc.field_1755 != null) {
                                    mc.field_1755.method_25419();
                                 }

                                 for(int ixx = 0; ixx < 9; ++ixx) {
                                    if (mc.field_1724.method_31548().method_5438(ixx).method_7960()) {
                                       InventoryUtil.switchToSlot(ixx);
                                       mc.field_1761.method_2905(mc.field_1724, this.llama, class_1268.field_5808);
                                       return;
                                    }
                                 }

                                 for(int ixxx = 0; ixxx < 9; ++ixxx) {
                                    class_1792 var77 = mc.field_1724.method_31548().method_5438(ixxx).method_7909();
                                    if (var77 instanceof class_1747) {
                                       class_1747 blockItemxx = (class_1747)var77;
                                       if (blockItemxx.method_7711() instanceof class_2480) {
                                          InventoryUtil.switchToSlot(ixxx);
                                          mc.field_1724.method_7290(true);
                                          mc.field_1761.method_2905(mc.field_1724, this.llama, class_1268.field_5808);
                                          return;
                                       }
                                    }
                                 }

                                 return;
                              }

                              if (this.llama.method_6703()) {
                                 for(int i = 0; i < 9; ++i) {
                                    if (mc.field_1724.method_31548().method_5438(i).method_7960()) {
                                       InventoryUtil.switchToSlot(i);
                                       mc.field_1761.method_2905(mc.field_1724, this.llama, class_1268.field_5808);
                                       return;
                                    }
                                 }

                                 int ix = 0;

                                 while(true) {
                                    if (ix >= 9) {
                                       return;
                                    }

                                    class_1792 var76 = mc.field_1724.method_31548().method_5438(ix).method_7909();
                                    if (var76 instanceof class_1747) {
                                       class_1747 blockItemxx = (class_1747)var76;
                                       if (blockItemxx.method_7711() instanceof class_2480) {
                                          break;
                                       }
                                    }

                                    if (mc.field_1724.method_31548().method_5438(ix).method_7909() == class_1802.field_8745) {
                                       break;
                                    }

                                    ++ix;
                                 }

                                 InventoryUtil.switchToSlot(ix);
                                 mc.field_1724.method_7290(true);
                                 mc.field_1761.method_2905(mc.field_1724, this.llama, class_1268.field_5808);
                                 return;
                              }

                              this.putTimer.reset();
                              this.putIn = false;
                              if (mc.field_1755 != null) {
                                 mc.field_1755.method_25419();
                              }

                              InventoryUtil.inventorySwap(chestSlot, mc.field_1724.method_31548().field_7545);
                              Alien.ROTATION.lookAt(this.llama.method_19538());
                              mc.field_1761.method_2905(mc.field_1724, this.llama, class_1268.field_5808);
                              InventoryUtil.inventorySwap(chestSlot, mc.field_1724.method_31548().field_7545);
                           }

                           return;
                        }

                        this.closeScreen.reset();
                        this.stage = Bot.Stage.Open;
                        return;
                     }

                     this.stage = Bot.Stage.Summon;
                     return;
                  case 4:
                     if (this.llama != null && !this.llama.method_29504()) {
                        if (mc.field_1755 != null) {
                           mc.field_1755.method_25419();
                        }

                        if (mc.field_1724.method_5765()) {
                           mc.method_1562().method_52787(new class_2851(0.0F, 0.0F, false, true));
                           return;
                        }

                        if (mc.field_1724.method_19538().method_1022(this.llama.method_19538()) > (double)1.0F) {
                           this.closeTo(this.llama.method_24515());
                        }

                        if (mc.field_1724.method_19538().method_1022(this.llama.method_19538()) > (double)2.0F) {
                           return;
                        }

                        InventoryUtil.switchToSlot(swordSlot);
                        if (this.check()) {
                           Alien.ROTATION.lookAt(this.llama.method_33571());
                           mc.field_1761.method_2918(mc.field_1724, this.llama);
                           EntityUtil.swingHand(class_1268.field_5808, SwingSide.All);
                        }

                        return;
                     }

                     this.llama = null;
                     this.stage = Bot.Stage.Summon;
                     return;
                  default:
                     return;
               }
            case 3:
               class_2338 placePos = SpeedMine.getBreakPos();
               if (placePos != null && BlockUtil.canPlace(placePos)) {
                  this.placeBlock(placePos);
               }
               break;
            case 4:
               long currentTime = System.currentTimeMillis();
               Iterator<class_3545<Long, Runnable>> iterator = this.scheduledTasks.iterator();

               while(iterator.hasNext()) {
                  class_3545<Long, Runnable> entry = (class_3545)iterator.next();
                  if ((Long)entry.method_15442() <= currentTime) {
                     ((Runnable)entry.method_15441()).run();
                     iterator.remove();
                  }
               }

               iterator = this.scheduledTasks2.iterator();

               while(iterator.hasNext()) {
                  class_3545<Long, Runnable> entry = (class_3545)iterator.next();
                  if ((Long)entry.method_15442() <= currentTime) {
                     ((Runnable)entry.method_15441()).run();
                     iterator.remove();
                  }
               }
               break;
            case 5:
               int seagrass = InventoryUtil.findItem(class_1802.field_8158);
               if (seagrass == -1) {
                  if (BaritoneUtil.isActive()) {
                     BaritoneUtil.cancelEverything();
                  }

                  return;
               }

               if (this.timeOut.passedS((double)300.0F)) {
                  this.inLove.clear();
                  this.timeOut.reset();
               }

               double distance = (double)0.0F;
               class_1481 target = null;

               for(class_1297 entity : Alien.THREAD.getEntities()) {
                  if (entity instanceof class_1481) {
                     class_1481 turtle = (class_1481)entity;
                     if (!turtle.method_6109() && !this.inLove.contains(turtle) && !(Math.abs(mc.field_1724.method_23318() - turtle.method_23318()) > (double)3.0F)) {
                        double dis = (double)mc.field_1724.method_5739(turtle);
                        if (target == null || dis < distance) {
                           distance = dis;
                           target = turtle;
                        }
                     }
                  }
               }

               if (target == null) {
                  if (this.timeOut.passedS((double)20.0F)) {
                     this.inLove.clear();
                     this.timeOut.reset();
                  }

                  if (BaritoneUtil.isActive()) {
                     BaritoneUtil.cancelEverything();
                  }

                  return;
               }

               if (mc.field_1724.method_5739(target) < 3.0F) {
                  BaritoneUtil.cancelEverything();
                  InventoryUtil.switchToSlot(seagrass);
                  Alien.ROTATION.snapAt(target.method_19538());
                  mc.method_1562().method_52787(class_2824.method_34207(target, mc.field_1724.method_5715(), class_1268.field_5808));
                  EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.interactSwing.getValue());
                  Alien.ROTATION.snapBack();
                  this.inLove.add(target);
               } else {
                  BaritoneUtil.gotoPos(target.method_24515());
               }
               break;
            case 6:
               int seagrassx = InventoryUtil.findItem(class_1802.field_8158);
               if (seagrassx == -1) {
                  return;
               }

               if (this.timeOut.passedS((double)300.0F)) {
                  this.inLove.clear();
                  this.timeOut.reset();
               }

               double distance = (double)0.0F;
               class_1481 target = null;

               for(class_1297 entityx : Alien.THREAD.getEntities()) {
                  if (entityx instanceof class_1481) {
                     class_1481 turtlex = (class_1481)entityx;
                     if (!turtlex.method_6109() && !this.inLove.contains(turtlex) && !(mc.field_1724.method_5739(turtlex) > 3.0F)) {
                        double dis = (double)mc.field_1724.method_5739(turtlex);
                        if (target == null || dis < distance) {
                           distance = dis;
                           target = turtlex;
                        }
                     }
                  }
               }

               if (target == null) {
                  if (this.timeOut.passedS((double)20.0F)) {
                     this.inLove.clear();
                     this.timeOut.reset();
                  }

                  return;
               }

               InventoryUtil.switchToSlot(seagrassx);
               Alien.ROTATION.snapAt(target.method_19538());
               mc.method_1562().method_52787(class_2824.method_34207(target, mc.field_1724.method_5715(), class_1268.field_5808));
               EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.interactSwing.getValue());
               Alien.ROTATION.snapBack();
               this.inLove.add(target);
               break;
            case 7:
               class_2248 sandBlock = class_2246.field_10102;
               if (this.redSand.getValue()) {
                  sandBlock = class_2246.field_10534;
               }

               if (Aura.INSTANCE.isOn() && Aura.INSTANCE.getTarget(Aura.INSTANCE.range.getValue()) != null) {
                  if (mc.field_1755 != null) {
                     mc.field_1755.method_25419();
                  }

                  int slot = InventoryUtil.findClass(class_1829.class);
                  if (mc.field_1724.method_31548().field_7545 != slot) {
                     InventoryUtil.switchToSlot(slot);
                  }

                  BaritoneUtil.cancelEverything();
                  return;
               }

               if (mc.field_1755 == null) {
                  this.screenTimeout.reset();
               } else if (this.screenTimeout.passedS((double)5.0F)) {
                  mc.field_1755.method_25419();
               }

               if (InventoryUtil.findClassInventorySlot(class_1821.class) == -1) {
                  BaritoneUtil.cancelEverything();
                  if (mc.field_1724.field_7512 instanceof class_1714) {
                     if (this.craftTimer.passedS((double)1.0F)) {
                        for(class_516 recipeResult : mc.field_1724.method_3130().method_1393()) {
                           for(class_8786<?> recipe : recipeResult.method_2648(true)) {
                              if (recipe.comp_1933().method_8110(mc.field_1687.method_30349()).method_7909() instanceof class_1821) {
                                 this.craftTimer.reset();
                                 mc.field_1761.method_2912(mc.field_1724.field_7512.field_7763, recipe, false);
                                 mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 0, 1, class_1713.field_7794, mc.field_1724);
                                 return;
                              }
                           }
                        }
                     }
                  } else {
                     class_2338 bestPos = null;
                     double distance = (double)100.0F;

                     for(class_2338 pos : BlockUtil.getSphere(3.0F)) {
                        if (mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9980 && BlockUtil.getClickSideStrict(pos) != null) {
                           BlockUtil.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue());
                           return;
                        }

                        if (BlockUtil.canPlace(pos) && (bestPos == null || (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558())) < distance)) {
                           bestPos = pos;
                           distance = (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558()));
                        }
                     }

                     if (bestPos != null) {
                        int craftTable = InventoryUtil.findItemInventorySlot(class_1802.field_8465);
                        if (craftTable == -1) {
                           return;
                        }

                        InventoryUtil.inventorySwap(craftTable, mc.field_1724.method_31548().field_7545);
                        BlockUtil.placeBlock(bestPos, this.rotate.getValue());
                        InventoryUtil.inventorySwap(craftTable, mc.field_1724.method_31548().field_7545);
                     }
                  }

                  return;
               }

               class_1297 hasShulkerItemEntity = null;

               for(class_1297 entityxx : Alien.THREAD.getEntities()) {
                  if (entityxx instanceof class_1542) {
                     class_1542 itemEntity = (class_1542)entityxx;
                     class_1792 turtle = itemEntity.method_6983().method_7909();
                     if (turtle instanceof class_1747) {
                        class_1747 item = (class_1747)turtle;
                        if (item.method_7711() instanceof class_2480 || item.method_7711() == class_2246.field_10443) {
                           hasShulkerItemEntity = itemEntity;
                           break;
                        }
                     }
                  }
               }

               if (mc.field_1724.field_7512 instanceof class_1714 && mc.field_1755 != null) {
                  mc.field_1755.method_25419();
               }

               int sands = InventoryUtil.getItemCount(class_1802.field_8858);
               if (sands >= 1728) {
                  for(int slot1 = 9; slot1 < 36; ++slot1) {
                     class_1799 stack = mc.field_1724.method_31548().method_5438(slot1);
                     if (!stack.method_7960()) {
                        class_1792 turtlex = stack.method_7909();
                        if (turtlex instanceof class_1747) {
                           class_1747 blockItem = (class_1747)turtlex;
                           if (blockItem.method_7711() == sandBlock && stack.method_7947() < stack.method_7914()) {
                              mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1, 1, class_1713.field_7795, mc.field_1724);
                           }
                        }
                     }
                  }

                  if (mc.field_1755 instanceof class_490) {
                     mc.field_1755.method_25419();
                     return;
                  }

                  int shulkerSlot = InventoryUtil.findClassInventorySlot(class_2480.class);
                  class_2338 shulker = BlockUtil.getBlock(class_2480.class, 3.0F);
                  if (mc.field_1755 instanceof class_465) {
                     class_1703 var104 = mc.field_1724.field_7512;
                     if (var104 instanceof class_1703) {
                        if (!(mc.field_1724.field_7512 instanceof class_1733)) {
                           BaritoneUtil.cancelEverything();
                           if (shulkerSlot != -1) {
                              mc.field_1755.method_25419();
                              break;
                           } else {
                              for(class_1735 slot : var104.field_7761) {
                                 if (slot.field_7874 < 27) {
                                    class_1792 var151 = slot.method_7677().method_7909();
                                    if (var151 instanceof class_1747) {
                                       class_1747 blockItem = (class_1747)var151;
                                       if (blockItem.method_7711() instanceof class_2480 && !ShulkerViewer.hasItems(slot.method_7677())) {
                                          mc.field_1761.method_2906(var104.field_7763, slot.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                          return;
                                       }
                                    }
                                 }
                              }

                              return;
                           }
                        } else {
                           BaritoneUtil.cancelEverything();
                           this.storageSand = true;
                           class_1703 bestPos = mc.field_1724.field_7512;
                           if (bestPos instanceof class_1733) {
                              class_1733 shulkerBoxScreenHandler = (class_1733)bestPos;

                              for(class_1735 slot : shulkerBoxScreenHandler.field_7761) {
                                 if (slot.field_7874 < 27 && slot.method_7677().method_7960()) {
                                    for(class_1735 slot2 : shulkerBoxScreenHandler.field_7761) {
                                       if (slot2.field_7874 >= 27 && slot2.method_7677().method_7909() == class_1802.field_8858 && slot2.method_7677().method_7947() == slot2.method_7677().method_7914()) {
                                          mc.field_1761.method_2906(shulkerBoxScreenHandler.field_7763, slot2.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                          return;
                                       }
                                    }
                                 }
                              }
                           }
                           break;
                        }
                     }
                  }

                  if (shulker == null) {
                     double distance = (double)100.0F;
                     class_2338 bestPos = null;

                     for(class_2338 pos : BlockUtil.getSphere(3.0F, mc.field_1724.method_33571())) {
                        if (mc.field_1687.method_22347(pos.method_10084()) && BlockUtil.clientCanPlace(pos, false) && BlockUtil.isStrictDirection(pos.method_10093(class_2350.field_11033), class_2350.field_11036) && BlockUtil.canClick(pos.method_10093(class_2350.field_11033)) && (bestPos == null || (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558())) < distance)) {
                           distance = (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558()));
                           bestPos = pos;
                        }
                     }

                     if (bestPos != null) {
                        BaritoneUtil.cancelEverything();
                        if (shulkerSlot != -1) {
                           InventoryUtil.inventorySwap(shulkerSlot, mc.field_1724.method_31548().field_7545);
                           BlockUtil.clickBlock(bestPos.method_10093(class_2350.field_11033), class_2350.field_11036, this.rotate.getValue());
                           InventoryUtil.inventorySwap(shulkerSlot, mc.field_1724.method_31548().field_7545);
                        } else {
                           class_2338 ec = BlockUtil.getBlock(class_2246.field_10443, 3.0F);
                           if (ec != null) {
                              BlockUtil.clickBlock(ec, BlockUtil.getClickSide(ec), this.rotate.getValue());
                           } else {
                              int enderChest = InventoryUtil.findBlockInventorySlot(class_2246.field_10443);
                              if (enderChest != -1) {
                                 InventoryUtil.inventorySwap(enderChest, mc.field_1724.method_31548().field_7545);
                                 BlockUtil.placeBlock(bestPos, true);
                                 InventoryUtil.inventorySwap(enderChest, mc.field_1724.method_31548().field_7545);
                              }
                           }
                        }
                     }
                  } else {
                     BlockUtil.clickBlock(shulker, BlockUtil.getClickSide(shulker), this.rotate.getValue());
                  }
               } else if (this.storageSand) {
                  class_1703 shulker = mc.field_1724.field_7512;
                  if (shulker instanceof class_1733) {
                     class_1733 shulkerBoxScreenHandler = (class_1733)shulker;

                     for(class_1735 slotx : shulkerBoxScreenHandler.field_7761) {
                        if (slotx.field_7874 < 27 && slotx.method_7677().method_7960()) {
                           for(class_1735 slot2x : shulkerBoxScreenHandler.field_7761) {
                              if (slot2x.field_7874 >= 27 && slot2x.method_7677().method_7909() == class_1802.field_8858 && slot2x.method_7677().method_7947() == slot2x.method_7677().method_7914()) {
                                 mc.field_1761.method_2906(shulkerBoxScreenHandler.field_7763, slot2x.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                 return;
                              }
                           }
                        }
                     }

                     mc.field_1755.method_25419();
                     this.storageSand = false;
                  }
               } else {
                  class_2338 shulkerx = BlockUtil.getBlock(class_2480.class, 3.0F);
                  if (shulkerx != null) {
                     BaritoneUtil.mine(mc.field_1687.method_8320(shulkerx).method_26204());
                  } else {
                     int fillShulker = InventoryUtil.findClassInventorySlot(class_2480.class);
                     if (fillShulker != -1) {
                        BaritoneUtil.cancelEverything();
                        class_1703 var110 = mc.field_1724.field_7512;
                        if (var110 instanceof class_1703) {
                           for(class_1735 slotxx : var110.field_7761) {
                              if (slotxx.field_7874 >= 27) {
                                 class_1792 var154 = slotxx.method_7677().method_7909();
                                 if (var154 instanceof class_1747) {
                                    class_1747 blockItem = (class_1747)var154;
                                    if (blockItem.method_7711() instanceof class_2480) {
                                       mc.field_1761.method_2906(var110.field_7763, slotxx.field_7874, 0, class_1713.field_7794, mc.field_1724);
                                       return;
                                    }
                                 }
                              }
                           }
                        }

                        double distance = (double)100.0F;
                        class_2338 bestPos = null;

                        for(class_2338 posx : BlockUtil.getSphere(3.0F)) {
                           if (mc.field_1687.method_22347(posx.method_10084()) && BlockUtil.clientCanPlace(posx, false) && BlockUtil.isStrictDirection(posx.method_10093(class_2350.field_11033), class_2350.field_11036) && BlockUtil.canClick(posx.method_10093(class_2350.field_11033)) && (bestPos == null || (double)class_3532.method_15355((float)mc.field_1724.method_5707(posx.method_46558())) < distance)) {
                              distance = (double)class_3532.method_15355((float)mc.field_1724.method_5707(posx.method_46558()));
                              bestPos = posx;
                           }
                        }

                        if (bestPos != null) {
                           class_2338 ec = BlockUtil.getBlock(class_2246.field_10443, 3.0F);
                           if (ec != null) {
                              BlockUtil.clickBlock(ec, BlockUtil.getClickSide(ec), this.rotate.getValue());
                           } else {
                              int enderChest = InventoryUtil.findBlockInventorySlot(class_2246.field_10443);
                              if (enderChest != -1) {
                                 InventoryUtil.inventorySwap(enderChest, mc.field_1724.method_31548().field_7545);
                                 BlockUtil.placeBlock(bestPos, true);
                                 InventoryUtil.inventorySwap(enderChest, mc.field_1724.method_31548().field_7545);
                              }
                           }
                        }

                        return;
                     }

                     if (hasShulkerItemEntity != null) {
                        BaritoneUtil.gotoPos(hasShulkerItemEntity.method_24515());
                     }

                     class_2338 posxx = BlockUtil.getBlock(class_2246.field_10443, 5.0F);
                     if (posxx != null) {
                        BaritoneUtil.mine(class_2246.field_10443);
                        if (mc.field_1755 != null) {
                           mc.field_1755.method_25419();
                        }

                        return;
                     }

                     if (this.ai.getValue()) {
                        BaritoneUtil.mine(sandBlock);
                     }

                     if (this.nuker.getValue()) {
                        if (!mc.field_1724.method_24828()) {
                           return;
                        }

                        int b = 0;

                        for(class_2338 sand : BlockUtil.getSphere(3.0F, mc.field_1724.method_33571())) {
                           if (sandBlock == mc.field_1687.method_8320(sand).method_26204()) {
                              class_2350 side = BlockUtil.getClickSideStrict(sand);
                              if (side != null) {
                                 PlaceRender.INSTANCE.create(sand);
                                 Alien.ROTATION.snapAt(sand.method_46558());
                                 sendSequencedPacket((id) -> new class_2846(class_2847.field_12973, sand, side, id));
                                 sendSequencedPacket((id) -> new class_2846(class_2847.field_12968, sand, side, id));
                                 sendSequencedPacket((id) -> new class_2846(class_2847.field_12973, sand, side, id));
                                 Alien.ROTATION.snapBack();
                                 ++b;
                                 if ((double)b >= this.breaks.getValue()) {
                                    return;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
               break;
            case 8:
               if (this.getScore.getValue() && this.commandTimer.passedS((double)4.0F)) {
                  mc.field_1724.field_3944.method_45731("duel " + this.name.getValue());
                  this.commandTimer.reset();
               }

               if (mc.field_1724.field_7512 instanceof class_1707 && this.duelItemTimer.passedS((double)1.0F)) {
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 0, 0, class_1713.field_7790, mc.field_1724);
                  this.duelItemTimer.reset();
               }
               break;
            case 9:
               if (this.inventory.getValue() && !EntityUtil.inInventory()) {
                  return;
               }

               this.placeProgress = 0;
               if (!this.slabPlacerDelay.passed((long)this.placeDelay.getValue())) {
                  return;
               }

               if (mc.field_1724.method_6115() && this.usingPause.getValue()) {
                  return;
               }

               for(class_2338 posxxx : BlockUtil.getSphere(this.range.getValueFloat(), mc.field_1724.method_19538())) {
                  if ((mc.field_1687.method_8320(posxxx).method_26234(mc.field_1687, posxxx) || !(mc.field_1687.method_8320(posxxx).method_26204() instanceof class_2482)) && !mc.field_1687.method_22347(posxxx) && BlockUtil.canReplace(posxxx.method_10084())) {
                     this.tryPlaceBlock(posxxx.method_10084());
                  }
               }
               break;
            case 10:
               int ominousSlot = InventoryUtil.findItem(class_1802.field_50140);
               if (ominousSlot != -1) {
                  if (!mc.field_1724.method_6059(class_1294.field_16595) && !mc.field_1724.method_6059(class_1294.field_50117)) {
                     if (this.ominousTimer.passedS(this.maxTime.getValue())) {
                        if (mc.field_1724.method_6047().method_7909() != class_1802.field_50140) {
                           this.lastSlot = mc.field_1724.method_31548().field_7545;
                           InventoryUtil.switchToSlot(ominousSlot);
                        } else {
                           mc.field_1690.field_1904.method_23481(true);
                           this.ominousTimer.reset();
                        }
                     }
                  } else if (mc.field_1724.method_6047().method_7909() == class_1802.field_50140) {
                     mc.field_1690.field_1904.method_23481(false);
                     if (this.lastSlot != -1) {
                        InventoryUtil.switchToSlot(this.lastSlot);
                        this.lastSlot = -1;
                     }
                  }
               }
         }
      } else {
         int food = InventoryUtil.findItem(class_1802.field_8367);
         if (food == -1) {
            food = InventoryUtil.findItem(class_1802.field_8463);
         }

         if (food == -1) {
            food = InventoryUtil.findItem(class_1802.field_8071);
         }

         if (food == -1 && this.anyFood.getValue()) {
            food = InventoryUtil.getFood();
         }

         if (food != -1) {
            if (mc.field_1755 != null && this.mode.is(Bot.Mode.XinDupe)) {
               mc.field_1755.method_25419();
            }

            if (mc.field_1724.method_31548().field_7545 != food) {
               InventoryUtil.switchToSlot(food);
            }

            mc.field_1690.field_1904.method_23481(true);
            this.endEat = true;
            sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
         }

         BaritoneUtil.cancelEverything();
      }

   }

   @EventListener
   private void PacketReceive(PacketEvent.Receive event) {
      if (!nullCheck() && this.mode.is(Bot.Mode.ScoreFarmer)) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_7439) {
            class_7439 packet = (class_7439)var3;
            if (packet.comp_763() != null) {
               String received = packet.comp_763().getString().replaceAll("§[a-zA-Z0-9]", "");
               if (!this.getScore.getValue() && received.contains("你收到一个决斗申请")) {
                  mc.field_1724.field_3944.method_45731("duel accept " + this.name.getValue());
               } else if (!received.contains("<")) {
                  if (this.getScore.getValue() && received.contains("Starting in 3 seconds")) {
                     mc.field_1724.field_3944.method_45731("suicide");
                  } else if (!this.getScore.getValue() && received.contains("Starting in 4 seconds")) {
                     mc.field_1724.field_3944.method_45731("suicide");
                  }
               }
            }
         }
      }

   }

   @EventListener
   public void onRotate(UpdateRotateEvent event) {
      if (this.mode.is(Bot.Mode.XinDupe)) {
         event.setPitch(88.0F);
      }

   }

   private boolean check() {
      int at = ((ILivingEntity)mc.field_1724).getLastAttackedTicks();
      return (double)Math.max((float)at / Aura.getAttackCooldownProgressPerTick(), 0.0F) >= 1.3;
   }

   private void closeTo(class_2338 pos) {
      double speed = 0.19153333333333333;
      float forward = 1.0F;
      float side = 0.0F;
      float yaw = RotationManager.getRotation(pos.method_46558())[0];
      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double posX = (double)forward * speed * cos + (double)side * speed * sin;
      double posZ = (double)forward * speed * sin - (double)side * speed * cos;
      MovementUtil.setMotionX(posX);
      MovementUtil.setMotionZ(posZ);
   }

   public static enum Mode {
      AutoTrade,
      ItemFrameDupe,
      XinDupe,
      NPlusOneDupe,
      TridentDupe,
      TurtlePath,
      Turtle,
      SandMiner,
      ScoreFarmer,
      SlabPlacer,
      Ominous,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{AutoTrade, ItemFrameDupe, XinDupe, NPlusOneDupe, TridentDupe, TurtlePath, Turtle, SandMiner, ScoreFarmer, SlabPlacer, Ominous, None};
      }
   }

   public static enum Stage {
      Open,
      Take,
      Summon,
      Tame,
      Kill;

      // $FF: synthetic method
      private static Stage[] $values() {
         return new Stage[]{Open, Take, Summon, Tame, Kill};
      }
   }
}
