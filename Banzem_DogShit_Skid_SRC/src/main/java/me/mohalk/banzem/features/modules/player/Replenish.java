//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.player;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.item.ItemStack;
import java.util.Map;
import me.mohalk.banzem.util.InventoryUtil;
import java.util.Queue;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class Replenish extends Module
{
    private final Setting<Integer> threshold;
    private final Setting<Integer> replenishments;
    private final Setting<Integer> updates;
    private final Setting<Integer> actions;
    private final Setting<Boolean> pauseInv;
    private final Setting<Boolean> putBack;
    private final Timer timer;
    private final Timer replenishTimer;
    private final Queue<InventoryUtil.Task> taskList;
    private Map<Integer, ItemStack> hotbar;
    
    public Replenish() {
        super("Replenish", "Replenishes your hotbar", Category.PLAYER, false, false, false);
        this.threshold = (Setting<Integer>)this.register(new Setting("Threshold", 0, 0, 63));
        this.replenishments = (Setting<Integer>)this.register(new Setting("RUpdates", 0, 0, 1000));
        this.updates = (Setting<Integer>)this.register(new Setting("HBUpdates", 100, 0, 1000));
        this.actions = (Setting<Integer>)this.register(new Setting("Actions", 2, 1, 30));
        this.pauseInv = (Setting<Boolean>)this.register(new Setting("PauseInv", true));
        this.putBack = (Setting<Boolean>)this.register(new Setting("PutBack", true));
        this.timer = new Timer();
        this.replenishTimer = new Timer();
        this.taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
        this.hotbar = new ConcurrentHashMap<Integer, ItemStack>();
    }
    
    @Override
    public void onUpdate() {
        if (Replenish.mc.currentScreen instanceof GuiContainer && (!(Replenish.mc.currentScreen instanceof GuiInventory) || this.pauseInv.getValue())) {
            return;
        }
        if (this.timer.passedMs(this.updates.getValue())) {
            this.mapHotbar();
        }
        if (this.replenishTimer.passedMs(this.replenishments.getValue())) {
            for (int i = 0; i < this.actions.getValue(); ++i) {
                final InventoryUtil.Task task = this.taskList.poll();
                if (task != null) {
                    task.run();
                }
            }
            this.replenishTimer.reset();
        }
    }
    
    @Override
    public void onDisable() {
        this.hotbar.clear();
    }
    
    @Override
    public void onLogout() {
        this.onDisable();
    }
    
    private void mapHotbar() {
        final ConcurrentHashMap<Integer, ItemStack> map = new ConcurrentHashMap<Integer, ItemStack>();
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Replenish.mc.player.inventory.getStackInSlot(i);
            map.put(i, stack);
        }
        if (this.hotbar.isEmpty()) {
            this.hotbar = map;
            return;
        }
        final ConcurrentHashMap<Integer, Integer> fromTo = new ConcurrentHashMap<Integer, Integer>();
        for (final Map.Entry hotbarItem : map.entrySet()) {
            final ItemStack stack2 = (ItemStack) hotbarItem.getValue();
            final Integer slotKey = (Integer) hotbarItem.getKey();
            if (slotKey != null && stack2 != null) {
                if (!stack2.isEmpty && stack2.getItem() != Items.AIR) {
                    if (stack2.stackSize > this.threshold.getValue()) {
                        continue;
                    }
                    if (stack2.stackSize >= stack2.getMaxStackSize()) {
                        continue;
                    }
                }
                ItemStack previousStack = (ItemStack) hotbarItem.getValue();
                if (stack2.isEmpty || stack2.getItem() != Items.AIR) {
                    previousStack = this.hotbar.get(slotKey);
                }
                if (previousStack == null || previousStack.isEmpty || previousStack.getItem() == Items.AIR) {
                    continue;
                }
                final int replenishSlot;
                if ((replenishSlot = this.getReplenishSlot(previousStack)) == -1) {
                    continue;
                }
                fromTo.put(replenishSlot, InventoryUtil.convertHotbarToInv(slotKey));
            }
        }
        if (!fromTo.isEmpty()) {
            for (final Map.Entry slotMove : fromTo.entrySet()) {
                this.taskList.add(new InventoryUtil.Task((Integer) slotMove.getKey()));
                this.taskList.add(new InventoryUtil.Task((Integer) slotMove.getValue()));
                this.taskList.add(new InventoryUtil.Task((Integer) slotMove.getKey()));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
        this.hotbar = map;
    }
    
    private int getReplenishSlot(final ItemStack stack) {
        final AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (final Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getKey() < 36) {
                if (!InventoryUtil.areStacksCompatible(stack, entry.getValue())) {
                    continue;
                }
                slot.set(entry.getKey());
                return slot.get();
            }
        }
        return slot.get();
    }
}
