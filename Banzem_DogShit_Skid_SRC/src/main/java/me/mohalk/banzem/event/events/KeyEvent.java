// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.event.events;

import me.mohalk.banzem.event.EventStage;

public class KeyEvent extends EventStage
{
    public boolean info;
    public boolean pressed;
    
    public KeyEvent(final int stage, final boolean info, final boolean pressed) {
        super(stage);
        this.info = info;
        this.pressed = pressed;
    }
}
