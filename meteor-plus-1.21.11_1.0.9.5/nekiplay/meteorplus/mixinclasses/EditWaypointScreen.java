package nekiplay.meteorplus.mixinclasses;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.screens.EditSystemScreen;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.waypoints.Waypoint;
import meteordevelopment.meteorclient.systems.waypoints.Waypoints;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;

public class EditWaypointScreen extends EditSystemScreen<Waypoint> {
   private Runnable reload;

   public EditWaypointScreen(GuiTheme theme, Waypoint value, Runnable reload) {
      super(theme, value, reload);
      this.reload = reload;
   }

   public Waypoint create() {
      return (new Waypoint.Builder()).pos(MeteorClient.mc.field_1724.method_24515().method_10086(2)).dimension(PlayerUtils.getDimension()).build();
   }

   public boolean save() {
      boolean added = !this.isNew || Waypoints.get().add((Waypoint)this.value);
      return added;
   }

   public Settings getSettings() {
      return ((Waypoint)this.value).settings;
   }
}
