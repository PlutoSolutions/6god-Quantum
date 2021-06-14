//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.event.events.PushEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.alpha432.oyvey.features.modules.misc.BetterPortals;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.List;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.alpha432.oyvey.event.events.StepEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.EnumFacing;
import java.util.Arrays;
import net.minecraft.entity.MoverType;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Random;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public abstract class MixinEntity
{
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public float rotationYaw;
    @Shadow
    public float rotationPitch;
    @Shadow
    public boolean onGround;
    @Shadow
    public boolean noClip;
    @Shadow
    public float prevDistanceWalkedModified;
    @Shadow
    public World world;
    @Shadow
    @Final
    private double[] pistonDeltas;
    @Shadow
    private long pistonDeltasGameTime;
    @Shadow
    protected boolean isInWeb;
    @Shadow
    public float stepHeight;
    @Shadow
    public boolean collidedHorizontally;
    @Shadow
    public boolean collidedVertically;
    @Shadow
    public boolean collided;
    @Shadow
    public float distanceWalkedModified;
    @Shadow
    public float distanceWalkedOnStepModified;
    @Shadow
    private int fire;
    @Shadow
    private int nextStepDistance;
    @Shadow
    private float nextFlap;
    @Shadow
    protected Random rand;
    
    @Shadow
    public abstract boolean isSprinting();
    
    @Shadow
    public abstract boolean isRiding();
    
    @Shadow
    public abstract boolean isSneaking();
    
    @Shadow
    public abstract void setEntityBoundingBox(final AxisAlignedBB p0);
    
    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    
    @Shadow
    public abstract void resetPositionToBB();
    
    @Shadow
    protected abstract void updateFallState(final double p0, final boolean p1, final IBlockState p2, final BlockPos p3);
    
    @Shadow
    protected abstract boolean canTriggerWalking();
    
    @Shadow
    public abstract boolean isInWater();
    
    @Shadow
    public abstract boolean isBeingRidden();
    
    @Shadow
    public abstract Entity getControllingPassenger();
    
    @Shadow
    public abstract void playSound(final SoundEvent p0, final float p1, final float p2);
    
    @Shadow
    protected abstract void doBlockCollisions();
    
    @Shadow
    public abstract boolean isWet();
    
    @Shadow
    protected abstract void playStepSound(final BlockPos p0, final Block p1);
    
    @Shadow
    protected abstract SoundEvent getSwimSound();
    
    @Shadow
    protected abstract float playFlySound(final float p0);
    
    @Shadow
    protected abstract boolean makeFlySound();
    
    @Shadow
    public abstract void addEntityCrashInfo(final CrashReportCategory p0);
    
    @Shadow
    protected abstract void dealFireDamage(final int p0);
    
    @Shadow
    public abstract void setFire(final int p0);
    
    @Shadow
    protected abstract int getFireImmuneTicks();
    
    @Shadow
    public abstract boolean isBurning();
    
    @Shadow
    public abstract int getMaxInPortalTime();
    
    @Overwrite
    public void move(final MoverType type, double x, double y, double z) {
        final Entity _this = (Entity)this;
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        }
        else {
            if (type == MoverType.PISTON) {
                final long i = this.world.getTotalWorldTime();
                if (i != this.pistonDeltasGameTime) {
                    Arrays.fill(this.pistonDeltas, 0.0);
                    this.pistonDeltasGameTime = i;
                }
                if (x != 0.0) {
                    final int j = EnumFacing.Axis.X.ordinal();
                    final double d0 = MathHelper.clamp(x + this.pistonDeltas[j], -0.51, 0.51);
                    x = d0 - this.pistonDeltas[j];
                    this.pistonDeltas[j] = d0;
                    if (Math.abs(x) <= 9.999999747378752E-6) {
                        return;
                    }
                }
                else if (y != 0.0) {
                    final int l4 = EnumFacing.Axis.Y.ordinal();
                    final double d2 = MathHelper.clamp(y + this.pistonDeltas[l4], -0.51, 0.51);
                    y = d2 - this.pistonDeltas[l4];
                    this.pistonDeltas[l4] = d2;
                    if (Math.abs(y) <= 9.999999747378752E-6) {
                        return;
                    }
                }
                else {
                    if (z == 0.0) {
                        return;
                    }
                    final int i2 = EnumFacing.Axis.Z.ordinal();
                    final double d3 = MathHelper.clamp(z + this.pistonDeltas[i2], -0.51, 0.51);
                    z = d3 - this.pistonDeltas[i2];
                    this.pistonDeltas[i2] = d3;
                    if (Math.abs(z) <= 9.999999747378752E-6) {
                        return;
                    }
                }
            }
            this.world.profiler.startSection("move");
            final double d4 = this.posX;
            final double d5 = this.posY;
            final double d6 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            double d7 = x;
            final double d8 = y;
            double d9 = z;
            if ((type == MoverType.SELF || type == MoverType.PLAYER) && this.onGround && this.isSneaking() && _this instanceof EntityPlayer) {
                final double d10 = 0.05;
                while (x != 0.0 && this.world.getCollisionBoxes(_this, this.getEntityBoundingBox().offset(x, (double)(-this.stepHeight), 0.0)).isEmpty()) {
                    x = (d7 = ((x < 0.05 && x >= -0.05) ? 0.0 : ((x > 0.0) ? (x -= 0.05) : (x += 0.05))));
                }
                while (z != 0.0 && this.world.getCollisionBoxes(_this, this.getEntityBoundingBox().offset(0.0, (double)(-this.stepHeight), z)).isEmpty()) {
                    z = (d9 = ((z < 0.05 && z >= -0.05) ? 0.0 : ((z > 0.0) ? (z -= 0.05) : (z += 0.05))));
                }
                while (x != 0.0 && z != 0.0 && this.world.getCollisionBoxes(_this, this.getEntityBoundingBox().offset(x, (double)(-this.stepHeight), z)).isEmpty()) {
                    x = (d7 = ((x < 0.05 && x >= -0.05) ? 0.0 : ((x > 0.0) ? (x -= 0.05) : (x += 0.05))));
                    z = (d9 = ((z < 0.05 && z >= -0.05) ? 0.0 : ((z > 0.0) ? (z -= 0.05) : (z += 0.05))));
                }
            }
            final List list1 = this.world.getCollisionBoxes(_this, this.getEntityBoundingBox().expand(x, y, z));
            final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            if (y != 0.0) {
                for (int k = list1.size(), m = 0; m < k; ++m) {
                    y = list1.get(m).calculateYOffset(this.getEntityBoundingBox(), y);
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
            }
            if (x != 0.0) {
                for (int l5 = list1.size(), j2 = 0; j2 < l5; ++j2) {
                    x = list1.get(j2).calculateXOffset(this.getEntityBoundingBox(), x);
                }
                if (x != 0.0) {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
                }
            }
            if (z != 0.0) {
                for (int i3 = list1.size(), k2 = 0; k2 < i3; ++k2) {
                    z = list1.get(k2).calculateZOffset(this.getEntityBoundingBox(), z);
                }
                if (z != 0.0) {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
                }
            }
            final boolean bl;
            final boolean flag = bl = (this.onGround || (d8 != y && d8 < 0.0));
            if (this.stepHeight > 0.0f && flag && (d7 != x || d9 != z)) {
                final StepEvent preEvent = new StepEvent(0, _this);
                MinecraftForge.EVENT_BUS.post((Event)preEvent);
                final double d11 = x;
                final double d12 = y;
                final double d13 = z;
                final AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y = preEvent.getHeight();
                final List list2 = this.world.getCollisionBoxes(_this, this.getEntityBoundingBox().expand(d7, y, d9));
                AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
                final AxisAlignedBB axisalignedbb4 = axisalignedbb3.expand(d7, 0.0, d9);
                double d14 = y;
                for (int k3 = list2.size(), j3 = 0; j3 < k3; ++j3) {
                    d14 = list2.get(j3).calculateYOffset(axisalignedbb4, d14);
                }
                axisalignedbb3 = axisalignedbb3.offset(0.0, d14, 0.0);
                double d15 = d7;
                for (int i4 = list2.size(), l6 = 0; l6 < i4; ++l6) {
                    d15 = list2.get(l6).calculateXOffset(axisalignedbb3, d15);
                }
                axisalignedbb3 = axisalignedbb3.offset(d15, 0.0, 0.0);
                double d16 = d9;
                for (int k4 = list2.size(), j4 = 0; j4 < k4; ++j4) {
                    d16 = list2.get(j4).calculateZOffset(axisalignedbb3, d16);
                }
                axisalignedbb3 = axisalignedbb3.offset(0.0, 0.0, d16);
                AxisAlignedBB axisalignedbb5 = this.getEntityBoundingBox();
                double d17 = y;
                for (int i5 = list2.size(), l7 = 0; l7 < i5; ++l7) {
                    d17 = list2.get(l7).calculateYOffset(axisalignedbb5, d17);
                }
                axisalignedbb5 = axisalignedbb5.offset(0.0, d17, 0.0);
                double d18 = d7;
                for (int k5 = list2.size(), j5 = 0; j5 < k5; ++j5) {
                    d18 = list2.get(j5).calculateXOffset(axisalignedbb5, d18);
                }
                axisalignedbb5 = axisalignedbb5.offset(d18, 0.0, 0.0);
                double d19 = d9;
                for (int i6 = list2.size(), l8 = 0; l8 < i6; ++l8) {
                    d19 = list2.get(l8).calculateZOffset(axisalignedbb5, d19);
                }
                axisalignedbb5 = axisalignedbb5.offset(0.0, 0.0, d19);
                final double d20 = d15 * d15 + d16 * d16;
                final double d21 = d18 * d18 + d19 * d19;
                if (d20 > d21) {
                    x = d15;
                    z = d16;
                    y = -d14;
                    this.setEntityBoundingBox(axisalignedbb3);
                }
                else {
                    x = d18;
                    z = d19;
                    y = -d17;
                    this.setEntityBoundingBox(axisalignedbb5);
                }
                for (int k6 = list2.size(), j6 = 0; j6 < k6; ++j6) {
                    y = list2.get(j6).calculateYOffset(this.getEntityBoundingBox(), y);
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
                if (d11 * d11 + d13 * d13 >= x * x + z * z) {
                    x = d11;
                    y = d12;
                    z = d13;
                    this.setEntityBoundingBox(axisalignedbb2);
                }
                else {
                    final StepEvent postEvent = new StepEvent(1, _this);
                    MinecraftForge.EVENT_BUS.post((Event)postEvent);
                }
            }
            this.world.profiler.endSection();
            this.world.profiler.startSection("rest");
            this.resetPositionToBB();
            this.collidedHorizontally = (d7 != x || d9 != z);
            this.collidedVertically = (d8 != y);
            this.onGround = (this.collidedVertically && d8 < 0.0);
            this.collided = (this.collidedHorizontally || this.collidedVertically);
            final int j7 = MathHelper.floor(this.posX);
            final int i7 = MathHelper.floor(this.posY - 0.20000000298023224);
            final int k7 = MathHelper.floor(this.posZ);
            BlockPos blockpos = new BlockPos(j7, i7, k7);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            final BlockPos blockpos2;
            final IBlockState iblockstate2;
            final Block block1;
            if (iblockstate.getMaterial() == Material.AIR && ((block1 = (iblockstate2 = this.world.getBlockState(blockpos2 = blockpos.down())).getBlock()) instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)) {
                iblockstate = iblockstate2;
                blockpos = blockpos2;
            }
            this.updateFallState(y, this.onGround, iblockstate, blockpos);
            if (d7 != x) {
                this.motionX = 0.0;
            }
            if (d9 != z) {
                this.motionZ = 0.0;
            }
            final Block block2 = iblockstate.getBlock();
            if (d8 != y) {
                block2.onLanded(this.world, _this);
            }
            if (this.canTriggerWalking() && (!this.onGround || !this.isSneaking() || !(_this instanceof EntityPlayer)) && !this.isRiding()) {
                final double d22 = this.posX - d4;
                double d23 = this.posY - d5;
                final double d24 = this.posZ - d6;
                if (block2 != Blocks.LADDER) {
                    d23 = 0.0;
                }
                if (block2 != null && this.onGround) {
                    block2.onEntityWalk(this.world, blockpos, _this);
                }
                this.distanceWalkedModified += (float)(MathHelper.sqrt(d22 * d22 + d24 * d24) * 0.6);
                this.distanceWalkedOnStepModified += (float)(MathHelper.sqrt(d22 * d22 + d23 * d23 + d24 * d24) * 0.6);
                if (this.distanceWalkedOnStepModified > this.nextStepDistance && iblockstate.getMaterial() != Material.AIR) {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
                    if (this.isInWater()) {
                        final Entity entity = (this.isBeingRidden() && this.getControllingPassenger() != null) ? this.getControllingPassenger() : _this;
                        final float f = (entity == _this) ? 0.35f : 0.4f;
                        float f2 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224 + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224) * f;
                        if (f2 > 1.0f) {
                            f2 = 1.0f;
                        }
                        this.playSound(this.getSwimSound(), f2, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                    }
                    else {
                        this.playStepSound(blockpos, block2);
                    }
                }
                else if (this.distanceWalkedOnStepModified > this.nextFlap && this.makeFlySound() && iblockstate.getMaterial() == Material.AIR) {
                    this.nextFlap = this.playFlySound(this.distanceWalkedOnStepModified);
                }
            }
            try {
                this.doBlockCollisions();
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag2 = this.isWet();
            if (this.world.isFlammableWithin(this.getEntityBoundingBox().shrink(0.001))) {
                this.dealFireDamage(1);
                if (!flag2) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0) {
                this.fire = -this.getFireImmuneTicks();
            }
            if (flag2 && this.isBurning()) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7f, 1.6f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                this.fire = -this.getFireImmuneTicks();
            }
            this.world.profiler.endSection();
        }
    }
    
    @Redirect(method = { "onEntityUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getMaxInPortalTime()I"))
    private int getMaxInPortalTimeHook(final Entity entity) {
        int time = this.getMaxInPortalTime();
        if (BetterPortals.getInstance().isOn() && BetterPortals.getInstance().fastPortal.getValue()) {
            time = BetterPortals.getInstance().time.getValue();
        }
        return time;
    }
    
    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(final Entity entity, final double x, final double y, final double z) {
        final PushEvent event = new PushEvent(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            entity.motionX += event.x;
            entity.motionY += event.y;
            entity.motionZ += event.z;
            entity.isAirBorne = event.airbone;
        }
    }
}
