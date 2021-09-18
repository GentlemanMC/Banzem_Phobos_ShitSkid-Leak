//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.mohalk.banzem.util.EntityUtil;
import java.awt.Color;
import me.mohalk.banzem.util.RenderUtil;
import me.mohalk.banzem.features.modules.client.Colors;
import org.lwjgl.opengl.GL11;
import me.mohalk.banzem.event.events.RenderEntityModelEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import me.mohalk.banzem.features.modules.render.CrystalChanger;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderEnderCrystal.class })
public class MixinRenderEnderCrystal
{
    @Shadow
    @Final
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    private static ResourceLocation glint;
    
    @Redirect(method = { "doRender" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (CrystalChanger.INSTANCE.isEnabled()) {
            if (CrystalChanger.INSTANCE.animateScale.getValue() && CrystalChanger.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale((float)CrystalChanger.INSTANCE.scaleMap.get(entity), (float)CrystalChanger.INSTANCE.scaleMap.get(entity), (float)CrystalChanger.INSTANCE.scaleMap.get(entity));
            }
            else {
                GlStateManager.scale((float)CrystalChanger.INSTANCE.scale.getValue(), (float)CrystalChanger.INSTANCE.scale.getValue(), (float)CrystalChanger.INSTANCE.scale.getValue());
            }
        }
        if (CrystalChanger.INSTANCE.isEnabled() && CrystalChanger.INSTANCE.wireframe.getValue()) {
            final RenderEntityModelEvent event = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            CrystalChanger.INSTANCE.onRenderModel(event);
        }
        if (CrystalChanger.INSTANCE.isEnabled() && CrystalChanger.INSTANCE.chams.getValue()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            if (CrystalChanger.INSTANCE.rainbow.getValue()) {
                final Color rainbowColor1 = CrystalChanger.INSTANCE.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(CrystalChanger.INSTANCE.speed.getValue() * 100, 0, CrystalChanger.INSTANCE.saturation.getValue() / 100.0f, CrystalChanger.INSTANCE.brightness.getValue() / 100.0f));
                final Color rainbowColor2 = EntityUtil.getColor(entity, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), CrystalChanger.INSTANCE.alpha.getValue(), true);
                if (CrystalChanger.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, CrystalChanger.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChanger.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            }
            else if (CrystalChanger.INSTANCE.xqz.getValue() && CrystalChanger.INSTANCE.throughWalls.getValue()) {
                final Color hiddenColor = CrystalChanger.INSTANCE.colorSync.getValue() ? EntityUtil.getColor(entity, CrystalChanger.INSTANCE.hiddenRed.getValue(), CrystalChanger.INSTANCE.hiddenGreen.getValue(), CrystalChanger.INSTANCE.hiddenBlue.getValue(), CrystalChanger.INSTANCE.hiddenAlpha.getValue(), true) : EntityUtil.getColor(entity, CrystalChanger.INSTANCE.hiddenRed.getValue(), CrystalChanger.INSTANCE.hiddenGreen.getValue(), CrystalChanger.INSTANCE.hiddenBlue.getValue(), CrystalChanger.INSTANCE.hiddenAlpha.getValue(), true);
                final Color color;
                final Color visibleColor = color = (CrystalChanger.INSTANCE.colorSync.getValue() ? EntityUtil.getColor(entity, CrystalChanger.INSTANCE.red.getValue(), CrystalChanger.INSTANCE.green.getValue(), CrystalChanger.INSTANCE.blue.getValue(), CrystalChanger.INSTANCE.alpha.getValue(), true) : EntityUtil.getColor(entity, CrystalChanger.INSTANCE.red.getValue(), CrystalChanger.INSTANCE.green.getValue(), CrystalChanger.INSTANCE.blue.getValue(), CrystalChanger.INSTANCE.alpha.getValue(), true));
                if (CrystalChanger.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(hiddenColor.getRed() / 255.0f, hiddenColor.getGreen() / 255.0f, hiddenColor.getBlue() / 255.0f, CrystalChanger.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChanger.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                GL11.glColor4f(visibleColor.getRed() / 255.0f, visibleColor.getGreen() / 255.0f, visibleColor.getBlue() / 255.0f, CrystalChanger.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
            else {
                final Color color2;
                final Color visibleColor = color2 = (CrystalChanger.INSTANCE.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entity, CrystalChanger.INSTANCE.red.getValue(), CrystalChanger.INSTANCE.green.getValue(), CrystalChanger.INSTANCE.blue.getValue(), CrystalChanger.INSTANCE.alpha.getValue(), true));
                if (CrystalChanger.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(visibleColor.getRed() / 255.0f, visibleColor.getGreen() / 255.0f, visibleColor.getBlue() / 255.0f, CrystalChanger.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChanger.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            }
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
            if (CrystalChanger.INSTANCE.glint.getValue()) {
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GlStateManager.enableAlpha();
                GlStateManager.color(1.0f, 0.0f, 0.0f, 0.13f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.disableAlpha();
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            }
        }
        else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (CrystalChanger.INSTANCE.isEnabled()) {
            if (CrystalChanger.INSTANCE.animateScale.getValue() && CrystalChanger.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale(1.0f / CrystalChanger.INSTANCE.scaleMap.get(entity), 1.0f / CrystalChanger.INSTANCE.scaleMap.get(entity), 1.0f / CrystalChanger.INSTANCE.scaleMap.get(entity));
            }
            else {
                GlStateManager.scale(1.0f / CrystalChanger.INSTANCE.scale.getValue(), 1.0f / CrystalChanger.INSTANCE.scale.getValue(), 1.0f / CrystalChanger.INSTANCE.scale.getValue());
            }
        }
    }
    
    static {
        MixinRenderEnderCrystal.glint = new ResourceLocation("textures/glint.png");
    }
}
