package dev.Astra.api.utils.camera;

import net.minecraft.class_310;

public class CameraState {
   public float lookYaw;
   public float lookPitch;
   public float transitionInitialYaw;
   public float transitionInitialPitch;
   public boolean doLock;
   public boolean doTransition;

   public float originalYaw() {
      return class_310.method_1551().method_1560().method_5791();
   }

   public float originalPitch() {
      return class_310.method_1551().method_1560().method_36455();
   }
}
