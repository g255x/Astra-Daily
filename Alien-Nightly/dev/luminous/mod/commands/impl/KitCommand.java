package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.core.Manager;
import dev.luminous.mod.commands.Command;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class KitCommand extends Command {
   public KitCommand() {
      super("kit", "[list] | [create/delete] [name]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else {
         switch (parameters[0]) {
            case "list":
               if (parameters.length == 1) {
                  try {
                     for(File file : Manager.getFolder().listFiles()) {
                        if (file.getName().endsWith(".kit")) {
                           String name = file.getName();
                           String var10001 = name.substring(0, name.length() - 4);
                           this.sendChatMessage("Kit: [" + var10001 + "]");
                        }
                     }
                  } catch (Exception var11) {
                     var11.printStackTrace();
                  }

                  return;
               }

               this.sendUsage();
               return;
            case "create":
               if (parameters.length != 2) {
                  this.sendUsage();
                  return;
               }

               if (mc.field_1724 == null) {
                  return;
               }

               try {
                  File file = Manager.getFile(parameters[1] + ".kit");
                  PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

                  for(int i = 0; i < 36; ++i) {
                     printwriter.println(i + ":" + mc.field_1724.method_31548().method_5438(i).method_7909().method_7876());
                  }

                  printwriter.close();
                  this.sendChatMessage("§fKit [" + parameters[1] + "] created");
               } catch (Exception var10) {
                  this.sendChatMessage("§fKit [" + parameters[1] + "] create failed");
                  var10.printStackTrace();
               }

               return;
            case "delete":
               if (parameters.length == 2) {
                  try {
                     File file = Manager.getFile(parameters[1] + ".kit");
                     if (file.exists()) {
                        file.delete();
                     }

                     this.sendChatMessage("§fKit [" + parameters[1] + "] removed");
                  } catch (Exception var9) {
                     var9.printStackTrace();
                  }

                  return;
               }

               this.sendUsage();
               return;
            default:
               this.sendUsage();
         }
      }

   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      if (count != 1) {
         return null;
      } else {
         String input = ((String)seperated.getLast()).toLowerCase();
         List<String> correct = new ArrayList();

         for(String x : List.of("list", "create", "delete")) {
            if (input.equalsIgnoreCase(Alien.getPrefix() + "kit") || x.toLowerCase().startsWith(input)) {
               correct.add(x);
            }
         }

         int numCmds = correct.size();
         String[] commands = new String[numCmds];
         int i = 0;

         for(String xx : correct) {
            commands[i++] = xx;
         }

         return commands;
      }
   }
}
