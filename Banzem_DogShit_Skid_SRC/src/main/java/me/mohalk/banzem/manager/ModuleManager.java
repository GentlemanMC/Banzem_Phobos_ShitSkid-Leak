//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.manager;

import me.mohalk.banzem.features.gui.PhobosGui;
import org.lwjgl.input.Keyboard;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import me.mohalk.banzem.event.events.Render3DEvent;
import me.mohalk.banzem.event.events.Render2DEvent;
import java.util.function.Consumer;
import net.minecraftforge.common.MinecraftForge;
import java.util.Arrays;
import java.util.Iterator;
import me.mohalk.banzem.features.modules.client.RPC;
import me.mohalk.banzem.features.modules.client.CSGOWatermark;
import me.mohalk.banzem.features.modules.client.Media;
import me.mohalk.banzem.features.modules.client.Colors;
import me.mohalk.banzem.features.modules.player.Yaw;
import me.mohalk.banzem.features.modules.client.Components;
import me.mohalk.banzem.features.modules.client.Managers;
import me.mohalk.banzem.features.modules.client.ClickGui;
import me.mohalk.banzem.features.modules.client.FontMod;
import me.mohalk.banzem.features.modules.misc.ToolTips;
import me.mohalk.banzem.features.modules.client.HUD;
import me.mohalk.banzem.features.modules.client.Notifications;
import me.mohalk.banzem.features.modules.render.CrystalChanger;
import me.mohalk.banzem.features.modules.render.Nametags;
import me.mohalk.banzem.features.modules.render.XRay;
import me.mohalk.banzem.features.modules.render.LogoutSpots;
import me.mohalk.banzem.features.modules.render.Trajectories;
import me.mohalk.banzem.features.modules.render.BlockHighlight;
import me.mohalk.banzem.features.modules.render.PenisESP;
import me.mohalk.banzem.features.modules.render.HoleESP;
import me.mohalk.banzem.features.modules.render.ESP;
import me.mohalk.banzem.features.modules.render.Chams;
import me.mohalk.banzem.features.modules.render.BurrowESP;
import me.mohalk.banzem.features.modules.render.ViewModel;
import me.mohalk.banzem.features.modules.render.NoRender;
import me.mohalk.banzem.features.modules.misc.MCP;
import me.mohalk.banzem.features.modules.player.Replenish;
import me.mohalk.banzem.features.modules.player.Swing;
import me.mohalk.banzem.features.modules.player.NoEntityTrace;
import me.mohalk.banzem.features.modules.player.KeyEXP;
import me.mohalk.banzem.features.modules.player.XCarry;
import me.mohalk.banzem.features.modules.player.MultiTask;
import me.mohalk.banzem.features.modules.player.Speedmine;
import me.mohalk.banzem.features.modules.player.Freecam;
import me.mohalk.banzem.features.modules.player.FastPlace;
import me.mohalk.banzem.features.modules.movement.NoSlowDown;
import me.mohalk.banzem.features.modules.movement.ElytraFlight;
import me.mohalk.banzem.features.modules.movement.AntiVoid;
import me.mohalk.banzem.features.modules.movement.Anchor;
import me.mohalk.banzem.features.modules.movement.Step;
import me.mohalk.banzem.features.modules.movement.Speed;
import me.mohalk.banzem.features.modules.movement.BlockLag;
import me.mohalk.banzem.features.modules.movement.BoatFly;
import me.mohalk.banzem.features.modules.movement.Scaffold;
import me.mohalk.banzem.features.modules.client.ServerModule;
import me.mohalk.banzem.features.modules.movement.Velocity;
import me.mohalk.banzem.features.modules.movement.Strafe;
import me.mohalk.banzem.features.modules.movement.ReverseStep;
import me.mohalk.banzem.features.modules.misc.AutoGG;
import me.mohalk.banzem.features.modules.misc.PingSpoof;
import me.mohalk.banzem.features.modules.misc.NoSoundLag;
import me.mohalk.banzem.features.modules.misc.NoHandShake;
import me.mohalk.banzem.features.modules.misc.Spammer;
import me.mohalk.banzem.features.modules.misc.MCF;
import me.mohalk.banzem.features.modules.misc.FakePlayer;
import me.mohalk.banzem.features.modules.misc.NoRotate;
import me.mohalk.banzem.features.modules.misc.AutoRespawn;
import me.mohalk.banzem.features.modules.misc.ChatModifier;
import me.mohalk.banzem.features.modules.combat.GodModule;
import me.mohalk.banzem.features.modules.combat.AutoWeb;
import me.mohalk.banzem.features.modules.combat.Selftrap;
import me.mohalk.banzem.features.modules.combat.HoleFiller;
import me.mohalk.banzem.features.modules.combat.AutoCrystalTest;
import me.mohalk.banzem.features.modules.combat.Flatten;
import me.mohalk.banzem.features.modules.combat.Quiver;
import me.mohalk.banzem.features.modules.combat.Killaura;
import me.mohalk.banzem.features.modules.combat.Criticals;
import me.mohalk.banzem.features.modules.combat.AutoCrystal;
import me.mohalk.banzem.features.modules.combat.AutoArmor;
import me.mohalk.banzem.features.modules.combat.AutoTrap;
import me.mohalk.banzem.features.modules.combat.Surround;
import me.mohalk.banzem.features.modules.combat.Offhand;
import me.mohalk.banzem.features.modules.module.Display;
import me.mohalk.banzem.features.modules.combat.OffhandRewrite;
import me.mohalk.banzem.features.modules.module.FriendSettings;
import me.mohalk.banzem.features.modules.module.ModuleTools;
import me.mohalk.banzem.features.modules.player.Blink;
import java.util.HashMap;
import java.awt.Color;
import java.util.Map;
import java.util.List;
import me.mohalk.banzem.features.modules.Module;
import java.util.ArrayList;
import me.mohalk.banzem.features.Feature;

public class ModuleManager extends Feature
{
    public ArrayList<Module> modules;
    public List<Module> sortedModules;
    public List<Module> alphabeticallySortedModules;
    public Map<Module, Color> moduleColorMap;
    
    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.sortedModules = new ArrayList<Module>();
        this.alphabeticallySortedModules = new ArrayList<Module>();
        this.moduleColorMap = new HashMap<Module, Color>();
    }
    
    public void init() {
        this.modules.add(new Blink());
        this.modules.add(new ModuleTools());
        this.modules.add(new FriendSettings());
        this.modules.add(new OffhandRewrite());
        this.modules.add(new Display());
        this.modules.add(new Offhand());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoArmor());
        this.modules.add(new AutoCrystal());
        this.modules.add(new Criticals());
        this.modules.add(new Killaura());
        this.modules.add(new Quiver());
        this.modules.add(new Flatten());
        this.modules.add(new AutoCrystalTest());
        this.modules.add(new HoleFiller());
        this.modules.add(new Selftrap());
        this.modules.add(new AutoWeb());
        this.modules.add(new GodModule());
        this.modules.add(new ChatModifier());
        this.modules.add(new AutoRespawn());
        this.modules.add(new NoRotate());
        this.modules.add(new FakePlayer());
        this.modules.add(new MCF());
        this.modules.add(new Spammer());
        this.modules.add(new NoHandShake());
        this.modules.add(new NoSoundLag());
        this.modules.add(new PingSpoof());
        this.modules.add(new AutoGG());
        this.modules.add(new ReverseStep());
        this.modules.add(new Strafe());
        this.modules.add(new Velocity());
        this.modules.add(new ServerModule());
        this.modules.add(new Scaffold());
        this.modules.add(new BoatFly());
        this.modules.add(new BlockLag());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new Anchor());
        this.modules.add(new AntiVoid());
        this.modules.add(new ElytraFlight());
        this.modules.add(new NoSlowDown());
        this.modules.add(new FastPlace());
        this.modules.add(new Freecam());
        this.modules.add(new Speedmine());
        this.modules.add(new MultiTask());
        this.modules.add(new XCarry());
        this.modules.add(new KeyEXP());
        this.modules.add(new NoEntityTrace());
        this.modules.add(new Swing());
        this.modules.add(new Replenish());
        this.modules.add(new MCP());
        this.modules.add(new NoRender());
        this.modules.add(new ViewModel());
        this.modules.add(new BurrowESP());
        this.modules.add(new Chams());
        this.modules.add(new ESP());
        this.modules.add(new HoleESP());
        this.modules.add(new PenisESP());
        this.modules.add(new BlockHighlight());
        this.modules.add(new Trajectories());
        this.modules.add(new LogoutSpots());
        this.modules.add(new XRay());
        this.modules.add(new Nametags());
        this.modules.add(new CrystalChanger());
        this.modules.add(new Notifications());
        this.modules.add(new HUD());
        this.modules.add(new ToolTips());
        this.modules.add(new FontMod());
        this.modules.add(new ClickGui());
        this.modules.add(new Managers());
        this.modules.add(new Components());
        this.modules.add(new Yaw());
        this.modules.add(new Colors());
        this.modules.add(new Media());
        this.modules.add(new CSGOWatermark());
        this.modules.add(new RPC());
        this.moduleColorMap.put(this.getModuleByClass(AutoCrystal.class), new Color(255, 15, 43));
        this.moduleColorMap.put(this.getModuleByClass(AutoTrap.class), new Color(193, 49, 244));
        this.moduleColorMap.put(this.getModuleByClass(Criticals.class), new Color(204, 151, 184));
        this.moduleColorMap.put(this.getModuleByClass(HoleFiller.class), new Color(166, 55, 110));
        this.moduleColorMap.put(this.getModuleByClass(Killaura.class), new Color(255, 37, 0));
        this.moduleColorMap.put(this.getModuleByClass(Offhand.class), new Color(185, 212, 144));
        this.moduleColorMap.put(this.getModuleByClass(Selftrap.class), new Color(22, 127, 145));
        this.moduleColorMap.put(this.getModuleByClass(Surround.class), new Color(100, 0, 150));
        this.moduleColorMap.put(this.getModuleByClass(AutoWeb.class), new Color(11, 161, 121));
        this.moduleColorMap.put(this.getModuleByClass(AutoGG.class), new Color(240, 49, 110));
        this.moduleColorMap.put(this.getModuleByClass(ChatModifier.class), new Color(255, 59, 216));
        this.moduleColorMap.put(this.getModuleByClass(MCF.class), new Color(17, 85, 255));
        this.moduleColorMap.put(this.getModuleByClass(NoRotate.class), new Color(69, 81, 223));
        this.moduleColorMap.put(this.getModuleByClass(RPC.class), new Color(0, 64, 255));
        this.moduleColorMap.put(this.getModuleByClass(Spammer.class), new Color(140, 87, 166));
        this.moduleColorMap.put(this.getModuleByClass(ToolTips.class), new Color(209, 125, 156));
        this.moduleColorMap.put(this.getModuleByClass(BlockHighlight.class), new Color(103, 182, 224));
        this.moduleColorMap.put(this.getModuleByClass(Chams.class), new Color(34, 152, 34));
        this.moduleColorMap.put(this.getModuleByClass(ESP.class), new Color(255, 27, 155));
        this.moduleColorMap.put(this.getModuleByClass(HoleESP.class), new Color(95, 83, 130));
        this.moduleColorMap.put(this.getModuleByClass(LogoutSpots.class), new Color(2, 135, 134));
        this.moduleColorMap.put(this.getModuleByClass(Nametags.class), new Color(98, 82, 223));
        this.moduleColorMap.put(this.getModuleByClass(NoRender.class), new Color(255, 164, 107));
        this.moduleColorMap.put(this.getModuleByClass(ViewModel.class), new Color(145, 223, 187));
        this.moduleColorMap.put(this.getModuleByClass(Trajectories.class), new Color(98, 18, 223));
        this.moduleColorMap.put(this.getModuleByClass(XRay.class), new Color(217, 118, 37));
        this.moduleColorMap.put(this.getModuleByClass(ElytraFlight.class), new Color(55, 161, 201));
        this.moduleColorMap.put(this.getModuleByClass(NoSlowDown.class), new Color(61, 204, 78));
        this.moduleColorMap.put(this.getModuleByClass(Speed.class), new Color(55, 161, 196));
        this.moduleColorMap.put(this.getModuleByClass(AntiVoid.class), new Color(86, 53, 98));
        this.moduleColorMap.put(this.getModuleByClass(Step.class), new Color(144, 212, 203));
        this.moduleColorMap.put(this.getModuleByClass(Strafe.class), new Color(0, 204, 255));
        this.moduleColorMap.put(this.getModuleByClass(Velocity.class), new Color(115, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass(ReverseStep.class), new Color(1, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass(FakePlayer.class), new Color(37, 192, 170));
        this.moduleColorMap.put(this.getModuleByClass(FastPlace.class), new Color(217, 118, 37));
        this.moduleColorMap.put(this.getModuleByClass(Freecam.class), new Color(206, 232, 128));
        this.moduleColorMap.put(this.getModuleByClass(MCP.class), new Color(153, 68, 170));
        this.moduleColorMap.put(this.getModuleByClass(MultiTask.class), new Color(17, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass(Replenish.class), new Color(153, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass(Speedmine.class), new Color(152, 166, 113));
        this.moduleColorMap.put(this.getModuleByClass(XCarry.class), new Color(254, 161, 51));
        this.moduleColorMap.put(this.getModuleByClass(ClickGui.class), new Color(26, 81, 135));
        this.moduleColorMap.put(this.getModuleByClass(Colors.class), new Color(135, 133, 26));
        this.moduleColorMap.put(this.getModuleByClass(Components.class), new Color(135, 26, 26));
        this.moduleColorMap.put(this.getModuleByClass(FontMod.class), new Color(135, 26, 88));
        this.moduleColorMap.put(this.getModuleByClass(HUD.class), new Color(110, 26, 135));
        this.moduleColorMap.put(this.getModuleByClass(Managers.class), new Color(26, 90, 135));
        this.moduleColorMap.put(this.getModuleByClass(Notifications.class), new Color(170, 153, 255));
        this.moduleColorMap.put(this.getModuleByClass(Media.class), new Color(138, 45, 13));
        for (final Module module : this.modules) {
            module.animation.start();
        }
    }
    
    public Module getModuleByName(final String name) {
        for (final Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : this.modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
    
    public void enableModule(final Class clazz) {
        final Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).enable();
        }
    }

    public void disableModule(final Class clazz) {
        final Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class clazz) {
        final Object module = this.getModuleByClass(clazz);
        return module != null && ((Module)module).isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : this.modules) {
            if (!module.isEnabled() && !module.isSliding()) {
                continue;
            }
            enabledModules.add(module);
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        final ArrayList<Module> list = null;
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                list.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }
    
    public void sortModules(final boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(Module::getDisplayName)).collect(Collectors.toList());
    }
    
    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof PhobosGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    public List<Module> getAnimationModules(final Module.Category category) {
        final ArrayList<Module> animationModules = new ArrayList<Module>();
        for (final Module module : this.getEnabledModules()) {
            if (module.getCategory() == category && !module.isDisabled() && module.isSliding()) {
                if (!module.isDrawn()) {
                    continue;
                }
                animationModules.add(module);
            }
        }
        return animationModules;
    }
}
