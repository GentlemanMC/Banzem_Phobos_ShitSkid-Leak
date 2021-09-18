// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.client;

import me.mohalk.banzem.DiscordPresence;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class RPC extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> showIP;
    public Setting<String> state;
    public Setting<String> largeImageText;
    public Setting<String> smallImageText;
    
    public RPC() {
        super("RPC", "Discord rich presence", Category.CLIENT, false, false, false);
        this.showIP = (Setting<Boolean>)this.register(new Setting("ShowIP", true, "Shows the server IP in your discord presence."));
        this.state = (Setting<String>)this.register(new Setting("State", "Banzem", "Sets the state of the DiscordRPC."));
        this.largeImageText = (Setting<String>)this.register(new Setting("LargeImageText", "Banzem", "Sets the large image text of the DiscordRPC."));
        this.smallImageText = (Setting<String>)this.register(new Setting("SmallImageText", "Banzem", "Sets the small image text of the DiscordRPC."));
        RPC.INSTANCE = this;
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
