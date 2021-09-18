//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.combat;

import net.minecraft.block.BlockWeb;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.Item;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.Entity;
import me.mohalk.banzem.util.EntityUtil;
import net.minecraft.item.ItemSword;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.event.events.PacketEvent;
import org.lwjgl.input.Mouse;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import me.mohalk.banzem.event.events.ProcessRightClickBlockEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.util.InventoryUtil;
import java.util.Queue;
import me.mohalk.banzem.features.modules.Module;

public class OffhandRewrite extends Module
{
    private static OffhandRewrite instance;
    private final Queue<InventoryUtil.Task> taskList;
    private final Timer timer;
    private final Timer secondTimer;
    public Setting<Boolean> crystal;
    public Setting<Float> crystalHealth;
    public Setting<Float> crystalHoleHealth;
    public Setting<Boolean> gapple;
    public Setting<Boolean> antiGappleFail;
    public Setting<Boolean> armorCheck;
    public Setting<Integer> actions;
    public Setting<Boolean> fallDistance;
    public Setting<Float> Height;
    public Mode2 currentMode;
    public int totems;
    public int crystals;
    public int gapples;
    public int lastTotemSlot;
    public int lastGappleSlot;
    public int lastCrystalSlot;
    public int lastObbySlot;
    public int lastWebSlot;
    public boolean holdingCrystal;
    public boolean holdingTotem;
    public boolean holdingGapple;
    public boolean didSwitchThisTick;
    private boolean second;
    private boolean switchedForHealthReason;
    
    public OffhandRewrite() {
        super("OffhandRewrite", "Allows you to switch up your Offhand.", Category.COMBAT, true, false, false);
        this.taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
        this.timer = new Timer();
        this.secondTimer = new Timer();
        this.crystal = (Setting<Boolean>)this.register(new Setting("Crystal", true));
        this.crystalHealth = (Setting<Float>)this.register(new Setting("CrystalHP", 13.0f, 0.1f, 36.0f));
        this.crystalHoleHealth = (Setting<Float>)this.register(new Setting("CrystalHoleHP", 3.5f, 0.1f, 36.0f));
        this.gapple = (Setting<Boolean>)this.register(new Setting("Gapple", true));
        this.antiGappleFail = (Setting<Boolean>)this.register(new Setting("AntiGapFail", false));
        this.armorCheck = (Setting<Boolean>)this.register(new Setting("ArmorCheck", true));
        this.actions = (Setting<Integer>)this.register(new Setting("Packets", 4, 1, 4));
        this.fallDistance = (Setting<Boolean>)this.register(new Setting("FallDistance", false));
        this.Height = (Setting<Float>)this.register(new Setting("Height", 0.0f, 0.0f, 30.0f, v -> this.fallDistance.getValue()));
        this.currentMode = Mode2.TOTEMS;
        this.totems = 0;
        this.crystals = 0;
        this.gapples = 0;
        this.lastTotemSlot = -1;
        this.lastGappleSlot = -1;
        this.lastCrystalSlot = -1;
        this.lastObbySlot = -1;
        this.lastWebSlot = -1;
        this.holdingCrystal = false;
        this.holdingTotem = false;
        this.holdingGapple = false;
        this.didSwitchThisTick = false;
        this.second = false;
        this.switchedForHealthReason = false;
        OffhandRewrite.instance = this;
    }
    
    public static OffhandRewrite getInstance() {
        if (OffhandRewrite.instance == null) {
            OffhandRewrite.instance = new OffhandRewrite();
        }
        return OffhandRewrite.instance;
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final ProcessRightClickBlockEvent event) {
        if (event.hand == EnumHand.MAIN_HAND && event.stack.getItem() == Items.END_CRYSTAL && OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && OffhandRewrite.mc.objectMouseOver != null && event.pos == OffhandRewrite.mc.objectMouseOver.getBlockPos()) {
            event.setCanceled(true);
            OffhandRewrite.mc.player.setActiveHand(EnumHand.OFF_HAND);
            OffhandRewrite.mc.playerController.processRightClick((EntityPlayer)OffhandRewrite.mc.player, (World)OffhandRewrite.mc.world, EnumHand.OFF_HAND);
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.timer.passedMs(50L)) {
            if (OffhandRewrite.mc.player != null && OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && OffhandRewrite.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Mouse.isButtonDown(1)) {
                OffhandRewrite.mc.player.setActiveHand(EnumHand.OFF_HAND);
                OffhandRewrite.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }
        else if (OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && OffhandRewrite.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            OffhandRewrite.mc.gameSettings.keyBindUseItem.pressed = false;
        }
        if (Offhand.nullCheck()) {
            return;
        }
        this.doOffhand();
        if (this.secondTimer.passedMs(50L) && this.second) {
            this.second = false;
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (!Feature.fullNullCheck() && OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && OffhandRewrite.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && OffhandRewrite.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                final CPacketPlayerTryUseItemOnBlock packet2 = event.getPacket();
                if (packet2.getHand() == EnumHand.MAIN_HAND) {
                    if (this.timer.passedMs(50L)) {
                        OffhandRewrite.mc.player.setActiveHand(EnumHand.OFF_HAND);
                        OffhandRewrite.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                    }
                    event.setCanceled(true);
                }
            }
            else {
                final CPacketPlayerTryUseItem packet3;
                if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet3 = event.getPacket()).getHand() == EnumHand.OFF_HAND && !this.timer.passedMs(50L)) {
                    event.setCanceled(true);
                }
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return "Crystals";
        }
        if (OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return "Totems";
        }
        if (OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            return "Gapples";
        }
        return null;
    }
    
    public void doOffhand() {
        this.didSwitchThisTick = false;
        this.holdingCrystal = (OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
        this.holdingTotem = (OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING);
        this.holdingGapple = (OffhandRewrite.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE);
        this.totems = OffhandRewrite.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (this.holdingTotem) {
            this.totems += OffhandRewrite.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        }
        this.crystals = OffhandRewrite.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (this.holdingCrystal) {
            this.crystals += OffhandRewrite.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        }
        this.gapples = OffhandRewrite.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (this.holdingGapple) {
            this.gapples += OffhandRewrite.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        }
        this.doSwitch();
    }
    
    public void doSwitch() {
        this.currentMode = Mode2.TOTEMS;
        if (this.gapple.getValue() && OffhandRewrite.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && OffhandRewrite.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.currentMode = Mode2.GAPPLES;
        }
        else if (this.currentMode != Mode2.CRYSTALS && this.crystal.getValue() && ((EntityUtil.isSafe((Entity)OffhandRewrite.mc.player) && EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) > this.crystalHoleHealth.getValue()) || EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) > this.crystalHealth.getValue())) {
            this.currentMode = Mode2.CRYSTALS;
        }
        if (this.antiGappleFail.getValue() && this.currentMode == Mode2.GAPPLES && ((!EntityUtil.isSafe((Entity)OffhandRewrite.mc.player) && EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) <= this.crystalHealth.getValue()) || EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) <= this.crystalHoleHealth.getValue())) {
            this.switchedForHealthReason = true;
            this.setMode(Mode2.TOTEMS);
        }
        if (this.currentMode == Mode2.CRYSTALS && this.crystals == 0) {
            this.setMode(Mode2.TOTEMS);
        }
        if (this.currentMode == Mode2.CRYSTALS && ((!EntityUtil.isSafe((Entity)OffhandRewrite.mc.player) && EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) <= this.crystalHealth.getValue()) || EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) <= this.crystalHoleHealth.getValue())) {
            if (this.currentMode == Mode2.CRYSTALS) {
                this.switchedForHealthReason = true;
            }
            this.setMode(Mode2.TOTEMS);
        }
        if (this.switchedForHealthReason && ((EntityUtil.isSafe((Entity)OffhandRewrite.mc.player) && EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) > this.crystalHoleHealth.getValue()) || EntityUtil.getHealth((Entity)OffhandRewrite.mc.player, true) > this.crystalHealth.getValue())) {
            this.setMode(Mode2.CRYSTALS);
            this.switchedForHealthReason = false;
        }
        if (this.currentMode == Mode2.CRYSTALS && this.armorCheck.getValue() && (OffhandRewrite.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.AIR || OffhandRewrite.mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == Items.AIR || OffhandRewrite.mc.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == Items.AIR || OffhandRewrite.mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == Items.AIR)) {
            this.setMode(Mode2.TOTEMS);
        }
        if ((this.currentMode == Mode2.CRYSTALS || this.currentMode == Mode2.GAPPLES) && OffhandRewrite.mc.player.fallDistance > this.Height.getValue() && this.fallDistance.getValue()) {
            this.setMode(Mode2.TOTEMS);
        }
        if (OffhandRewrite.mc.currentScreen instanceof GuiContainer && !(OffhandRewrite.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        final Item currentOffhandItem = OffhandRewrite.mc.player.getHeldItemOffhand().getItem();
        switch (this.currentMode) {
            case TOTEMS: {
                if (this.totems <= 0) {
                    break;
                }
                if (this.holdingTotem) {
                    break;
                }
                this.lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastTotemSlot);
                this.putItemInOffhand(this.lastTotemSlot, lastSlot);
                break;
            }
            case GAPPLES: {
                if (this.gapples <= 0) {
                    break;
                }
                if (this.holdingGapple) {
                    break;
                }
                this.lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.GOLDEN_APPLE, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastGappleSlot);
                this.putItemInOffhand(this.lastGappleSlot, lastSlot);
                break;
            }
            default: {
                if (this.crystals <= 0) {
                    break;
                }
                if (this.holdingCrystal) {
                    break;
                }
                this.lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastCrystalSlot);
                this.putItemInOffhand(this.lastCrystalSlot, lastSlot);
                break;
            }
        }
        for (int i = 0; i < this.actions.getValue(); ++i) {
            final InventoryUtil.Task task = this.taskList.poll();
            if (task != null) {
                task.run();
                if (task.isSwitching()) {
                    this.didSwitchThisTick = true;
                }
            }
        }
    }
    
    private int getLastSlot(final Item item, final int slotIn) {
        if (item == Items.END_CRYSTAL) {
            return this.lastCrystalSlot;
        }
        if (item == Items.GOLDEN_APPLE) {
            return this.lastGappleSlot;
        }
        if (item == Items.TOTEM_OF_UNDYING) {
            return this.lastTotemSlot;
        }
        if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
            return this.lastObbySlot;
        }
        if (InventoryUtil.isBlock(item, BlockWeb.class)) {
            return this.lastWebSlot;
        }
        if (item == Items.AIR) {
            return -1;
        }
        return slotIn;
    }
    
    private void putItemInOffhand(final int slotIn, final int slotOut) {
        if (slotIn != -1 && this.taskList.isEmpty()) {
            this.taskList.add(new InventoryUtil.Task(slotIn));
            this.taskList.add(new InventoryUtil.Task(45));
            this.taskList.add(new InventoryUtil.Task(slotOut));
            this.taskList.add(new InventoryUtil.Task());
        }
    }
    
    public void setMode(final Mode2 mode) {
        this.currentMode = ((this.currentMode == mode) ? Mode2.TOTEMS : mode);
    }
    
    public enum Mode2
    {
        TOTEMS, 
        GAPPLES, 
        CRYSTALS;
    }
}
