/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package me.mohalk.banzem.event.events;

import me.mohalk.banzem.event.EventStage;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PushEvent
extends EventStage {
    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;

    public PushEvent(Entity entity, double x, double y, double z, boolean airbone) {
        super(0);
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airbone = airbone;
    }

    public PushEvent(int stage) {
        super(stage);
    }

    public PushEvent(int stage, Entity entity) {
        super(stage);
        this.entity = entity;
    }
}

