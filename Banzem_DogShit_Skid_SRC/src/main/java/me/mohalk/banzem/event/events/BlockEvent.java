// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.mohalk.banzem.event.EventStage;

@Cancelable
public class BlockEvent extends EventStage
{
    public BlockPos pos;
    public EnumFacing facing;
    
    public BlockEvent(final int stage, final BlockPos pos, final EnumFacing facing) {
        super(stage);
        this.pos = pos;
        this.facing = facing;
    }
}
