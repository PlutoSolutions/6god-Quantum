//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class NoFallOldfag extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Boolean> disconnect;
    private final Setting<Integer> fallDist;
    BlockPos n1;
    
    public NoFallOldfag() {
        super("NoFallBypass", "nf", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Predict));
        this.disconnect = (Setting<Boolean>)this.register(new Setting("Disconnect", (T)false));
        this.fallDist = (Setting<Integer>)this.register(new Setting("FallDistance", (T)4, (T)3, (T)30, v -> this.mode.getValue() == Mode.Old));
    }
    
    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event) {
        if (nullCheck()) {
            return;
        }
        if (this.mode.getValue().equals("Predict") && NoFallOldfag.mc.player.fallDistance > this.fallDist.getValue() && this.predict(new BlockPos(NoFallOldfag.mc.player.posX, NoFallOldfag.mc.player.posY, NoFallOldfag.mc.player.posZ))) {
            NoFallOldfag.mc.player.motionY = 0.0;
            NoFallOldfag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(NoFallOldfag.mc.player.posX, (double)this.n1.getY(), NoFallOldfag.mc.player.posZ, false));
            NoFallOldfag.mc.player.fallDistance = 0.0f;
            if (this.disconnect.getValue()) {
                NoFallOldfag.mc.player.connection.getNetworkManager().closeChannel((ITextComponent)new TextComponentString(ChatFormatting.GOLD + "NoFall"));
            }
        }
        if (this.mode.getValue().equals("Old") && NoFallOldfag.mc.player.fallDistance > this.fallDist.getValue()) {
            NoFallOldfag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(NoFallOldfag.mc.player.posX, 0.0, NoFallOldfag.mc.player.posZ, false));
            NoFallOldfag.mc.player.fallDistance = 0.0f;
        }
    }
    
    private boolean predict(final BlockPos blockPos) {
        final Minecraft mc = Minecraft.getMinecraft();
        this.n1 = blockPos.add(0, -this.fallDist.getValue(), 0);
        return mc.world.getBlockState(this.n1).getBlock() != Blocks.AIR;
    }
    
    public enum Mode
    {
        Predict, 
        Old;
    }
}
