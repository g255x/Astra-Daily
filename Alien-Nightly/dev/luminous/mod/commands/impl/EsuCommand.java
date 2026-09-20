package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.mod.commands.Command;
import dev.luminous.mod.modules.Module;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import net.minecraft.class_2561;

public class EsuCommand extends Command {
   public EsuCommand() {
      super("esu", "");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 1) {
         Alien.THREAD.execute(() -> {
            try {
               URL url = new URL("https://api.xywlapi.cc/qqapi?qq=" + parameters[0]);
               BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

               String inputLine;
               while((inputLine = in.readLine()) != null) {
                  if (Module.nullCheck()) {
                     return;
                  }

                  mc.field_1724.method_43496(class_2561.method_30163(inputLine));
               }
            } catch (Exception var4) {
               var4.printStackTrace();
            }

         });
      }

   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
