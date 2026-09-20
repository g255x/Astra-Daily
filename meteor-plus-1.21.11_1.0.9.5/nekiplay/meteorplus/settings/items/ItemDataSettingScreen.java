package nekiplay.meteorplus.settings.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.utils.misc.IChangeable;
import meteordevelopment.meteorclient.utils.misc.ICopyable;
import meteordevelopment.meteorclient.utils.misc.ISerializable;
import meteordevelopment.meteorclient.utils.misc.Names;
import net.minecraft.class_1792;
import net.minecraft.class_7923;
import org.apache.commons.lang3.StringUtils;

public class ItemDataSettingScreen extends WindowScreen {
   private static final List<class_1792> BLOCKS = new ArrayList(100);
   private final ItemDataSetting<?> setting;
   private WTable table;
   private String filterText = "";

   public ItemDataSettingScreen(GuiTheme theme, ItemDataSetting<?> setting) {
      super(theme, "Configure Items");
      this.setting = setting;
   }

   public void initWidgets() {
      WTextBox filter = (WTextBox)this.add(this.theme.textBox("")).minWidth((double)400.0F).expandX().widget();
      filter.setFocused(true);
      filter.action = () -> {
         this.filterText = filter.get().trim();
         this.table.clear();
         this.initTable();
      };
      this.table = (WTable)this.add(this.theme.table()).expandX().widget();
      this.initTable();
   }

   public <T extends ICopyable<T> & ISerializable<T> & IChangeable & IItemData<T>> void initTable() {
      for(class_1792 Item : class_7923.field_41178) {
         T blockData = (T)(((Map)this.setting.get()).get(Item));
         if (blockData != null && ((IChangeable)blockData).isChanged()) {
            BLOCKS.add(0, Item);
         } else {
            BLOCKS.add(Item);
         }
      }

      for(class_1792 block : BLOCKS) {
         String name = Names.get(block);
         if (StringUtils.containsIgnoreCase(name, this.filterText)) {
            T blockData = (T)(((Map)this.setting.get()).get(block));
            this.table.add(this.theme.itemWithLabel(block.method_8389().method_7854(), Names.get(block))).expandCellX();
            this.table.add(this.theme.label(blockData != null && ((IChangeable)blockData).isChanged() ? "*" : " "));
            WButton edit = (WButton)this.table.add(this.theme.button(GuiRenderer.EDIT)).widget();
            edit.action = () -> {
               T data = blockData;
               if (blockData == null) {
                  data = (T)((ICopyable)this.setting.defaultData.get()).copy();
               }

               MeteorClient.mc.method_1507(((IItemData)data).createScreen(this.theme, block, this.setting));
            };
            WButton reset = (WButton)this.table.add(this.theme.button(GuiRenderer.RESET)).widget();
            reset.action = () -> {
               ((Map)this.setting.get()).remove(block);
               this.setting.onChanged();
               if (blockData != null && ((IChangeable)blockData).isChanged()) {
                  this.table.clear();
                  this.initTable();
               }

            };
            this.table.row();
         }
      }

      BLOCKS.clear();
   }
}
