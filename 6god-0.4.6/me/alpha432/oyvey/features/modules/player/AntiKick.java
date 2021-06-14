// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.NoSuchElementException;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.event.events.PlayerUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.NettyChannelEvent;
import me.alpha432.oyvey.util.Timer;
import io.netty.channel.Channel;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AntiKick extends Module
{
    private final Setting<Integer> timeout;
    private final Setting<Mode> mode;
    private boolean handlerRemoved;
    private Channel nettyChannel;
    private Integer timeoutLast;
    private Timer changeThrottle;
    
    public AntiKick() {
        super("AntiKick", "ak", Category.PLAYER, true, false, true);
        this.timeout = (Setting<Integer>)this.register(new Setting("Timeout", (T)240, (T)30, (T)600));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Change));
        this.handlerRemoved = false;
        this.nettyChannel = null;
        this.timeoutLast = 240;
        this.changeThrottle = new Timer();
        this.timeoutLast = this.timeout.getValue();
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.getValue().name() + ((this.mode.getValue() == Mode.Change) ? (" " + String.valueOf(this.timeoutLast)) : "") + " | " + ((this.nettyChannel == null) ? "NC" : "OK");
    }
    
    @SubscribeEvent
    public void onNettyChannelSet(final NettyChannelEvent event) {
        this.nettyChannel = event.getChannel();
        this.handlerRemoved = false;
        if (this.isEnabled()) {
            this.updateTimeout(this.timeoutLast, this.mode.getValue() == Mode.Change);
        }
    }
    
    @SubscribeEvent
    public void onPlayerUpdate(final PlayerUpdateEvent event) {
        if (this.isEnabled() && this.changeThrottle.passedMs(1000L) && this.timeout.getValue() != this.timeoutLast && this.nettyChannel != null) {
            this.timeoutLast = this.timeout.getValue();
            this.changeThrottle.reset();
            this.updateTimeout(this.timeoutLast, this.mode.getValue() == Mode.Change);
        }
    }
    
    @SubscribeEvent
    public void onSettingsUpdate(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this) && event.getSetting().equals(this.mode)) {
            this.timeoutLast = this.timeout.getValue();
            this.updateTimeout(this.timeoutLast, this.mode.getPlannedValue() == Mode.Change);
        }
    }
    
    private void updateTimeout(final int seconds, final boolean addBack) {
        if (this.nettyChannel != null) {
            try {
                if (!this.handlerRemoved) {
                    this.nettyChannel.pipeline().remove("timeout");
                }
            }
            catch (NoSuchElementException e) {
                OyVey.LOGGER.info("AntiLagKick: catched NSEE trying to remove timeout");
            }
            if (addBack) {
                this.nettyChannel.pipeline().addFirst("timeout", (ChannelHandler)new ReadTimeoutHandler(seconds));
            }
            this.handlerRemoved = !addBack;
        }
    }
    
    public enum Mode
    {
        Change, 
        Remove;
    }
}
