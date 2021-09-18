//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.client;

import me.mohalk.banzem.util.Util;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class Media extends Module
{
    private static Media instance;
    public final Setting<Boolean> changeOwn;
    public final Setting<String> ownName;
    
    public Media() {
        super("Media", "Helps with creating Media", Category.CLIENT, false, false, false);
        this.changeOwn = (Setting<Boolean>)this.register(new Setting("MyName", true));
        this.ownName = (Setting<String>)this.register(new Setting("Name", "Name here...", v -> this.changeOwn.getValue()));
        Media.instance = this;
    }
    
    public static Media getInstance() {
        if (Media.instance == null) {
            Media.instance = new Media();
        }
        return Media.instance;
    }
    
    public static String getPlayerName() {
        if (Feature.fullNullCheck() || !ServerModule.getInstance().isConnected()) {
            return Util.mc.getSession().getUsername();
        }
        final String name = ServerModule.getInstance().getPlayerName();
        if (name == null || name.isEmpty()) {
            return Util.mc.getSession().getUsername();
        }
        return name;
    }
}
