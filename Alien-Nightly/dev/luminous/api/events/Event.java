package dev.luminous.api.events;

import dev.luminous.api.events.eventbus.Cancelable;

public class Event {
   public Stage stage;
   private boolean cancel;
   private final boolean cancelable;
   private boolean canceled;

   public Event() {
      this(Event.Stage.Pre);
   }

   public Event(Stage stage) {
      this.cancel = false;
      this.cancelable = this.getClass().isAnnotationPresent(Cancelable.class);
      this.stage = stage;
   }

   public void cancel() {
      this.setCancelled(true);
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean cancel) {
      this.cancel = cancel;
   }

   public boolean isPost() {
      return this.stage == Event.Stage.Post;
   }

   public boolean isPre() {
      return this.stage == Event.Stage.Pre;
   }

   public void setCanceled(boolean cancel) {
      if (this.isCancelable()) {
         this.canceled = cancel;
      } else {
         throw new IllegalStateException("Cannot set event canceled");
      }
   }

   public boolean isCancelable() {
      return this.cancelable;
   }

   public static enum Stage {
      Pre,
      Post;

      // $FF: synthetic method
      private static Stage[] $values() {
         return new Stage[]{Pre, Post};
      }
   }
}
