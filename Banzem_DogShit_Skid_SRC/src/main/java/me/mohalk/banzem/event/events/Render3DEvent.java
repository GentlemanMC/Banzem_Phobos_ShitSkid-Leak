// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.event.events;

import me.mohalk.banzem.event.EventStage;

public class Render3DEvent extends EventStage
{
    private final float partialTicks;
    
    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
