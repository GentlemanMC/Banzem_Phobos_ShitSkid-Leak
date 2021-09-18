/*
 * Decompiled with CFR 0.150.
 */
package me.mohalk.banzem.features.modules.client;

import me.mohalk.banzem.DiscordPresence;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.features.setting.Setting;

public class RPC
extends Module {
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = this.register(new Setting<Boolean>("ShowIP", Boolean.valueOf(true), "Shows the server IP in your discord presence."));
    public Setting<String> state = this.register(new Setting<String>("State", "Banzem", "Sets the state of the DiscordRPC."));
    public Setting<String> largeImageText = this.register(new Setting<String>("LargeImageText", "Banzem", "Sets the large image text of the DiscordRPC."));
    public Setting<String> smallImageText = this.register(new Setting<String>("SmallImageText", "Banzem", "Sets the small image text of the DiscordRPC."));

    public RPC() {
        super("RPC", "Discord rich presence", Module.Category.CLIENT, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}

