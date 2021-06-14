//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.util.RenderUtil;
import java.awt.Color;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ChorusESP extends Module
{
    private final Setting<Integer> alpha;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Timer timer;
    private BlockPos chorusPos;
    
    public ChorusESP() {
        super("ChorusESP", "cesp", Category.RENDER, true, false, false);
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.timer = new Timer();
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = event.getPacket();
            if (packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
                this.chorusPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                this.timer.reset();
            }
        }
    }
    
    public void onRender3D() {
        if (this.chorusPos != null) {
            if (this.timer.passedMs(2000L)) {
                this.chorusPos = null;
                return;
            }
            RenderUtil.drawBoxRealth(this.chorusPos, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()), this.alpha.getValue());
        }
    }
}
