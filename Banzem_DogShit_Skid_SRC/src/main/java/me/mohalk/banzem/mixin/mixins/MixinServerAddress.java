// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.mohalk.banzem.mixin.mixins.accessors.IServerAddress;
import me.mohalk.banzem.features.modules.client.ServerModule;
import net.minecraft.client.multiplayer.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ServerAddress.class })
public abstract class MixinServerAddress
{
    @Redirect(method = { "fromString" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ServerAddress;getServerAddress(Ljava/lang/String;)[Ljava/lang/String;"))
    private static String[] getServerAddressHook(final String ip) {
        final ServerModule module;
        final int port;
        if (ip.equals(ServerModule.getInstance().ip.getValue()) && (port = (module = ServerModule.getInstance()).getPort()) != -1) {
            return new String[] { ServerModule.getInstance().ip.getValue(), Integer.toString(port) };
        }
        return IServerAddress.getServerAddress(ip);
    }
}
