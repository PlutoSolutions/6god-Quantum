//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.Feature;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Sprint extends Module
{
    private static Sprint INSTANCE;
    public Setting<Mode> mode;
    
    public Sprint() {
        super("Sprint", "Modifies sprinting", Category.MOVEMENT, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.LEGIT));
        this.setInstance();
    }
    
    public static Sprint getInstance() {
        if (Sprint.INSTANCE == null) {
            Sprint.INSTANCE = new Sprint();
        }
        return Sprint.INSTANCE;
    }
    
    private void setInstance() {
        Sprint.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onSprint(final MoveEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (Sprint.mc.player.movementInput.moveForward != 0.0f || Sprint.mc.player.movementInput.moveStrafe != 0.0f)) {
            event.setCanceled(true);
        }
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RAGE: {
                if ((!Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !Sprint.mc.gameSettings.keyBindBack.isKeyDown() && !Sprint.mc.gameSettings.keyBindLeft.isKeyDown() && !Sprint.mc.gameSettings.keyBindRight.isKeyDown()) || Sprint.mc.player.isSneaking() || Sprint.mc.player.collidedHorizontally) {
                    break;
                }
                if (Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f) {
                    break;
                }
                Sprint.mc.player.setSprinting(true);
                break;
            }
            case LEGIT: {
                if (!Sprint.mc.gameSettings.keyBindForward.isKeyDown() || Sprint.mc.player.isSneaking() || Sprint.mc.player.isHandActive() || Sprint.mc.player.collidedHorizontally || Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f) {
                    break;
                }
                if (Sprint.mc.currentScreen != null) {
                    break;
                }
                Sprint.mc.player.setSprinting(true);
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.nullCheck()) {
            Sprint.mc.player.setSprinting(false);
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        Sprint.INSTANCE = new Sprint();
    }
    
    public enum Mode
    {
        LEGIT, 
        RAGE;
    }
}
