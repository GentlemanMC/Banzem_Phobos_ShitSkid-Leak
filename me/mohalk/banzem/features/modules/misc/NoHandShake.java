/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.internal.FMLProxyPacket
 */
package me.mohalk.banzem.features.modules.misc;

import io.netty.buffer.Unpooled;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.features.modules.Module;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class NoHandShake
extends Module {
    public NoHandShake() {
        super("NoHandshake", "Doesn't send your mod list to the server.", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketCustomPayload packet;
        if (event.getPacket() instanceof FMLProxyPacket && !mc.func_71356_B()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && (packet = (CPacketCustomPayload)event.getPacket()).func_149559_c().equals("MC|Brand")) {
            packet.field_149561_c = new PacketBuffer(Unpooled.buffer()).func_180714_a("vanilla");
        }
    }
}

