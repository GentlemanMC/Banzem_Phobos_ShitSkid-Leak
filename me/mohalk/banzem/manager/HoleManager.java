/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.mohalk.banzem.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.features.modules.client.Managers;
import me.mohalk.banzem.features.modules.combat.HoleFiller;
import me.mohalk.banzem.features.modules.render.HoleESP;
import me.mohalk.banzem.util.BlockUtil;
import me.mohalk.banzem.util.EntityUtil;
import me.mohalk.banzem.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleManager
extends Feature
implements Runnable {
    private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true, true));
    private final List<BlockPos> midSafety = new ArrayList<BlockPos>();
    private final Timer syncTimer = new Timer();
    private final AtomicBoolean shouldInterrupt = new AtomicBoolean(false);
    private final Timer holeTimer = new Timer();
    private List<BlockPos> holes = new ArrayList<BlockPos>();
    private ScheduledExecutorService executorService;
    private int lastUpdates = 0;
    private Thread thread;

    public void update() {
        if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.WHILE) {
            if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || this.syncTimer.passedMs(Managers.getInstance().holeSync.getValue().intValue())) {
                if (this.thread == null) {
                    this.thread = new Thread(this);
                } else if (this.syncTimer.passedMs(Managers.getInstance().holeSync.getValue().intValue()) && !this.shouldInterrupt.get()) {
                    this.shouldInterrupt.set(true);
                    this.syncTimer.reset();
                    return;
                }
                if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
                    this.thread = new Thread(this);
                }
                if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
                    try {
                        this.thread.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.syncTimer.reset();
                }
            }
        } else if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.WHILE) {
            if (this.executorService == null || this.executorService.isTerminated() || this.executorService.isShutdown() || this.syncTimer.passedMs(10000L) || this.lastUpdates != Managers.getInstance().holeUpdates.getValue()) {
                this.lastUpdates = Managers.getInstance().holeUpdates.getValue();
                if (this.executorService != null) {
                    this.executorService.shutdown();
                }
                this.executorService = this.getExecutor();
            }
        } else if (this.holeTimer.passedMs(Managers.getInstance().holeUpdates.getValue().intValue()) && !HoleManager.fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
            this.holes = this.calcHoles();
            this.holeTimer.reset();
        }
    }

    public void settingChanged() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
        if (this.thread != null) {
            this.shouldInterrupt.set(true);
        }
    }

    private ScheduledExecutorService getExecutor() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, Managers.getInstance().holeUpdates.getValue().intValue(), TimeUnit.MILLISECONDS);
        return service;
    }

    @Override
    public void run() {
        if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.WHILE) {
            while (true) {
                if (this.shouldInterrupt.get()) {
                    this.shouldInterrupt.set(false);
                    this.syncTimer.reset();
                    Thread.currentThread().interrupt();
                    return;
                }
                if (!HoleManager.fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
                    this.holes = this.calcHoles();
                }
                try {
                    Thread.sleep(Managers.getInstance().holeUpdates.getValue().intValue());
                }
                catch (InterruptedException e) {
                    this.thread.interrupt();
                    e.printStackTrace();
                }
            }
        }
        if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.POOL && !HoleManager.fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
            this.holes = this.calcHoles();
        }
    }

    public List<BlockPos> getHoles() {
        return this.holes;
    }

    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }

    public List<BlockPos> getSortedHoles() {
        this.holes.sort(Comparator.comparingDouble(hole -> HoleManager.mc.field_71439_g.func_174818_b(hole)));
        return this.getHoles();
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        this.midSafety.clear();
        List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)HoleManager.mc.field_71439_g), Managers.getInstance().holeRange.getValue().floatValue(), Managers.getInstance().holeRange.getValue().intValue(), false, true, 0);
        for (BlockPos pos : positions) {
            if (!HoleManager.mc.field_71441_e.func_180495_p(pos).func_177230_c().equals((Object)Blocks.field_150350_a) || !HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals((Object)Blocks.field_150350_a) || !HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals((Object)Blocks.field_150350_a)) continue;
            boolean isSafe = true;
            boolean midSafe = true;
            for (BlockPos offset : surroundOffset) {
                Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
                if (BlockUtil.isBlockUnSolid(block)) {
                    midSafe = false;
                }
                if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ) continue;
                isSafe = false;
            }
            if (isSafe) {
                safeSpots.add(pos);
            }
            if (!midSafe) continue;
            this.midSafety.add(pos);
        }
        return safeSpots;
    }

    public boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for (BlockPos offset : surroundOffset) {
            Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
            if (block == Blocks.field_150357_h) continue;
            isSafe = false;
            break;
        }
        return isSafe;
    }
}

