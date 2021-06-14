// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.util.MovementUtil;
import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class VanillaSpeed extends Module
{
    private final Setting<Float> speed;
    
    public VanillaSpeed() {
        super("VanillaSpeed", "vs", Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Speed", (T)1.0f, (T)1.0f, (T)10.0f));
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        final double[] calc = MovementUtil.directionSpeed(this.speed.getValue() / 10.0);
        event.setMotionX(calc[0]);
        event.setMotionZ(calc[1]);
    }
}
