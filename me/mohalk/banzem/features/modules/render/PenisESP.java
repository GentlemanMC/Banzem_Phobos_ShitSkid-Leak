/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 *  org.lwjgl.util.glu.Sphere
 */
package me.mohalk.banzem.features.modules.render;

import me.mohalk.banzem.event.events.Render3DEvent;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.features.setting.Setting;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

public class PenisESP
extends Module {
    private final Setting<Float> penisSize = this.register(new Setting<Float>("PenisSize", Float.valueOf(1.5f), Float.valueOf(0.1f), Float.valueOf(5.0f)));

    public PenisESP() {
        super("PenisESP", "caution:ur pp is small", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (Object o : PenisESP.mc.field_71441_e.field_72996_f) {
            if (!(o instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer)o;
            double n = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)PenisESP.mc.field_71428_T.field_194147_b;
            mc.func_175598_ae();
            double x = n - PenisESP.mc.func_175598_ae().field_78725_b;
            double n2 = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)PenisESP.mc.field_71428_T.field_194147_b;
            mc.func_175598_ae();
            double y = n2 - PenisESP.mc.func_175598_ae().field_78726_c;
            double n3 = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)PenisESP.mc.field_71428_T.field_194147_b;
            mc.func_175598_ae();
            double z = n3 - PenisESP.mc.func_175598_ae().field_78723_d;
            GL11.glPushMatrix();
            RenderHelper.func_74518_a();
            this.esp(player, x, y, z);
            RenderHelper.func_74519_b();
            GL11.glPopMatrix();
        }
    }

    public void esp(EntityPlayer player, double x, double y, double z) {
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)true);
        GL11.glLineWidth((float)1.0f);
        GL11.glTranslated((double)x, (double)y, (double)z);
        GL11.glRotatef((float)(-player.field_70177_z), (float)0.0f, (float)player.field_70131_O, (float)0.0f);
        GL11.glTranslated((double)(-x), (double)(-y), (double)(-z));
        GL11.glTranslated((double)x, (double)(y + (double)(player.field_70131_O / 2.0f) - (double)0.225f), (double)z);
        GL11.glColor4f((float)1.38f, (float)0.55f, (float)2.38f, (float)1.0f);
        GL11.glRotated((double)(player.func_70093_af() ? 35 : 0), (double)1.0, (double)0.0, (double)0.0);
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.075f);
        Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f * this.penisSize.getValue().floatValue(), 0.11f, 0.4f, 25, 20);
        GL11.glTranslated((double)0.0, (double)0.0, (double)-0.12500000298023223);
        GL11.glTranslated((double)-0.09000000074505805, (double)0.0, (double)0.0);
        Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f * this.penisSize.getValue().floatValue(), 10, 20);
        GL11.glTranslated((double)0.16000000149011612, (double)0.0, (double)0.0);
        Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f * this.penisSize.getValue().floatValue(), 10, 20);
        GL11.glColor4f((float)1.35f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glTranslated((double)-0.07000000074505806, (double)0.0, (double)0.589999952316284);
        Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f * this.penisSize.getValue().floatValue(), 15, 20);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
    }
}

