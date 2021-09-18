//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.modules.client;

import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import me.mohalk.banzem.util.Util;
import me.mohalk.banzem.util.ColorUtil;
import me.mohalk.banzem.util.RenderUtil;
import me.mohalk.banzem.features.command.Command;
import java.net.Socket;
import me.mohalk.banzem.event.events.Render2DEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import me.mohalk.banzem.manager.WaypointManager;
import me.mohalk.banzem.Banzem;
import me.mohalk.banzem.features.Feature;
import me.mohalk.banzem.event.events.Render3DEvent;
import org.lwjgl.input.Keyboard;
import me.mohalk.banzem.features.gui.PhobosGui;
import java.util.Collection;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import net.minecraft.item.ItemStack;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.awt.Color;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.util.math.BlockPos;
import me.mohalk.banzem.util.Timer;
import me.mohalk.banzem.features.setting.Bind;
import me.mohalk.banzem.features.setting.Setting;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import me.mohalk.banzem.features.modules.Module;

public class IRC extends Module
{
    public static final Random avRandomizer;
    private static final ResourceLocation SHULKER_GUI_TEXTURE;
    public static IRC INSTANCE;
    public static IRCHandler handler;
    public static List<String> phobosUsers;
    public Setting<String> ip;
    public Setting<Boolean> waypoints;
    public Setting<Boolean> ding;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    public Setting<Boolean> inventories;
    public Setting<Boolean> render;
    public Setting<Boolean> own;
    public Setting<Integer> cooldown;
    public Setting<Boolean> offsets;
    private final Setting<Integer> yPerPlayer;
    private final Setting<Integer> xOffset;
    private final Setting<Integer> yOffset;
    private final Setting<Integer> trOffset;
    public Setting<Integer> invH;
    public Setting<Bind> pingBind;
    public boolean status;
    public Timer updateTimer;
    public Timer downTimer;
    public BlockPos waypointTarget;
    private int textRadarY;
    private boolean down;
    private boolean pressed;
    
    public IRC() {
        super("PhobosChat", "Banzem chat server", Category.CLIENT, true, false, true);
        this.ip = (Setting<String>)this.register(new Setting("IP", "206.189.218.150"));
        this.waypoints = (Setting<Boolean>)this.register(new Setting("Waypoints", false));
        this.ding = (Setting<Boolean>)this.register(new Setting("Ding", false, v -> this.waypoints.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255, v -> this.waypoints.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255, v -> this.waypoints.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255, v -> this.waypoints.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255, v -> this.waypoints.getValue()));
        this.inventories = (Setting<Boolean>)this.register(new Setting("Inventories", false));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", true, v -> this.inventories.getValue()));
        this.own = (Setting<Boolean>)this.register(new Setting("OwnShulker", true, v -> this.inventories.getValue()));
        this.cooldown = (Setting<Integer>)this.register(new Setting("ShowForS", 2, 0, 5, v -> this.inventories.getValue()));
        this.offsets = (Setting<Boolean>)this.register(new Setting("Offsets", false));
        this.yPerPlayer = (Setting<Integer>)this.register(new Setting("Y/Player", 18, v -> this.offsets.getValue()));
        this.xOffset = (Setting<Integer>)this.register(new Setting("XOffset", 4, v -> this.offsets.getValue()));
        this.yOffset = (Setting<Integer>)this.register(new Setting("YOffset", 2, v -> this.offsets.getValue()));
        this.trOffset = (Setting<Integer>)this.register(new Setting("TROffset", 2, v -> this.offsets.getValue()));
        this.invH = (Setting<Integer>)this.register(new Setting("InvH", 3, v -> this.inventories.getValue()));
        this.pingBind = (Setting<Bind>)this.register(new Setting("Ping", new Bind(-1)));
        this.status = false;
        this.updateTimer = new Timer();
        this.downTimer = new Timer();
        this.textRadarY = 0;
        this.down = false;
        this.pressed = false;
        IRC.INSTANCE = this;
    }

    public static void updateInventory() throws IOException {
        IRC.handler.outputStream.writeUTF("updateinventory");
        IRC.handler.outputStream.writeUTF(IRC.mc.player.getName());
        writeByteArray(serializeInventory(), IRC.handler.outputStream);
    }
    
    public static void updateInventories() {
        for (final String player : IRC.phobosUsers) {
            try {
                send("inventory", player);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void updateWaypoint(final BlockPos pos, final String server, final String dimension, final Color color) throws IOException {
        send("waypoint", server + ":" + dimension + ":" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ(), color.getRed() + ":" + color.getGreen() + ":" + color.getBlue() + ":" + color.getAlpha());
    }
    
    public static void removeWaypoint() throws IOException {
        IRC.handler.outputStream.writeUTF("removewaypoint");
        IRC.handler.outputStream.writeUTF(IRC.mc.player.getName());
        IRC.handler.outputStream.flush();
    }
    
    public static void send(final String command, final String data, final String data1) throws IOException {
        IRC.handler.outputStream.writeUTF(command);
        IRC.handler.outputStream.writeUTF(IRC.mc.player.getName());
        IRC.handler.outputStream.writeUTF(data);
        IRC.handler.outputStream.writeUTF(data1);
        IRC.handler.outputStream.flush();
    }
    
    public static void send(final String command, final String data) throws IOException {
        IRC.handler.outputStream.writeUTF(command);
        IRC.handler.outputStream.writeUTF(IRC.mc.player.getName());
        IRC.handler.outputStream.writeUTF(data);
        IRC.handler.outputStream.flush();
    }
    
    private static byte[] readByteArrayLWithLength(final DataInputStream reader) throws IOException {
        final int length = reader.readInt();
        if (length > 0) {
            final byte[] cifrato = new byte[length];
            reader.readFully(cifrato, 0, cifrato.length);
            return cifrato;
        }
        return null;
    }
    
    public static void writeByteArray(final byte[] data, final DataOutputStream writer) throws IOException {
        writer.writeInt(data.length);
        writer.write(data);
        writer.flush();
    }
    
    public static List<ItemStack> deserializeInventory(final byte[] inventory) throws IOException, ClassNotFoundException {
        final ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(inventory));
        final ArrayList<ItemStack> inventoryList = (ArrayList<ItemStack>)stream.readObject();
        return inventoryList;
    }
    
    public static byte[] serializeInventory() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(new ArrayList((Collection<?>)IRC.mc.player.inventory.mainInventory));
        return bos.toByteArray();
    }
    
    public static void say(final String message) throws IOException {
        IRC.handler.outputStream.writeUTF("message");
        IRC.handler.outputStream.writeUTF(IRC.mc.player.getName());
        IRC.handler.outputStream.writeUTF(message);
        IRC.handler.outputStream.flush();
    }
    
    public static void cockt(final int id) throws IOException {
        IRC.handler.outputStream.writeUTF("cockt");
        IRC.handler.outputStream.writeInt(id);
        IRC.handler.outputStream.flush();
    }
    
    public static String getDimension(final int dim) {
        switch (dim) {
            case 0: {
                return "Overworld";
            }
            case -1: {
                return "Nether";
            }
            case 1: {
                return "End";
            }
            default: {
                return "";
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (IRC.handler != null && IRC.handler.isAlive() && !IRC.handler.isInterrupted()) {
            this.status = !IRC.handler.socket.isClosed();
        }
        else {
            this.status = false;
        }
        if (this.updateTimer.passedMs(5000L) && IRC.handler != null && IRC.handler.isAlive() && !IRC.handler.socket.isClosed()) {
            try {
                IRC.handler.outputStream.writeUTF("update");
                IRC.handler.outputStream.writeUTF(IRC.mc.player.getName());
                IRC.handler.outputStream.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            this.updateTimer.reset();
        }
        if (!IRC.mc.isSingleplayer() && !(IRC.mc.currentScreen instanceof PhobosGui) && IRC.handler != null && !IRC.handler.socket.isClosed() && this.status) {
            if (this.down) {
                if (this.downTimer.passedMs(2000L)) {
                    try {
                        removeWaypoint();
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    this.down = false;
                    this.downTimer.reset();
                }
                if (!Keyboard.isKeyDown(this.pingBind.getValue().getKey())) {
                    try {
                        updateWaypoint(this.waypointTarget, IRC.mc.currentServerData.serverIP, String.valueOf(IRC.mc.player.dimension), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            if (Keyboard.isKeyDown(this.pingBind.getValue().getKey())) {
                if (!this.pressed) {
                    this.down = true;
                    this.pressed = true;
                }
            }
            else {
                this.down = false;
                this.pressed = false;
                this.downTimer.reset();
            }
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (Feature.fullNullCheck() || IRC.mc.isSingleplayer()) {
            return;
        }
        final RayTraceResult result = IRC.mc.player.rayTrace(2000.0, event.getPartialTicks());
        if (result != null) {
            this.waypointTarget = new BlockPos(result.hitVec);
        }
        if (this.waypoints.getValue()) {
            for (final WaypointManager.Waypoint waypoint : Banzem.waypointManager.waypoints.values()) {
                if (IRC.mc.player.dimension == waypoint.dimension) {
                    if (!IRC.mc.currentServerData.serverIP.equals(waypoint.server)) {
                        continue;
                    }
                    waypoint.renderBox();
                    waypoint.render();
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderHelper.disableStandardItemLighting();
                }
            }
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.inventories.getValue()) {
            final int x = -4 + this.xOffset.getValue();
            int y = 10 + this.yOffset.getValue();
            this.textRadarY = 0;
            for (final String player : IRC.phobosUsers) {
                if (Banzem.inventoryManager.inventories.get(player) != null) {
                    continue;
                }
                final List<ItemStack> stacks = Banzem.inventoryManager.inventories.get(player);
                this.renderShulkerToolTip(stacks, x, y, player);
                y += this.yPerPlayer.getValue() + 60;
                this.textRadarY = y - 10 - this.yOffset.getValue() + this.trOffset.getValue();
            }
        }
    }
    
    public void connect() throws IOException {
        if (!IRC.INSTANCE.status) {
            final Socket socket = new Socket(this.ip.getValue(), 1488);
            (IRC.handler = new IRCHandler(socket)).start();
            IRC.handler.outputStream.writeUTF("update");
            IRC.handler.outputStream.writeUTF(IRC.mc.player.getName());
            IRC.handler.outputStream.flush();
            IRC.INSTANCE.status = true;
            Command.sendMessage("\u00c2§aIRC connected successfully!");
        }
        else {
            Command.sendMessage("\u00c2§cIRC is already connected!");
        }
    }
    
    public void disconnect() throws IOException {
        if (IRC.INSTANCE.status) {
            IRC.handler.socket.close();
            if (!IRC.handler.isInterrupted()) {
                IRC.handler.interrupt();
            }
        }
        else {
            Command.sendMessage("\u00c2§cIRC is not connected!");
        }
    }
    
    public void friendAll() throws IOException {
        IRC.handler.outputStream.writeUTF("friendall");
        IRC.handler.outputStream.flush();
    }
    
    public void list() throws IOException {
        IRC.handler.outputStream.writeUTF("list");
        IRC.handler.outputStream.flush();
    }
    
    public void renderShulkerToolTip(final List<ItemStack> stacks, final int x, final int y, final String name) {
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        IRC.mc.getTextureManager().bindTexture(IRC.SHULKER_GUI_TEXTURE);
        RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + this.invH.getValue(), 500);
        RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
        GlStateManager.disableDepth();
        final Color color = new Color(0, 0, 0, 255);
        this.renderer.drawStringWithShadow(name, (float)(x + 8), (float)(y + 6), ColorUtil.toRGBA(color));
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        for (int i = 0; i < stacks.size(); ++i) {
            final int iX = x + i % 9 * 18 + 8;
            final int iY = y + i / 9 * 18 + 18;
            final ItemStack itemStack = stacks.get(i);
            IRC.mc.getRenderItem().zLevel = 501.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(IRC.mc.fontRenderer, itemStack, iX, iY, (String)null);
            IRC.mc.getRenderItem().zLevel = 0.0f;
        }
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        avRandomizer = new Random();
        SHULKER_GUI_TEXTURE = null;
    }
    
    private static class IRCHandler extends Thread
    {
        public Socket socket;
        public DataInputStream inputStream;
        public DataOutputStream outputStream;
        
        public IRCHandler(final Socket socket) {
            super(Util.mc.player.getName());
            this.socket = socket;
            try {
                this.inputStream = new DataInputStream(socket.getInputStream());
                this.outputStream = new DataOutputStream(socket.getOutputStream());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            Command.sendMessage("\u00c2§aSocket thread starting!");
        Label_0005_Outer:
            while (true) {
                while (true) {
                    try {
                        while (true) {
                            final String input = this.inputStream.readUTF();
                            if (input.equalsIgnoreCase("message")) {
                                final String name = this.inputStream.readUTF();
                                final String message = this.inputStream.readUTF();
                                Command.sendMessage("\u00c2§c[IRC] \u00c2§r<" + name + ">: " + message);
                            }
                            if (input.equalsIgnoreCase("list")) {
                                final String f = this.inputStream.readUTF();
                                final String[] friends;
                                final String[] array;
                                final String[] split = array = (friends = f.split("%%%"));
                                for (final String friend : array) {
                                    Command.sendMessage("\u00c2§b" + friend.replace("_&_", " ID: "));
                                }
                            }
                            else if (input.equalsIgnoreCase("friendall")) {
                                final String f = this.inputStream.readUTF();
                                final String[] friends;
                                final String[] array2;
                                final String[] split2 = array2 = (friends = f.split("%%%"));
                                for (final String friend : array2) {
                                    if (!friend.equals(Util.mc.player.getName())) {
                                        Banzem.friendManager.addFriend(friend);
                                        Command.sendMessage("\u00c2§b" + friend + " has been friended");
                                    }
                                }
                            }
                            else if (input.equalsIgnoreCase("waypoint")) {
                                final String name = this.inputStream.readUTF();
                                final String[] inputs = this.inputStream.readUTF().split(":");
                                final String[] colors = this.inputStream.readUTF().split(":");
                                final String server = inputs[0];
                                final String dimension = inputs[1];
                                final Color color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]), Integer.parseInt(colors[3]));
                                Banzem.waypointManager.waypoints.put(name, new WaypointManager.Waypoint(name, server, Integer.parseInt(dimension), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]), Integer.parseInt(inputs[4]), color));
                                Command.sendMessage("\u00c2§c[IRC] \u00c2§r" + name + " has set a waypoint at \u00c2§c(" + Integer.parseInt(inputs[2]) + "," + Integer.parseInt(inputs[3]) + "," + Integer.parseInt(inputs[4]) + ")\u00c2§r on the server \u00c2§c" + server + "\u00c2§r in the dimension \u00c2§c" + IRC.getDimension(Integer.parseInt(dimension)));
                                if (IRC.INSTANCE.ding.getValue()) {
                                    Util.mc.world.playSound(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 0.7f, false);
                                }
                            }
                            else if (input.equalsIgnoreCase("removewaypoint")) {
                                final String name = this.inputStream.readUTF();
                                Banzem.waypointManager.waypoints.remove(name);
                                Command.sendMessage("\u00c2§c[IRC] \u00c2§r" + name + " has removed their waypoint");
                                if (IRC.INSTANCE.ding.getValue()) {
                                    Util.mc.world.playSound(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, -0.7f, false);
                                }
                            }
                            else if (input.equalsIgnoreCase("inventory")) {
                                final String name = this.inputStream.readUTF();
                                final byte[] inventory = readByteArrayLWithLength(this.inputStream);
                                for (final String player : IRC.phobosUsers) {
                                    if (player.equalsIgnoreCase(name)) {
                                        Banzem.inventoryManager.inventories.put(player, IRC.deserializeInventory(inventory));
                                    }
                                }
                            }
                            else if (input.equalsIgnoreCase("users")) {
                                final byte[] inputBytes = readByteArrayLWithLength(this.inputStream);
                                final ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(inputBytes));
                                final List<String> players = (List<String>)stream.readObject();
                                Command.sendMessage("\u00c2§c[IRC]\u00c2§r Active Users:");
                                for (final String name2 : players) {
                                    Command.sendMessage(name2);
                                    if (!IRC.phobosUsers.contains(name2)) {
                                        IRC.phobosUsers.add(name2);
                                    }
                                }
                            }
                            IRC.INSTANCE.status = !this.socket.isClosed();
                        }
                    }
                    catch (IOException | ClassNotFoundException ex2) {
                        final Exception ex = new Exception();
                        final Exception e = ex;
                        e.printStackTrace();
                        continue Label_0005_Outer;
                    }
                }
            }
        }
    }
}
