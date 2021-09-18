// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.command.commands;

import java.io.IOException;
import me.mohalk.banzem.features.modules.client.IRC;
import me.mohalk.banzem.features.command.Command;

public class IRCCommand extends Command
{
    public IRCCommand() {
        super("IRC");
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(IRC.INSTANCE.status ? "§aIRC is connected." : "§cIRC is not connected.");
        }
        else if (commands.length == 2) {
            if (commands[0].equalsIgnoreCase("connect")) {
                Command.sendMessage("§aConnecting to the PhobosClient IRC...");
                try {
                    IRC.INSTANCE.connect();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("disconnect")) {
                Command.sendMessage("§aDisconnecting from the PhobosClient IRC...");
                try {
                    IRC.INSTANCE.disconnect();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("friendall")) {
                Command.sendMessage("§aFriending all...");
                try {
                    IRC.INSTANCE.friendAll();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("list")) {
                Command.sendMessage("§aListing PhobosClient Users...");
                try {
                    IRC.INSTANCE.list();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (commands.length >= 3) {
            if (commands[0].equalsIgnoreCase("say")) {
                Command.sendMessage("§aSending message to the PhobosClient chat server...");
                final StringBuilder builder = new StringBuilder();
                for (int i = 1; i < commands.length - 1; ++i) {
                    builder.append(commands[i]).append(" ");
                }
                final String message = builder.toString();
                try {
                    IRC.say(message);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("cockt")) {
                Command.sendMessage("§acockkk");
                try {
                    IRC.cockt(Integer.parseInt(commands[1]));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
