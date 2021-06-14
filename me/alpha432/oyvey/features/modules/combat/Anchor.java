//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.math.Vec3d;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Anchor extends Module
{
    private final Setting<Integer> pitch;
    private final Setting<Boolean> pull;
    private Vec3d Center;
    public static boolean AnchorING;
    int holeblocks;
    
    public Anchor() {
        super("Anchor", "a", Category.COMBAT, true, false, false);
        this.pitch = (Setting<Integer>)this.register(new Setting("Pitch", (T)60, (T)0, (T)90));
        this.pull = (Setting<Boolean>)this.register(new Setting("Pull", (T)true));
        this.Center = Vec3d.ZERO;
    }
    
    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event) {
        if (nullCheck()) {
            return;
        }
        if (Anchor.mc.player.rotationPitch >= this.pitch.getValue()) {
            if (this.isBlockHole(this.getPlayerPos().down(1)) || this.isBlockHole(this.getPlayerPos().down(2)) || this.isBlockHole(this.getPlayerPos().down(3)) || this.isBlockHole(this.getPlayerPos().down(4))) {
                Anchor.AnchorING = true;
                if (!this.pull.getValue()) {
                    Anchor.mc.player.motionX = 0.0;
                    Anchor.mc.player.motionZ = 0.0;
                }
                else {
                    this.Center = this.GetCenter(Anchor.mc.player.posX, Anchor.mc.player.posY, Anchor.mc.player.posZ);
                    final double XDiff = Math.abs(this.Center.x - Anchor.mc.player.posX);
                    final double ZDiff = Math.abs(this.Center.z - Anchor.mc.player.posZ);
                    if (XDiff <= 0.1 && ZDiff <= 0.1) {
                        this.Center = Vec3d.ZERO;
                    }
                    else {
                        final double MotionX = this.Center.x - Anchor.mc.player.posX;
                        final double MotionZ = this.Center.z - Anchor.mc.player.posZ;
                        Anchor.mc.player.motionX = MotionX / 2.0;
                        Anchor.mc.player.motionZ = MotionZ / 2.0;
                    }
                }
            }
            else {
                Anchor.AnchorING = false;
            }
        }
    }
    
    @Override
    public void onDisable() {
        Anchor.AnchorING = false;
        this.holeblocks = 0;
    }
    
    public boolean isBlockHole(final BlockPos blockpos) {
        this.holeblocks = 0;
        if (Anchor.mc.world.getBlockState(blockpos.add(0, 3, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(0, 1, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(0, 0, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockpos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockpos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockpos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockpos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockpos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockpos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        return this.holeblocks >= 9;
    }
    
    public Vec3d GetCenter(final double posX, final double posY, final double posZ) {
        final double x = Math.floor(posX) + 0.5;
        final double y = Math.floor(posY);
        final double z = Math.floor(posZ) + 0.5;
        return new Vec3d(x, y, z);
    }
    
    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Anchor.mc.player.posX), Math.floor(Anchor.mc.player.posY), Math.floor(Anchor.mc.player.posZ));
    }
}
