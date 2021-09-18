//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem;

import me.mohalk.banzem.features.modules.client.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.util.concurrent.atomic.AtomicReference;

public class DiscordPresence
{
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;
    
    public static void start() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordPresence.rpc.Discord_Initialize("883797697189019678", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.details = ((Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) ? "In the main menu." : ("Playing " + ((Minecraft.getMinecraft().getCurrentServerData() != null) ? (RPC.INSTANCE.showIP.getValue() ? ("on " + Minecraft.getMinecraft().getCurrentServerData().serverIP + ".") : " multiplayer.") : " singleplayer.")));
        DiscordPresence.presence.state = RPC.INSTANCE.state.getValue();
        DiscordPresence.presence.largeImageText = RPC.INSTANCE.largeImageText.getValue();
        DiscordPresence.presence.largeImageKey = "banzem";
        DiscordPresence.presence.smallImageText = RPC.INSTANCE.smallImageText.getValue();
        DiscordPresence.presence.partyId = "banzem";
        DiscordPresence.presence.partyMax = 50;
        DiscordPresence.presence.partySize = 1;
        DiscordPresence.presence.joinSecret = "Banzem 1.0.0!";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
        AtomicReference<String> string = new AtomicReference<>();
        AtomicReference<StringBuilder> sb = new AtomicReference<>();
        AtomicReference<DiscordRichPresence> presence = new AtomicReference<>();
        AtomicReference<String> string2 = new AtomicReference<>();
        (DiscordPresence.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DiscordPresence.rpc.Discord_RunCallbacks();
                string.set("");
                sb.set(new StringBuilder());
                presence.set(DiscordPresence.presence);
                new StringBuilder().append("Playing ");
                if (Minecraft.getMinecraft().getCurrentServerData() != null) {
                    if (RPC.INSTANCE.showIP.getValue()) {
                        string2.set("on " + Minecraft.getMinecraft().getCurrentServerData().serverIP + ".");
                    }
                    else {
                        string2.set(" multiplayer.");
                    }
                }
                else {
                    string2.set(" not multiplayer.");
                }
                DiscordPresence.presence.state = RPC.INSTANCE.state.getValue();
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException ex) {}
            }
        }, "RPC-Callback-Handler")).start();
    }
    
    public static void stop() {
        if (DiscordPresence.thread != null && !DiscordPresence.thread.isInterrupted()) {
            DiscordPresence.thread.interrupt();
        }
        DiscordPresence.rpc.Discord_Shutdown();
    }
    
    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
    }
}
