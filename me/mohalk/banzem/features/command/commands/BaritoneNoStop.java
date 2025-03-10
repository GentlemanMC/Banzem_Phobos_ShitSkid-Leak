/*
 * Decompiled with CFR 0.150.
 */
package me.mohalk.banzem.features.command.commands;

import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.command.Command;

public class BaritoneNoStop
extends Command {
    public BaritoneNoStop() {
        super("noStop", new String[]{"<prefix>", "<x>", "<y>", "<z>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 5) {
            Banzem.baritoneManager.setPrefix(commands[0]);
            int x = 0;
            int y = 0;
            int z = 0;
            try {
                x = Integer.parseInt(commands[1]);
                y = Integer.parseInt(commands[2]);
                z = Integer.parseInt(commands[3]);
            }
            catch (NumberFormatException e) {
                BaritoneNoStop.sendMessage("Invalid Input for x, y or z!");
                Banzem.baritoneManager.stop();
                return;
            }
            Banzem.baritoneManager.start(x, y, z);
            return;
        }
        BaritoneNoStop.sendMessage("Stoping Baritone-Nostop.");
        Banzem.baritoneManager.stop();
    }
}

