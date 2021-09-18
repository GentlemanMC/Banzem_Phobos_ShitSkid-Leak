//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.mohalk.banzem.event.events.KeyEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ KeyBinding.class })
public class MixinKeyBinding
{
    @Shadow
    private boolean pressed;
    
    @Inject(method = { "isKeyDown" }, at = { @At("RETURN") }, cancellable = true)
    private void isKeyDown(final CallbackInfoReturnable<Boolean> info) {
        final KeyEvent event = new KeyEvent(0, info.getReturnValue(), this.pressed);
        MinecraftForge.EVENT_BUS.post((Event)event);
        info.setReturnValue(event.info);
    }
}
