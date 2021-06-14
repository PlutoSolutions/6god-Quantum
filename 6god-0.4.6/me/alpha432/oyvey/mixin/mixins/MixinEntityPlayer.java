//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.movement.Phase;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import me.alpha432.oyvey.features.modules.misc.BetterPortals;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.alpha432.oyvey.event.events.PlayerJumpEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.alpha432.oyvey.OyVey;
import net.minecraft.entity.SharedMonsterAttributes;
import me.alpha432.oyvey.features.modules.player.TpsSync;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    EntityPlayer player;
    
    public MixinEntityPlayer(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn);
    }
    
    @Inject(method = { "getCooldownPeriod" }, at = { @At("HEAD") }, cancellable = true)
    private void getCooldownPeriodHook(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (TpsSync.getInstance().isOn() && TpsSync.getInstance().attack.getValue()) {
            callbackInfoReturnable.setReturnValue((float)(1.0 / EntityPlayer.class.cast(this).getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue() * 20.0 * OyVey.serverManager.getTpsFactor()));
        }
    }
    
    @Inject(method = { "jump" }, at = { @At("HEAD") }, cancellable = true)
    public void onJump(final CallbackInfo ci) {
        if (Minecraft.getMinecraft().player.getName() == this.getName()) {
            MinecraftForge.EVENT_BUS.post((Event)new PlayerJumpEvent(this.motionX, this.motionY));
        }
    }
    
    @ModifyConstant(method = { "getPortalCooldown" }, constant = { @Constant(intValue = 10) })
    private int getPortalCooldownHook(final int cooldown) {
        int time = cooldown;
        if (BetterPortals.getInstance().isOn() && BetterPortals.getInstance().fastPortal.getValue()) {
            time = BetterPortals.getInstance().cooldown.getValue();
        }
        return time;
    }
    
    @Inject(method = { "isEntityInsideOpaqueBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void isEntityInsideOpaqueBlockHook(final CallbackInfoReturnable<Boolean> info) {
        if (Phase.getInstance().isOn()) {
            info.setReturnValue(false);
        }
        else if (Phase.getInstance().mode.getValue() == Phase.Mode.Packetfly && Phase.getInstance().isOn()) {
            info.setReturnValue(false);
        }
    }
}
