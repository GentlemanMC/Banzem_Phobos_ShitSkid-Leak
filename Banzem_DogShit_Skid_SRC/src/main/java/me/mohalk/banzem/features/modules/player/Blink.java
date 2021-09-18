//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayer;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import me.mohalk.banzem.features.Feature;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.mohalk.banzem.features.setting.Setting;
import net.minecraft.network.Packet;
import java.util.Queue;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.modules.Module;

public class Blink extends Module
{
    private static Blink INSTANCE;
    private final Timer timer;
    private final Queue<Packet<?>> packets;
    public Setting<Boolean> cPacketPlayer;
    public Setting<Mode> autoOff;
    public Setting<Integer> timeLimit;
    public Setting<Integer> packetLimit;
    public Setting<Float> distance;
    private EntityOtherPlayerMP entity;
    private int packetsCanceled;
    private BlockPos startPos;
    
    public Blink() {
        super("Blink", "Fakelag.", Category.PLAYER, true, false, false);
        this.timer = new Timer();
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.cPacketPlayer = (Setting<Boolean>)this.register(new Setting("CPacketPlayer", true));
        this.autoOff = (Setting<Mode>)this.register(new Setting("AutoOff", Mode.MANUAL));
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Time", 20, 1, 500, v -> this.autoOff.getValue() == Mode.TIME));
        this.packetLimit = (Setting<Integer>)this.register(new Setting("Packets", 20, 1, 500, v -> this.autoOff.getValue() == Mode.PACKETS));
        this.distance = (Setting<Float>)this.register(new Setting("Distance", 10.0f, 1.0f, 100.0f, v -> this.autoOff.getValue() == Mode.DISTANCE));
        this.setInstance();
    }
    
    public static Blink getInstance() {
        if (Blink.INSTANCE == null) {
            Blink.INSTANCE = new Blink();
        }
        return Blink.INSTANCE;
    }
    
    private void setInstance() {
        Blink.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck()) {
            (this.entity = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.session.getProfile())).copyLocationAndAnglesFrom((Entity)Blink.mc.player);
            this.entity.rotationYaw = Blink.mc.player.rotationYaw;
            this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Blink.mc.player.inventory);
            Blink.mc.world.addEntityToWorld(6942069, (Entity)this.entity);
            this.startPos = Blink.mc.player.getPosition();
        }
        else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
    }
    
    @Override
    public void onUpdate() {
        if (Feature.nullCheck() || (this.autoOff.getValue() == Mode.TIME && this.timer.passedS(this.timeLimit.getValue())) || (this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && Blink.mc.player.getDistanceSq(this.startPos) >= MathUtil.square(this.distance.getValue())) || (this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= this.packetLimit.getValue())) {
            this.disable();
        }
    }
    
    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getStage() == 0 && Blink.mc.world != null && !Blink.mc.isSingleplayer()) {
            final Object packet = event.getPacket();
            if (this.cPacketPlayer.getValue() && packet instanceof CPacketPlayer) {
                event.setCanceled(true);
                this.packets.add((Packet<?>)packet);
                ++this.packetsCanceled;
            }
            if (!this.cPacketPlayer.getValue()) {
                if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
                    return;
                }
                this.packets.add((Packet<?>)packet);
                event.setCanceled(true);
                ++this.packetsCanceled;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            Blink.mc.world.removeEntity((Entity)this.entity);
            while (!this.packets.isEmpty()) {
                Blink.mc.player.connection.sendPacket((Packet)this.packets.poll());
            }
        }
        this.startPos = null;
    }
    
    static {
        Blink.INSTANCE = new Blink();
    }
    
    public enum Mode
    {
        MANUAL, 
        TIME, 
        DISTANCE, 
        PACKETS;
    }
}
