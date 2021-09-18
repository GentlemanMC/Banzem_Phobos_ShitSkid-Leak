//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.mohalk.banzem.util.MathUtil;
import java.util.Random;
import me.mohalk.banzem.manager.FileManager;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.Banzem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.mohalk.banzem.event.events.DeathEvent;
import java.util.Iterator;
import me.mohalk.banzem.features.modules.combat.AutoCrystal;
import me.mohalk.banzem.features.command.Command;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import me.mohalk.banzem.util.Timer;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class AutoGG extends Module
{
    private static final String path = "banzem/autogg.txt";
    private final Setting<Boolean> onOwnDeath;
    private final Setting<Boolean> greentext;
    private final Setting<Boolean> loadFiles;
    private final Setting<Integer> targetResetTimer;
    private final Setting<Integer> delay;
    private final Setting<Boolean> test;
    public Map<EntityPlayer, Integer> targets;
    public List<String> messages;
    public EntityPlayer cauraTarget;
    private final Timer timer;
    private final Timer cooldownTimer;
    private boolean cooldown;
    
    public AutoGG() {
        super("AutoGG", "Automatically GGs", Category.MISC, true, false, false);
        this.onOwnDeath = (Setting<Boolean>)this.register(new Setting("OwnDeath", false));
        this.greentext = (Setting<Boolean>)this.register(new Setting("Greentext", false));
        this.loadFiles = (Setting<Boolean>)this.register(new Setting("LoadFiles", false));
        this.targetResetTimer = (Setting<Integer>)this.register(new Setting("Reset", 30, 0, 90));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", 10, 0, 30));
        this.test = (Setting<Boolean>)this.register(new Setting("Test", false));
        this.targets = new ConcurrentHashMap<EntityPlayer, Integer>();
        this.messages = new ArrayList<String>();
        this.timer = new Timer();
        this.cooldownTimer = new Timer();
        final File file = new File("banzem/autogg.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.loadMessages();
        this.timer.reset();
        this.cooldownTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (this.loadFiles.getValue()) {
            this.loadMessages();
            Command.sendMessage("<AutoGG> Loaded messages.");
            this.loadFiles.setValue(false);
        }
        if (AutoCrystal.target != null && this.cauraTarget != AutoCrystal.target) {
            this.cauraTarget = AutoCrystal.target;
        }
        if (this.test.getValue()) {
            this.announceDeath((EntityPlayer)AutoGG.mc.player);
            this.test.setValue(false);
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.passedS(this.delay.getValue()) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        if (AutoCrystal.target != null) {
            this.targets.put(AutoCrystal.target, (int)(this.timer.getPassedTimeMs() / 1000L));
        }
        this.targets.replaceAll((p, v) -> Integer.valueOf((int)(this.timer.getPassedTimeMs() / 1000L)));
        for (final EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get(player) <= this.targetResetTimer.getValue()) {
                continue;
            }
            this.targets.remove(player);
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onEntityDeath(final DeathEvent event) {
        if (this.targets.containsKey(event.player) && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
            this.targets.remove(event.player);
        }
        if (event.player == this.cauraTarget && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
        if (event.player == AutoGG.mc.player && this.onOwnDeath.getValue()) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !Banzem.friendManager.isFriend(event.getEntityPlayer())) {
            this.targets.put((EntityPlayer)event.getTarget(), 0);
        }
    }
    
    @SubscribeEvent
    public void onSendAttackPacket(final PacketEvent.Send event) {
        final CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)AutoGG.mc.world) instanceof EntityPlayer && !Banzem.friendManager.isFriend((EntityPlayer)packet.getEntityFromWorld((World)AutoGG.mc.world))) {
            this.targets.put((EntityPlayer)packet.getEntityFromWorld((World)AutoGG.mc.world), 0);
        }
    }
    
    public void loadMessages() {
        this.messages = FileManager.readTextFileAllLines("banzem/autogg.txt");
    }
    
    public String getRandomMessage() {
        this.loadMessages();
        final Random rand = new Random();
        if (this.messages.size() == 0) {
            return "<player> is a noob hahaha fobus on tope";
        }
        if (this.messages.size() == 1) {
            return this.messages.get(0);
        }
        return this.messages.get(MathUtil.clamp(rand.nextInt(this.messages.size()), 0, this.messages.size() - 1));
    }
    
    public void announceDeath(final EntityPlayer target) {
        AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((this.greentext.getValue() ? ">" : "") + this.getRandomMessage().replaceAll("<player>", target.getDisplayNameString())));
    }
}
