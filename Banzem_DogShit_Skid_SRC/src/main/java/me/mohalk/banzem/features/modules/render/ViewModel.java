// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.render;

import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class ViewModel extends Module
{
    public final Setting<Integer> translateX;
    public final Setting<Integer> translateY;
    public final Setting<Integer> translateZ;
    public final Setting<Integer> rotateX;
    public final Setting<Integer> rotateY;
    public final Setting<Integer> rotateZ;
    public final Setting<Integer> scaleX;
    public final Setting<Integer> scaleY;
    public final Setting<Integer> scaleZ;
    public static ViewModel INSTANCE;
    
    public ViewModel() {
        super("ViewModel", "Cool", Category.RENDER, true, false, false);
        this.translateX = (Setting<Integer>)this.register(new Setting("TranslateX", 0, (-200), 200));
        this.translateY = (Setting<Integer>)this.register(new Setting("TranslateY", 0, (-200), 200));
        this.translateZ = (Setting<Integer>)this.register(new Setting("TranslateZ", 0, (-200), 200));
        this.rotateX = (Setting<Integer>)this.register(new Setting("RotateX", 0, (-200), 200));
        this.rotateY = (Setting<Integer>)this.register(new Setting("RotateY", 0, (-200), 200));
        this.rotateZ = (Setting<Integer>)this.register(new Setting("RotateZ", 0, (-200), 200));
        this.scaleX = (Setting<Integer>)this.register(new Setting("ScaleX", 100, 0, 200));
        this.scaleY = (Setting<Integer>)this.register(new Setting("ScaleY", 100, 0, 200));
        this.scaleZ = (Setting<Integer>)this.register(new Setting("ScaleZ", 100, 0, 200));
        ViewModel.INSTANCE = this;
    }
}
