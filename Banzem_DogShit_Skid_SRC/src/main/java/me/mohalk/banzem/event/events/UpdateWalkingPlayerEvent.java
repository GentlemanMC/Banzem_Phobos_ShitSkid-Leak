// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.mohalk.banzem.event.EventStage;

@Cancelable
public class UpdateWalkingPlayerEvent extends EventStage
{
    public UpdateWalkingPlayerEvent(final int stage) {
        super(stage);
    }
}
