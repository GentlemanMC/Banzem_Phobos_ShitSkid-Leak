//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.player;

import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import me.mohalk.banzem.util.BlockUtil;
import net.minecraft.block.BlockEndPortalFrame;
import me.mohalk.banzem.event.events.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.network.play.client.CPacketAnimation;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.util.RenderUtil;
import java.awt.Color;
import me.mohalk.banzem.event.events.Render3DEvent;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import me.mohalk.banzem.util.InventoryUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.util.MathUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class Speedmine extends Module
{
    private static Speedmine INSTANCE;
    private final Setting<Float> range;
    private final Timer timer;
    public Setting<Boolean> tweaks;
    public Setting<Mode> mode;
    public Setting<Boolean> reset;
    public Setting<Float> damage;
    public Setting<Boolean> noBreakAnim;
    public Setting<Boolean> noDelay;
    public Setting<Boolean> noSwing;
    public Setting<Boolean> noTrace;
    public Setting<Boolean> noGapTrace;
    public Setting<Boolean> allow;
    public Setting<Boolean> pickaxe;
    public Setting<Boolean> doubleBreak;
    public Setting<Boolean> webSwitch;
    public Setting<Boolean> silentSwitch;
    public Setting<Boolean> render;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    private final Setting<Float> lineWidth;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;
    
    public Speedmine() {
        super("Speedmine", "Speeds up mining.", Category.PLAYER, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", 10.0f, 0.0f, 50.0f));
        this.timer = new Timer();
        this.tweaks = (Setting<Boolean>)this.register(new Setting("Tweaks", true));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.PACKET, v -> this.tweaks.getValue()));
        this.reset = (Setting<Boolean>)this.register(new Setting("Reset", true));
        this.damage = (Setting<Float>)this.register(new Setting("Damage", 0.7f, 0.0f, 1.0f, v -> this.mode.getValue() == Mode.DAMAGE && this.tweaks.getValue()));
        this.noBreakAnim = (Setting<Boolean>)this.register(new Setting("NoBreakAnim", false));
        this.noDelay = (Setting<Boolean>)this.register(new Setting("NoDelay", false));
        this.noSwing = (Setting<Boolean>)this.register(new Setting("NoSwing", false));
        this.noTrace = (Setting<Boolean>)this.register(new Setting("NoTrace", false));
        this.noGapTrace = (Setting<Boolean>)this.register(new Setting("NoGapTrace", false, v -> this.noTrace.getValue()));
        this.allow = (Setting<Boolean>)this.register(new Setting("AllowMultiTask", false));
        this.pickaxe = (Setting<Boolean>)this.register(new Setting("Pickaxe", true, v -> this.noTrace.getValue()));
        this.doubleBreak = (Setting<Boolean>)this.register(new Setting("DoubleBreak", false));
        this.webSwitch = (Setting<Boolean>)this.register(new Setting("WebSwitch", false));
        this.silentSwitch = (Setting<Boolean>)this.register(new Setting("SilentSwitch", false));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", false));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", false, v -> this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 85, 0, 255, v -> this.box.getValue() && this.render.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true, v -> this.render.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f, v -> this.outline.getValue() && this.render.getValue()));
        this.isMining = false;
        this.lastPos = null;
        this.lastFacing = null;
        this.setInstance();
    }
    
    public static Speedmine getInstance() {
        if (Speedmine.INSTANCE == null) {
            Speedmine.INSTANCE = new Speedmine();
        }
        return Speedmine.INSTANCE;
    }
    
    private void setInstance() {
        Speedmine.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (this.currentPos != null) {
            if (Speedmine.mc.player != null && Speedmine.mc.player.getDistanceSq(this.currentPos) > MathUtil.square(this.range.getValue())) {
                this.currentPos = null;
                this.currentBlockState = null;
                return;
            }
            if (Speedmine.mc.player != null && this.silentSwitch.getValue() && this.timer.passedMs((int)(2000.0f * Banzem.serverManager.getTpsFactor())) && this.getPickSlot() != -1) {
                Speedmine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.getPickSlot()));
            }
            if (!Speedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) || Speedmine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.currentBlockState = null;
            }
            else if (this.webSwitch.getValue() && this.currentBlockState.getBlock() == Blocks.WEB && Speedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (this.noDelay.getValue()) {
            Speedmine.mc.playerController.blockHitDelay = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue()) {
            Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue() && Speedmine.mc.gameSettings.keyBindUseItem.isKeyDown() && !this.allow.getValue()) {
            Speedmine.mc.playerController.isHittingBlock = false;
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.render.getValue() && this.currentPos != null) {
            final Color color = new Color(this.timer.passedMs((int)(2000.0f * Banzem.serverManager.getTpsFactor())) ? 0 : 255, this.timer.passedMs((int)(2000.0f * Banzem.serverManager.getTpsFactor())) ? 255 : 0, 0, 255);
            RenderUtil.drawBoxESP(this.currentPos, color, false, color, this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            if (this.noSwing.getValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCanceled(true);
            }
            final CPacketPlayerDigging packet;
            if (this.noBreakAnim.getValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = event.getPacket()) != null && packet.getPosition() != null) {
                try {
                    for (final Entity entity : Speedmine.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(packet.getPosition()))) {
                        if (!(entity instanceof EntityEnderCrystal)) {
                            continue;
                        }
                        this.showAnimation();
                        return;
                    }
                }
                catch (Exception ex) {}
                if (packet.getAction().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.showAnimation(true, packet.getPosition(), packet.getFacing());
                }
                if (packet.getAction().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.showAnimation();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockEvent(final BlockEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && Speedmine.mc.world.getBlockState(event.pos).getBlock() instanceof BlockEndPortalFrame) {
            Speedmine.mc.world.getBlockState(event.pos).getBlock().setHardness(50.0f);
        }
        if (event.getStage() == 3 && this.reset.getValue() && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            Speedmine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4 && this.tweaks.getValue()) {
            if (BlockUtil.canBreak(event.pos)) {
                if (this.reset.getValue()) {
                    Speedmine.mc.playerController.isHittingBlock = false;
                }
                switch (this.mode.getValue()) {
                    case PACKET: {
                        if (this.currentPos == null) {
                            this.currentPos = event.pos;
                            this.currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                            this.timer.reset();
                        }
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;
                    }
                    case DAMAGE: {
                        if (Speedmine.mc.playerController.curBlockDamageMP < this.damage.getValue()) {
                            break;
                        }
                        Speedmine.mc.playerController.curBlockDamageMP = 1.0f;
                        break;
                    }
                    case INSTANT: {
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.playerController.onPlayerDestroyBlock(event.pos);
                        Speedmine.mc.world.setBlockToAir(event.pos);
                        break;
                    }
                }
            }
            final BlockPos above;
            if (this.doubleBreak.getValue() && BlockUtil.canBreak(above = event.pos.add(0, 1, 0)) && Speedmine.mc.player.getDistance((double)above.getX(), (double)above.getY(), (double)above.getZ()) <= 5.0) {
                Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.playerController.onPlayerDestroyBlock(above);
                Speedmine.mc.world.setBlockToAir(above);
            }
        }
    }
    
    private int getPickSlot() {
        for (int i = 0; i < 9; ++i) {
            if (Speedmine.mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                return i;
            }
        }
        return -1;
    }
    
    private void showAnimation(final boolean isMining, final BlockPos lastPos, final EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }
    
    public void showAnimation() {
        this.showAnimation(false, null, null);
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        Speedmine.INSTANCE = new Speedmine();
    }
    
    public enum Mode
    {
        PACKET, 
        DAMAGE, 
        INSTANT;
    }
}
