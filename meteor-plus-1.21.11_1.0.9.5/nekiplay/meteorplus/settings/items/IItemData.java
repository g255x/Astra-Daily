package nekiplay.meteorplus.settings.items;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WidgetScreen;
import meteordevelopment.meteorclient.utils.misc.IChangeable;
import meteordevelopment.meteorclient.utils.misc.ICopyable;
import meteordevelopment.meteorclient.utils.misc.ISerializable;
import net.minecraft.class_1792;

public interface IItemData<T extends ICopyable<T> & ISerializable<T> & IChangeable & IItemData<T>> {
   WidgetScreen createScreen(GuiTheme var1, class_1792 var2, ItemDataSetting<T> var3);
}
