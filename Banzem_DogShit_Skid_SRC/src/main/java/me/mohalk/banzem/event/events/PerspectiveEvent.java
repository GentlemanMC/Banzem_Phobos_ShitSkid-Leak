// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.event.events;

import me.mohalk.banzem.event.EventStage;

public class PerspectiveEvent extends EventStage
{
    private float aspect;
    
    public PerspectiveEvent(final float f) {
        this.aspect = f;
    }
    
    public float getAspect() {
        return this.aspect;
    }
    
    public void setAspect(final float f) {
        this.aspect = f;
    }
}
