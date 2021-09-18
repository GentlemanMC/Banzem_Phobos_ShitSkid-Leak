// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.command.commands;

import java.util.Iterator;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.command.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("commands");
    }
    
    @Override
    public void execute(final String[] commands) {
        Command.sendMessage("You can use following commands: ");
        for (final Command command : Banzem.commandManager.getCommands()) {
            Command.sendMessage(Banzem.commandManager.getPrefix() + command.getName());
        }
    }
}
