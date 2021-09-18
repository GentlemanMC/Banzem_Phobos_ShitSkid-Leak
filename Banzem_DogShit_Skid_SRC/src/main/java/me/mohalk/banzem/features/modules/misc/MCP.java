//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import me.mohalk.banzem.util.InventoryUtil;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class MCP extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Boolean> stopRotation;
    private final Setting<Boolean> antiFriend;
    private final Setting<Integer> rotation;
    private boolean clicked;
    
    public MCP() {
        super("MCP", "Throws a pearl", Category.MISC, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.MIDDLECLICK));
        this.stopRotation = (Setting<Boolean>)this.register(new Setting("Rotation", true));
        this.antiFriend = (Setting<Boolean>)this.register(new Setting("AntiFriend", true));
        this.rotation = (Setting<Integer>)this.register(new Setting("Delay", 10, 0, 100, v -> this.stopRotation.getValue()));
        this.clicked = false;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck() && this.mode.getValue() == Mode.TOGGLE) {
            this.throwPearl();
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (this.mode.getValue() == Mode.MIDDLECLICK) {
            if (Mouse.isButtonDown(2)) {
                if (!this.clicked) {
                    this.throwPearl();
                }
                this.clicked = true;
            }
            else {
                this.clicked = false;
            }
        }
    }
    
    private void throwPearl() {
        final RayTraceResult result;
        final Entity entity;
        if (this.antiFriend.getValue() && (result = MCP.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            return;
        }
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean bl;
        final boolean offhand = bl = (MCP.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL);
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.playerController.processRightClick((EntityPlayer)MCP.mc.player, (World)MCP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
    
    public enum Mode
    {
        TOGGLE, 
        MIDDLECLICK;
    }
}
