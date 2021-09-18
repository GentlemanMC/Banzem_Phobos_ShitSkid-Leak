//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.render;

import java.util.function.Consumer;
import me.mohalk.banzem.event.events.Render3DEvent;
import java.awt.Color;
import net.minecraft.init.Blocks;
import me.mohalk.banzem.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import me.mohalk.banzem.util.EntityUtil;
import me.mohalk.banzem.Banzem;
import java.util.HashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class BurrowESP extends Module
{
    private final Setting<Integer> boxRed;
    private final Setting<Integer> outlineGreen;
    private final Setting<Integer> boxGreen;
    private final Setting<Boolean> box;
    private final Setting<Boolean> cOutline;
    private final Setting<Integer> outlineBlue;
    private final Setting<Boolean> name;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> outlineWidth;
    private final Setting<Integer> outlineRed;
    private final Setting<Boolean> outline;
    private final Setting<Integer> boxBlue;
    private final Map<EntityPlayer, BlockPos> burrowedPlayers;
    private final Setting<Integer> outlineAlpha;
    
    public BurrowESP() {
        super("BurrowESP", "Shows burrowed people.", Category.RENDER, true, false, false);
        this.name = (Setting<Boolean>)this.register(new Setting("Name", false));
        this.box = new Setting<Boolean>("Box", true);
        this.boxRed = (Setting<Integer>)this.register(new Setting("BoxRed", 255, 0, 255, v -> this.box.getValue()));
        this.boxGreen = (Setting<Integer>)this.register(new Setting("BoxGreen", 255, 0, 255, v -> this.box.getValue()));
        this.boxBlue = (Setting<Integer>)this.register(new Setting("BoxBlue", 255, 0, 255, v -> this.box.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 125, 0, 255, v -> this.box.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.outlineWidth = (Setting<Float>)this.register(new Setting("OutlineWidth", 1.0f, 0.0f, 5.0f, v -> this.outline.getValue()));
        this.cOutline = (Setting<Boolean>)this.register(new Setting("CustomOutline", false, v -> this.outline.getValue()));
        this.outlineRed = (Setting<Integer>)this.register(new Setting("OutlineRed", 255, 0, 255, v -> this.cOutline.getValue()));
        this.outlineGreen = (Setting<Integer>)this.register(new Setting("OutlineGreen", 255, 0, 255, v -> this.cOutline.getValue()));
        this.outlineBlue = (Setting<Integer>)this.register(new Setting("OutlineBlue", 255, 0, 255, v -> this.cOutline.getValue()));
        this.outlineAlpha = (Setting<Integer>)this.register(new Setting("OutlineAlpha", 255, 0, 255, v -> this.cOutline.getValue()));
        this.burrowedPlayers = new HashMap<EntityPlayer, BlockPos>();
    }
    
    private void getPlayers() {
        for (final EntityPlayer entityPlayer : BurrowESP.mc.world.playerEntities) {
            if (entityPlayer != BurrowESP.mc.player && !Banzem.friendManager.isFriend(entityPlayer.getName()) && EntityUtil.isLiving((Entity)entityPlayer)) {
                if (!this.isBurrowed(entityPlayer)) {
                    continue;
                }
                this.burrowedPlayers.put(entityPlayer, new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.burrowedPlayers.clear();
    }
    
    private void lambda$onRender3D$8(final Map.Entry entry) {
        this.renderBurrowedBlock((BlockPos) entry.getValue());
        if (this.name.getValue()) {
            RenderUtil.drawText(new AxisAlignedBB((BlockPos)entry.getValue()), ((EntityPlayer)entry.getKey()).getGameProfile().getName());
        }
    }
    
    private boolean isBurrowed(final EntityPlayer entityPlayer) {
        final BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 0.2), Math.floor(entityPlayer.posZ));
        return BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST || BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.ANVIL;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        this.burrowedPlayers.clear();
        this.getPlayers();
    }
    
    private void renderBurrowedBlock(final BlockPos blockPos) {
        RenderUtil.drawBoxESP(blockPos, new Color(this.boxRed.getValue(), this.boxGreen.getValue(), this.boxBlue.getValue(), this.boxAlpha.getValue()), true, new Color(this.outlineRed.getValue(), this.outlineGreen.getValue(), this.outlineBlue.getValue(), this.outlineAlpha.getValue()), this.outlineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
    }
    
    @Override
    public void onRender3D(final Render3DEvent render3DEvent) {
        if (!this.burrowedPlayers.isEmpty()) {
            this.burrowedPlayers.entrySet().forEach(this::lambda$onRender3D$8);
        }
    }
}
