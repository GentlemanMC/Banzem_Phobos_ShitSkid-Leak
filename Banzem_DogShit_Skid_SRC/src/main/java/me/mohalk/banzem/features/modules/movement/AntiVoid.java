//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.movement;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class AntiVoid extends Module
{
    public Setting<Double> yLevel;
    public Setting<Double> yForce;
    
    public AntiVoid() {
        super("AntiVoid", "Glitches you up from void.", Category.MOVEMENT, false, false, false);
        this.yLevel = (Setting<Double>)this.register(new Setting("YLevel", 1.0, 0.1, 5.0));
        this.yForce = (Setting<Double>)this.register(new Setting("YMotion", 0.1, 0.0, 1.0));
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (!AntiVoid.mc.player.noClip && AntiVoid.mc.player.posY <= this.yLevel.getValue()) {
            final RayTraceResult trace = AntiVoid.mc.world.rayTraceBlocks(AntiVoid.mc.player.getPositionVector(), new Vec3d(AntiVoid.mc.player.posX, 0.0, AntiVoid.mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                return;
            }
            AntiVoid.mc.player.motionY = this.yForce.getValue();
            if (AntiVoid.mc.player.getRidingEntity() != null) {
                AntiVoid.mc.player.getRidingEntity().motionY = this.yForce.getValue();
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.yLevel.getValue().toString() + ", " + this.yForce.getValue().toString();
    }
}
