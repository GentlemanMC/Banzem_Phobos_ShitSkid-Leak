// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem.event.events;

import me.mohalk.banzem.features.setting.Setting;
import me.mohalk.banzem.event.EventStage;

public class ValueChangeEvent extends EventStage
{
    public Setting setting;
    public Object value;
    
    public ValueChangeEvent(final Setting setting, final Object value) {
        this.setting = setting;
        this.value = value;
    }
}
