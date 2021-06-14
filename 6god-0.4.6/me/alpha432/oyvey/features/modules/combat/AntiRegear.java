//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.block.BlockShulkerBox;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.event.events.UpdateEvent;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AntiRegear extends Module
{
    private final Setting<Float> reach;
    private final Setting<Integer> retry;
    private final List<BlockPos> retries;
    private final List<BlockPos> selfPlaced;
    private int ticks;
    
    public AntiRegear() {
        super("AntiRegear", "ar", Category.COMBAT, true, false, false);
        this.reach = (Setting<Float>)this.register(new Setting("Reach", (T)5.0f, (T)1.0f, (T)6.0f));
        this.retry = (Setting<Integer>)this.register(new Setting("Retry Delay", (T)10, (T)0, (T)20));
        this.retries = new ArrayList<BlockPos>();
        this.selfPlaced = new ArrayList<BlockPos>();
    }
    
    @SubscribeEvent
    public void onUpdate(final UpdateEvent event) {
        if (this.ticks++ < this.retry.getValue()) {
            this.ticks = 0;
            this.retries.clear();
        }
        final List<BlockPos> sphere = BlockUtil.getSphereRealth(this.reach.getValue(), true);
        for (int size = sphere.size(), i = 0; i < size; ++i) {
            final BlockPos pos = sphere.get(i);
            if (!this.retries.contains(pos)) {
                if (!this.selfPlaced.contains(pos)) {
                    if (AntiRegear.mc.world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) {
                        AntiRegear.mc.player.swingArm(EnumHand.MAIN_HAND);
                        AntiRegear.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
                        AntiRegear.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
                        this.retries.add(pos);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
            if (AntiRegear.mc.player.getHeldItem(packet.getHand()).getItem() instanceof ItemShulkerBox) {
                this.selfPlaced.add(packet.getPos().offset(packet.getDirection()));
            }
        }
    }
}
