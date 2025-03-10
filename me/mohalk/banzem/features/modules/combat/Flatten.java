/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.mohalk.banzem.features.modules.combat;

import java.util.ArrayList;
import java.util.Collections;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.util.BlockUtil;
import me.mohalk.banzem.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Flatten
extends Module {
    private final Setting<Float> placerange = this.register(new Setting<Float>("PlaceRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f)));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Block/Place", 8, 1, 20));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("PacketPlace", false));
    private final Setting<Boolean> autoDisable = this.register(new Setting<Boolean>("AutoDisable", true));
    private final Vec3d[] offsetsDefault = new Vec3d[]{new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0)};
    private int offsetStep = 0;
    private int oldSlot = -1;

    public Flatten() {
        super("Flatten", "Flatter then zprestiges 9 yr gf.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        this.oldSlot = Flatten.mc.field_71439_g.field_71071_by.field_70461_c;
    }

    @Override
    public void onDisable() {
        this.oldSlot = -1;
    }

    @Override
    public void onUpdate() {
        EntityPlayer closest_target = this.findClosestTarget();
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (closest_target == null) {
            this.disable();
            return;
        }
        ArrayList place_targets = new ArrayList();
        Collections.addAll(place_targets, this.offsetsDefault);
        int blocks_placed = 0;
        while (blocks_placed < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= place_targets.size()) {
                this.offsetStep = 0;
                break;
            }
            BlockPos offset_pos = new BlockPos((Vec3d)place_targets.get(this.offsetStep));
            BlockPos target_pos = new BlockPos(closest_target.func_174791_d()).func_177977_b().func_177982_a(offset_pos.func_177958_n(), offset_pos.func_177956_o(), offset_pos.func_177952_p());
            boolean should_try_place = Flatten.mc.field_71441_e.func_180495_p(target_pos).func_185904_a().func_76222_j();
            for (Entity entity : Flatten.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(target_pos))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                should_try_place = false;
                break;
            }
            if (should_try_place) {
                this.place(target_pos, obbySlot, this.oldSlot);
                ++blocks_placed;
            }
            ++this.offsetStep;
        }
        if (this.autoDisable.getValue().booleanValue()) {
            this.disable();
        }
    }

    private void place(BlockPos pos, int slot, int oldSlot) {
        Flatten.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        Flatten.mc.field_71442_b.func_78765_e();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), Flatten.mc.field_71439_g.func_70093_af());
        Flatten.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
        Flatten.mc.field_71442_b.func_78765_e();
    }

    private EntityPlayer findClosestTarget() {
        if (Flatten.mc.field_71441_e.field_73010_i.isEmpty()) {
            return null;
        }
        EntityPlayer closestTarget = null;
        for (EntityPlayer target : Flatten.mc.field_71441_e.field_73010_i) {
            if (target == Flatten.mc.field_71439_g || !target.func_70089_S() || Banzem.friendManager.isFriend(target.func_70005_c_()) || target.func_110143_aJ() <= 0.0f || Flatten.mc.field_71439_g.func_70032_d((Entity)target) > this.placerange.getValue().floatValue() || closestTarget != null && Flatten.mc.field_71439_g.func_70032_d((Entity)target) > Flatten.mc.field_71439_g.func_70032_d((Entity)closestTarget)) continue;
            closestTarget = target;
        }
        return closestTarget;
    }
}

