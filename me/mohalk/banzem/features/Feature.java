/*
 * Decompiled with CFR 0.150.
 */
package me.mohalk.banzem.features;

import java.util.ArrayList;
import java.util.List;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.gui.PhobosGui;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.manager.TextManager;
import me.mohalk.banzem.util.Util;

public class Feature
implements Util {
    public List<Setting> settings = new ArrayList<Setting>();
    public TextManager renderer = Banzem.textManager;
    private String name;

    public Feature() {
    }

    public Feature(String name) {
        this.name = name;
    }

    public static boolean nullCheck() {
        return Feature.mc.field_71439_g == null;
    }

    public static boolean fullNullCheck() {
        return Feature.mc.field_71439_g == null || Feature.mc.field_71441_e == null;
    }

    public String getName() {
        return this.name;
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    public boolean isEnabled() {
        if (this instanceof Module) {
            return ((Module)this).isOn();
        }
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public Setting register(Setting setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && Feature.mc.field_71462_r instanceof PhobosGui) {
            PhobosGui.getInstance().updateModule((Module)this);
        }
        return setting;
    }

    public void unregister(Setting settingIn) {
        ArrayList<Setting> removeList = new ArrayList<Setting>();
        for (Setting setting : this.settings) {
            if (!setting.equals(settingIn)) continue;
            removeList.add(setting);
        }
        if (!removeList.isEmpty()) {
            this.settings.removeAll(removeList);
        }
        if (this instanceof Module && Feature.mc.field_71462_r instanceof PhobosGui) {
            PhobosGui.getInstance().updateModule((Module)this);
        }
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }

    public void reset() {
        for (Setting setting : this.settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }

    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }
}

