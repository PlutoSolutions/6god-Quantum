// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.PerspectiveEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Aspect extends Module
{
    public Setting<Float> aspect;
    
    public Aspect() {
        super("Aspect", "a", Category.RENDER, true, false, false);
        this.aspect = (Setting<Float>)this.register(new Setting("Aspect", (T)0.0f, (T)0.0f, (T)3.0f));
    }
    
    @SubscribeEvent
    public void onPerspectiveEvent(final PerspectiveEvent event) {
        event.setAspect(this.aspect.getValue());
    }
}
