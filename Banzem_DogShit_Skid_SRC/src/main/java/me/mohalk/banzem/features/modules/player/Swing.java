//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.player;

import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class Swing extends Module
{
    private Setting<Hand> hand;
    
    public Swing() {
        super("Swing", "Changes the hand you swing with", Category.PLAYER, false, false, false);
        this.hand = (Setting<Hand>)this.register(new Setting("Hand", Hand.OFFHAND));
    }
    
    @Override
    public void onUpdate() {
        if (Swing.mc.world == null) {
            return;
        }
        if (this.hand.getValue().equals(Hand.OFFHAND)) {
            Swing.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.hand.getValue().equals(Hand.MAINHAND)) {
            Swing.mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
        if (this.hand.getValue().equals(Hand.PACKETSWING) && Swing.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && Swing.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Swing.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Swing.mc.entityRenderer.itemRenderer.itemStackMainHand = Swing.mc.player.getHeldItemMainhand();
        }
    }
    
    public enum Hand
    {
        OFFHAND, 
        MAINHAND, 
        PACKETSWING;
    }
}
