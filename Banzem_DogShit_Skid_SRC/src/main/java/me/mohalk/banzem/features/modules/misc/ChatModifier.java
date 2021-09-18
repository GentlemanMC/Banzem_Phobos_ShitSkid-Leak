//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.misc;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.Vec3i;
import me.mohalk.banzem.features.modules.client.Managers;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.features.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import me.mohalk.banzem.util.TextUtil;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.modules.Module;

public class ChatModifier extends Module
{
    private static ChatModifier INSTANCE;
    private final Timer timer;
    public Setting<Suffix> suffix;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public Setting<Boolean> autoQMain;
    public Setting<Boolean> qNotification;
    public Setting<Integer> qDelay;
    public Setting<TextUtil.Color> timeStamps;
    public Setting<Boolean> rainbowTimeStamps;
    public Setting<TextUtil.Color> bracket;
    public Setting<Boolean> space;
    public Setting<Boolean> all;
    public Setting<Boolean> shrug;
    
    public ChatModifier() {
        super("ChatModifier", "Modifies your chat", Category.MISC, true, false, false);
        this.timer = new Timer();
        this.suffix = (Setting<Suffix>)this.register(new Setting("Suffix", Suffix.NONE, "Your Suffix."));
        this.clean = (Setting<Boolean>)this.register(new Setting("CleanChat", false, "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("Infinite", false, "Makes your chat infinite."));
        this.autoQMain = (Setting<Boolean>)this.register(new Setting("AutoQMain", false, "Spams AutoQMain"));
        this.qNotification = (Setting<Boolean>)this.register(new Setting("QNotification", false, v -> this.autoQMain.getValue()));
        this.qDelay = (Setting<Integer>)this.register(new Setting("QDelay", 9, 1, 90, v -> this.autoQMain.getValue()));
        this.timeStamps = (Setting<TextUtil.Color>)this.register(new Setting("Time", TextUtil.Color.NONE));
        this.rainbowTimeStamps = (Setting<Boolean>)this.register(new Setting("RainbowTimeStamps", false, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.bracket = (Setting<TextUtil.Color>)this.register(new Setting("Bracket", TextUtil.Color.WHITE, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.space = (Setting<Boolean>)this.register(new Setting("Space", true, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.all = (Setting<Boolean>)this.register(new Setting("All", false, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.shrug = (Setting<Boolean>)this.register(new Setting("Shrug", false));
        this.setInstance();
    }
    
    public static ChatModifier getInstance() {
        if (ChatModifier.INSTANCE == null) {
            ChatModifier.INSTANCE = new ChatModifier();
        }
        return ChatModifier.INSTANCE;
    }
    
    private void setInstance() {
        ChatModifier.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.shrug.getValue()) {
            ChatModifier.mc.player.sendChatMessage(TextUtil.shrug);
            this.shrug.setValue(false);
        }
        if (this.autoQMain.getValue()) {
            if (!this.shouldSendMessage((EntityPlayer)ChatModifier.mc.player)) {
                return;
            }
            if (this.qNotification.getValue()) {
                Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
            }
            ChatModifier.mc.player.sendChatMessage("/queue main");
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/") || s.startsWith("!")) {
                return;
            }
            switch (this.suffix.getValue()) {
                case Banzem: {
                    s += " \u23d0 Banzem";
                    break;
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.message = s;
        }
    }
    
    @SubscribeEvent
    public void onChatPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() != 0 || event.getPacket() instanceof SPacketChat) {}
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!((SPacketChat) event.getPacket()).isSystem()) {
                return;
            }
            final String originalMessage = ((SPacketChat) event.getPacket()).chatComponent.getFormattedText();
            final String message = this.getTimeString(originalMessage) + originalMessage;
            ((SPacketChat) event.getPacket()).chatComponent = new TextComponentString(message);
        }
    }
    
    public String getTimeString(final String message) {
        final String date = new SimpleDateFormat("k:mm").format(new Date());
        if (this.rainbowTimeStamps.getValue()) {
            final String timeString = "<" + date + ">" + (this.space.getValue() ? " " : "");
            final StringBuilder builder = new StringBuilder(timeString);
            builder.insert(0, "§+");
            if (!message.contains(Managers.getInstance().getRainbowCommandMessage())) {
                builder.append("§r");
            }
            return builder.toString();
        }
        return ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString("<", this.bracket.getValue())) + TextUtil.coloredString(date, this.timeStamps.getValue()) + ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString(">", this.bracket.getValue())) + (this.space.getValue() ? " " : "") + "§r";
    }
    
    private boolean shouldSendMessage(final EntityPlayer player) {
        return player.dimension == 1 && this.timer.passedS(this.qDelay.getValue()) && player.getPosition().equals((Object)new Vec3i(0, 240, 0));
    }
    
    static {
        ChatModifier.INSTANCE = new ChatModifier();
    }
    
    public enum Suffix
    {
        NONE, 
        Banzem;
    }
}
