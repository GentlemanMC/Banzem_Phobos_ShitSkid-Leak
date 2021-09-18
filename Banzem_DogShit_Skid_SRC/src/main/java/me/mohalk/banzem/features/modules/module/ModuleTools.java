// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.module;

import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class ModuleTools extends Module
{
    private static ModuleTools INSTANCE;
    public Setting<Notifier> notifier;
    public Setting<PopNotifier> popNotifier;
    
    public ModuleTools() {
        super("ModuleTools", "Change settings", Category.MODULE, true, false, false);
        this.notifier = (Setting<Notifier>)this.register(new Setting("ModuleNotifier", Notifier.FUTURE));
        this.popNotifier = (Setting<PopNotifier>)this.register(new Setting("PopNotifier", PopNotifier.FUTURE));
        ModuleTools.INSTANCE = this;
    }
    
    public static ModuleTools getInstance() {
        if (ModuleTools.INSTANCE == null) {
            ModuleTools.INSTANCE = new ModuleTools();
        }
        return ModuleTools.INSTANCE;
    }
    
    public enum Notifier
    {
        PHOBOS, 
        FUTURE, 
        DOTGOD;
    }
    
    public enum PopNotifier
    {
        PHOBOS, 
        FUTURE, 
        DOTGOD, 
        NONE;
    }
}
