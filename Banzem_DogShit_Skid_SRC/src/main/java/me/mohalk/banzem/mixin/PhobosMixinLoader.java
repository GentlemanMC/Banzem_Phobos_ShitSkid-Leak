// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import me.mohalk.banzem.Banzem;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class PhobosMixinLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public PhobosMixinLoader() {
        Banzem.LOGGER.info("Banzem mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.phobos.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        Banzem.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        PhobosMixinLoader.isObfuscatedEnvironment = (boolean) data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        PhobosMixinLoader.isObfuscatedEnvironment = false;
    }
}
