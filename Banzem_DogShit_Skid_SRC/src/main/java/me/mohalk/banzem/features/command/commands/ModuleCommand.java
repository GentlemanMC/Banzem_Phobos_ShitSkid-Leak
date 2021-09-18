// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.command.commands;

import java.util.Iterator;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.manager.ConfigManager;
import com.google.gson.JsonParser;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.command.Command;

public class ModuleCommand extends Command
{
    public ModuleCommand() {
        super("module", new String[] { "<module>", "<set/reset>", "<setting>", "<value>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("Modules: ");
            for (final Module.Category category : Banzem.moduleManager.getCategories()) {
                String modules = category.getName() + ": ";
                for (final Module module : Banzem.moduleManager.getModulesByCategory(category)) {
                    modules = modules + (module.isEnabled() ? "§a" : "§c") + module.getName() + "§r, ";
                }
                Command.sendMessage(modules);
            }
            return;
        }
        Module module2 = Banzem.moduleManager.getModuleByDisplayName(commands[0]);
        if (module2 == null) {
            module2 = Banzem.moduleManager.getModuleByName(commands[0]);
            if (module2 == null) {
                Command.sendMessage("§cThis module doesnt exist.");
                return;
            }
            Command.sendMessage("§c This is the original name of the module. Its current name is: " + module2.getDisplayName());
        }
        else {
            if (commands.length == 2) {
                Command.sendMessage(module2.getDisplayName() + " : " + module2.getDescription());
                for (final Setting setting2 : module2.getSettings()) {
                    Command.sendMessage(setting2.getName() + " : " + setting2.getValue() + ", " + setting2.getDescription());
                }
                return;
            }
            if (commands.length == 3) {
                if (commands[1].equalsIgnoreCase("set")) {
                    Command.sendMessage("§cPlease specify a setting.");
                }
                else if (commands[1].equalsIgnoreCase("reset")) {
                    for (final Setting setting3 : module2.getSettings()) {
                        setting3.setValue(setting3.getDefaultValue());
                    }
                }
                else {
                    Command.sendMessage("§cThis command doesnt exist.");
                }
                return;
            }
            if (commands.length == 4) {
                Command.sendMessage("§cPlease specify a value.");
                return;
            }
            final Setting setting4;
            if (commands.length == 5 && (setting4 = module2.getSettingByName(commands[2])) != null) {
                final JsonParser jp = new JsonParser();
                if (setting4.getType().equalsIgnoreCase("String")) {
                    setting4.setValue(commands[3]);
                    Command.sendMessage("§a" + module2.getName() + " " + setting4.getName() + " has been set to " + commands[3] + ".");
                    return;
                }
                try {
                    if (setting4.getName().equalsIgnoreCase("Enabled")) {
                        if (commands[3].equalsIgnoreCase("true")) {
                            module2.enable();
                        }
                        if (commands[3].equalsIgnoreCase("false")) {
                            module2.disable();
                        }
                    }
                    ConfigManager.setValueFromJson(module2, setting4, jp.parse(commands[3]));
                }
                catch (Exception e) {
                    Command.sendMessage("§cBad Value! This setting requires a: " + setting4.getType() + " value.");
                    return;
                }
                Command.sendMessage("§a" + module2.getName() + " " + setting4.getName() + " has been set to " + commands[3] + ".");
            }
        }
    }
}
