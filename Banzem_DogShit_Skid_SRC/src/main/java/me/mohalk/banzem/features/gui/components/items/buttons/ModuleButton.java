//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.gui.components.items.buttons;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import me.mohalk.banzem.util.Util;
import me.mohalk.banzem.features.gui.PhobosGui;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.modules.client.ClickGui;
import java.util.Iterator;
import me.mohalk.banzem.features.setting.Bind;
import me.mohalk.banzem.features.setting.Setting;
import java.util.ArrayList;
import me.mohalk.banzem.features.gui.components.items.Item;
import java.util.List;
import me.mohalk.banzem.features.modules.Module;

public class ModuleButton extends Button
{
    private final Module module;
    private List<Item> items;
    private boolean subOpen;
    
    public ModuleButton(final Module module) {
        super(module.getName());
        this.items = new ArrayList<Item>();
        this.module = module;
        this.initSettings();
    }
    
    public void initSettings() {
        final ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (final Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(setting));
                }
                if (setting.getValue() instanceof String || setting.getValue() instanceof Character) {
                    newItems.add(new StringButton(setting));
                }
                if (setting.isNumberSetting()) {
                    if (setting.hasRestriction()) {
                        newItems.add(new Slider(setting));
                        continue;
                    }
                    newItems.add(new UnlimitedSlider(setting));
                }
                if (!setting.isEnumSetting()) {
                    continue;
                }
                newItems.add(new EnumButton(setting));
            }
        }
        this.items = newItems;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            final ClickGui gui = Banzem.moduleManager.getModuleByClass(ClickGui.class);
            Banzem.textManager.drawStringWithShadow(((boolean)gui.openCloseChange.getValue()) ? (this.subOpen ? gui.close.getValue() : gui.open.getValue()) : gui.moduleButton.getValue(), this.x - 1.5f + this.width - 7.4f, this.y - 2.0f - PhobosGui.getClickGui().getTextOffset(), -1);
            if (this.subOpen) {
                float height = 1.0f;
                for (final Item item : this.items) {
                    if (!item.isHidden()) {
                        item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                        item.setHeight(15);
                        item.setWidth(this.width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                    }
                    item.update();
                }
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                Util.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_NOTE_HARP, 1.0f));
            }
            if (this.subOpen) {
                for (final Item item : this.items) {
                    if (item.isHidden()) {
                        continue;
                    }
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }
    
    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    @Override
    public void toggle() {
        this.module.toggle();
    }
    
    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}
