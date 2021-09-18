//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.mixin.mixins;

import net.minecraft.client.renderer.GlStateManager;
import me.mohalk.banzem.features.modules.render.ViewModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import me.mohalk.banzem.features.modules.render.NoRender;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.EnumHandSide;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    @Shadow
    @Final
    public Minecraft mc;
    private boolean injection;
    
    public MixinItemRenderer() {
        this.injection = true;
    }
    
    @Shadow
    public abstract void renderItemInFirstPerson(final AbstractClientPlayer p0, final float p1, final float p2, final EnumHand p3, final float p4, final ItemStack p5, final float p6);
    
    @Shadow
    protected abstract void renderArmFirstPerson(final float p0, final float p1, final EnumHandSide p2);
    
    @Inject(method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V" }, at = { @At("HEAD") }, cancellable = true)
    public void renderItemInFirstPersonHook(final AbstractClientPlayer player, final float p_1874572, final float p_1874573, final EnumHand hand, final float p_1874575, final ItemStack stack, final float p_1874577, final CallbackInfo info) {
    }
    
    @Inject(method = { "renderFireInFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    public void renderFireInFirstPersonHook(final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fire.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderSuffocationOverlay" }, at = { @At("HEAD") }, cancellable = true)
    public void renderSuffocationOverlay(final CallbackInfo ci) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().blocks.getValue()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderItemSide" }, at = { @At("HEAD") })
    public void renderItemSide(final EntityLivingBase entitylivingbaseIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo ci) {
        if (ViewModel.INSTANCE.isEnabled()) {
            GlStateManager.scale(ViewModel.INSTANCE.scaleX.getValue() / 100.0f, ViewModel.INSTANCE.scaleY.getValue() / 100.0f, ViewModel.INSTANCE.scaleZ.getValue() / 100.0f);
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate(ViewModel.INSTANCE.translateX.getValue() / 200.0f, ViewModel.INSTANCE.translateY.getValue() / 200.0f, ViewModel.INSTANCE.translateZ.getValue() / 200.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateX.getValue(), 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateY.getValue(), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateZ.getValue(), 0.0f, 0.0f, 1.0f);
            }
            else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate(-ViewModel.INSTANCE.translateX.getValue() / 200.0f, ViewModel.INSTANCE.translateY.getValue() / 200.0f, ViewModel.INSTANCE.translateZ.getValue() / 200.0f);
                GlStateManager.rotate((float)(-ViewModel.INSTANCE.rotateX.getValue()), 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateY.getValue(), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateZ.getValue(), 0.0f, 0.0f, 1.0f);
            }
        }
    }
}
