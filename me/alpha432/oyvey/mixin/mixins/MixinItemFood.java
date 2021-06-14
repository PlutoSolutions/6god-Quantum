// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.alpha432.oyvey.features.modules.combat.Offhand;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemFood.class })
public class MixinItemFood
{
    @Inject(method = { "onItemUseFinish" }, at = { @At("RETURN") }, cancellable = true)
    public void onItemUseFinishHook(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving, final CallbackInfoReturnable<ItemStack> info) {
        Offhand.getInstance().onItemFinish(stack, entityLiving);
    }
}
