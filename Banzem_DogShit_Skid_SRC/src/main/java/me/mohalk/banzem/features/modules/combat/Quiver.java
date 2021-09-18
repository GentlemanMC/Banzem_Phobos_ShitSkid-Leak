//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.combat;

import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemTippedArrow;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.item.ItemStack;
import me.mohalk.banzem.util.RotationUtil;
import net.minecraft.entity.Entity;
import me.mohalk.banzem.util.EntityUtil;
import me.mohalk.banzem.features.command.Command;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import me.mohalk.banzem.util.InventoryUtil;
import net.minecraft.item.ItemBow;
import java.util.ArrayList;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class Quiver extends Module
{
    private final Setting<Integer> delay;
    private final Setting<Integer> holdLength;
    private final Setting<mainEnum> main;
    private final Setting<mainEnum> secondary;
    private final Timer delayTimer;
    private final Timer holdTimer;
    private int stage;
    private ArrayList<Integer> map;
    private int strSlot;
    private int speedSlot;
    private int oldSlot;
    
    public Quiver() {
        super("Quiver", "Automatically shoots yourself with good effects.", Category.COMBAT, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", 200, 0, 500));
        this.holdLength = (Setting<Integer>)this.register(new Setting("Hold Length", 350, 100, 1000));
        this.main = (Setting<mainEnum>)this.register(new Setting("Main", mainEnum.SPEED));
        this.secondary = (Setting<mainEnum>)this.register(new Setting("Secondary", mainEnum.STRENGTH));
        this.delayTimer = new Timer();
        this.holdTimer = new Timer();
        this.strSlot = -1;
        this.speedSlot = -1;
        this.oldSlot = 1;
    }
    
    @Override
    public void onEnable() {
        if (nullCheck()) {
            return;
        }
        InventoryUtil.switchToHotbarSlot(ItemBow.class, false);
        this.clean();
        this.oldSlot = Quiver.mc.player.inventory.currentItem;
        Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
    }
    
    @Override
    public void onDisable() {
        if (nullCheck()) {
            return;
        }
        InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
        Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
        this.clean();
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (Quiver.mc.currentScreen != null) {
            return;
        }
        if (InventoryUtil.findItemInventorySlot((Item)Items.BOW, true) == -1) {
            Command.sendMessage("Couldn't find bow in inventory! Toggling!");
            this.toggle();
        }
        RotationUtil.faceVector(EntityUtil.getInterpolatedPos((Entity)Quiver.mc.player, Quiver.mc.timer.elapsedPartialTicks).add(0.0, 3.0, 0.0), false);
        if (this.stage == 0) {
            this.map = this.mapArrows();
            for (final int a : this.map) {
                final ItemStack arrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a);
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_STRENGTH)) && this.strSlot == -1) {
                    this.strSlot = a;
                }
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_SWIFTNESS)) && this.speedSlot == -1) {
                    this.speedSlot = a;
                }
            }
            ++this.stage;
        }
        else if (this.stage == 1) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 2) {
            this.switchTo(this.main.getValue());
            ++this.stage;
        }
        else if (this.stage == 3) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 4) {
            Quiver.mc.gameSettings.keyBindUseItem.pressed = true;
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 5) {
            if (!this.holdTimer.passedMs(this.holdLength.getValue())) {
                return;
            }
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 6) {
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
            Quiver.mc.player.resetActiveHand();
            Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
            ++this.stage;
        }
        else if (this.stage == 7) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 8) {
            this.map = this.mapArrows();
            this.strSlot = -1;
            this.speedSlot = -1;
            for (final int a : this.map) {
                final ItemStack arrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a);
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_STRENGTH)) && this.strSlot == -1) {
                    this.strSlot = a;
                }
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_SWIFTNESS)) && this.speedSlot == -1) {
                    this.speedSlot = a;
                }
            }
            ++this.stage;
        }
        if (this.stage == 9) {
            this.switchTo(this.secondary.getValue());
            ++this.stage;
        }
        else if (this.stage == 10) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            ++this.stage;
        }
        else if (this.stage == 11) {
            Quiver.mc.gameSettings.keyBindUseItem.pressed = true;
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 12) {
            if (!this.holdTimer.passedMs(this.holdLength.getValue())) {
                return;
            }
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 13) {
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
            Quiver.mc.player.resetActiveHand();
            Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
            ++this.stage;
        }
        else if (this.stage == 14) {
            final ArrayList<Integer> map = this.mapEmpty();
            if (!map.isEmpty()) {
                final int a = map.get(0);
                Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, a, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
            }
            ++this.stage;
        }
        else if (this.stage == 15) {
            this.setEnabled(false);
        }
    }
    
    private void switchTo(final Enum<mainEnum> mode) {
        if (mode.toString().equalsIgnoreCase("STRENGTH") && this.strSlot != -1) {
            this.switchTo(this.strSlot);
        }
        if (mode.toString().equalsIgnoreCase("SPEED") && this.speedSlot != -1) {
            this.switchTo(this.speedSlot);
        }
    }
    
    private ArrayList<Integer> mapArrows() {
        final ArrayList<Integer> map = new ArrayList<Integer>();
        for (int a = 9; a < 45; ++a) {
            if (((ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a)).getItem() instanceof ItemTippedArrow) {
                final ItemStack arrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a);
                if (PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_STRENGTH)) {
                    map.add(a);
                }
                if (PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_SWIFTNESS)) {
                    map.add(a);
                }
            }
        }
        return map;
    }
    
    private ArrayList<Integer> mapEmpty() {
        final ArrayList<Integer> map = new ArrayList<Integer>();
        for (int a = 9; a < 45; ++a) {
            if (((ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a)).getItem() instanceof ItemAir || Quiver.mc.player.inventoryContainer.getInventory().get(a) == ItemStack.EMPTY) {
                map.add(a);
            }
        }
        return map;
    }
    
    private void switchTo(final int from) {
        if (from == 9) {
            return;
        }
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, 9, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.updateController();
    }
    
    private void clean() {
        this.holdTimer.reset();
        this.delayTimer.reset();
        this.map = null;
        this.speedSlot = -1;
        this.strSlot = -1;
        this.stage = 0;
    }
    
    private enum mainEnum
    {
        STRENGTH, 
        SPEED;
    }
}
