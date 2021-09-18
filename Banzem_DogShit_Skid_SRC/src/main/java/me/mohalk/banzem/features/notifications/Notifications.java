//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.notifications;

import me.mohalk.banzem.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.modules.client.HUD;
import me.mohalk.banzem.util.Timer;

public class Notifications
{
    private final String text;
    private final long disableTime;
    private final float width;
    private final Timer timer;
    
    public Notifications(final String text, final long disableTime) {
        this.timer = new Timer();
        this.text = text;
        this.disableTime = disableTime;
        this.width = (float)Banzem.moduleManager.getModuleByClass(HUD.class).renderer.getStringWidth(text);
        this.timer.reset();
    }
    
    public void onDraw(final int y) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (this.timer.passedMs(this.disableTime)) {
            Banzem.notificationManager.getNotifications().remove(this);
        }
        RenderUtil.drawRect(scaledResolution.getScaledWidth() - 4 - this.width, (float)y, (float)(scaledResolution.getScaledWidth() - 2), (float)(y + Banzem.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 3), 1962934272);
        Banzem.moduleManager.getModuleByClass(HUD.class).renderer.drawString(this.text, scaledResolution.getScaledWidth() - this.width - 3.0f, (float)(y + 2), -1, true);
    }
}
