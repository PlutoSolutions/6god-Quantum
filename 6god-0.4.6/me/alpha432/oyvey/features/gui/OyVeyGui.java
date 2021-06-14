//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.gui;

import net.minecraft.util.ResourceLocation;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import java.util.Iterator;
import me.alpha432.oyvey.features.gui.components.items.Item;
import java.util.function.Function;
import java.util.Comparator;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.components.items.buttons.Button;
import me.alpha432.oyvey.features.gui.components.items.buttons.ModuleButton;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.components.Component;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class OyVeyGui extends GuiScreen
{
    private static OyVeyGui oyveyGui;
    private static OyVeyGui INSTANCE;
    private final ArrayList<Component> components;
    
    public OyVeyGui() {
        this.components = new ArrayList<Component>();
        this.setInstance();
        this.load();
    }
    
    public static OyVeyGui getInstance() {
        if (OyVeyGui.INSTANCE == null) {
            OyVeyGui.INSTANCE = new OyVeyGui();
        }
        return OyVeyGui.INSTANCE;
    }
    
    public static OyVeyGui getClickGui() {
        return getInstance();
    }
    
    private void setInstance() {
        OyVeyGui.INSTANCE = this;
    }
    
    private void load() {
        int x = -84;
        for (final Module.Category category : OyVey.moduleManager.getCategories()) {
            final ArrayList<Component> components2 = this.components;
            final String name = category.getName();
            x += 90;
            components2.add(new Component(name, x, 4, true) {
                @Override
                public void setupItems() {
                    OyVeyGui$1.counter1 = new int[] { 1 };
                    OyVey.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing((Function<? super Item, ? extends Comparable>)Feature::getName)));
    }
    
    public void updateModule(final Module module) {
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) {
                    continue;
                }
                final ModuleButton button = (ModuleButton)item;
                final Module mod = button.getModule();
                if (module == null) {
                    continue;
                }
                if (!module.equals(mod)) {
                    continue;
                }
                button.initSettings();
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public final ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void checkMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        }
        else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }
    
    public int getTextOffset() {
        return -6;
    }
    
    public Component getComponentByName(final String name) {
        for (final Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return component;
        }
        return null;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
    
    public void onGuiClosed() {
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    
    public void initGui() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer && ClickGui.getInstance().mode.getValue() == ClickGui.Mode.BLUR) {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
    
    static {
        OyVeyGui.INSTANCE = new OyVeyGui();
    }
}
