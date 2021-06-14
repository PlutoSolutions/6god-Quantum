//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.Minecraft;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.alpha432.oyvey.features.modules.client.Chat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.Gui;

@Mixin({ GuiNewChat.class })
public class MixinGuiNewChat extends Gui
{
    @Shadow
    @Final
    public List<ChatLine> drawnChatLines;
    private ChatLine chatLine;
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.drawRect(left, top, right, bottom, (Chat.getInstance().isOn() && Chat.getInstance().clean.getValue()) ? 0 : color);
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadow(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        if (text.contains("§+")) {
            final float colorSpeed = (float)(101 - ClickGui.getInstance().rainbowHue.getValue());
            OyVey.textManager.drawRainbowString(text, x, y, 100, 1.0f, true);
        }
        else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return 0;
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false))
    public int drawnChatLinesSize(final List<ChatLine> list) {
        return (Chat.getInstance().isOn() && Chat.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2, remap = false))
    public int chatLinesSize(final List<ChatLine> list) {
        return (Chat.getInstance().isOn() && Chat.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
}
