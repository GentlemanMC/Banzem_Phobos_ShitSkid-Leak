//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.mohalk.banzem.features.modules.client.ServerModule;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ C00Handshake.class })
public abstract class MixinC00Handshake
{
    @Redirect(method = { "writePacketData" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketBuffer;"))
    public PacketBuffer writePacketDataHook(final PacketBuffer packetBuffer, final String string) {
        if (ServerModule.getInstance().noFML.getValue()) {
            final String ipNoFML = string.substring(0, string.length() - "\u0000FML\u0000".length());
            return packetBuffer.writeString(ipNoFML);
        }
        return packetBuffer.writeString(string);
    }
}
