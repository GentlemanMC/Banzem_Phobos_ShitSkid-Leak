/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.ChatAllowedCharacters
 *  net.minecraft.util.SoundEvent
 *  org.lwjgl.input.Keyboard
 */
package me.mohalk.banzem.features.gui.components.items.buttons;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.gui.PhobosGui;
import me.mohalk.banzem.features.gui.components.items.buttons.Button;
import me.mohalk.banzem.features.modules.client.ClickGui;
import me.mohalk.banzem.features.modules.client.HUD;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.util.ColorUtil;
import me.mohalk.banzem.util.MathUtil;
import me.mohalk.banzem.util.RenderUtil;
import me.mohalk.banzem.util.Util;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.SoundEvent;
import org.lwjgl.input.Keyboard;

public class StringButton
extends Button {
    public boolean isListening;
    private final Setting setting;
    private CurrentString currentString = new CurrentString("");

    public StringButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.getInstance().rainbowRolling.getValue().booleanValue()) {
            int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), Banzem.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            int color1 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), Banzem.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, (float)this.width + 7.4f, (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)) : color) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515), this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)) : color1) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        } else {
            RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? Banzem.colorManager.getColorWithAlpha(Banzem.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : Banzem.colorManager.getColorWithAlpha(Banzem.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        }
        if (this.isListening) {
            Banzem.textManager.drawStringWithShadow(this.currentString.getString() + Banzem.textManager.getIdleSign(), this.x + 2.3f, this.y - 1.7f - (float)PhobosGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            Banzem.textManager.drawStringWithShadow((this.setting.shouldRenderName() ? this.setting.getName() + " \u00a77" : "") + this.setting.getValue(), this.x + 2.3f, this.y - 1.7f - (float)PhobosGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Util.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a((SoundEvent)SoundEvents.field_187682_dG, (float)1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            if (keyCode == 1) {
                return;
            }
            if (keyCode == 28) {
                this.enterString();
            } else if (keyCode == 14) {
                this.setString(StringButton.removeLastChar(this.currentString.getString()));
            } else if (keyCode == 47 && (Keyboard.isKeyDown((int)157) || Keyboard.isKeyDown((int)29))) {
                try {
                    this.setString(this.currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ChatAllowedCharacters.func_71566_a((char)typedChar)) {
                this.setString(this.currentString.getString() + typedChar);
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        } else {
            this.setting.setValue(this.currentString.getString());
        }
        this.setString("");
        super.onMouseClick();
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static class CurrentString {
        private final String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}

