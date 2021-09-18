// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.command.commands;

import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.modules.client.ClickGui;
import me.mohalk.banzem.features.command.Command;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", new String[] { "<char>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("§cSpecify a new prefix.");
            return;
        }
        Banzem.moduleManager.getModuleByClass(ClickGui.class).prefix.setValue(commands[0]);
        Command.sendMessage("Prefix set to §a" + Banzem.commandManager.getPrefix());
    }
}
