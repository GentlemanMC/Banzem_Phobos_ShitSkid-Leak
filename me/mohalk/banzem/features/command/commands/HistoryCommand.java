/*
 * Decompiled with CFR 0.150.
 */
package me.mohalk.banzem.features.command.commands;

import java.util.List;
import java.util.UUID;
import me.mohalk.banzem.features.command.Command;
import me.mohalk.banzem.util.PlayerUtil;

public class HistoryCommand
extends Command {
    public HistoryCommand() {
        super("history", new String[]{"<player>"});
    }

    @Override
    public void execute(String[] commands) {
        List<String> names;
        UUID uuid;
        if (commands.length == 1 || commands.length == 0) {
            HistoryCommand.sendMessage("Please specify a player.");
        }
        try {
            uuid = PlayerUtil.getUUIDFromName(commands[0]);
        }
        catch (Exception e) {
            HistoryCommand.sendMessage("An error occured.");
            return;
        }
        try {
            names = PlayerUtil.getHistoryOfNames(uuid);
        }
        catch (Exception e) {
            HistoryCommand.sendMessage("An error occured.");
            return;
        }
        if (names != null) {
            HistoryCommand.sendMessage(commands[0] + " name history:");
            for (String name : names) {
                HistoryCommand.sendMessage(name);
            }
        } else {
            HistoryCommand.sendMessage("No names found.");
        }
    }
}

