//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.combat;

import me.mohalk.banzem.features.modules.player.Freecam;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import me.mohalk.banzem.features.Feature;
import net.minecraft.util.EnumHand;
import java.util.Comparator;
import net.minecraft.util.math.Vec3i;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import me.mohalk.banzem.util.BlockUtil;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.mohalk.banzem.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.entity.Entity;
import me.mohalk.banzem.util.EntityUtil;
import me.mohalk.banzem.Banzem;
import java.util.HashMap;
import me.mohalk.banzem.features.setting.Bind;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.util.InventoryUtil;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class Selftrap extends Module
{
    private final Setting<Boolean> smart;
    private final Setting<Double> smartRange;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> disable;
    private final Setting<Integer> disableTime;
    private final Setting<Boolean> offhand;
    private final Setting<InventoryUtil.Switch> switchMode;
    private final Setting<Boolean> onlySafe;
    private final Setting<Boolean> highWeb;
    private final Setting<Boolean> freecam;
    private final Setting<Boolean> packet;
    private final Timer offTimer;
    private final Timer timer;
    private final Map<BlockPos, Integer> retries;
    private final Timer retryTimer;
    public Setting<Mode> mode;
    public Setting<PlaceMode> placeMode;
    public Setting<Bind> obbyBind;
    public Setting<Bind> webBind;
    public Mode currentMode;
    private boolean accessedViaBind;
    private int blocksThisTick;
    private Offhand.Mode offhandMode;
    private Offhand.Mode2 offhandMode2;
    private boolean isSneaking;
    private boolean hasOffhand;
    private boolean placeHighWeb;
    private int lastHotbarSlot;
    private boolean switchedItem;
    
    public Selftrap() {
        super("Selftrap", "Lure your enemies in!", Category.COMBAT, true, false, true);
        this.smart = (Setting<Boolean>)this.register(new Setting("Smart", false));
        this.smartRange = (Setting<Double>)this.register(new Setting("SmartRange", 6.0, 0.0, 10.0));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay/Place", 50, 0, 250));
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("Block/Place", 8, 1, 20));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.disable = (Setting<Boolean>)this.register(new Setting("Disable", true));
        this.disableTime = (Setting<Integer>)this.register(new Setting("Ms/Disable", 200, 1, 250));
        this.offhand = (Setting<Boolean>)this.register(new Setting("OffHand", true));
        this.switchMode = (Setting<InventoryUtil.Switch>)this.register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
        this.onlySafe = (Setting<Boolean>)this.register(new Setting("OnlySafe", true, v -> this.offhand.getValue()));
        this.highWeb = (Setting<Boolean>)this.register(new Setting("HighWeb", false));
        this.freecam = (Setting<Boolean>)this.register(new Setting("Freecam", false));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", false));
        this.offTimer = new Timer();
        this.timer = new Timer();
        this.retries = new HashMap<BlockPos, Integer>();
        this.retryTimer = new Timer();
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.OBSIDIAN));
        this.placeMode = (Setting<PlaceMode>)this.register(new Setting("PlaceMode", PlaceMode.NORMAL, v -> this.mode.getValue() == Mode.OBSIDIAN));
        this.obbyBind = (Setting<Bind>)this.register(new Setting("Obsidian", new Bind(-1)));
        this.webBind = (Setting<Bind>)this.register(new Setting("Webs", new Bind(-1)));
        this.currentMode = Mode.OBSIDIAN;
        this.accessedViaBind = false;
        this.blocksThisTick = 0;
        this.offhandMode = Offhand.Mode.CRYSTALS;
        this.offhandMode2 = Offhand.Mode2.CRYSTALS;
        this.hasOffhand = false;
        this.placeHighWeb = false;
        this.lastHotbarSlot = -1;
        this.switchedItem = false;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
        this.lastHotbarSlot = Selftrap.mc.player.inventory.currentItem;
        if (!this.accessedViaBind) {
            this.currentMode = this.mode.getValue();
        }
        final Offhand module = Banzem.moduleManager.getModuleByClass(Offhand.class);
        this.offhandMode = module.mode;
        this.offhandMode2 = module.currentMode;
        if (this.offhand.getValue() && (EntityUtil.isSafe((Entity)Selftrap.mc.player) || !this.onlySafe.getValue())) {
            if (module.type.getValue() == Offhand.Type.OLD) {
                if (this.currentMode == Mode.WEBS) {
                    module.setMode(Offhand.Mode2.WEBS);
                }
                else {
                    module.setMode(Offhand.Mode2.OBSIDIAN);
                }
            }
            else if (this.currentMode == Mode.WEBS) {
                module.setSwapToTotem(false);
                module.setMode(Offhand.Mode.WEBS);
            }
            else {
                module.setSwapToTotem(false);
                module.setMode(Offhand.Mode.OBSIDIAN);
            }
        }
        Banzem.holeManager.update();
        this.offTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (this.isOn() && (this.blocksPerTick.getValue() != 1 || !this.rotate.getValue())) {
            this.doHoleFill();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.isOn() && event.getStage() == 0 && this.blocksPerTick.getValue() == 1 && this.rotate.getValue()) {
            this.doHoleFill();
        }
    }
    
    @Override
    public void onDisable() {
        if (this.offhand.getValue()) {
            Banzem.moduleManager.getModuleByClass(Offhand.class).setMode(this.offhandMode);
            Banzem.moduleManager.getModuleByClass(Offhand.class).setMode(this.offhandMode2);
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.retries.clear();
        this.accessedViaBind = false;
        this.hasOffhand = false;
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (this.obbyBind.getValue().getKey() == Keyboard.getEventKey()) {
                this.accessedViaBind = true;
                this.currentMode = Mode.OBSIDIAN;
                this.toggle();
            }
            if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                this.accessedViaBind = true;
                this.currentMode = Mode.WEBS;
                this.toggle();
            }
        }
    }
    
    private void doHoleFill() {
        if (this.check()) {
            return;
        }
        if (this.placeHighWeb) {
            final BlockPos pos = new BlockPos(Selftrap.mc.player.posX, Selftrap.mc.player.posY + 1.0, Selftrap.mc.player.posZ);
            this.placeBlock(pos);
            this.placeHighWeb = false;
        }
        for (final BlockPos position : this.getPositions()) {
            if (this.smart.getValue() && !this.isPlayerInRange()) {
                continue;
            }
            final int placeability = BlockUtil.isPositionPlaceable(position, false);
            if (placeability == 1) {
                switch (this.currentMode) {
                    case WEBS: {
                        this.placeBlock(position);
                        break;
                    }
                    case OBSIDIAN: {
                        if (this.switchMode.getValue() != InventoryUtil.Switch.SILENT) {
                            break;
                        }
                        if (this.retries.get(position) != null && this.retries.get(position) >= 4) {
                            break;
                        }
                        this.placeBlock(position);
                        this.retries.put(position, (this.retries.get(position) == null) ? 1 : (this.retries.get(position) + 1));
                        break;
                    }
                }
            }
            if (placeability != 3) {
                continue;
            }
            this.placeBlock(position);
        }
    }
    
    private boolean isPlayerInRange() {
        for (final EntityPlayer player : Selftrap.mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, this.smartRange.getValue())) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    private List<BlockPos> getPositions() {
        final ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        Label_0540: {
            switch (this.currentMode) {
                case WEBS: {
                    positions.add(new BlockPos(Selftrap.mc.player.posX, Selftrap.mc.player.posY, Selftrap.mc.player.posZ));
                    if (!this.highWeb.getValue()) {
                        break;
                    }
                    positions.add(new BlockPos(Selftrap.mc.player.posX, Selftrap.mc.player.posY + 1.0, Selftrap.mc.player.posZ));
                    break;
                }
                case OBSIDIAN: {
                    if (this.placeMode.getValue() == PlaceMode.NORMAL) {
                        positions.add(new BlockPos(Selftrap.mc.player.posX, Selftrap.mc.player.posY + 2.0, Selftrap.mc.player.posZ));
                        final int placeability = BlockUtil.isPositionPlaceable(positions.get(0), false);
                        switch (placeability) {
                            case 0: {
                                return new ArrayList<BlockPos>();
                            }
                            case 3: {
                                return positions;
                            }
                            case 1: {
                                if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
                                    return positions;
                                }
                            }
                            case 2: {
                                positions.add(new BlockPos(Selftrap.mc.player.posX + 1.0, Selftrap.mc.player.posY + 1.0, Selftrap.mc.player.posZ));
                                positions.add(new BlockPos(Selftrap.mc.player.posX + 1.0, Selftrap.mc.player.posY + 2.0, Selftrap.mc.player.posZ));
                                break Label_0540;
                            }
                            default: {
                                break Label_0540;
                            }
                        }
                    }
                    else {
                        positions.add(new BlockPos(Selftrap.mc.player.posX, Selftrap.mc.player.posY, Selftrap.mc.player.posZ));
                        if (this.placeMode.getValue() == PlaceMode.SELFHIGH) {
                            positions.add(new BlockPos(Selftrap.mc.player.posX, Selftrap.mc.player.posY + 1.0, Selftrap.mc.player.posZ));
                        }
                        final int placeability = BlockUtil.isPositionPlaceable(positions.get(0), false);
                        switch (placeability) {
                            case 0: {
                                return new ArrayList<BlockPos>();
                            }
                            case 3: {
                                return positions;
                            }
                            case 1: {
                                if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
                                    return positions;
                                }
                            }
                            case 2: {
                                break Label_0540;
                            }
                        }
                    }
                    break;
                }
            }
        }
        positions.sort(Comparator.comparingDouble(Vec3i::getY));
        return positions;
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.blocksThisTick < this.blocksPerTick.getValue() && this.switchItem(false)) {
            final boolean bl;
            final boolean smartRotate = bl = (this.blocksPerTick.getValue() == 1 && this.rotate.getValue());
            this.isSneaking = (smartRotate ? BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking));
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }
    
    private boolean check() {
        if (Feature.fullNullCheck() || (this.disable.getValue() && this.offTimer.passedMs(this.disableTime.getValue()))) {
            this.disable();
            return true;
        }
        if (Selftrap.mc.player.inventory.currentItem != this.lastHotbarSlot && Selftrap.mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock((Class)((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class))) {
            this.lastHotbarSlot = Selftrap.mc.player.inventory.currentItem;
        }
        this.switchItem(true);
        if (!this.freecam.getValue() && Banzem.moduleManager.isModuleEnabled(Freecam.class)) {
            return true;
        }
        this.blocksThisTick = 0;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        int targetSlot = -1;
        switch (this.currentMode) {
            case WEBS: {
                this.hasOffhand = InventoryUtil.isBlock(Selftrap.mc.player.getHeldItemOffhand().getItem(), BlockWeb.class);
                targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
                break;
            }
            case OBSIDIAN: {
                this.hasOffhand = InventoryUtil.isBlock(Selftrap.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
                targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                break;
            }
        }
        if (this.onlySafe.getValue() && !EntityUtil.isSafe((Entity)Selftrap.mc.player)) {
            this.disable();
            return true;
        }
        return (!this.hasOffhand && targetSlot == -1 && (!this.offhand.getValue() || (!EntityUtil.isSafe((Entity)Selftrap.mc.player) && this.onlySafe.getValue()))) || (this.offhand.getValue() && !this.hasOffhand) || !this.timer.passedMs(this.delay.getValue());
    }
    
    private boolean switchItem(final boolean back) {
        if (this.offhand.getValue()) {
            return true;
        }
        final boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), (Class)((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class));
        this.switchedItem = value[0];
        return value[1];
    }
    
    public enum PlaceMode
    {
        NORMAL, 
        SELF, 
        SELFHIGH;
    }
    
    public enum Mode
    {
        WEBS, 
        OBSIDIAN;
    }
}
