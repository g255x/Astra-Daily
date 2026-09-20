package dev.luminous.api.utils.math;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.IEntity;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;

public class PredictUtil implements Wrapper {
   public static class_243 getPos(class_1657 entity, int ticks) {
      return ticks <= 0 ? entity.method_19538() : getPos(entity, AntiCheat.INSTANCE.maxMotionY.getValue(), AntiCheat.INSTANCE.predictTicks.getValueInt(), AntiCheat.INSTANCE.simulation.getValueInt(), AntiCheat.INSTANCE.step.getValue(), AntiCheat.INSTANCE.doubleStep.getValue(), AntiCheat.INSTANCE.jump.getValue(), AntiCheat.INSTANCE.inBlockPause.getValue());
   }

   public static class_243 getPos(class_1657 e, double maxMotionY, int ticks, int simulation, boolean step, boolean doubleStep, boolean jump, boolean inBlockPause) {
      if (inBlockPause && BlockUtil.canCollide(e, e.method_5829())) {
         return e.method_19538();
      } else {
         double velocityX;
         double velocityY;
         double velocityZ;
         if (AntiCheat.INSTANCE.motion.is(AntiCheat.Motion.Position)) {
            velocityX = e.method_23317() - e.field_6014;
            velocityY = e.method_23318() - e.field_6036;
            velocityZ = e.method_23321() - e.field_5969;
            if (velocityY > maxMotionY) {
               velocityY = maxMotionY;
            }
         } else {
            velocityX = e.method_18798().field_1352;
            velocityY = e.method_18798().field_1351;
            velocityZ = e.method_18798().field_1350;
         }

         double motionX = velocityX;
         double motionY = velocityY;
         double motionZ = velocityZ;
         double x = e.method_23317();
         double y = e.method_23318();
         double z = e.method_23321();
         class_243 lastPos = new class_243(x, y, z);
         if (velocityX == (double)0.0F && velocityY == (double)0.0F && velocityZ == (double)0.0F) {
            return lastPos;
         } else {
            for(int i = 0; i < ticks; ++i) {
               lastPos = new class_243(x, y, z);
               boolean move = false;
               boolean fall = false;

               label141:
               for(int yTime = simulation; yTime >= 0; --yTime) {
                  for(int xTime = simulation; xTime >= 0; --xTime) {
                     double xFactor = (double)xTime / (double)simulation;
                     double yFactor = (double)yTime / (double)simulation;
                     if (canMove(lastPos.method_1031(motionX * xFactor, motionY * yFactor, motionZ * xFactor), e)) {
                        if (Math.abs(motionX * xFactor) + Math.abs(motionZ * xFactor) + Math.abs(motionY * yFactor) <= 0.05) {
                           if (step && !canMove(lastPos.method_1031(velocityX, (double)0.0F, velocityZ), e) && canMove(lastPos.method_1031(velocityX, 1.1, velocityZ), e)) {
                              ++y;
                              motionY = 0.03;

                              for(int yTime2 = simulation; yTime2 >= 0; --yTime2) {
                                 for(int xTime2 = simulation; xTime2 >= 0; --xTime2) {
                                    double xFactor2 = (double)xTime2 / (double)simulation;
                                    double yFactor2 = (double)yTime2 / (double)simulation;
                                    if (canMove(lastPos.method_1031(motionX * xFactor2, motionY * yFactor2, motionZ * xFactor2), e)) {
                                       move = true;
                                       x += motionX * xFactor2;
                                       z += motionZ * xFactor2;
                                       if (yTime2 > 0) {
                                          y += motionY * yFactor2;
                                          fall = true;
                                       }
                                       break label141;
                                    }
                                 }
                              }

                              return lastPos;
                           } else {
                              if (!doubleStep || canMove(lastPos.method_1031(velocityX, (double)0.0F, velocityZ), e) || !canMove(lastPos.method_1031(velocityX, 2.1, velocityZ), e)) {
                                 return lastPos;
                              }

                              y += 2.05;
                              motionY = 0.03;

                              for(int yTime2 = simulation; yTime2 >= 0; --yTime2) {
                                 for(int xTime2x = simulation; xTime2x >= 0; --xTime2x) {
                                    double xFactor2 = (double)xTime2x / (double)simulation;
                                    double yFactor2 = (double)yTime2 / (double)simulation;
                                    if (canMove(lastPos.method_1031(motionX * xFactor2, motionY * yFactor2, motionZ * xFactor2), e)) {
                                       move = true;
                                       x += motionX * xFactor2;
                                       z += motionZ * xFactor2;
                                       if (yTime2 > 0) {
                                          y += motionY * yFactor2;
                                          fall = true;
                                       }
                                       break label141;
                                    }
                                 }
                              }

                              return lastPos;
                           }
                        } else {
                           move = true;
                           x += motionX * xFactor;
                           z += motionZ * xFactor;
                           if (yTime > 0) {
                              y += motionY * yFactor;
                              fall = true;
                           }
                           break label141;
                        }
                     }
                  }
               }

               if (!move) {
                  return lastPos;
               }

               if (!e.method_6128()) {
                  motionX *= 0.99;
                  motionZ *= 0.99;
                  motionY *= 0.99;
                  motionY -= (double)0.05F;
               }

               if (!fall) {
                  if (e.method_24828()) {
                     motionX = velocityX;
                     motionZ = velocityZ;
                     motionY = (double)0.0F;
                  } else if (jump) {
                     motionX = velocityX;
                     motionZ = velocityZ;
                     motionY = 0.333;
                  } else {
                     motionY = (double)0.0F;
                  }
               }
            }

            return lastPos;
         }
      }
   }

   public static boolean canMove(class_243 pos, class_1657 player) {
      return !BlockUtil.canCollide(player, ((IEntity)player).getDimensions().method_30757(pos)) || (new class_238(new BlockPosX(pos))).method_994(player.method_5829());
   }
}
