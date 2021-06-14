// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class EntityControl extends Module
{
    public static EntityControl INSTANCE;
    
    public EntityControl() {
        super("EntityControl", "Control entities with the force or some shit", Category.MOVEMENT, false, false, false);
        EntityControl.INSTANCE = this;
    }
}
