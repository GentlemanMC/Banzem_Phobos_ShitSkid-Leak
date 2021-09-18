//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.misc;

import me.mohalk.banzem.util.MathUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketKeepAlive;
import me.mohalk.banzem.event.events.PacketEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.mohalk.banzem.util.Timer;
import net.minecraft.network.Packet;
import java.util.Queue;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.features.modules.Module;

public class PingSpoof extends Module
{
    private final Setting<Boolean> seconds;
    private final Setting<Integer> delay;
    private final Setting<Integer> secondDelay;
    private final Setting<Boolean> offOnLogout;
    private final Queue<Packet<?>> packets;
    private final Timer timer;
    private boolean receive;
    
    public PingSpoof() {
        super("PingSpoof", "Spoofs your ping!", Category.MISC, true, false, false);
        this.seconds = (Setting<Boolean>)this.register(new Setting("Seconds", false));
        this.delay = (Setting<Integer>)this.register(new Setting("DelayMS", 20, 0, 1000, v -> !this.seconds.getValue()));
        this.secondDelay = (Setting<Integer>)this.register(new Setting("DelayS", 5, 0, 30, v -> this.seconds.getValue()));
        this.offOnLogout = (Setting<Boolean>)this.register(new Setting("Logout", false));
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.timer = new Timer();
        this.receive = true;
    }
    
    @Override
    public void onLoad() {
        if (this.offOnLogout.getValue()) {
            this.disable();
        }
    }
    
    @Override
    public void onLogout() {
        if (this.offOnLogout.getValue()) {
            this.disable();
        }
    }
    
    @Override
    public void onUpdate() {
        this.clearQueue();
    }
    
    @Override
    public void onDisable() {
        this.clearQueue();
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.receive && PingSpoof.mc.player != null && !PingSpoof.mc.isSingleplayer() && PingSpoof.mc.player.isEntityAlive() && event.getStage() == 0 && event.getPacket() instanceof CPacketKeepAlive) {
            this.packets.add(event.getPacket());
            event.setCanceled(true);
        }
    }
    
    public void clearQueue() {
        if (PingSpoof.mc.player != null && !PingSpoof.mc.isSingleplayer() && PingSpoof.mc.player.isEntityAlive() && ((!this.seconds.getValue() && this.timer.passedMs(this.delay.getValue())) || (this.seconds.getValue() && this.timer.passedS(this.secondDelay.getValue())))) {
            final double limit = MathUtil.getIncremental(Math.random() * 10.0, 1.0);
            this.receive = false;
            for (int i = 0; i < limit; ++i) {
                final Packet<?> packet = this.packets.poll();
                if (packet != null) {
                    PingSpoof.mc.player.connection.sendPacket((Packet)packet);
                }
            }
            this.timer.reset();
            this.receive = true;
        }
    }
}
