/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.mohalk.banzem.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.event.events.ClientEvent;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.features.command.Command;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.features.modules.module.ModuleTools;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.manager.FileManager;
import me.mohalk.banzem.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Notifications
extends Module {
    private static final String fileName = "phobos/util/ModuleMessage_List.txt";
    private static final List<String> modules = new ArrayList<String>();
    private static Notifications INSTANCE = new Notifications();
    private final Timer timer = new Timer();
    public Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPops", false));
    public Setting<Boolean> totemNoti = this.register(new Setting<Object>("TotemNoti", Boolean.valueOf(true), v -> this.totemPops.getValue()));
    public Setting<Integer> delay = this.register(new Setting<Object>("Delay", 2000, 0, 5000, v -> this.totemPops.getValue(), "Delays messages."));
    public Setting<Boolean> clearOnLogout = this.register(new Setting<Boolean>("LogoutClear", false));
    public Setting<Boolean> moduleMessage = this.register(new Setting<Boolean>("ModuleMessage", false));
    public Setting<Boolean> list = this.register(new Setting<Object>("List", Boolean.valueOf(false), v -> this.moduleMessage.getValue()));
    public Setting<Boolean> watermark = this.register(new Setting<Object>("Watermark", Boolean.valueOf(true), v -> this.moduleMessage.getValue()));
    public Setting<Boolean> visualRange = this.register(new Setting<Boolean>("VisualRange", false));
    public Setting<Boolean> VisualRangeSound = this.register(new Setting<Boolean>("VisualRangeSound", false));
    public Setting<Boolean> coords = this.register(new Setting<Object>("Coords", Boolean.valueOf(true), v -> this.visualRange.getValue()));
    public Setting<Boolean> leaving = this.register(new Setting<Object>("Leaving", Boolean.valueOf(false), v -> this.visualRange.getValue()));
    public Setting<Boolean> pearls = this.register(new Setting<Boolean>("PearlNotifs", false));
    public Setting<Boolean> crash = this.register(new Setting<Boolean>("Crash", false));
    public Setting<Boolean> popUp = this.register(new Setting<Boolean>("PopUpVisualRange", false));
    public Timer totemAnnounce = new Timer();
    private final Setting<Boolean> readfile = this.register(new Setting<Object>("LoadFile", Boolean.valueOf(false), v -> this.moduleMessage.getValue()));
    private List<EntityPlayer> knownPlayers = new ArrayList<EntityPlayer>();
    private boolean check;

    public Notifications() {
        super("Notifications", "Sends Messages.", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static Notifications getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notifications();
        }
        return INSTANCE;
    }

    public static void displayCrash(Exception e) {
        Command.sendMessage("\u00a7cException caught: " + e.getMessage());
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        this.check = true;
        this.loadFile();
        this.check = false;
    }

    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<EntityPlayer>();
        if (!this.check) {
            this.loadFile();
        }
    }

    @Override
    public void onUpdate() {
        if (this.readfile.getValue().booleanValue()) {
            if (!this.check) {
                Command.sendMessage("Loading File...");
                this.timer.reset();
                this.loadFile();
            }
            this.check = true;
        }
        if (this.check && this.timer.passedMs(750L)) {
            this.readfile.setValue(false);
            this.check = false;
        }
        if (this.visualRange.getValue().booleanValue()) {
            ArrayList tickPlayerList = new ArrayList(Notifications.mc.field_71441_e.field_73010_i);
            if (tickPlayerList.size() > 0) {
                for (EntityPlayer player : tickPlayerList) {
                    if (player.func_70005_c_().equals(Notifications.mc.field_71439_g.func_70005_c_()) || this.knownPlayers.contains((Object)player)) continue;
                    this.knownPlayers.add(player);
                    if (Banzem.friendManager.isFriend(player)) {
                        Command.sendMessage("Player \u00a7a" + player.func_70005_c_() + "\u00a7r entered your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), this.popUp.getValue());
                    } else {
                        Command.sendMessage("Player \u00a7c" + player.func_70005_c_() + "\u00a7r entered your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), this.popUp.getValue());
                    }
                    if (this.VisualRangeSound.getValue().booleanValue()) {
                        Notifications.mc.field_71439_g.func_184185_a(SoundEvents.field_187689_f, 1.0f, 1.0f);
                    }
                    return;
                }
            }
            if (this.knownPlayers.size() > 0) {
                for (EntityPlayer player : this.knownPlayers) {
                    if (tickPlayerList.contains((Object)player)) continue;
                    this.knownPlayers.remove((Object)player);
                    if (this.leaving.getValue().booleanValue()) {
                        if (Banzem.friendManager.isFriend(player)) {
                            Command.sendMessage("Player \u00a7a" + player.func_70005_c_() + "\u00a7r left your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), this.popUp.getValue());
                        } else {
                            Command.sendMessage("Player \u00a7c" + player.func_70005_c_() + "\u00a7r left your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), this.popUp.getValue());
                        }
                    }
                    return;
                }
            }
        }
    }

    public void loadFile() {
        List<String> fileInput = FileManager.readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        modules.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            modules.add(s);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject && this.pearls.getValue().booleanValue()) {
            SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            EntityPlayer player = Notifications.mc.field_71441_e.func_184137_a(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e(), 1.0, false);
            if (player == null) {
                return;
            }
            if (packet.func_149001_c() == 85) {
                Command.sendMessage("\u00a7cPearl thrown by " + player.func_70005_c_() + " at X:" + (int)packet.func_186880_c() + " Y:" + (int)packet.func_186882_d() + " Z:" + (int)packet.func_186881_e());
            }
        }
    }

    public TextComponentString getNotifierOn(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case FUTURE: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.RED + "[Future] " + (Object)ChatFormatting.GRAY + module.getDisplayName() + " toggled " + (Object)ChatFormatting.GREEN + "on" + (Object)ChatFormatting.GRAY + ".");
                    return text;
                }
                case DOTGOD: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_PURPLE + "[" + (Object)ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + (Object)ChatFormatting.DARK_PURPLE + "] " + (Object)ChatFormatting.DARK_AQUA + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + " was " + (Object)ChatFormatting.GREEN + "enabled.");
                    return text;
                }
                case PHOBOS: {
                    TextComponentString text = new TextComponentString(Banzem.commandManager.getClientMessage() + (Object)ChatFormatting.BOLD + module.getDisplayName() + (Object)ChatFormatting.RESET + (Object)ChatFormatting.GREEN + " enabled.");
                    return text;
                }
            }
        }
        TextComponentString text = new TextComponentString(Banzem.commandManager.getClientMessage() + (Object)ChatFormatting.GREEN + module.getDisplayName() + " toggled on.");
        return text;
    }

    public TextComponentString getNotifierOff(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case FUTURE: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.RED + "[Future] " + (Object)ChatFormatting.GRAY + module.getDisplayName() + " toggled " + (Object)ChatFormatting.RED + "off" + (Object)ChatFormatting.GRAY + ".");
                    return text;
                }
                case DOTGOD: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_PURPLE + "[" + (Object)ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + (Object)ChatFormatting.DARK_PURPLE + "] " + (Object)ChatFormatting.DARK_AQUA + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + " was " + (Object)ChatFormatting.RED + "disabled.");
                    return text;
                }
                case PHOBOS: {
                    TextComponentString text = new TextComponentString(Banzem.commandManager.getClientMessage() + (Object)ChatFormatting.BOLD + module.getDisplayName() + (Object)ChatFormatting.RESET + (Object)ChatFormatting.RED + " disabled.");
                    return text;
                }
            }
        }
        TextComponentString text = new TextComponentString(Banzem.commandManager.getClientMessage() + (Object)ChatFormatting.RED + module.getDisplayName() + " toggled off.");
        return text;
    }

    @SubscribeEvent
    public void onToggleModule(ClientEvent event) {
        int moduleNumber;
        Module module;
        if (!this.moduleMessage.getValue().booleanValue()) {
            return;
        }
        if (!(event.getStage() != 0 || (module = (Module)event.getFeature()).equals(this) || !modules.contains(module.getDisplayName()) && this.list.getValue().booleanValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)this.getNotifierOff(module), moduleNumber);
        }
        if (event.getStage() == 1 && (modules.contains((module = (Module)event.getFeature()).getDisplayName()) || !this.list.getValue().booleanValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)this.getNotifierOn(module), moduleNumber);
        }
    }
}

