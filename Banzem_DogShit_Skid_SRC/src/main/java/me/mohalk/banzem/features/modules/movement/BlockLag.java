//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.movement;

import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import me.mohalk.banzem.features.command.Command;
import me.mohalk.banzem.util.BlockLagUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class BlockLag extends Module
{
    private final Setting<Integer> offset;
    private final Setting<Boolean> rotate;
    private final Setting<Mode> mode;
    private BlockPos originalPos;
    private int oldSlot;
    Block returnBlock;
    
    public BlockLag() {
        super("BlockLag", "TPs you into a block", Category.MOVEMENT, true, false, false);
        this.offset = (Setting<Integer>)this.register(new Setting("Offset", 3, (-10), 10));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", false));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.OBBY));
        this.oldSlot = -1;
        this.returnBlock = null;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.originalPos = new BlockPos(BlockLag.mc.player.posX, BlockLag.mc.player.posY, BlockLag.mc.player.posZ);
        switch (this.mode.getValue()) {
            case OBBY: {
                this.returnBlock = Blocks.OBSIDIAN;
                break;
            }
            case ECHEST: {
                this.returnBlock = Blocks.ENDER_CHEST;
                break;
            }
            case EABypass: {
                this.returnBlock = (Block)Blocks.CHEST;
                break;
            }
        }
        if (BlockLag.mc.world.getBlockState(new BlockPos(BlockLag.mc.player.posX, BlockLag.mc.player.posY, BlockLag.mc.player.posZ)).getBlock().equals(this.returnBlock) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = BlockLag.mc.player.inventory.currentItem;
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case OBBY: {
                if (BlockLagUtil.findHotbarBlock(BlockObsidian.class) == -1) {
                    Command.sendMessage("Can't find obby in hotbar!");
                    this.disable();
                    return;
                }
                break;
            }
            case ECHEST: {
                if (BlockLagUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                    Command.sendMessage("Can't find echest in hotbar!");
                    this.disable();
                    return;
                }
                break;
            }
            case EABypass: {
                if (BlockLagUtil.findHotbarBlock(BlockChest.class) == -1) {
                    Command.sendMessage("Can't find chest in hotbar!");
                    this.disable();
                    return;
                }
                break;
            }
        }
        BlockLagUtil.switchToSlot((this.mode.getValue() == Mode.OBBY) ? BlockLagUtil.findHotbarBlock(BlockObsidian.class) : ((this.mode.getValue() == Mode.ECHEST) ? BlockLagUtil.findHotbarBlock(BlockEnderChest.class) : BlockLagUtil.findHotbarBlock(BlockChest.class)));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 0.41999998688698, BlockLag.mc.player.posZ, true));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 0.7531999805211997, BlockLag.mc.player.posZ, true));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 1.00133597911214, BlockLag.mc.player.posZ, true));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 1.16610926093821, BlockLag.mc.player.posZ, true));
        BlockLagUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + this.offset.getValue(), BlockLag.mc.player.posZ, false));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockLag.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        BlockLag.mc.player.setSneaking(false);
        BlockLagUtil.switchToSlot(this.oldSlot);
        this.toggle();
    }
    
    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : BlockLag.mc.world.loadedEntityList) {
            if (entity.equals((Object)BlockLag.mc.player)) {
                continue;
            }
            if (entity instanceof EntityItem) {
                continue;
            }
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    public enum Mode
    {
        OBBY, 
        ECHEST, 
        EABypass;
    }
}
