//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.gui.components.items.buttons;

import me.alpha432.oyvey.features.setting.Bind;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import me.alpha432.oyvey.Minecraftable;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.setting.Setting;

public class BindButton extends Button
{
    private final Setting setting;
    public boolean isListening;
    
    public BindButton(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077) : (this.isHovering(mouseX, mouseY) ? OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue()) : OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue())));
        if (this.isListening) {
            OyVey.textManager.drawStringWithShadow("Listening...", this.x + 2.3f, this.y - 1.7f - OyVeyGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
        else {
            OyVey.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString(), this.x + 2.3f, this.y - 1.7f - OyVeyGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Minecraftable.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }
    
    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (this.isListening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.getKey() == 211) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            super.onMouseClick();
        }
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }
    
    @Override
    public boolean getState() {
        return !this.isListening;
    }
}
