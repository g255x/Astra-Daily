package nekiplay.meteorplus.settings.items;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.misc.IChangeable;
import meteordevelopment.meteorclient.utils.misc.ICopyable;
import meteordevelopment.meteorclient.utils.misc.IGetter;
import meteordevelopment.meteorclient.utils.misc.ISerializable;
import net.minecraft.class_1792;
import net.minecraft.class_2487;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public class ItemDataSetting<T extends ICopyable<T> & ISerializable<T> & IChangeable & IItemData<T>> extends Setting<Map<class_1792, T>> {
   public final IGetter<T> defaultData;

   public ItemDataSetting(String name, String description, Map<class_1792, T> defaultValue, Consumer<Map<class_1792, T>> onChanged, Consumer<Setting<Map<class_1792, T>>> onModuleActivated, IGetter<T> defaultData, IVisible visible) {
      super(name, description, defaultValue, onChanged, onModuleActivated, visible);
      this.defaultData = defaultData;
   }

   public void resetImpl() {
      this.value = new HashMap((Map)this.defaultValue);
   }

   protected Map<class_1792, T> parseImpl(String str) {
      return new HashMap(0);
   }

   protected boolean isValueValid(Map<class_1792, T> value) {
      return true;
   }

   protected class_2487 save(class_2487 tag) {
      class_2487 valueTag = new class_2487();

      for(class_1792 block : ((Map)this.get()).keySet()) {
         valueTag.method_10566(class_7923.field_41178.method_10221(block).toString(), ((ISerializable)((ICopyable)((Map)this.get()).get(block))).toTag());
      }

      tag.method_10566("value", valueTag);
      return tag;
   }

   protected Map<class_1792, T> load(class_2487 tag) {
      ((Map)this.get()).clear();
      class_2487 valueTag = (class_2487)tag.method_10562("value").get();

      for(String key : valueTag.method_10541()) {
         ((Map)this.get()).put((class_1792)class_7923.field_41178.method_63535(class_2960.method_60654(key)), (ICopyable)((ISerializable)((ICopyable)this.defaultData.get()).copy()).fromTag((class_2487)valueTag.method_10562(key).get()));
      }

      return (Map)this.get();
   }

   public static class Builder<T extends ICopyable<T> & ISerializable<T> & IChangeable & IItemData<T>> extends Setting.SettingBuilder<Builder<T>, Map<class_1792, T>, ItemDataSetting<T>> {
      private IGetter<T> defaultData;

      public Builder() {
         super(new HashMap(0));
      }

      public Builder<T> defaultData(IGetter<T> defaultData) {
         this.defaultData = defaultData;
         return this;
      }

      public ItemDataSetting<T> build() {
         return new ItemDataSetting<T>(this.name, this.description, (Map)this.defaultValue, this.onChanged, this.onModuleActivated, this.defaultData, this.visible);
      }
   }
}
