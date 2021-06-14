//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.gui.components;

import me.alpha432.oyvey.features.gui.components.items.buttons.Button;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import java.util.Iterator;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.gui.components.items.Item;
import java.util.ArrayList;
import me.alpha432.oyvey.features.Feature;

public class Component extends Feature
{
    public static int[] counter1;
    private final ArrayList<Item> items;
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private final int barHeight;
    private boolean hidden;
    private int startcolor;
    private int endcolor;
    
    public Component(final String name, final int x, final int y, final boolean open) {
        super(name);
        this.items = new ArrayList<Item>();
        this.hidden = false;
        this.hidden = false;
        this.x = x;
        this.y = y;
        this.width = 88;
        this.height = 18;
        this.barHeight = 15;
        this.open = open;
        this.setupItems();
    }
    
    public void setupItems() {
    }
    
    private void drag(final int mouseX, final int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drag(mouseX, mouseY);
        Component.counter1 = new int[] { 1 };
        final float totalItemHeight = this.open ? (this.getTotalItemHeight() - 2.0f) : 0.0f;
        if (ClickGui.getInstance().rainbowg.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.startcolor = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
                this.endcolor = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
            }
        }
        else {
            this.startcolor = ColorUtil.toRGBA(ClickGui.getInstance().g_red.getValue(), ClickGui.getInstance().g_green.getValue(), ClickGui.getInstance().g_blue.getValue(), ClickGui.getInstance().g_alpha.getValue());
        }
        this.endcolor = ColorUtil.toRGBA(ClickGui.getInstance().g_red1.getValue(), ClickGui.getInstance().g_green1.getValue(), ClickGui.getInstance().g_blue1.getValue(), ClickGui.getInstance().g_alpha1.getValue());
        RenderUtil.drawRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height - 5), ColorUtil.toRGBA(116, 75, 229, 255));
        RenderUtil.drawGradientSideways(this.x - 1, this.y, this.x + this.width + 1, this.y + this.barHeight - 2.0f, this.startcolor, this.endcolor);
        if (this.open) {
            RenderUtil.drawGradientSideways(this.x - 1, this.y + 13.2f, this.x + this.width + 1, this.y + totalItemHeight + 19.0f, this.startcolor, this.endcolor);
            RenderUtil.drawRect((float)this.x, this.y + 13.2f, (float)(this.x + this.width), this.y + this.height + totalItemHeight, ColorUtil.toRGBA(0, 0, 0, ClickGui.getInstance().alphaBox.getValue()));
        }
        OyVey.textManager.drawStringWithShadow(this.getName(), this.x + 3.0f, this.y - 4.0f - OyVeyGui.getClickGui().getTextOffset(), -1);
        if (this.open) {
            float y = this.getY() + this.getHeight() - 3.0f;
            for (final Item item : this.getItems()) {
                final int[] counter1 = Component.counter1;
                final int n = 0;
                ++counter1[n];
                if (item.isHidden()) {
                    continue;
                }
                item.setLocation(this.x + 2.0f, y);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += item.getHeight() + 1.5f;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            OyVeyGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
                return;
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            Component.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }
    
    public void addButton(final Button button) {
        this.items.add(button);
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public final ArrayList<Item> getItems() {
        return this.items;
    }
    
    private boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }
    
    private float getTotalItemHeight() {
        float height = 0.0f;
        for (final Item item : this.getItems()) {
            height += item.getHeight() + 1.5f;
        }
        return height;
    }
    
    static {
        Component.counter1 = new int[] { 1 };
        Component.counter1 = new int[] { 1 };
    }
}
