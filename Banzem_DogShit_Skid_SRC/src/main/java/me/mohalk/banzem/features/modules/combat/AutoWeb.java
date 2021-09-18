//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.combat;

import net.minecraft.util.EnumHand;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.command.Command;
import net.minecraft.block.BlockWeb;
import java.util.Iterator;
import me.mohalk.banzem.util.MathUtil;
import me.mohalk.banzem.util.BlockUtil;
import java.util.Comparator;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.mohalk.banzem.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.mohalk.banzem.features.modules.client.ClickGui;
import net.minecraft.entity.Entity;
import me.mohalk.banzem.util.EntityUtil;
import me.mohalk.banzem.features.modules.client.ServerModule;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.util.InventoryUtil;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class AutoWeb extends Module
{
    public static boolean isPlacing;
    private final Setting<Boolean> server;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerPlace;
    private final Setting<Double> targetRange;
    private final Setting<Double> range;
    private final Setting<TargetMode> targetMode;
    private final Setting<InventoryUtil.Switch> switchMode;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> raytrace;
    private final Setting<Double> speed;
    private final Setting<Boolean> upperBody;
    private final Setting<Boolean> lowerbody;
    private final Setting<Boolean> ylower;
    private final Setting<Boolean> antiSelf;
    private final Setting<Integer> eventMode;
    private final Setting<Boolean> freecam;
    private final Setting<Boolean> info;
    private final Setting<Boolean> disable;
    private final Setting<Boolean> packet;
    private final Timer timer;
    public EntityPlayer target;
    private boolean didPlace;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements;
    private boolean smartRotate;
    private BlockPos startPos;
    
    public AutoWeb() {
        super("AutoWeb", "Traps other players in webs", Category.COMBAT, true, false, false);
        this.server = (Setting<Boolean>)this.register(new Setting("Server", false));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay/Place", 50, 0, 250));
        this.blocksPerPlace = (Setting<Integer>)this.register(new Setting("Block/Place", 8, 1, 30));
        this.targetRange = (Setting<Double>)this.register(new Setting("TargetRange", 10.0, 0.0, 20.0));
        this.range = (Setting<Double>)this.register(new Setting("PlaceRange", 6.0, 0.0, 10.0));
        this.targetMode = (Setting<TargetMode>)this.register(new Setting("Target", TargetMode.CLOSEST));
        this.switchMode = (Setting<InventoryUtil.Switch>)this.register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.raytrace = (Setting<Boolean>)this.register(new Setting("Raytrace", false));
        this.speed = (Setting<Double>)this.register(new Setting("Speed", 30.0, 0.0, 30.0));
        this.upperBody = (Setting<Boolean>)this.register(new Setting("Upper", false));
        this.lowerbody = (Setting<Boolean>)this.register(new Setting("Lower", true));
        this.ylower = (Setting<Boolean>)this.register(new Setting("Y-1", false));
        this.antiSelf = (Setting<Boolean>)this.register(new Setting("AntiSelf", false));
        this.eventMode = (Setting<Integer>)this.register(new Setting("Updates", 3, 1, 3));
        this.freecam = (Setting<Boolean>)this.register(new Setting("Freecam", false));
        this.info = (Setting<Boolean>)this.register(new Setting("Info", false));
        this.disable = (Setting<Boolean>)this.register(new Setting("TSelfMove", false));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", false));
        this.timer = new Timer();
        this.didPlace = false;
        this.placements = 0;
        this.smartRotate = false;
        this.startPos = null;
    }
    
    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue();
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            return;
        }
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.player);
        this.lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
        if (this.shouldServer()) {
            AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module AutoWeb set Enabled true"));
        }
    }
    
    @Override
    public void onTick() {
        if (this.eventMode.getValue() == 3) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.eventMode.getValue() == 2) {
            this.smartRotate = (this.rotate.getValue() && this.blocksPerPlace.getValue() == 1);
            this.doTrap();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.eventMode.getValue() == 1) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.info.getValue() && this.target != null) {
            return this.target.getName();
        }
        return null;
    }
    
    @Override
    public void onDisable() {
        if (this.shouldServer()) {
            AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module AutoWeb set Enabled false"));
            return;
        }
        AutoWeb.isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.switchItem(true);
    }
    
    private void doTrap() {
        if (this.shouldServer() || this.check()) {
            return;
        }
        this.doWebTrap();
        if (this.didPlace) {
            this.timer.reset();
        }
    }
    
    private void doWebTrap() {
        final List<Vec3d> placeTargets = this.getPlacements();
        this.placeList(placeTargets);
    }
    
    private List<Vec3d> getPlacements() {
        final ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        final Vec3d baseVec = this.target.getPositionVector();
        if (this.ylower.getValue()) {
            list.add(baseVec.add(0.0, -1.0, 0.0));
        }
        if (this.lowerbody.getValue()) {
            list.add(baseVec);
        }
        if (this.upperBody.getValue()) {
            list.add(baseVec.add(0.0, 1.0, 0.0));
        }
        return list;
    }
    
    private void placeList(final List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoWeb.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), AutoWeb.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (final Vec3d vec3d3 : list) {
            final BlockPos position = new BlockPos(vec3d3);
            final int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue());
            if (placeability == 3 || placeability == 1) {
                if (this.antiSelf.getValue() && MathUtil.areVec3dsAligned(AutoWeb.mc.player.getPositionVector(), vec3d3)) {
                    continue;
                }
                this.placeBlock(position);
            }
        }
    }
    
    private boolean check() {
        AutoWeb.isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        if (this.isOff()) {
            return true;
        }
        if (this.disable.getValue() && !this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.player))) {
            this.disable();
            return true;
        }
        if (obbySlot == -1) {
            if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
                if (this.info.getValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> §cYou are out of Webs.");
                }
                this.disable();
            }
            return true;
        }
        if (AutoWeb.mc.player.inventory.currentItem != this.lastHotbarSlot && AutoWeb.mc.player.inventory.currentItem != obbySlot) {
            this.lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.target = this.getTarget(this.targetRange.getValue(), this.targetMode.getValue() == TargetMode.UNTRAPPED);
        return this.target == null || (Banzem.moduleManager.isModuleEnabled("Freecam") && !this.freecam.getValue()) || !this.timer.passedMs(this.delay.getValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && AutoWeb.mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock(BlockWeb.class));
    }
    
    private EntityPlayer getTarget(final double range, final boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : AutoWeb.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range) && (!trapped || !player.isInWeb) && (!EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.player).equals((Object)EntityUtil.getRoundedBlockPos((Entity)player)) || !this.antiSelf.getValue())) {
                if (Banzem.speedManager.getPlayerSpeed(player) > this.speed.getValue()) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = AutoWeb.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (AutoWeb.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = AutoWeb.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && AutoWeb.mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue()) && this.switchItem(false)) {
            AutoWeb.isPlacing = true;
            this.isSneaking = (this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking));
            this.didPlace = true;
            ++this.placements;
        }
    }
    
    private boolean switchItem(final boolean back) {
        final boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), BlockWeb.class);
        this.switchedItem = value[0];
        return value[1];
    }
    
    static {
        AutoWeb.isPlacing = false;
    }
    
    public enum TargetMode
    {
        CLOSEST, 
        UNTRAPPED;
    }
}
