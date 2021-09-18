//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.gui.components;

import me.mohalk.banzem.features.gui.components.items.buttons.Button;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import me.mohalk.banzem.util.Util;
import java.util.Iterator;
import me.mohalk.banzem.features.gui.PhobosGui;
import me.mohalk.banzem.Banzem;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import me.mohalk.banzem.util.RenderUtil;
import me.mohalk.banzem.util.MathUtil;
import me.mohalk.banzem.features.modules.client.HUD;
import me.mohalk.banzem.util.ColorUtil;
import me.mohalk.banzem.features.modules.client.Colors;
import me.mohalk.banzem.features.modules.client.ClickGui;
import me.mohalk.banzem.features.gui.components.items.Item;
import java.util.ArrayList;
import me.mohalk.banzem.features.Feature;

public class Component extends Feature
{
    private final ArrayList<Item> items;
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden;
    
    public Component(final String name, final int x, final int y, final boolean open) {
        super(name);
        this.items = new ArrayList<Item>();
        this.hidden = false;
        this.x = x;
        this.y = y;
        this.width = 88;
        this.height = 18;
        this.open = open;
        this.setupItems();
    }
    
    public void setupItems() {
    }
    
    private void drag(final int mouseX, final int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drag(mouseX, mouseY);
        final float totalItemHeight = this.open ? (this.getTotalItemHeight() - 2.0f) : 0.0f;
        int color = -7829368;
        if (ClickGui.getInstance().devSettings.getValue()) {
            int argb = 0;
            if (ClickGui.getInstance().colorSync.getValue()) {
                Colors.INSTANCE.getCurrentColorHex();
            }
            else {
                argb = ColorUtil.toARGB(ClickGui.getInstance().topRed.getValue(), ClickGui.getInstance().topGreen.getValue(), ClickGui.getInstance().topBlue.getValue(), ClickGui.getInstance().topAlpha.getValue());
            }
            color = argb;
        }
        if (ClickGui.getInstance().rainbowRolling.getValue() && ClickGui.getInstance().colorSync.getValue() && Colors.INSTANCE.rainbow.getValue()) {
            RenderUtil.drawGradientRect((float)this.x, this.y - 1.5f, (float)this.width, (float)(this.height - 4), HUD.getInstance().colorMap.get(MathUtil.clamp(this.y, 0, this.renderer.scaledHeight)), HUD.getInstance().colorMap.get(MathUtil.clamp(this.y + this.height - 4, 0, this.renderer.scaledHeight)));
        }
        else {
            RenderUtil.drawRect((float)this.x, this.y - 1.5f, (float)(this.x + this.width), (float)(this.y + this.height - 6), color);
        }
        if (this.open) {
            RenderUtil.drawRect((float)this.x, this.y + 12.5f, (float)(this.x + this.width), this.y + this.height + totalItemHeight, 1996488704);
            if (ClickGui.getInstance().outline.getValue()) {
                if (ClickGui.getInstance().rainbowRolling.getValue()) {
                    GlStateManager.disableTexture2D();
                    GlStateManager.enableBlend();
                    GlStateManager.disableAlpha();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.shadeModel(7425);
                    GL11.glBegin(1);
                    Color currentColor = new Color(HUD.getInstance().colorMap.get(MathUtil.clamp(this.y, 0, this.renderer.scaledHeight)));
                    GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                    GL11.glVertex3f((float)(this.x + this.width), this.y - 1.5f, 0.0f);
                    GL11.glVertex3f((float)this.x, this.y - 1.5f, 0.0f);
                    GL11.glVertex3f((float)this.x, this.y - 1.5f, 0.0f);
                    float currentHeight = this.getHeight() - 1.5f;
                    for (final Item item : this.getItems()) {
                        currentColor = new Color(HUD.getInstance().colorMap.get(MathUtil.clamp((int)(this.y + (currentHeight += item.getHeight() + 1.5f)), 0, this.renderer.scaledHeight)));
                        GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                        GL11.glVertex3f((float)this.x, this.y + currentHeight, 0.0f);
                        GL11.glVertex3f((float)this.x, this.y + currentHeight, 0.0f);
                    }
                    currentColor = new Color(HUD.getInstance().colorMap.get(MathUtil.clamp((int)(this.y + this.height + totalItemHeight), 0, this.renderer.scaledHeight)));
                    GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                    GL11.glVertex3f((float)(this.x + this.width), this.y + this.height + totalItemHeight, 0.0f);
                    GL11.glVertex3f((float)(this.x + this.width), this.y + this.height + totalItemHeight, 0.0f);
                    for (final Item item : this.getItems()) {
                        currentColor = new Color(HUD.getInstance().colorMap.get(MathUtil.clamp((int)(this.y + (currentHeight -= item.getHeight() + 1.5f)), 0, this.renderer.scaledHeight)));
                        GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                        GL11.glVertex3f((float)(this.x + this.width), this.y + currentHeight, 0.0f);
                        GL11.glVertex3f((float)(this.x + this.width), this.y + currentHeight, 0.0f);
                    }
                    GL11.glVertex3f((float)(this.x + this.width), (float)this.y, 0.0f);
                    GL11.glEnd();
                    GlStateManager.shadeModel(7424);
                    GlStateManager.disableBlend();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableTexture2D();
                }
                else {
                    GlStateManager.disableTexture2D();
                    GlStateManager.enableBlend();
                    GlStateManager.disableAlpha();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.shadeModel(7425);
                    GL11.glBegin(2);
                    final Color outlineColor = ClickGui.getInstance().colorSync.getValue() ? new Color(Colors.INSTANCE.getCurrentColorHex()) : new Color(Banzem.colorManager.getColorAsIntFullAlpha());
                    GL11.glColor4f((float)outlineColor.getRed(), (float)outlineColor.getGreen(), (float)outlineColor.getBlue(), (float)outlineColor.getAlpha());
                    GL11.glVertex3f((float)this.x, this.y - 1.5f, 0.0f);
                    GL11.glVertex3f((float)(this.x + this.width), this.y - 1.5f, 0.0f);
                    GL11.glVertex3f((float)(this.x + this.width), this.y + this.height + totalItemHeight, 0.0f);
                    GL11.glVertex3f((float)this.x, this.y + this.height + totalItemHeight, 0.0f);
                    GL11.glEnd();
                    GlStateManager.shadeModel(7424);
                    GlStateManager.disableBlend();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableTexture2D();
                }
            }
        }
        Banzem.textManager.drawStringWithShadow(this.getName(), this.x + 3.0f, this.y - 4.0f - PhobosGui.getClickGui().getTextOffset(), -1);
        if (this.open) {
            float y = this.getY() + this.getHeight() - 3.0f;
            for (final Item item2 : this.getItems()) {
                if (item2.isHidden()) {
                    continue;
                }
                item2.setLocation(this.x + 2.0f, y);
                item2.setWidth(this.getWidth() - 4);
                item2.drawScreen(mouseX, mouseY, partialTicks);
                y += item2.getHeight() + 1.5f;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            PhobosGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
                return;
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            Util.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_NOTE_HARP, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }
    
    public void addButton(final Button button) {
        this.items.add(button);
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public final ArrayList<Item> getItems() {
        return this.items;
    }
    
    private boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }
    
    private float getTotalItemHeight() {
        float height = 0.0f;
        for (final Item item : this.getItems()) {
            height += item.getHeight() + 1.5f;
        }
        return height;
    }
}
