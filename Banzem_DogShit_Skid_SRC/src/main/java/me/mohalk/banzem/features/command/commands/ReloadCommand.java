// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.command.commands;

import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.command.Command;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        Banzem.reload();
    }
}
