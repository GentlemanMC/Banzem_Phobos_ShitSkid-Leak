/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.chunk.VisGraph
 *  net.minecraft.util.math.BlockPos
 */
package me.mohalk.banzem.mixin.mixins;

import me.mohalk.banzem.features.modules.render.XRay;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={VisGraph.class})
public class MixinVisGraph {
    @Inject(method={"setOpaqueCube"}, at={@At(value="HEAD")}, cancellable=true)
    public void setOpaqueCubeHook(BlockPos pos, CallbackInfo info) {
        try {
            if (XRay.getInstance().isOn()) {
                info.cancel();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

