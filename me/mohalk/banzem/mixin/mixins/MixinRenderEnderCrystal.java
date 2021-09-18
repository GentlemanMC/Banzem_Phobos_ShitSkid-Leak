/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderEnderCrystal
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.mohalk.banzem.mixin.mixins;

import java.awt.Color;
import me.mohalk.banzem.event.events.RenderEntityModelEvent;
import me.mohalk.banzem.features.modules.client.Colors;
import me.mohalk.banzem.features.modules.render.CrystalChanger;
import me.mohalk.banzem.util.EntityUtil;
import me.mohalk.banzem.util.RenderUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {
    @Shadow
    @Final
    private static ResourceLocation field_110787_a;
    private static ResourceLocation glint;

    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (CrystalChanger.INSTANCE.isEnabled()) {
            if (CrystalChanger.INSTANCE.animateScale.getValue().booleanValue() && CrystalChanger.INSTANCE.scaleMap.containsKey((Object)((EntityEnderCrystal)entity))) {
                GlStateManager.func_179152_a((float)CrystalChanger.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue(), (float)CrystalChanger.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue(), (float)CrystalChanger.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue());
            } else {
                GlStateManager.func_179152_a((float)CrystalChanger.INSTANCE.scale.getValue().floatValue(), (float)CrystalChanger.INSTANCE.scale.getValue().floatValue(), (float)CrystalChanger.INSTANCE.scale.getValue().floatValue());
            }
        }
        if (CrystalChanger.INSTANCE.isEnabled() && CrystalChanger.INSTANCE.wireframe.getValue().booleanValue()) {
            RenderEntityModelEvent event = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            CrystalChanger.INSTANCE.onRenderModel(event);
        }
        if (CrystalChanger.INSTANCE.isEnabled() && CrystalChanger.INSTANCE.chams.getValue().booleanValue()) {
            Color visibleColor;
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.5f);
            GL11.glEnable((int)2960);
            if (CrystalChanger.INSTANCE.rainbow.getValue().booleanValue()) {
                Color rainbowColor1 = CrystalChanger.INSTANCE.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(CrystalChanger.INSTANCE.speed.getValue() * 100, 0, (float)CrystalChanger.INSTANCE.saturation.getValue().intValue() / 100.0f, (float)CrystalChanger.INSTANCE.brightness.getValue().intValue() / 100.0f));
                Color rainbowColor = EntityUtil.getColor(entity, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), CrystalChanger.INSTANCE.alpha.getValue(), true);
                if (CrystalChanger.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)rainbowColor.getRed() / 255.0f), (float)((float)rainbowColor.getGreen() / 255.0f), (float)((float)rainbowColor.getBlue() / 255.0f), (float)((float)CrystalChanger.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChanger.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
            } else if (CrystalChanger.INSTANCE.xqz.getValue().booleanValue() && CrystalChanger.INSTANCE.throughWalls.getValue().booleanValue()) {
                Color hiddenColor = CrystalChanger.INSTANCE.colorSync.getValue() != false ? EntityUtil.getColor(entity, CrystalChanger.INSTANCE.hiddenRed.getValue(), CrystalChanger.INSTANCE.hiddenGreen.getValue(), CrystalChanger.INSTANCE.hiddenBlue.getValue(), CrystalChanger.INSTANCE.hiddenAlpha.getValue(), true) : EntityUtil.getColor(entity, CrystalChanger.INSTANCE.hiddenRed.getValue(), CrystalChanger.INSTANCE.hiddenGreen.getValue(), CrystalChanger.INSTANCE.hiddenBlue.getValue(), CrystalChanger.INSTANCE.hiddenAlpha.getValue(), true);
                visibleColor = CrystalChanger.INSTANCE.colorSync.getValue() != false ? EntityUtil.getColor(entity, CrystalChanger.INSTANCE.red.getValue(), CrystalChanger.INSTANCE.green.getValue(), CrystalChanger.INSTANCE.blue.getValue(), CrystalChanger.INSTANCE.alpha.getValue(), true) : EntityUtil.getColor(entity, CrystalChanger.INSTANCE.red.getValue(), CrystalChanger.INSTANCE.green.getValue(), CrystalChanger.INSTANCE.blue.getValue(), CrystalChanger.INSTANCE.alpha.getValue(), true);
                Color color = visibleColor;
                if (CrystalChanger.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)hiddenColor.getRed() / 255.0f), (float)((float)hiddenColor.getGreen() / 255.0f), (float)((float)hiddenColor.getBlue() / 255.0f), (float)((float)CrystalChanger.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChanger.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
                GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)CrystalChanger.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
                visibleColor = CrystalChanger.INSTANCE.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entity, CrystalChanger.INSTANCE.red.getValue(), CrystalChanger.INSTANCE.green.getValue(), CrystalChanger.INSTANCE.blue.getValue(), CrystalChanger.INSTANCE.alpha.getValue(), true);
                Color color = visibleColor;
                if (CrystalChanger.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)CrystalChanger.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChanger.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
            }
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
            if (CrystalChanger.INSTANCE.glint.getValue().booleanValue()) {
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                GlStateManager.func_179141_d();
                GlStateManager.func_179131_c((float)1.0f, (float)0.0f, (float)0.0f, (float)0.13f);
                model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.func_179118_c();
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
            }
        } else {
            model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (CrystalChanger.INSTANCE.isEnabled()) {
            if (CrystalChanger.INSTANCE.animateScale.getValue().booleanValue() && CrystalChanger.INSTANCE.scaleMap.containsKey((Object)((EntityEnderCrystal)entity))) {
                GlStateManager.func_179152_a((float)(1.0f / CrystalChanger.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue()), (float)(1.0f / CrystalChanger.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue()), (float)(1.0f / CrystalChanger.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue()));
            } else {
                GlStateManager.func_179152_a((float)(1.0f / CrystalChanger.INSTANCE.scale.getValue().floatValue()), (float)(1.0f / CrystalChanger.INSTANCE.scale.getValue().floatValue()), (float)(1.0f / CrystalChanger.INSTANCE.scale.getValue().floatValue()));
            }
        }
    }

    static {
        glint = new ResourceLocation("textures/glint.png");
    }
}

