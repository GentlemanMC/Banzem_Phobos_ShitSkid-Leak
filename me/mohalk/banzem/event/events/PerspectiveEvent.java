/*
 * Decompiled with CFR 0.150.
 */
package me.mohalk.banzem.event.events;

import me.mohalk.banzem.event.EventStage;

public class PerspectiveEvent
extends EventStage {
    private float aspect;

    public PerspectiveEvent(float f) {
        this.aspect = f;
    }

    public float getAspect() {
        return this.aspect;
    }

    public void setAspect(float f) {
        this.aspect = f;
    }
}

