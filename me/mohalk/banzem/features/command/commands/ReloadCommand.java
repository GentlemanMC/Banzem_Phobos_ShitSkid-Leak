/*
 * Decompiled with CFR 0.150.
 */
package me.mohalk.banzem.features.command.commands;

import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.command.Command;

public class ReloadCommand
extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Banzem.reload();
    }
}

