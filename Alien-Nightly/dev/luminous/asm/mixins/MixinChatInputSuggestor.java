package dev.luminous.asm.mixins;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.luminous.Alien;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.commands.Command;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4717;
import net.minecraft.class_5481;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4717.class})
public abstract class MixinChatInputSuggestor {
   @Final
   @Shadow
   class_342 field_21599;
   @Shadow
   private CompletableFuture<Suggestions> field_21611;
   @Final
   @Shadow
   private List<class_5481> field_21607;
   @Unique
   private boolean showOutline = false;

   @Shadow
   public abstract void method_23920(boolean var1);

   @Inject(
      at = {@At("HEAD")},
      method = {"method_23923"}
   )
   private void onRender(class_332 context, int mouseX, int mouseY, CallbackInfo ci) {
      if (this.showOutline) {
         int x = this.field_21599.method_46426() - 3;
         int y = this.field_21599.method_46427() - 3;
         Render2DUtil.drawRect(context.method_51448(), (float)x, (float)y, (float)(this.field_21599.method_25368() + 1), 1.0F, ClientSetting.INSTANCE.color.getValue().getRGB());
         Render2DUtil.drawRect(context.method_51448(), (float)x, (float)(y + this.field_21599.method_25364() + 1), (float)(this.field_21599.method_25368() + 1), 1.0F, ClientSetting.INSTANCE.color.getValue().getRGB());
         Render2DUtil.drawRect(context.method_51448(), (float)x, (float)y, 1.0F, (float)(this.field_21599.method_25364() + 1), ClientSetting.INSTANCE.color.getValue().getRGB());
         Render2DUtil.drawRect(context.method_51448(), (float)(x + this.field_21599.method_25368() + 1), (float)y, 1.0F, (float)(this.field_21599.method_25364() + 2), ClientSetting.INSTANCE.color.getValue().getRGB());
      }

   }

   @Inject(
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_342;method_1881()I",
   ordinal = 0
)},
      method = {"method_23934()V"}
   )
   private void onRefresh(CallbackInfo ci) {
      String prefix = Alien.getPrefix();
      String string = this.field_21599.method_1882();
      this.showOutline = string.startsWith(prefix);
      if (!string.isEmpty()) {
         int cursorPos = this.field_21599.method_1881();
         String string2 = string.substring(0, cursorPos);
         if (prefix.startsWith(string2) || string2.startsWith(prefix)) {
            int j = 0;

            for(Matcher matcher = Pattern.compile("(\\s+)").matcher(string2); matcher.find(); j = matcher.end()) {
            }

            SuggestionsBuilder builder = new SuggestionsBuilder(string2, j);
            if (string2.length() < prefix.length()) {
               if (!prefix.startsWith(string2)) {
                  return;
               }

               builder.suggest(prefix);
            } else {
               if (!string2.startsWith(prefix)) {
                  return;
               }

               int count = StringUtils.countMatches(string2, " ");
               List<String> seperated = Arrays.asList(string2.split(" "));
               if (count == 0) {
                  for(Object strObj : Alien.COMMAND.getCommands().keySet().toArray()) {
                     String str = (String)strObj;
                     String var10001 = Alien.getPrefix();
                     builder.suggest(var10001 + str + " ");
                  }
               } else {
                  if (seperated.isEmpty()) {
                     return;
                  }

                  Command c = Alien.COMMAND.getCommandBySyntax(((String)seperated.getFirst()).substring(prefix.length()));
                  if (c == null) {
                     this.field_21607.add(class_2561.method_30163(" §4no commands found: §f" + ((String)seperated.getFirst()).substring(prefix.length())).method_30937());
                     return;
                  }

                  String[] suggestions = c.getAutocorrect(count, seperated);
                  if (suggestions == null || suggestions.length == 0) {
                     return;
                  }

                  for(String str : suggestions) {
                     builder.suggest(str + " ");
                  }
               }
            }

            this.field_21611 = builder.buildFuture();
            this.method_23920(false);
         }
      }

   }
}
