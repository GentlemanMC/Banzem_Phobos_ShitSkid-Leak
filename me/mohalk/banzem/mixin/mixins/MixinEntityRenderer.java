/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  javax.annotation.Nullable
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.world.World
 */
package me.mohalk.banzem.mixin.mixins;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import me.mohalk.banzem.features.modules.client.Notifications;
import me.mohalk.banzem.features.modules.player.Speedmine;
import me.mohalk.banzem.features.modules.render.NoRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class})
public abstract class MixinEntityRenderer {
    private boolean injection = true;
    @Shadow
    public ItemStack field_190566_ab;
    @Shadow
    @Final
    public Minecraft field_78531_r;

    @Shadow
    public abstract void func_78473_a(float var1);

    @Inject(method={"renderItemActivation"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemActivationHook(CallbackInfo info) {
        if (this.field_190566_ab != null && NoRender.getInstance().isOn() && NoRender.getInstance().totemPops.getValue().booleanValue() && this.field_190566_ab.func_77973_b() == Items.field_190929_cY) {
            info.cancel();
        }
    }

    @Inject(method={"updateLightmap"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ENTITY || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.cancel();
        }
    }

    @Inject(method={"getMouseOver(F)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void getMouseOverHook(float partialTicks, CallbackInfo info) {
        if (this.injection) {
            block3: {
                info.cancel();
                this.injection = false;
                try {
                    this.func_78473_a(partialTicks);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (!Notifications.getInstance().isOn() || !Notifications.getInstance().crash.getValue().booleanValue()) break block3;
                    Notifications.displayCrash(e);
                }
            }
            this.injection = true;
        }
    }

    @Redirect(method={"setupCameraTransform"}, at=@At(value="FIELD", target="Lnet/minecraft/client/entity/EntityPlayerSP;prevTimeInPortal:F"))
    public float prevTimeInPortalHook(EntityPlayerSP entityPlayerSP) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().nausea.getValue().booleanValue()) {
            return -3.4028235E38f;
        }
        return entityPlayerSP.field_71080_cy;
    }

    @Inject(method={"setupFog"}, at={@At(value="HEAD")}, cancellable=true)
    public void setupFogHook(int startCoords, float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.NOFOG) {
            info.cancel();
        }
    }

    @Redirect(method={"setupFog"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getBlockStateAtEntityViewpointHook(World worldIn, Entity entityIn, float p_186703_2_) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.AIR) {
            return Blocks.field_150350_a.field_176228_M;
        }
        return ActiveRenderInfo.func_186703_a((World)worldIn, (Entity)entityIn, (float)p_186703_2_);
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().hurtcam.getValue().booleanValue()) {
            info.cancel();
        }
    }

    @Redirect(method={"getMouseOver"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcludingHook(WorldClient worldClient, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
        if (Speedmine.getInstance().isOn() && Speedmine.getInstance().noTrace.getValue().booleanValue() && (!Speedmine.getInstance().pickaxe.getValue().booleanValue() || this.field_78531_r.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemPickaxe)) {
            return new ArrayList<Entity>();
        }
        if (Speedmine.getInstance().isOn() && Speedmine.getInstance().noTrace.getValue().booleanValue() && Speedmine.getInstance().noGapTrace.getValue().booleanValue() && this.field_78531_r.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151153_ao) {
            return new ArrayList<Entity>();
        }
        return worldClient.func_175674_a(entityIn, boundingBox, predicate);
    }
}

