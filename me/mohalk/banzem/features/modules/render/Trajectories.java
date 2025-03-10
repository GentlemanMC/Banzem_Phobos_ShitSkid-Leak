/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemEgg
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.item.ItemFishingRod
 *  net.minecraft.item.ItemSnowball
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package me.mohalk.banzem.features.modules.render;

import java.util.ArrayList;
import java.util.List;
import me.mohalk.banzem.event.events.Render3DEvent;
import me.mohalk.banzem.features.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

public class Trajectories
extends Module {
    public Trajectories() {
        super("Trajectories", "Shows the way of projectiles.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Trajectories.mc.field_71441_e == null || Trajectories.mc.field_71439_g == null) {
            return;
        }
        this.drawTrajectories((EntityPlayer)Trajectories.mc.field_71439_g, event.getPartialTicks());
    }

    public void enableGL3D(float lineWidth) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        Trajectories.mc.field_71460_t.func_175072_h();
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glLineWidth((float)lineWidth);
    }

    public void disableGL3D() {
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    private void drawTrajectories(EntityPlayer player, float partialTicks) {
        float pow;
        double renderPosX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)partialTicks;
        double renderPosY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)partialTicks;
        double renderPosZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)partialTicks;
        player.func_184586_b(EnumHand.MAIN_HAND);
        if (!(Trajectories.mc.field_71474_y.field_74320_O == 0 && (player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemBow || player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemFishingRod || player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemEnderPearl || player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemEgg || player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemSnowball || player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemExpBottle))) {
            return;
        }
        GL11.glPushMatrix();
        Item item = player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b();
        double posX = renderPosX - (double)(MathHelper.func_76134_b((float)(player.field_70177_z / 180.0f * (float)Math.PI)) * 0.16f);
        double posY = renderPosY + (double)player.func_70047_e() - 0.1000000014901161;
        double posZ = renderPosZ - (double)(MathHelper.func_76126_a((float)(player.field_70177_z / 180.0f * (float)Math.PI)) * 0.16f);
        double motionX = (double)(-MathHelper.func_76126_a((float)(player.field_70177_z / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(player.field_70125_A / 180.0f * (float)Math.PI))) * (item instanceof ItemBow ? 1.0 : 0.4);
        double motionY = (double)(-MathHelper.func_76126_a((float)(player.field_70125_A / 180.0f * (float)Math.PI))) * (item instanceof ItemBow ? 1.0 : 0.4);
        double motionZ = (double)(MathHelper.func_76134_b((float)(player.field_70177_z / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(player.field_70125_A / 180.0f * (float)Math.PI))) * (item instanceof ItemBow ? 1.0 : 0.4);
        int var6 = 72000 - player.func_184605_cv();
        float power = (float)var6 / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }
        float distance = MathHelper.func_76133_a((double)(motionX * motionX + motionY * motionY + motionZ * motionZ));
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        float f = item instanceof ItemBow ? power * 2.0f : (item instanceof ItemFishingRod ? 1.25f : (pow = player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by ? 0.9f : 1.0f));
        motionX *= (double)(pow * (item instanceof ItemFishingRod ? 0.75f : (player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by ? 0.75f : 1.5f)));
        motionY *= (double)(pow * (item instanceof ItemFishingRod ? 0.75f : (player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by ? 0.75f : 1.5f)));
        motionZ *= (double)(pow * (item instanceof ItemFishingRod ? 0.75f : (player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by ? 0.75f : 1.5f)));
        this.enableGL3D(2.0f);
        if (power > 0.6f) {
            GlStateManager.func_179131_c((float)0.0f, (float)1.0f, (float)0.0f, (float)1.0f);
        } else {
            GlStateManager.func_179131_c((float)0.8f, (float)0.5f, (float)0.0f, (float)1.0f);
        }
        GL11.glEnable((int)2848);
        float size = (float)(item instanceof ItemBow ? 0.3 : 0.25);
        boolean hasLanded = false;
        Entity landingOnEntity = null;
        RayTraceResult landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            Vec3d present = new Vec3d(posX, posY, posZ);
            Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            RayTraceResult possibleLandingStrip = Trajectories.mc.field_71441_e.func_147447_a(present, future, false, true, false);
            if (possibleLandingStrip != null && possibleLandingStrip.field_72313_a != RayTraceResult.Type.MISS) {
                landingPosition = possibleLandingStrip;
                hasLanded = true;
            }
            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - (double)size, posY - (double)size, posZ - (double)size, posX + (double)size, posY + (double)size, posZ + (double)size);
            List entities = this.getEntitiesWithinAABB(arrowBox.func_72317_d(motionX, motionY, motionZ).func_72321_a(1.0, 1.0, 1.0));
            for (Object entity : entities) {
                Entity boundingBox = (Entity)entity;
                if (!boundingBox.func_70067_L() || boundingBox == player) continue;
                float var7 = 0.3f;
                AxisAlignedBB var8 = boundingBox.func_174813_aQ().func_72321_a((double)var7, (double)var7, (double)var7);
                RayTraceResult possibleEntityLanding = var8.func_72327_a(present, future);
                if (possibleEntityLanding == null) continue;
                hasLanded = true;
                landingOnEntity = boundingBox;
                landingPosition = possibleEntityLanding;
            }
            if (landingOnEntity != null) {
                GlStateManager.func_179131_c((float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float motionAdjustment = 0.99f;
            motionX *= (double)motionAdjustment;
            motionY *= (double)motionAdjustment;
            motionZ *= (double)motionAdjustment;
            motionY -= item instanceof ItemBow ? 0.05 : 0.03;
        }
        if (landingPosition != null && landingPosition.field_72313_a == RayTraceResult.Type.BLOCK) {
            GlStateManager.func_179137_b((double)(posX - renderPosX), (double)(posY - renderPosY), (double)(posZ - renderPosZ));
            int side = landingPosition.field_178784_b.func_176745_a();
            if (side == 2) {
                GlStateManager.func_179114_b((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            } else if (side == 3) {
                GlStateManager.func_179114_b((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            } else if (side == 4) {
                GlStateManager.func_179114_b((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            } else if (side == 5) {
                GlStateManager.func_179114_b((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            Cylinder c = new Cylinder();
            GlStateManager.func_179114_b((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            c.setDrawStyle(100011);
            if (landingOnEntity != null) {
                GlStateManager.func_179131_c((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glLineWidth((float)2.5f);
                c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                GL11.glLineWidth((float)0.1f);
                GlStateManager.func_179131_c((float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            c.draw(0.6f, 0.3f, 0.0f, 4, 1);
        }
        this.disableGL3D();
        GL11.glPopMatrix();
    }

    private List getEntitiesWithinAABB(AxisAlignedBB bb) {
        ArrayList list = new ArrayList();
        int chunkMinX = MathHelper.func_76128_c((double)((bb.field_72340_a - 2.0) / 16.0));
        int chunkMaxX = MathHelper.func_76128_c((double)((bb.field_72336_d + 2.0) / 16.0));
        int chunkMinZ = MathHelper.func_76128_c((double)((bb.field_72339_c - 2.0) / 16.0));
        int chunkMaxZ = MathHelper.func_76128_c((double)((bb.field_72334_f + 2.0) / 16.0));
        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (Trajectories.mc.field_71441_e.func_72863_F().func_186026_b(x, z) == null) continue;
                Trajectories.mc.field_71441_e.func_72964_e(x, z).func_177414_a((Entity)Trajectories.mc.field_71439_g, bb, list, null);
            }
        }
        return list;
    }
}

