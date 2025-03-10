/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.mohalk.banzem.features.modules.misc;

import me.mohalk.banzem.features.command.Command;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.features.setting.Setting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawn
extends Module {
    public Setting<Boolean> antiDeathScreen = this.register(new Setting<Boolean>("AntiDeathScreen", true));
    public Setting<Boolean> deathCoords = this.register(new Setting<Boolean>("DeathCoords", false));
    public Setting<Boolean> respawn = this.register(new Setting<Boolean>("Respawn", true));

    public AutoRespawn() {
        super("AutoRespawn", "Respawns you when you die.", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (this.deathCoords.getValue().booleanValue() && event.getGui() instanceof GuiGameOver) {
                Command.sendMessage(String.format("You died at x %d y %d z %d", (int)AutoRespawn.mc.field_71439_g.field_70165_t, (int)AutoRespawn.mc.field_71439_g.field_70163_u, (int)AutoRespawn.mc.field_71439_g.field_70161_v));
            }
            if (this.respawn.getValue() != false && AutoRespawn.mc.field_71439_g.func_110143_aJ() <= 0.0f || this.antiDeathScreen.getValue().booleanValue() && AutoRespawn.mc.field_71439_g.func_110143_aJ() > 0.0f) {
                event.setCanceled(true);
                AutoRespawn.mc.field_71439_g.func_71004_bE();
            }
        }
    }
}

