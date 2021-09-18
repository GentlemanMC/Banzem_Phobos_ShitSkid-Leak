//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.client;

import me.mohalk.banzem.util.RenderUtil;
import me.mohalk.banzem.util.ColorUtil;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.event.events.Render2DEvent;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.modules.Module;

public class CSGOWatermark extends Module
{
    Timer delayTimer;
    public Setting<Integer> X;
    public Setting<Integer> Y;
    public float hue;
    public int red;
    public int green;
    public int blue;
    private String message;
    
    public CSGOWatermark() {
        super("CSGOWatermark", "mohalk actually makes something", Category.CLIENT, true, false, false);
        this.delayTimer = new Timer();
        this.X = (Setting<Integer>)this.register(new Setting("WatermarkX", 0, 0, 300));
        this.Y = (Setting<Integer>)this.register(new Setting("WatermarkY", 0, 0, 300));
        this.red = 1;
        this.green = 1;
        this.blue = 1;
        this.message = "";
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        this.drawCsgoWatermark();
    }
    
    public void drawCsgoWatermark() {
        final int padding = 5;
        this.message = "Banzem 1.0.0 | " + CSGOWatermark.mc.player.getName() + " | " + Banzem.serverManager.getPing() + "ms";
        final Integer textWidth = CSGOWatermark.mc.fontRenderer.getStringWidth(this.message);
        final Integer textHeight = CSGOWatermark.mc.fontRenderer.FONT_HEIGHT;
        RenderUtil.drawRectangleCorrectly(this.X.getValue() - 4, this.Y.getValue() - 4, textWidth + 16, textHeight + 12, ColorUtil.toRGBA(22, 22, 22, 255));
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 4, textHeight + 4, ColorUtil.toRGBA(0, 0, 0, 255));
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 8, textHeight + 4, ColorUtil.toRGBA(0, 0, 0, 255));
        CSGOWatermark.mc.fontRenderer.drawString(this.message, (float)(this.X.getValue() + 3), (float)(this.Y.getValue() + 3), ColorUtil.toRGBA(255, 255, 255, 255), false);
    }
}
