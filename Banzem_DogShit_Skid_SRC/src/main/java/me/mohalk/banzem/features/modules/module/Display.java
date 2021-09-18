// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.module;

import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class Display extends Module
{
    private static Display INSTANCE;
    public Setting<String> gang;
    public Setting<Boolean> version;
    
    public Display() {
        super("Display", "Sets the title of your game", Category.MODULE, true, false, false);
        this.gang = (Setting<String>)this.register(new Setting("Title", "Banzem"));
        this.version = (Setting<Boolean>)this.register(new Setting("Version", true));
        this.setInstance();
    }
    
    public static Display getInstance() {
        if (Display.INSTANCE == null) {
            Display.INSTANCE = new Display();
        }
        return Display.INSTANCE;
    }
    
    private void setInstance() {
        Display.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        org.lwjgl.opengl.Display.setTitle((String)this.gang.getValue());
    }
    
    static {
        Display.INSTANCE = new Display();
    }
}
