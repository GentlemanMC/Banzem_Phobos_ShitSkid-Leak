//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.command.commands;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import me.mohalk.banzem.util.Util;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import me.mohalk.banzem.features.command.Command;

public class ReloadSoundCommand extends Command
{
    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        try {
            final SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue((Class)SoundHandler.class, (Object)Util.mc.getSoundHandler(), new String[] { "sndManager", "sndManager" });
            sndManager.reloadSoundSystem();
            Command.sendMessage("§aReloaded Sound System.");
        }
        catch (Exception e) {
            System.out.println("Could not restart sound manager: " + e.toString());
            e.printStackTrace();
            Command.sendMessage("§cCouldnt Reload Sound System!");
        }
    }
}
