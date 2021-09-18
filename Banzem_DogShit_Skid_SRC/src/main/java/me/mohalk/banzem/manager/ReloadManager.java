//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.manager;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.mohalk.banzem.Banzem;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.features.command.Command;
import net.minecraftforge.common.MinecraftForge;
import me.mohalk.banzem.features.Feature;

public class ReloadManager extends Feature
{
    public String prefix;
    
    public void init(final String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register((Object)this);
        if (!Feature.fullNullCheck()) {
            Command.sendMessage("§cBanzem has been unloaded. Type " + prefix + "reload to reload.");
        }
    }
    
    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = event.getPacket()).getMessage().startsWith(this.prefix) && packet.getMessage().contains("reload")) {
            Banzem.load();
            event.setCanceled(true);
        }
    }
}
