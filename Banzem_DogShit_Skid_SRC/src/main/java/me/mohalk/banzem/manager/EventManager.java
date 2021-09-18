//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.manager;

import me.mohalk.banzem.features.command.Command;
import net.minecraftforge.client.event.ClientChatEvent;
import me.mohalk.banzem.event.events.Render2DEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import me.mohalk.banzem.util.GLUProjection;
import me.mohalk.banzem.event.events.Render3DEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import java.util.UUID;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import me.mohalk.banzem.event.events.ConnectionEvent;
import com.google.common.base.Strings;
import java.util.function.Predicate;
import java.util.Objects;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.mohalk.banzem.event.events.TotemPopEvent;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import me.mohalk.banzem.event.events.PacketEvent;
import me.mohalk.banzem.event.events.UpdateWalkingPlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import me.mohalk.banzem.features.modules.combat.AutoCrystal;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.mohalk.banzem.features.modules.client.ServerModule;
import me.mohalk.banzem.event.events.ClientEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.mohalk.banzem.features.modules.client.Managers;
import me.mohalk.banzem.Banzem;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.concurrent.atomic.AtomicBoolean;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.Feature;

public class EventManager extends Feature
{
    private final Timer timer;
    private final Timer logoutTimer;
    private final Timer switchTimer;
    private boolean keyTimeout;
    private final AtomicBoolean tickOngoing;
    
    public EventManager() {
        this.timer = new Timer();
        this.logoutTimer = new Timer();
        this.switchTimer = new Timer();
        this.tickOngoing = new AtomicBoolean(false);
    }
    
    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (!Feature.fullNullCheck() && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals((Object)EventManager.mc.player)) {
            Banzem.potionManager.update();
            Banzem.totemPopManager.onUpdate();
            Banzem.inventoryManager.update();
            Banzem.holeManager.update();
            Banzem.safetyManager.onUpdate();
            Banzem.moduleManager.onUpdate();
            Banzem.timerManager.update();
            if (this.timer.passedMs(Managers.getInstance().moduleListUpdates.getValue())) {
                Banzem.moduleManager.sortModules(true);
                Banzem.moduleManager.alphabeticallySortModules();
                this.timer.reset();
            }
        }
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && EventManager.mc.getConnection() != null && ServerModule.getInstance().isConnected() && EventManager.mc.world != null) {
            final String command = "@Server" + ServerModule.getInstance().getServerPrefix() + "module " + event.getSetting().getFeature().getName() + " set " + event.getSetting().getName() + " " + event.getSetting().getPlannedValue().toString();
            final CPacketChatMessage cPacketChatMessage = new CPacketChatMessage(command);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTickHighest(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.tickOngoing.set(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTickLowest(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.tickOngoing.set(false);
            AutoCrystal.getInstance().postTick();
        }
    }
    
    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }
    
    @SubscribeEvent
    public void onClientConnect(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        Banzem.moduleManager.onLogin();
    }
    
    @SubscribeEvent
    public void onClientDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Banzem.moduleManager.onLogout();
        Banzem.totemPopManager.onLogout();
        Banzem.potionManager.onLogout();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (fullNullCheck()) {
            return;
        }
        Banzem.moduleManager.onTick();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            Banzem.baritoneManager.onUpdateWalkingPlayer();
            Banzem.speedManager.updateValues();
            Banzem.rotationManager.updateRotations();
            Banzem.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            Banzem.rotationManager.restoreRotations();
            Banzem.positionManager.restorePosition();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.switchTimer.reset();
        }
    }
    
    public boolean isOnSwitchCoolDown() {
        return !this.switchTimer.passedMs(500L);
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() != 0) {
            return;
        }
        Banzem.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity((World)EventManager.mc.world) instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)packet.getEntity((World)EventManager.mc.world);
                MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
                Banzem.totemPopManager.onTotemPop(player);
                Banzem.potionManager.onTotemPop(player);
            }
        }
        else if (event.getPacket() instanceof SPacketPlayerListItem && !Feature.fullNullCheck() && this.logoutTimer.passedS(1.0)) {
            final SPacketPlayerListItem packet2 = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet2.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet2.getAction())) {
                return;
            }
            final UUID[] id = new UUID[1];
            final SPacketPlayerListItem sPacketPlayerListItem = new SPacketPlayerListItem();
            final String[] name = new String[1];
            final EntityPlayer[] entity = new EntityPlayer[1];
            final String[] logoutName = new String[1];
            packet2.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null).forEach(data -> {
                id[0] = data.getProfile().getId();
                switch (sPacketPlayerListItem.getAction()) {
                    case ADD_PLAYER: {
                        name[0] = data.getProfile().getName();
                        MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(0, id[0], name[0]));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        entity[0] = EventManager.mc.world.getPlayerEntityByUUID(id[0]);
                        if (entity[0] != null) {
                            logoutName[0] = entity[0].getName();
                            MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(1, entity[0], id[0], logoutName[0]));
                            break;
                        }
                        else {
                            MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(2, id[0], null));
                            break;
                        }
                    }
                }
            });
        }
        else if (event.getPacket() instanceof SPacketTimeUpdate) {
            Banzem.serverManager.update();
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EventManager.mc.profiler.startSection("phobos");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        final Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        final GLUProjection projection = GLUProjection.getInstance();
        final IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
        final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projectionPort);
        GL11.glGetInteger(2978, viewPort);
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        projection.updateMatrices(viewPort, modelView, projectionPort, scaledResolution.getScaledWidth() / (double)Minecraft.getMinecraft().displayWidth, scaledResolution.getScaledHeight() / (double)Minecraft.getMinecraft().displayHeight);
        Banzem.moduleManager.onRender3D(render3dEvent);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        EventManager.mc.profiler.endSection();
    }
    
    @SubscribeEvent
    public void renderHUD(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Banzem.textManager.updateResolution();
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            final ScaledResolution resolution = new ScaledResolution(EventManager.mc);
            final Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            Banzem.moduleManager.onRender2D(render2DEvent);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                EventManager.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    Banzem.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                }
                else {
                    Command.sendMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage("§cAn error occurred while running this command. Check the log!");
            }
            event.setMessage("");
        }
    }
}
