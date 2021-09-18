// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.module;

import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class FriendSettings extends Module
{
    private static FriendSettings INSTANCE;
    public Setting<Boolean> notify;
    
    public FriendSettings() {
        super("FriendSettings", "Change aspects of friends", Category.MODULE, true, false, false);
        this.notify = (Setting<Boolean>)this.register(new Setting("Notify", false));
        FriendSettings.INSTANCE = this;
    }
    
    public static FriendSettings getInstance() {
        if (FriendSettings.INSTANCE == null) {
            FriendSettings.INSTANCE = new FriendSettings();
        }
        return FriendSettings.INSTANCE;
    }
}
