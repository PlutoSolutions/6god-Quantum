// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class BetterPortals extends Module
{
    private static BetterPortals INSTANCE;
    public Setting<Boolean> portalChat;
    public Setting<Boolean> godmode;
    public Setting<Boolean> fastPortal;
    public Setting<Integer> cooldown;
    public Setting<Integer> time;
    
    public BetterPortals() {
        super("BetterPortals", "Tweaks for Portals", Category.MISC, true, false, false);
        this.portalChat = (Setting<Boolean>)this.register(new Setting("Chat", (T)true, "Allows you to chat in portals."));
        this.godmode = (Setting<Boolean>)this.register(new Setting("Godmode", (T)false, "Portal Godmode."));
        this.fastPortal = (Setting<Boolean>)this.register(new Setting("FastPortal", (T)false));
        this.cooldown = (Setting<Integer>)this.register(new Setting("Cooldown", (T)5, (T)1, (T)10, v -> this.fastPortal.getValue(), "Portal cooldown."));
        this.time = (Setting<Integer>)this.register(new Setting("Time", (T)5, (T)0, (T)80, v -> this.fastPortal.getValue(), "Time in Portal"));
        this.setInstance();
    }
    
    public static BetterPortals getInstance() {
        if (BetterPortals.INSTANCE == null) {
            BetterPortals.INSTANCE = new BetterPortals();
        }
        return BetterPortals.INSTANCE;
    }
    
    private void setInstance() {
        BetterPortals.INSTANCE = this;
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.godmode.getValue()) {
            return "Godmode";
        }
        return null;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && this.godmode.getValue() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
    
    static {
        BetterPortals.INSTANCE = new BetterPortals();
    }
}
