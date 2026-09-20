package dev.Astra.api.events;

public class Event {
   public Stage stage;
   private boolean cancel;

   public Event() {
      this(Event.Stage.Pre);
   }

   public Event(Stage stage) {
      this.cancel = false;
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

   public static enum Stage {
      Pre,
      Post;

      // $FF: synthetic method
      private static Stage[] $values() {
         return new Stage[]{Pre, Post};
      }
   }
}
