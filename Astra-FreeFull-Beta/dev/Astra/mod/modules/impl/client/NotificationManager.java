package dev.Astra.mod.modules.impl.client;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import net.minecraft.class_310;

public class NotificationManager {
   public static final NotificationManager INSTANCE = new NotificationManager();
   private static final int MAX_NOTIFICATIONS = 5;
   private final Queue<NotificationInfo> notifications = new ArrayDeque();
   private final Map<Integer, NotificationInfo> hashCodeMap = new HashMap();

   public void post(String title, String subTitle, NotificationMode mode, int displayTime) {
      this.makeRoomIfNeeded();
      NotificationInfo notification = new NotificationInfo(title, subTitle, mode, displayTime, this.getScreenHeight(), false);
      this.notifications.add(notification);
   }

   public void postModuleNotification(String moduleName, boolean enabled, int displayTime) {
      int hashCode = moduleName.hashCode();
      NotificationInfo existing = (NotificationInfo)this.hashCodeMap.get(hashCode);
      if (existing != null) {
         if (!existing.isExiting()) {
            String newTitle = enabled ? "Enabled" : "Disabled";
            NotificationMode mode = enabled ? NotificationMode.Success : NotificationMode.Error;
            existing.updateModuleState(newTitle, moduleName, mode, displayTime);
            return;
         }

         this.notifications.remove(existing);
         this.hashCodeMap.remove(hashCode);
      }

      this.makeRoomIfNeeded();
      String title = enabled ? "Enabled" : "Disabled";
      NotificationMode mode = enabled ? NotificationMode.Success : NotificationMode.Error;
      NotificationInfo notification = new NotificationInfo(hashCode, title, moduleName, mode, displayTime, this.getScreenHeight(), true);
      this.notifications.add(notification);
      this.hashCodeMap.put(hashCode, notification);
   }

   public void update() {
      Iterator<NotificationInfo> iterator = this.notifications.iterator();

      while(iterator.hasNext()) {
         NotificationInfo notification = (NotificationInfo)iterator.next();
         notification.update();
         if (notification.isExpired()) {
            iterator.remove();
            this.hashCodeMap.remove(notification.getHashCode());
         }
      }

   }

   public Queue<NotificationInfo> getNotifications() {
      return this.notifications;
   }

   public boolean isEmpty() {
      return this.notifications.isEmpty();
   }

   public void clear() {
      this.notifications.clear();
      this.hashCodeMap.clear();
   }

   private void makeRoomIfNeeded() {
      if (this.notifications.size() >= 5) {
         NotificationInfo oldest = (NotificationInfo)this.notifications.poll();
         if (oldest != null) {
            this.hashCodeMap.remove(oldest.getHashCode());
         }
      }

   }

   private float getScreenHeight() {
      return class_310.method_1551().method_22683() == null ? 0.0F : (float)class_310.method_1551().method_22683().method_4502();
   }
}
