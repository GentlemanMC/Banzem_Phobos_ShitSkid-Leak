/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.mohalk.banzem.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.event.events.ClientEvent;
import me.mohalk.banzem.event.events.Render2DEvent;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.features.modules.Module;
import me.mohalk.banzem.features.modules.client.Colors;
import me.mohalk.banzem.features.modules.client.Managers;
import me.mohalk.banzem.features.modules.client.ServerModule;
import me.mohalk.banzem.features.modules.misc.ToolTips;
import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.manager.TextManager;
import me.mohalk.banzem.util.ColorUtil;
import me.mohalk.banzem.util.EntityUtil;
import me.mohalk.banzem.util.MathUtil;
import me.mohalk.banzem.util.RenderUtil;
import me.mohalk.banzem.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD
extends Module {
    private static final ItemStack totem = new ItemStack(Items.field_190929_cY);
    private static final ResourceLocation codHitmarker = new ResourceLocation("earthhack", "cod_hitmarker");
    private static final ResourceLocation csgoHitmarker = new ResourceLocation("earthhack", "csgo_hitmarker");
    private static HUD INSTANCE = new HUD();
    private final Setting<Boolean> renderingUp = this.register(new Setting<Boolean>("RenderingUp", Boolean.valueOf(false), "Orientation of the HUD-Elements."));
    private final Setting<WaterMark> watermark = this.register(new Setting<WaterMark>("Logo", WaterMark.NONE, "WaterMark"));
    private final Setting<Boolean> modeVer = this.register(new Setting<Object>("Version", Boolean.valueOf(false), v -> this.watermark.getValue() != WaterMark.NONE));
    private final Setting<Boolean> arrayList = this.register(new Setting<Boolean>("ActiveModules", Boolean.valueOf(false), "Lists the active modules."));
    private final Setting<Boolean> moduleColors = this.register(new Setting<Object>("ModuleColors", Boolean.valueOf(false), v -> this.arrayList.getValue()));
    private final Setting<Boolean> alphabeticalSorting = this.register(new Setting<Object>("AlphabeticalSorting", Boolean.valueOf(false), v -> this.arrayList.getValue()));
    private final Setting<Boolean> serverBrand = this.register(new Setting<Boolean>("ServerBrand", Boolean.valueOf(false), "Brand of the server you are on."));
    private final Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", Boolean.valueOf(false), "Your response time to the server."));
    private final Setting<Boolean> tps = this.register(new Setting<Boolean>("TPS", Boolean.valueOf(false), "Ticks per second of the server."));
    private final Setting<Boolean> fps = this.register(new Setting<Boolean>("FPS", Boolean.valueOf(false), "Your frames per second."));
    private final Setting<Boolean> coords = this.register(new Setting<Boolean>("Coords", Boolean.valueOf(false), "Your current coordinates"));
    private final Setting<Boolean> direction = this.register(new Setting<Boolean>("Direction", Boolean.valueOf(false), "The Direction you are facing."));
    private final Setting<Boolean> speed = this.register(new Setting<Boolean>("Speed", Boolean.valueOf(false), "Your Speed"));
    private final Setting<Boolean> potions = this.register(new Setting<Boolean>("Potions", Boolean.valueOf(false), "Active potion effects"));
    private final Setting<Boolean> altPotionsColors = this.register(new Setting<Object>("AltPotionColors", Boolean.valueOf(false), v -> this.potions.getValue()));
    private final Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", Boolean.valueOf(false), "ArmorHUD"));
    private final Setting<Boolean> durability = this.register(new Setting<Boolean>("Durability", Boolean.valueOf(false), "Durability"));
    private final Setting<Boolean> percent = this.register(new Setting<Object>("Percent", Boolean.valueOf(true), v -> this.armor.getValue()));
    private final Setting<Boolean> totems = this.register(new Setting<Boolean>("Totems", Boolean.valueOf(false), "TotemHUD"));
    private final Setting<Greeter> greeter = this.register(new Setting<Greeter>("Greeter", Greeter.NONE, "Greets you."));
    private final Setting<String> spoofGreeter = this.register(new Setting<Object>("GreeterName", "MohalkWare", v -> this.greeter.getValue() == Greeter.CUSTOM));
    private final Setting<LagNotify> lag = this.register(new Setting<LagNotify>("Lag", LagNotify.GRAY, "Lag Notifier"));
    private final Setting<Boolean> hitMarkers = this.register(new Setting<Boolean>("HitMarkers", false));
    private final Setting<Boolean> grayNess = this.register(new Setting<Boolean>("FutureColour", false));
    private final Timer timer = new Timer();
    private final Timer moduleTimer = new Timer();
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", Boolean.valueOf(false), "Universal colors for hud."));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", Boolean.valueOf(false), "Rainbow hud."));
    public Setting<Integer> factor = this.register(new Setting<Object>("Factor", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(20), v -> this.rainbow.getValue()));
    public Setting<Boolean> rolling = this.register(new Setting<Object>("Rolling", Boolean.valueOf(false), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowSpeed = this.register(new Setting<Object>("RSpeed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowSaturation = this.register(new Setting<Object>("Saturation", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowBrightness = this.register(new Setting<Object>("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Boolean> potionIcons = this.register(new Setting<Boolean>("PotionIcons", Boolean.valueOf(true), "Draws Potion Icons."));
    public Setting<Boolean> shadow = this.register(new Setting<Boolean>("Shadow", Boolean.valueOf(false), "Draws the text with a shadow."));
    public Setting<Integer> animationHorizontalTime = this.register(new Setting<Object>("AnimationHTime", Integer.valueOf(500), Integer.valueOf(1), Integer.valueOf(1000), v -> this.arrayList.getValue()));
    public Setting<Integer> animationVerticalTime = this.register(new Setting<Object>("AnimationVTime", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(500), v -> this.arrayList.getValue()));
    public Setting<Boolean> textRadar = this.register(new Setting<Boolean>("TextRadar", Boolean.valueOf(false), "A TextRadar"));
    public Setting<Boolean> time = this.register(new Setting<Boolean>("Time", Boolean.valueOf(false), "The time"));
    public Setting<Integer> hudRed = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> hudGreen = this.register(new Setting<Object>("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> hudBlue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Boolean> potions1 = this.register(new Setting<Object>("LevelPotions", Boolean.valueOf(false), v -> this.potions.getValue()));
    public Setting<Boolean> MS = this.register(new Setting<Object>("ms", Boolean.valueOf(false), v -> this.ping.getValue()));
    public Map<Module, Float> moduleProgressMap = new HashMap<Module, Float>();
    public Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
    private Map<String, Integer> players = new HashMap<String, Integer>();
    private final Map<Potion, Color> potionColorMap = new HashMap<Potion, Color>();
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT, true, false, false);
        this.setInstance();
        this.potionColorMap.put(MobEffects.field_76424_c, new Color(124, 175, 198));
        this.potionColorMap.put(MobEffects.field_76421_d, new Color(90, 108, 129));
        this.potionColorMap.put(MobEffects.field_76422_e, new Color(217, 192, 67));
        this.potionColorMap.put(MobEffects.field_76419_f, new Color(74, 66, 23));
        this.potionColorMap.put(MobEffects.field_76420_g, new Color(147, 36, 35));
        this.potionColorMap.put(MobEffects.field_76432_h, new Color(67, 10, 9));
        this.potionColorMap.put(MobEffects.field_76433_i, new Color(67, 10, 9));
        this.potionColorMap.put(MobEffects.field_76430_j, new Color(34, 255, 76));
        this.potionColorMap.put(MobEffects.field_76431_k, new Color(85, 29, 74));
        this.potionColorMap.put(MobEffects.field_76428_l, new Color(205, 92, 171));
        this.potionColorMap.put(MobEffects.field_76429_m, new Color(153, 69, 58));
        this.potionColorMap.put(MobEffects.field_76426_n, new Color(228, 154, 58));
        this.potionColorMap.put(MobEffects.field_76427_o, new Color(46, 82, 153));
        this.potionColorMap.put(MobEffects.field_76441_p, new Color(127, 131, 146));
        this.potionColorMap.put(MobEffects.field_76440_q, new Color(31, 31, 35));
        this.potionColorMap.put(MobEffects.field_76439_r, new Color(31, 31, 161));
        this.potionColorMap.put(MobEffects.field_76438_s, new Color(88, 118, 83));
        this.potionColorMap.put(MobEffects.field_76437_t, new Color(72, 77, 72));
        this.potionColorMap.put(MobEffects.field_76436_u, new Color(78, 147, 49));
        this.potionColorMap.put(MobEffects.field_82731_v, new Color(53, 42, 39));
        this.potionColorMap.put(MobEffects.field_180152_w, new Color(248, 125, 35));
        this.potionColorMap.put(MobEffects.field_76444_x, new Color(37, 82, 165));
        this.potionColorMap.put(MobEffects.field_76443_y, new Color(248, 36, 35));
        this.potionColorMap.put(MobEffects.field_188423_x, new Color(148, 160, 97));
        this.potionColorMap.put(MobEffects.field_188424_y, new Color(206, 255, 255));
        this.potionColorMap.put(MobEffects.field_188425_z, new Color(51, 153, 0));
        this.potionColorMap.put(MobEffects.field_189112_A, new Color(192, 164, 77));
    }

    public static HUD getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HUD();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        for (Module module : Banzem.moduleManager.sortedModules) {
            if (!module.isDisabled() || module.arrayListOffset != 0.0f) continue;
            module.sliding = true;
        }
        if (this.timer.passedMs(Managers.getInstance().textRadarUpdates.getValue().intValue())) {
            this.players = this.getTextRadarPlayers();
            this.timer.reset();
        }
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }

    @SubscribeEvent
    public void onModuleToggle(ClientEvent event) {
        block4: {
            block5: {
                if (!(event.getFeature() instanceof Module)) break block4;
                if (event.getStage() != 0) break block5;
                for (float i = 0.0f; i <= (float)this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()); i += (float)this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) / 500.0f) {
                    if (this.moduleTimer.passedMs(1L)) {
                        this.moduleProgressMap.put((Module)event.getFeature(), Float.valueOf((float)this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) - i));
                    }
                    this.timer.reset();
                }
                break block4;
            }
            if (event.getStage() != 1) break block4;
            for (float i = 0.0f; i <= (float)this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()); i += (float)this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) / 500.0f) {
                if (this.moduleTimer.passedMs(1L)) {
                    this.moduleProgressMap.put((Module)event.getFeature(), Float.valueOf((float)this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) - i));
                }
                this.timer.reset();
            }
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int color;
        String fpsText;
        String text3;
        Object text2;
        String text;
        int k;
        int j;
        if (Feature.fullNullCheck()) {
            return;
        }
        int colorSpeed = 101 - this.rainbowSpeed.getValue();
        float hue = this.colorSync.getValue() != false ? Colors.INSTANCE.hue : (float)(System.currentTimeMillis() % (long)(360 * colorSpeed)) / (360.0f * (float)colorSpeed);
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        float tempHue = hue;
        for (int i = 0; i <= height; ++i) {
            if (this.colorSync.getValue().booleanValue()) {
                this.colorMap.put(i, Color.HSBtoRGB(tempHue, (float)Colors.INSTANCE.rainbowSaturation.getValue().intValue() / 255.0f, (float)Colors.INSTANCE.rainbowBrightness.getValue().intValue() / 255.0f));
            } else {
                this.colorMap.put(i, Color.HSBtoRGB(tempHue, (float)this.rainbowSaturation.getValue().intValue() / 255.0f, (float)this.rainbowBrightness.getValue().intValue() / 255.0f));
            }
            tempHue += 1.0f / (float)height * (float)this.factor.getValue().intValue();
        }
        GlStateManager.func_179094_E();
        if (this.rainbow.getValue().booleanValue() && !this.rolling.getValue().booleanValue()) {
            this.color = this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColorHex() : Color.HSBtoRGB(hue, (float)this.rainbowSaturation.getValue().intValue() / 255.0f, (float)this.rainbowBrightness.getValue().intValue() / 255.0f);
        } else if (!this.rainbow.getValue().booleanValue()) {
            this.color = this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(this.hudRed.getValue(), this.hudGreen.getValue(), this.hudBlue.getValue());
        }
        String grayString = this.grayNess.getValue() != false ? String.valueOf((Object)ChatFormatting.GRAY) : "";
        switch (this.watermark.getValue()) {
            case BANZEM: {
                this.renderer.drawString("Banzem " + (this.modeVer.getValue() != false ? "1.0.0" : ""), 2.0f, 2.0f, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2) : this.color, true);
            }
        }
        if (this.textRadar.getValue().booleanValue()) {
            this.drawTextRadar(ToolTips.getInstance().isOff() || ToolTips.getInstance().shulkerSpy.getValue() == false || ToolTips.getInstance().render.getValue() == false ? 0 : ToolTips.getInstance().getTextRadarY());
        }
        int n = this.renderingUp.getValue() != false ? 0 : (j = HUD.mc.field_71462_r instanceof GuiChat ? 14 : 0);
        if (this.arrayList.getValue().booleanValue()) {
            Color moduleColor;
            Module module;
            if (this.renderingUp.getValue().booleanValue()) {
                for (k = 0; k < (this.alphabeticalSorting.getValue() != false ? Banzem.moduleManager.alphabeticallySortedModules.size() : Banzem.moduleManager.sortedModules.size()); ++k) {
                    module = this.alphabeticalSorting.getValue() != false ? Banzem.moduleManager.alphabeticallySortedModules.get(k) : Banzem.moduleManager.sortedModules.get(k);
                    text = module.getDisplayName() + (Object)ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + (Object)ChatFormatting.WHITE + module.getDisplayInfo() + (Object)ChatFormatting.GRAY + "]" : "");
                    moduleColor = Banzem.moduleManager.moduleColorMap.get(module);
                    this.renderer.drawString(text, (float)(width - 2 - this.renderer.getStringWidth(text)) + (this.animationHorizontalTime.getValue() == 1 ? 0.0f : module.arrayListOffset), 2 + j * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(MathUtil.clamp(2 + j * 10, 0, height)) : (this.moduleColors.getValue() != false && moduleColor != null ? moduleColor.getRGB() : this.color), true);
                    ++j;
                }
            } else {
                for (k = 0; k < (this.alphabeticalSorting.getValue() != false ? Banzem.moduleManager.alphabeticallySortedModules.size() : Banzem.moduleManager.sortedModules.size()); ++k) {
                    module = this.alphabeticalSorting.getValue() != false ? Banzem.moduleManager.alphabeticallySortedModules.get(Banzem.moduleManager.alphabeticallySortedModules.size() - 1 - k) : Banzem.moduleManager.sortedModules.get(k);
                    text = module.getDisplayName() + (Object)ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + (Object)ChatFormatting.WHITE + module.getDisplayInfo() + (Object)ChatFormatting.GRAY + "]" : "");
                    moduleColor = Banzem.moduleManager.moduleColorMap.get(module);
                    TextManager renderer = this.renderer;
                    String text5 = text;
                    float x = (float)(width - 2 - this.renderer.getStringWidth(text)) + (this.animationHorizontalTime.getValue() == 1 ? 0.0f : module.arrayListOffset);
                    int n2 = height;
                    renderer.drawString(text5, x, n2 - (j += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(MathUtil.clamp(height - j, 0, height)) : (this.moduleColors.getValue() != false && moduleColor != null ? moduleColor.getRGB() : this.color), true);
                }
            }
        }
        int n3 = this.renderingUp.getValue().booleanValue() ? (HUD.mc.field_71462_r instanceof GuiChat ? 0 : 0) : (k = 0);
        if (this.renderingUp.getValue().booleanValue()) {
            int itemDamage;
            if (this.serverBrand.getValue().booleanValue()) {
                text2 = grayString + "Server brand " + (Object)ChatFormatting.WHITE + Banzem.serverManager.getServerBrand();
                TextManager renderer2 = this.renderer;
                Object text6 = text2;
                float x2 = width - (this.renderer.getStringWidth((String)text2) + 2);
                int n2 = height - 2;
                renderer2.drawString((String)text6, x2, n2 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
            }
            if (this.potions.getValue().booleanValue()) {
                for (PotionEffect effect : Banzem.potionManager.getOwnPotions()) {
                    text3 = this.altPotionsColors.getValue() != false ? Banzem.potionManager.getPotionString(effect) : Banzem.potionManager.getColoredPotionString(effect);
                    TextManager renderer3 = this.renderer;
                    String text7 = text3;
                    float x3 = width - (this.renderer.getStringWidth(text3) + 2);
                    int n32 = height - 2;
                    renderer3.drawString(text7, x3, n32 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : (this.altPotionsColors.getValue() != false ? this.potionColorMap.get((Object)effect.func_188419_a()).getRGB() : this.color), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                text2 = grayString + "Speed " + (Object)ChatFormatting.WHITE + Banzem.speedManager.getSpeedKpH() + " km/h";
                TextManager renderer4 = this.renderer;
                Object text8 = text2;
                float x4 = width - (this.renderer.getStringWidth((String)text2) + 2);
                int n4 = height - 2;
                renderer4.drawString((String)text8, x4, n4 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
            }
            if (this.time.getValue().booleanValue()) {
                text2 = grayString + "Time " + (Object)ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                TextManager renderer5 = this.renderer;
                Object text9 = text2;
                float x5 = width - (this.renderer.getStringWidth((String)text2) + 2);
                int n5 = height - 2;
                renderer5.drawString((String)text9, x5, n5 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
            }
            if (this.durability.getValue().booleanValue() && (itemDamage = HUD.mc.field_71439_g.func_184614_ca().func_77958_k() - HUD.mc.field_71439_g.func_184614_ca().func_77952_i()) > 0) {
                text = grayString + "Durability " + (Object)ChatFormatting.RESET + itemDamage;
                TextManager renderer6 = this.renderer;
                String text10 = text;
                float x6 = width - (this.renderer.getStringWidth(text) + 2);
                int n6 = height - 2;
                renderer6.drawString(text10, x6, n6 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
            }
            if (this.tps.getValue().booleanValue()) {
                String text22 = grayString + "TPS " + (Object)ChatFormatting.WHITE + Banzem.serverManager.getTPS();
                TextManager renderer7 = this.renderer;
                String text11 = text22;
                float x7 = width - (this.renderer.getStringWidth(text22) + 2);
                int n7 = height - 2;
                renderer7.drawString(text11, x7, n7 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
            }
            fpsText = grayString + "FPS " + (Object)ChatFormatting.WHITE + Minecraft.field_71470_ab;
            text = grayString + "Ping " + (Object)ChatFormatting.WHITE + (ServerModule.getInstance().isConnected() ? ServerModule.getInstance().getServerPing() : (long)Banzem.serverManager.getPing()) + (this.MS.getValue() != false ? "ms" : "");
            if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    TextManager renderer8 = this.renderer;
                    String text12 = text;
                    float x8 = width - (this.renderer.getStringWidth(text) + 2);
                    int n8 = height - 2;
                    renderer8.drawString(text12, x8, n8 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
                }
                if (this.fps.getValue().booleanValue()) {
                    TextManager renderer9 = this.renderer;
                    String text13 = fpsText;
                    float x9 = width - (this.renderer.getStringWidth(fpsText) + 2);
                    int n9 = height - 2;
                    renderer9.drawString(text13, x9, n9 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    TextManager renderer10 = this.renderer;
                    String text14 = fpsText;
                    float x10 = width - (this.renderer.getStringWidth(fpsText) + 2);
                    int n10 = height - 2;
                    renderer10.drawString(text14, x10, n10 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
                }
                if (this.ping.getValue().booleanValue()) {
                    TextManager renderer11 = this.renderer;
                    String text15 = text;
                    float x11 = width - (this.renderer.getStringWidth(text) + 2);
                    int n11 = height - 2;
                    renderer11.drawString(text15, x11, n11 - (k += 10), this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(height - k) : this.color, true);
                }
            }
        } else {
            int itemDamage;
            if (this.serverBrand.getValue().booleanValue()) {
                text2 = grayString + "Server brand " + (Object)ChatFormatting.WHITE + Banzem.serverManager.getServerBrand();
                this.renderer.drawString((String)text2, width - (this.renderer.getStringWidth((String)text2) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
            }
            if (this.potions.getValue().booleanValue()) {
                for (PotionEffect effect : Banzem.potionManager.getOwnPotions()) {
                    text3 = this.altPotionsColors.getValue() != false ? Banzem.potionManager.getPotionString(effect) : Banzem.potionManager.getColoredPotionString(effect);
                    this.renderer.drawString(text3, width - (this.renderer.getStringWidth(text3) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : (this.altPotionsColors.getValue() != false ? this.potionColorMap.get((Object)effect.func_188419_a()).getRGB() : this.color), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                text2 = grayString + "Speed " + (Object)ChatFormatting.WHITE + Banzem.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString((String)text2, width - (this.renderer.getStringWidth((String)text2) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
            }
            if (this.time.getValue().booleanValue()) {
                text2 = grayString + "Time " + (Object)ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString((String)text2, width - (this.renderer.getStringWidth((String)text2) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
            }
            if (this.durability.getValue().booleanValue() && (itemDamage = HUD.mc.field_71439_g.func_184614_ca().func_77958_k() - HUD.mc.field_71439_g.func_184614_ca().func_77952_i()) > 0) {
                text = grayString + "Durability " + (Object)ChatFormatting.GREEN + itemDamage;
                this.renderer.drawString(text, width - (this.renderer.getStringWidth(text) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
            }
            if (this.tps.getValue().booleanValue()) {
                String text23 = grayString + "TPS " + (Object)ChatFormatting.WHITE + Banzem.serverManager.getTPS();
                this.renderer.drawString(text23, width - (this.renderer.getStringWidth(text23) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
            }
            fpsText = grayString + "FPS " + (Object)ChatFormatting.WHITE + Minecraft.field_71470_ab;
            text = grayString + "Ping " + (Object)ChatFormatting.WHITE + Banzem.serverManager.getPing();
            if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(text, width - (this.renderer.getStringWidth(text) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
                }
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - (this.renderer.getStringWidth(fpsText) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - (this.renderer.getStringWidth(fpsText) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
                }
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(text, width - (this.renderer.getStringWidth(text) + 2), 2 + k++ * 10, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2 + k * 10) : this.color, true);
                }
            }
        }
        boolean inHell = HUD.mc.field_71441_e.func_180494_b(HUD.mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
        int posX = (int)HUD.mc.field_71439_g.field_70165_t;
        int posY = (int)HUD.mc.field_71439_g.field_70163_u;
        int posZ = (int)HUD.mc.field_71439_g.field_70161_v;
        float nether = inHell ? 8.0f : 0.125f;
        int hposX = (int)(HUD.mc.field_71439_g.field_70165_t * (double)nether);
        int hposZ = (int)(HUD.mc.field_71439_g.field_70161_v * (double)nether);
        if (this.renderingUp.getValue().booleanValue()) {
            Banzem.notificationManager.handleNotifications(height - (k + 16));
        } else {
            Banzem.notificationManager.handleNotifications(height - (j + 16));
        }
        k = HUD.mc.field_71462_r instanceof GuiChat ? 14 : 0;
        String coordinates = String.valueOf((Object)ChatFormatting.WHITE) + posX + (Object)ChatFormatting.GRAY + " [" + hposX + "], " + (Object)ChatFormatting.WHITE + posY + (Object)ChatFormatting.GRAY + ", " + (Object)ChatFormatting.WHITE + posZ + (Object)ChatFormatting.GRAY + " [" + hposZ + "]";
        String text4 = (this.direction.getValue() != false ? Banzem.rotationManager.getDirection4D(false) + " " : "") + (this.coords.getValue() != false ? coordinates : "") + "";
        TextManager renderer12 = this.renderer;
        String text16 = text4;
        float x12 = 2.0f;
        int n12 = height;
        float y = n12 - (k += 10);
        if (this.rolling.getValue().booleanValue() && this.rainbow.getValue().booleanValue()) {
            Map<Integer, Integer> colorMap = this.colorMap;
            int n13 = height;
            color = colorMap.get(n13 - (k += 10));
        } else {
            color = this.color;
        }
        renderer12.drawString(text16, 2.0f, y, color, true);
        if (this.armor.getValue().booleanValue()) {
            this.renderArmorHUD(this.percent.getValue());
        }
        if (this.totems.getValue().booleanValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue() != Greeter.NONE) {
            this.renderGreeter();
        }
        if (this.lag.getValue() != LagNotify.NONE) {
            this.renderLag();
        }
        if (this.hitMarkers.getValue().booleanValue() && this.hitMarkerTimer > 0) {
            this.drawHitMarkers();
        }
        GlStateManager.func_179121_F();
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderGreeter() {
        int width = this.renderer.scaledWidth;
        String text = "";
        switch (this.greeter.getValue()) {
            case TIME: {
                text = text + MathUtil.getTimeOfDay() + HUD.mc.field_71439_g.getDisplayNameString();
                break;
            }
            case CHRISTMAS: {
                text = text + "Merry Christmas " + HUD.mc.field_71439_g.getDisplayNameString() + " :^)";
                break;
            }
            case LONG: {
                text = text + "Welcome to Banzem Client " + HUD.mc.field_71439_g.getDisplayNameString() + " :^)";
                break;
            }
            case CUSTOM: {
                text = text + this.spoofGreeter.getValue();
                break;
            }
            default: {
                text = text + "Welcome " + HUD.mc.field_71439_g.getDisplayNameString();
            }
        }
        this.renderer.drawString(text, (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(2) : this.color, true);
    }

    public void renderLag() {
        int width = this.renderer.scaledWidth;
        if (Banzem.serverManager.isServerNotResponding()) {
            String text = (Object)(this.lag.getValue() == LagNotify.GRAY ? ChatFormatting.GRAY : ChatFormatting.RED) + "Server not responding: " + MathUtil.round((float)Banzem.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(20) : this.color, true);
        }
    }

    public void renderArrayList() {
    }

    public void renderTotemHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int totems = HUD.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (HUD.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            totems += HUD.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.func_179098_w();
            int i = width / 2;
            boolean iteration = false;
            int y = height - 55 - (HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f() ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(totem, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, totem, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            this.renderer.drawStringWithShadow(totems + "", x + 19 - 2 - this.renderer.getStringWidth(totems + ""), y + 9, 0xFFFFFF);
            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
        }
    }

    public void renderArmorHUD(boolean percent) {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        GlStateManager.func_179098_w();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f() ? 10 : 0);
        for (ItemStack is : HUD.mc.field_71439_g.field_71071_by.field_70460_b) {
            ++iteration;
            if (is.func_190926_b()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(is, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, is, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
            if (!percent) continue;
            int dmg = 0;
            int itemDurability = is.func_77958_k() - is.func_77952_i();
            float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
            float red = 1.0f - green;
            dmg = percent ? 100 - (int)(red * 100.0f) : itemDurability;
            this.renderer.drawStringWithShadow(dmg + "", x + 8 - this.renderer.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.func_179126_j();
        GlStateManager.func_179140_f();
    }

    public void drawHitMarkers() {
        ScaledResolution resolution = new ScaledResolution(mc);
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f - 4.0f, (float)resolution.func_78328_b() / 2.0f - 4.0f, (float)resolution.func_78326_a() / 2.0f - 8.0f, (float)resolution.func_78328_b() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f + 4.0f, (float)resolution.func_78328_b() / 2.0f - 4.0f, (float)resolution.func_78326_a() / 2.0f + 8.0f, (float)resolution.func_78328_b() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f - 4.0f, (float)resolution.func_78328_b() / 2.0f + 4.0f, (float)resolution.func_78326_a() / 2.0f - 8.0f, (float)resolution.func_78328_b() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f + 4.0f, (float)resolution.func_78328_b() / 2.0f + 4.0f, (float)resolution.func_78326_a() / 2.0f + 8.0f, (float)resolution.func_78328_b() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
    }

    public void drawTextRadar(int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (Map.Entry<String, Integer> player : this.players.entrySet()) {
                String text = player.getKey() + " ";
                int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, y, this.rolling.getValue() != false && this.rainbow.getValue() != false ? this.colorMap.get(y) : this.color, true);
                y += textheight;
            }
        }
    }

    public static enum Sound {
        NONE,
        COD,
        CSGO;

    }

    public static enum WaterMark {
        NONE,
        BANZEM;

    }

    public static enum LagNotify {
        NONE,
        RED,
        GRAY;

    }

    public static enum Greeter {
        NONE,
        NAME,
        TIME,
        CHRISTMAS,
        LONG,
        CUSTOM;

    }
}

