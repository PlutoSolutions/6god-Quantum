// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.Feature;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.alpha432.oyvey.event.EventStage;

@Cancelable
public class ClientEvent extends EventStage
{
    private Feature feature;
    private Setting setting;
    
    public ClientEvent(final int stage, final Feature feature) {
        super(stage);
        this.feature = feature;
    }
    
    public ClientEvent(final Setting setting) {
        super(2);
        this.setting = setting;
    }
    
    public Feature getFeature() {
        return this.feature;
    }
    
    public Setting getSetting() {
        return this.setting;
    }
}
