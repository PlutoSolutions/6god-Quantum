//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.client.Minecraft;
import java.util.Arrays;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockAir;
import java.util.List;
import me.alpha432.oyvey.mixin.mixins.accessors.IEntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.awt.Color;
import net.minecraft.util.MovementInput;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import me.alpha432.oyvey.OyVey;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class EntityUtil implements Util
{
    public static final Vec3d[] antiDropOffsetList;
    public static final Vec3d[] platformOffsetList;
    public static final Vec3d[] legOffsetList;
    public static final Vec3d[] doubleLegOffsetList;
    public static final Vec3d[] OffsetList;
    public static final Vec3d[] headpiece;
    public static final Vec3d[] offsetsNoHead;
    public static final Vec3d[] antiStepOffsetList;
    public static final Vec3d[] antiScaffoldOffsetList;
    
    public static void attackEntity(final Entity entity, final boolean packet, final boolean swingArm) {
        if (packet) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        }
        else {
            EntityUtil.mc.playerController.attackEntity((EntityPlayer)EntityUtil.mc.player, entity);
        }
        if (swingArm) {
            EntityUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
    
    public static boolean isSafeOy(final Entity entity, final int height, final boolean floor) {
        return getUnsafeBlocks2(entity, height, floor).size() == 0;
    }
    
    public static boolean isSafeOy(final Entity entity) {
        return isSafeOy(entity, 0, false);
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float partialTicks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, partialTicks));
    }
    
    public static Vec3d getInterpolatedRenderPos(final Entity entity, final float partialTicks) {
        return getInterpolatedPos(entity, partialTicks).subtract(EntityUtil.mc.getRenderManager().renderPosX, EntityUtil.mc.getRenderManager().renderPosY, EntityUtil.mc.getRenderManager().renderPosZ);
    }
    
    public static Vec3d getInterpolatedRenderPos(final Vec3d vec) {
        return new Vec3d(vec.x, vec.y, vec.z).subtract(EntityUtil.mc.getRenderManager().renderPosX, EntityUtil.mc.getRenderManager().renderPosY, EntityUtil.mc.getRenderManager().renderPosZ);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double x, final double y, final double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final Vec3d vec) {
        return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final float partialTicks) {
        return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
    }
    
    public static boolean isPassive(final Entity entity) {
        return (!(entity instanceof EntityWolf) || !((EntityWolf)entity).isAngry()) && (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid || (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).getRevengeTarget() == null));
    }
    
    public static boolean stopSneaking(final boolean isSneaking) {
        if (isSneaking && EntityUtil.mc.player != null) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)EntityUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return false;
    }
    
    public static BlockPos getPlayerPos(final EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }
    
    public static boolean isMobAggressive(final Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry()) {
                return true;
            }
        }
        else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf)entity).isAngry() && !EntityUtil.mc.player.equals((Object)((EntityWolf)entity).getOwner());
            }
            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman)entity).isScreaming();
            }
        }
        return isHostileMob(entity);
    }
    
    public static boolean isNeutralMob(final Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }
    
    public static boolean isProjectile(final Entity entity) {
        return entity instanceof EntityShulkerBullet || entity instanceof EntityFireball;
    }
    
    public static boolean isVehicle(final Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }
    
    public static boolean isFriendlyMob(final Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity)) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || (isNeutralMob(entity) && !isMobAggressive(entity));
    }
    
    public static boolean isHostileMob(final Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity);
    }
    
    public static boolean isInHole(final Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }
    
    public static boolean isBlockValid(final BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }
    
    public static boolean isObbyHole(final BlockPos blockPos) {
        final BlockPos[] array;
        final BlockPos[] touchingBlocks = array = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() };
        for (final BlockPos pos : array) {
            final IBlockState touchingState = EntityUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBedrockHole(final BlockPos blockPos) {
        final BlockPos[] array;
        final BlockPos[] touchingBlocks = array = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() };
        for (final BlockPos pos : array) {
            final IBlockState touchingState = EntityUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBothHole(final BlockPos blockPos) {
        final BlockPos[] array;
        final BlockPos[] touchingBlocks = array = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() };
        for (final BlockPos pos : array) {
            final IBlockState touchingState = EntityUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN)) {
                return false;
            }
        }
        return true;
    }
    
    public static double getDst(final Vec3d vec) {
        return EntityUtil.mc.player.getPositionVector().distanceTo(vec);
    }
    
    public static boolean isInWater(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY + 0.01;
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int)y, z);
                if (EntityUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isDrivenByPlayer(final Entity entityIn) {
        return EntityUtil.mc.player != null && entityIn != null && entityIn.equals((Object)EntityUtil.mc.player.getRidingEntity());
    }
    
    public static boolean isPlayer(final Entity entity) {
        return entity instanceof EntityPlayer;
    }
    
    public static boolean isAboveWater(final Entity entity) {
        return isAboveWater(entity, false);
    }
    
    public static boolean isAboveWater(final Entity entity, final boolean packet) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY - (packet ? 0.03 : (isPlayer(entity) ? 0.2 : 0.5));
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (EntityUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Vec3d[] getHeightOffsets(final int min, final int max) {
        final ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        for (int i = min; i <= max; ++i) {
            offsets.add(new Vec3d(0.0, (double)i, 0.0));
        }
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static BlockPos getRoundedBlockPos(final Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.getPositionVector(), 0));
    }
    
    public static boolean isLiving(final Entity entity) {
        return entity instanceof EntityLivingBase;
    }
    
    public static boolean isAlive(final Entity entity) {
        return isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }
    
    public static boolean isDead(final Entity entity) {
        return !isAlive(entity);
    }
    
    public static float getHealth(final Entity entity) {
        if (isLiving(entity)) {
            final EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }
    
    public static float getHealth(final Entity entity, final boolean absorption) {
        if (isLiving(entity)) {
            final EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + (absorption ? livingBase.getAbsorptionAmount() : 0.0f);
        }
        return 0.0f;
    }
    
    public static boolean canEntityFeetBeSeen(final Entity entityIn) {
        return EntityUtil.mc.world.rayTraceBlocks(new Vec3d(EntityUtil.mc.player.posX, EntityUtil.mc.player.posX + EntityUtil.mc.player.getEyeHeight(), EntityUtil.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
    
    public static boolean isntValid(final Entity entity, final double range) {
        return entity == null || isDead(entity) || entity.equals((Object)EntityUtil.mc.player) || (entity instanceof EntityPlayer && OyVey.friendManager.isFriend(entity.getName())) || EntityUtil.mc.player.getDistanceSq(entity) > MathUtil.square(range);
    }
    
    public static boolean isValid(final Entity entity, final double range) {
        return !isntValid(entity, range);
    }
    
    public static boolean holdingWeapon(final EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof ItemSword || player.getHeldItemMainhand().getItem() instanceof ItemAxe;
    }
    
    public static double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (EntityUtil.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(1)))) {
            maxModifier *= 1.0 + 0.2 * (Objects.requireNonNull(EntityUtil.mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier() + 1);
        }
        return maxModifier;
    }
    
    public static void mutliplyEntitySpeed(final Entity entity, final double multiplier) {
        if (entity != null) {
            entity.motionX *= multiplier;
            entity.motionZ *= multiplier;
        }
    }
    
    public static boolean isEntityMoving(final Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return EntityUtil.mc.gameSettings.keyBindForward.isKeyDown() || EntityUtil.mc.gameSettings.keyBindBack.isKeyDown() || EntityUtil.mc.gameSettings.keyBindLeft.isKeyDown() || EntityUtil.mc.gameSettings.keyBindRight.isKeyDown();
        }
        return entity.motionX != 0.0 || entity.motionY != 0.0 || entity.motionZ != 0.0;
    }
    
    public static double getEntitySpeed(final Entity entity) {
        if (entity != null) {
            final double distTraveledLastTickX = entity.posX - entity.prevPosX;
            final double distTraveledLastTickZ = entity.posZ - entity.prevPosZ;
            final double speed = MathHelper.sqrt(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ);
            return speed * 20.0;
        }
        return 0.0;
    }
    
    public static boolean is32k(final ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) >= 1000;
    }
    
    public static void moveEntityStrafe(final double speed, final Entity entity) {
        if (entity != null) {
            final MovementInput movementInput = EntityUtil.mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = EntityUtil.mc.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                entity.motionX = 0.0;
                entity.motionZ = 0.0;
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                entity.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                entity.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }
    
    public static boolean rayTraceHitCheck(final Entity entity, final boolean shouldCheck) {
        return !shouldCheck || EntityUtil.mc.player.canEntityBeSeen(entity);
    }
    
    public static Color getColor(final Entity entity, final int red, final int green, final int blue, final int alpha, final boolean colorFriends) {
        Color color = new Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
        if (entity instanceof EntityPlayer && colorFriends && OyVey.friendManager.isFriend((EntityPlayer)entity)) {
            color = new Color(0.33333334f, 1.0f, 1.0f, alpha / 255.0f);
        }
        return color;
    }
    
    public static boolean isMoving() {
        return EntityUtil.mc.player.moveForward != 0.0 || EntityUtil.mc.player.moveStrafing != 0.0;
    }
    
    public static EntityPlayer getClosestEnemy(final double distance) {
        EntityPlayer closest = null;
        for (final EntityPlayer player : EntityUtil.mc.world.playerEntities) {
            if (isntValid((Entity)player, distance)) {
                continue;
            }
            if (closest == null) {
                closest = player;
            }
            else {
                if (EntityUtil.mc.player.getDistanceSq((Entity)player) >= EntityUtil.mc.player.getDistanceSq((Entity)closest)) {
                    continue;
                }
                closest = player;
            }
        }
        return closest;
    }
    
    public static boolean checkCollide() {
        return !EntityUtil.mc.player.isSneaking() && (EntityUtil.mc.player.getRidingEntity() == null || EntityUtil.mc.player.getRidingEntity().fallDistance < 3.0f) && EntityUtil.mc.player.fallDistance < 3.0f;
    }
    
    public static BlockPos getPlayerPosWithEntity() {
        return new BlockPos((EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posX : EntityUtil.mc.player.posX, (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posY : EntityUtil.mc.player.posY, (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posZ : EntityUtil.mc.player.posZ);
    }
    
    public static double[] forward(final double speed) {
        float forward = EntityUtil.mc.player.movementInput.moveForward;
        float side = EntityUtil.mc.player.movementInput.moveStrafe;
        float yaw = EntityUtil.mc.player.prevRotationYaw + (EntityUtil.mc.player.rotationYaw - EntityUtil.mc.player.prevRotationYaw) * EntityUtil.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static Map<String, Integer> getTextRadarPlayers() {
        Map<String, Integer> output = new HashMap<String, Integer>();
        final DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        final DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        final StringBuilder healthSB = new StringBuilder();
        final StringBuilder distanceSB = new StringBuilder();
        for (final EntityPlayer player : EntityUtil.mc.world.playerEntities) {
            if (!player.isInvisible()) {
                if (player.getName().equals(EntityUtil.mc.player.getName())) {
                    continue;
                }
                final int hpRaw = (int)getHealth((Entity)player);
                final String hp = dfHealth.format(hpRaw);
                healthSB.append("\u00c2§");
                if (hpRaw >= 20) {
                    healthSB.append("a");
                }
                else if (hpRaw >= 10) {
                    healthSB.append("e");
                }
                else if (hpRaw >= 5) {
                    healthSB.append("6");
                }
                else {
                    healthSB.append("c");
                }
                healthSB.append(hp);
                final int distanceInt = (int)EntityUtil.mc.player.getDistance((Entity)player);
                final String distance = dfDistance.format(distanceInt);
                distanceSB.append("\u00c2§");
                if (distanceInt >= 25) {
                    distanceSB.append("a");
                }
                else if (distanceInt > 10) {
                    distanceSB.append("6");
                }
                else {
                    distanceSB.append("c");
                }
                distanceSB.append(distance);
                output.put(healthSB.toString() + " " + (OyVey.friendManager.isFriend(player) ? ChatFormatting.AQUA : ChatFormatting.RED) + player.getName() + " " + distanceSB.toString() + " \u00c2§f0", (int)EntityUtil.mc.player.getDistance((Entity)player));
                healthSB.setLength(0);
                distanceSB.setLength(0);
            }
        }
        if (!output.isEmpty()) {
            output = MathUtil.sortByValue(output, false);
        }
        return output;
    }
    
    public static boolean isAboveBlock(final Entity entity, final BlockPos blockPos) {
        return entity.posY >= blockPos.getY();
    }
    
    public static boolean isCrystalAtFeet(final EntityEnderCrystal crystal, final double range) {
        for (final EntityPlayer player : EntityUtil.mc.world.playerEntities) {
            if (EntityUtil.mc.player.getDistanceSq((Entity)player) > range * range) {
                continue;
            }
            if (OyVey.friendManager.isFriend(player)) {
                continue;
            }
            for (final Vec3d vec : EntityUtil.doubleLegOffsetList) {
                if (new BlockPos(player.getPositionVector()).add(vec.x, vec.y, vec.z) == crystal.getPosition()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void swingArmNoPacket(final EnumHand hand, final EntityLivingBase entity) {
        final ItemStack stack = entity.getHeldItem(hand);
        if (!stack.isEmpty() && stack.getItem().onEntitySwing(entity, stack)) {
            return;
        }
        if (!entity.isSwingInProgress || entity.swingProgressInt >= ((IEntityLivingBase)entity).getArmSwingAnimationEnd() / 2 || entity.swingProgressInt < 0) {
            entity.swingProgressInt = -1;
            entity.isSwingInProgress = true;
            entity.swingingHand = hand;
        }
    }
    
    public static boolean isSafe2(final Entity entity, final int height, final boolean floor, final boolean face) {
        return getUnsafeBlocks2(entity, height, floor).size() == 0;
    }
    
    public static List<Vec3d> getUnsafeBlocks2(final Entity entity, final int height, final boolean floor) {
        return getUnsafeBlocksFromVec3d2(entity.getPositionVector(), height, floor);
    }
    
    public static List<Vec3d> getUnsafeBlocksFromVec3d2(final Vec3d pos, final int height, final boolean floor) {
        final ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        for (final Vec3d vector : getOffsets2(height, floor)) {
            final BlockPos targetPos = new BlockPos(pos).add(vector.x, vector.y, vector.z);
            final Block block = EntityUtil.mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
                vec3ds.add(vector);
            }
        }
        return vec3ds;
    }
    
    public static List<Vec3d> getOffsetList2(final int y, final boolean floor) {
        final ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        offsets.add(new Vec3d(-1.0, (double)y, 0.0));
        offsets.add(new Vec3d(1.0, (double)y, 0.0));
        offsets.add(new Vec3d(0.0, (double)y, -1.0));
        offsets.add(new Vec3d(0.0, (double)y, 1.0));
        if (floor) {
            offsets.add(new Vec3d(0.0, (double)(y - 1), 0.0));
        }
        return offsets;
    }
    
    public static Vec3d[] getOffsets2(final int y, final boolean floor) {
        final List<Vec3d> offsets = getOffsetList2(y, floor);
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static boolean isSafe2(final Entity entity) {
        return isSafe2(entity, 0, false, true);
    }
    
    public static Vec3d[] getUnsafeBlockArrayFromVec3d2(final Vec3d pos, final int height, final boolean floor) {
        final List<Vec3d> list = getUnsafeBlocksFromVec3d2(pos, height, floor);
        final Vec3d[] array = new Vec3d[list.size()];
        return list.toArray(array);
    }
    
    public static Vec3d[] getUnsafeBlockArray2(final Entity entity, final int height, final boolean floor) {
        final List<Vec3d> list = getUnsafeBlocks2(entity, height, floor);
        final Vec3d[] array = new Vec3d[list.size()];
        return list.toArray(array);
    }
    
    public static List<Vec3d> targets2(final Vec3d vec3d, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean raytrace) {
        final ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (antiDrop) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiDropOffsetList));
        }
        if (platform) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.platformOffsetList));
        }
        if (legs) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.legOffsetList));
        }
        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.OffsetList));
        if (antiStep) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiStepOffsetList));
        }
        else {
            final List<Vec3d> vec3ds = getUnsafeBlocksFromVec3d2(vec3d, 2, false);
            if (vec3ds.size() == 4) {
                for (final Vec3d vector : vec3ds) {
                    final BlockPos position = new BlockPos(vec3d).add(vector.x, vector.y, vector.z);
                    switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
                        case -1:
                        case 1:
                        case 2: {
                            continue;
                        }
                        case 3: {
                            placeTargets.add(vec3d.add(vector));
                            break;
                        }
                    }
                    if (antiScaffold) {
                        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiScaffoldOffsetList));
                    }
                    return placeTargets;
                }
            }
        }
        if (antiScaffold) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiScaffoldOffsetList));
        }
        return placeTargets;
    }
    
    public static boolean isTrapped2(final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        return getUntrappedBlocks2(player, antiScaffold, antiStep, legs, platform, antiDrop).size() == 0;
    }
    
    public static List<Vec3d> getUntrappedBlocks2(final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        final ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        if (!antiStep && getUnsafeBlocks2((Entity)player, 2, false).size() == 4) {
            vec3ds.addAll(getUnsafeBlocks2((Entity)player, 2, false));
        }
        for (int i = 0; i < getTrapOffsets2(antiScaffold, antiStep, legs, platform, antiDrop).length; ++i) {
            final Vec3d vector = getTrapOffsets2(antiScaffold, antiStep, legs, platform, antiDrop)[i];
            final BlockPos targetPos = new BlockPos(player.getPositionVector()).add(vector.x, vector.y, vector.z);
            final Block block = EntityUtil.mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
                vec3ds.add(vector);
            }
        }
        return vec3ds;
    }
    
    public static Vec3d[] getTrapOffsets2(final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        final List<Vec3d> offsets = getTrapOffsetsList2(antiScaffold, antiStep, legs, platform, antiDrop);
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static List<Vec3d> getTrapOffsetsList2(final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        final ArrayList<Vec3d> offsets = new ArrayList<Vec3d>(getOffsetList2(1, false));
        offsets.add(new Vec3d(0.0, 2.0, 0.0));
        if (antiScaffold) {
            offsets.add(new Vec3d(0.0, 3.0, 0.0));
        }
        if (antiStep) {
            offsets.addAll(getOffsetList2(2, false));
        }
        if (legs) {
            offsets.addAll(getOffsetList2(0, false));
        }
        if (platform) {
            offsets.addAll(getOffsetList2(-1, false));
            offsets.add(new Vec3d(0.0, -1.0, 0.0));
        }
        if (antiDrop) {
            offsets.add(new Vec3d(0.0, -2.0, 0.0));
        }
        return offsets;
    }
    
    public static boolean isInLiquid() {
        if (EntityUtil.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        boolean inLiquid = false;
        final AxisAlignedBB bb = (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().getEntityBoundingBox() : EntityUtil.mc.player.getEntityBoundingBox();
        final int y = (int)bb.minY;
        for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; ++x) {
            for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; ++z) {
                final Block block = EntityUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (!(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
    
    public static boolean isAboveLiquid(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double n = entity.posY + 0.01;
        for (int i = MathHelper.floor(entity.posX); i < MathHelper.ceil(entity.posX); ++i) {
            for (int j = MathHelper.floor(entity.posZ); j < MathHelper.ceil(entity.posZ); ++j) {
                if (EntityUtil.mc.world.getBlockState(new BlockPos(i, (int)n, j)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isOnLiquid(final double offset) {
        if (EntityUtil.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        final AxisAlignedBB bb = (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -offset, 0.0) : EntityUtil.mc.player.getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -offset, 0.0);
        boolean onLiquid = false;
        final int y = (int)bb.minY;
        for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX + 1.0); ++x) {
            for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ + 1.0); ++z) {
                final Block block = EntityUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != Blocks.AIR) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public static boolean checkForLiquid(final Entity entity, final boolean b) {
        if (entity == null) {
            return false;
        }
        final double posY = entity.posY;
        double n;
        if (b) {
            n = 0.03;
        }
        else if (entity instanceof EntityPlayer) {
            n = 0.2;
        }
        else {
            n = 0.5;
        }
        final double n2 = posY - n;
        for (int i = MathHelper.floor(entity.posX); i < MathHelper.ceil(entity.posX); ++i) {
            for (int j = MathHelper.floor(entity.posZ); j < MathHelper.ceil(entity.posZ); ++j) {
                if (EntityUtil.mc.world.getBlockState(new BlockPos(i, MathHelper.floor(n2), j)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isOnLiquid() {
        final double y = EntityUtil.mc.player.posY - 0.03;
        for (int x = MathHelper.floor(EntityUtil.mc.player.posX); x < MathHelper.ceil(EntityUtil.mc.player.posX); ++x) {
            for (int z = MathHelper.floor(EntityUtil.mc.player.posZ); z < MathHelper.ceil(EntityUtil.mc.player.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (EntityUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Vec3d[] getOffsets(final int y, final boolean floor, final boolean face) {
        final List<Vec3d> offsets = getOffsetList(y, floor, face);
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static List<Vec3d> getOffsetList(final int y, final boolean floor, final boolean face) {
        final List<Vec3d> offsets = new ArrayList<Vec3d>();
        if (face) {
            offsets.add(new Vec3d(-1.0, (double)y, 0.0));
            offsets.add(new Vec3d(1.0, (double)y, 0.0));
            offsets.add(new Vec3d(0.0, (double)y, -1.0));
            offsets.add(new Vec3d(0.0, (double)y, 1.0));
        }
        else {
            offsets.add(new Vec3d(-1.0, (double)y, 0.0));
        }
        if (floor) {
            offsets.add(new Vec3d(0.0, (double)(y - 1), 0.0));
        }
        return offsets;
    }
    
    public static List<Vec3d> getTrapOffsetsList(final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean face) {
        final List<Vec3d> offsets = new ArrayList<Vec3d>(getOffsetList(1, false, face));
        offsets.add(new Vec3d(0.0, 2.0, 0.0));
        if (antiScaffold) {
            offsets.add(new Vec3d(0.0, 3.0, 0.0));
        }
        if (antiStep) {
            offsets.addAll(getOffsetList(2, false, face));
        }
        if (legs) {
            offsets.addAll(getOffsetList(0, false, face));
        }
        if (platform) {
            offsets.addAll(getOffsetList(-1, false, face));
            offsets.add(new Vec3d(0.0, -1.0, 0.0));
        }
        if (antiDrop) {
            offsets.add(new Vec3d(0.0, -2.0, 0.0));
        }
        return offsets;
    }
    
    public static Vec3d[] getTrapOffsets(final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean face) {
        final List<Vec3d> offsets = getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop, face);
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static List<Vec3d> getUntrappedBlocks(final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean face) {
        final List<Vec3d> vec3ds = new ArrayList<Vec3d>();
        if (!antiStep && getUnsafeBlocks((Entity)player, 2, false, face).size() == 4) {
            vec3ds.addAll(getUnsafeBlocks((Entity)player, 2, false, face));
        }
        for (int i = 0; i < getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face).length; ++i) {
            final Vec3d vector = getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face)[i];
            final BlockPos targetPos = new BlockPos(player.getPositionVector()).add(vector.x, vector.y, vector.z);
            final Block block = EntityUtil.mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
                vec3ds.add(vector);
            }
        }
        return vec3ds;
    }
    
    public static List<Vec3d> getUnsafeBlocks(final Entity entity, final int height, final boolean floor, final boolean face) {
        return getUnsafeBlocksFromVec3d(entity.getPositionVector(), height, floor, face);
    }
    
    public static List<Vec3d> getUnsafeBlocksFromVec3d(final Vec3d pos, final int height, final boolean floor, final boolean face) {
        final List<Vec3d> vec3ds = new ArrayList<Vec3d>();
        for (final Vec3d vector : getOffsets(height, floor, face)) {
            final BlockPos targetPos = new BlockPos(pos).add(vector.x, vector.y, vector.z);
            final Block block = EntityUtil.mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
                vec3ds.add(vector);
            }
        }
        return vec3ds;
    }
    
    public static List<Vec3d> targets(final Vec3d vec3d, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean raytrace, final boolean face) {
        final List<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (antiDrop) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiDropOffsetList));
        }
        if (platform) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.platformOffsetList));
        }
        if (legs) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.legOffsetList));
        }
        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.OffsetList));
        if (antiStep) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiStepOffsetList));
        }
        else {
            final List<Vec3d> vec3ds = getUnsafeBlocksFromVec3d(vec3d, 2, false, face);
            if (vec3ds.size() == 4) {
                for (final Vec3d vector : vec3ds) {
                    final BlockPos position = new BlockPos(vec3d).add(vector.x, vector.y, vector.z);
                    switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
                        case -1:
                        case 1:
                        case 2: {
                            continue;
                        }
                        case 3: {
                            placeTargets.add(vec3d.add(vector));
                            break;
                        }
                    }
                    break;
                }
            }
        }
        if (antiScaffold) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiScaffoldOffsetList));
        }
        if (!face) {
            final List<Vec3d> offsets = new ArrayList<Vec3d>();
            offsets.add(new Vec3d(1.0, 1.0, 0.0));
            offsets.add(new Vec3d(0.0, 1.0, -1.0));
            offsets.add(new Vec3d(0.0, 1.0, 1.0));
            final Vec3d[] array = new Vec3d[offsets.size()];
            placeTargets.removeAll(Arrays.asList(BlockUtil.convertVec3ds(vec3d, offsets.toArray(array))));
        }
        return placeTargets;
    }
    
    public static List<Vec3d> getUntrappedBlocksExtended(final int extension, final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean raytrace, final boolean noScaffoldExtend, final boolean face) {
        final List<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (extension == 1) {
            placeTargets.addAll(targets(player.getPositionVector(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace, face));
        }
        else {
            int extend = 1;
            for (final Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
                if (extend > extension) {
                    break;
                }
                placeTargets.addAll(targets(vec3d, !noScaffoldExtend, antiStep, legs, platform, antiDrop, raytrace, face));
                ++extend;
            }
        }
        final List<Vec3d> removeList = new ArrayList<Vec3d>();
        for (final Vec3d vec3d : placeTargets) {
            final BlockPos pos = new BlockPos(vec3d);
            if (BlockUtil.isPositionPlaceable(pos, raytrace) == -1) {
                removeList.add(vec3d);
            }
        }
        for (final Vec3d vec3d : removeList) {
            placeTargets.remove(vec3d);
        }
        return placeTargets;
    }
    
    public static boolean isSafe(final Entity entity, final int height, final boolean floor, final boolean face) {
        return getUnsafeBlocks(entity, height, floor, face).size() == 0;
    }
    
    public static boolean isSafe(final Entity entity) {
        return isSafe(entity, 0, false, true);
    }
    
    public static boolean isTrappedExtended(final int extension, final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean raytrace, final boolean noScaffoldExtend, final boolean face) {
        return getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace, noScaffoldExtend, face).size() == 0;
    }
    
    public static boolean isTrapped(final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean face) {
        return getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop, face).size() == 0;
    }
    
    public static Vec3d[] getUnsafeBlockArray(final Entity entity, final int height, final boolean floor, final boolean face) {
        final List<Vec3d> list = getUnsafeBlocks(entity, height, floor, face);
        final Vec3d[] array = new Vec3d[list.size()];
        return list.toArray(array);
    }
    
    public static Vec3d[] getUnsafeBlockArrayFromVec3d(final Vec3d pos, final int height, final boolean floor, final boolean face) {
        final List<Vec3d> list = getUnsafeBlocksFromVec3d(pos, height, floor, face);
        final Vec3d[] array = new Vec3d[list.size()];
        return list.toArray(array);
    }
    
    public static void setTimer(final float speed) {
        Minecraft.getMinecraft().timer.tickLength = 50.0f / speed;
    }
    
    public static void resetTimer() {
        Minecraft.getMinecraft().timer.tickLength = 50.0f;
    }
    
    public static EntityPlayer getClosestPlayer(final float range) {
        EntityPlayer player = null;
        double maxDistance = 999.0;
        for (int size = EntityUtil.mc.world.playerEntities.size(), i = 0; i < size; ++i) {
            final EntityPlayer entityPlayer = EntityUtil.mc.world.playerEntities.get(i);
            if (isPlayerValid(entityPlayer, range)) {
                final double distanceSq = EntityUtil.mc.player.getDistanceSq((Entity)entityPlayer);
                if (maxDistance == 999.0 || distanceSq < maxDistance) {
                    maxDistance = distanceSq;
                    player = entityPlayer;
                }
            }
        }
        return player;
    }
    
    public static boolean isPlayerValid(final EntityPlayer player, final float range) {
        return player != EntityUtil.mc.player && EntityUtil.mc.player.getDistance((Entity)player) < range && !isDead((Entity)player) && !OyVey.friendManager.isFriend(player.getName());
    }
    
    public static float calculate(final double posX, final double posY, final double posZ, final EntityLivingBase entity) {
        final double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        return getBlastReduction(entity, getDamageMultiplied(damage), new Explosion((World)EntityUtil.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
    }
    
    public static float getBlockDensity(final Vec3d vec, final AxisAlignedBB bb) {
        final double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        final double d2 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        final double d3 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        final double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        final double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d0 >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int j2 = 0;
            int k2 = 0;
            for (float f = 0.0f; f <= 1.0f; f += (float)d0) {
                for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                    for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                        final double d6 = bb.minX + (bb.maxX - bb.minX) * f;
                        final double d7 = bb.minY + (bb.maxY - bb.minY) * f2;
                        final double d8 = bb.minZ + (bb.maxZ - bb.minZ) * f3;
                        if (rayTraceBlocks(new Vec3d(d6 + d4, d7, d8 + d5), vec) == null) {
                            ++j2;
                        }
                        ++k2;
                    }
                }
            }
            return j2 / (float)k2;
        }
        return 0.0f;
    }
    
    private static float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = EntityUtil.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static RayTraceResult rayTraceBlocks(Vec3d vec31, final Vec3d vec32) {
        final int i = MathHelper.floor(vec32.x);
        final int j = MathHelper.floor(vec32.y);
        final int k = MathHelper.floor(vec32.z);
        int l = MathHelper.floor(vec31.x);
        int i2 = MathHelper.floor(vec31.y);
        int j2 = MathHelper.floor(vec31.z);
        BlockPos blockpos = new BlockPos(l, i2, j2);
        IBlockState iblockstate = EntityUtil.mc.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block.canCollideCheck(iblockstate, false) && block != Blocks.WEB) {
            return iblockstate.collisionRayTrace((World)EntityUtil.mc.world, blockpos, vec31, vec32);
        }
        int k2 = 200;
        final double d6 = vec32.x - vec31.x;
        final double d7 = vec32.y - vec31.y;
        final double d8 = vec32.z - vec31.z;
        while (k2-- >= 0) {
            if (l == i && i2 == j && j2 == k) {
                return null;
            }
            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d9 = 999.0;
            double d10 = 999.0;
            double d11 = 999.0;
            if (i > l) {
                d9 = l + 1.0;
            }
            else if (i < l) {
                d9 = l + 0.0;
            }
            else {
                flag2 = false;
            }
            if (j > i2) {
                d10 = i2 + 1.0;
            }
            else if (j < i2) {
                d10 = i2 + 0.0;
            }
            else {
                flag3 = false;
            }
            if (k > j2) {
                d11 = j2 + 1.0;
            }
            else if (k < j2) {
                d11 = j2 + 0.0;
            }
            else {
                flag4 = false;
            }
            double d12 = 999.0;
            double d13 = 999.0;
            double d14 = 999.0;
            if (flag2) {
                d12 = (d9 - vec31.x) / d6;
            }
            if (flag3) {
                d13 = (d10 - vec31.y) / d7;
            }
            if (flag4) {
                d14 = (d11 - vec31.z) / d8;
            }
            if (d12 == -0.0) {
                d12 = -1.0E-4;
            }
            if (d13 == -0.0) {
                d13 = -1.0E-4;
            }
            if (d14 == -0.0) {
                d14 = -1.0E-4;
            }
            EnumFacing enumfacing;
            if (d12 < d13 && d12 < d14) {
                enumfacing = ((i > l) ? EnumFacing.WEST : EnumFacing.EAST);
                vec31 = new Vec3d(d9, vec31.y + d7 * d12, vec31.z + d8 * d12);
            }
            else if (d13 < d14) {
                enumfacing = ((j > i2) ? EnumFacing.DOWN : EnumFacing.UP);
                vec31 = new Vec3d(vec31.x + d6 * d13, d10, vec31.z + d8 * d13);
            }
            else {
                enumfacing = ((k > j2) ? EnumFacing.NORTH : EnumFacing.SOUTH);
                vec31 = new Vec3d(vec31.x + d6 * d14, vec31.y + d7 * d14, d11);
            }
            l = MathHelper.floor(vec31.x) - ((enumfacing == EnumFacing.EAST) ? 1 : 0);
            i2 = MathHelper.floor(vec31.y) - ((enumfacing == EnumFacing.UP) ? 1 : 0);
            j2 = MathHelper.floor(vec31.z) - ((enumfacing == EnumFacing.SOUTH) ? 1 : 0);
            blockpos = new BlockPos(l, i2, j2);
            iblockstate = EntityUtil.mc.world.getBlockState(blockpos);
            block = iblockstate.getBlock();
            if (block.canCollideCheck(iblockstate, false) && block != Blocks.WEB) {
                return iblockstate.collisionRayTrace((World)EntityUtil.mc.world, blockpos, vec31, vec32);
            }
        }
        return null;
    }
    
    static {
        antiDropOffsetList = new Vec3d[] { new Vec3d(0.0, -2.0, 0.0) };
        platformOffsetList = new Vec3d[] { new Vec3d(0.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(1.0, -1.0, 0.0) };
        legOffsetList = new Vec3d[] { new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0) };
        doubleLegOffsetList = new Vec3d[] { new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(0.0, 0.0, 2.0) };
        OffsetList = new Vec3d[] { new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, 0.0) };
        headpiece = new Vec3d[] { new Vec3d(0.0, 2.0, 0.0) };
        offsetsNoHead = new Vec3d[] { new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0) };
        antiStepOffsetList = new Vec3d[] { new Vec3d(-1.0, 2.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0) };
        antiScaffoldOffsetList = new Vec3d[] { new Vec3d(0.0, 3.0, 0.0) };
    }
}
