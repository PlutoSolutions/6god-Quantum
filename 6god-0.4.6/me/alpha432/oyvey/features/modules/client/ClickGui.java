//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.util.Util;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.event.events.Render2DEvent;
import net.minecraft.client.gui.GuiScreen;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import net.minecraft.client.settings.GameSettings;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ClickGui extends Module
{
    private final Setting<Settings> setting;
    private static ClickGui INSTANCE;
    public Setting<String> prefix;
    public Setting<Boolean> customFov;
    public Setting<Float> fov;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> hoverAlpha;
    public Setting<Integer> alphaBox;
    public Setting<Integer> alpha;
    public Setting<Boolean> rainbow;
    public Setting<rainbowMode> rainbowModeHud;
    public Setting<rainbowModeArray> rainbowModeA;
    public Setting<Integer> rainbowHue;
    public Setting<Float> rainbowBrightness;
    public Setting<Float> rainbowSaturation;
    public Setting<Boolean> rainbowg;
    public Setting<Boolean> guiComponent;
    public Setting<Integer> g_red;
    public Setting<Integer> g_green;
    public Setting<Integer> g_blue;
    public Setting<Integer> g_red1;
    public Setting<Integer> g_green1;
    public Setting<Integer> g_blue1;
    public Setting<Integer> g_alpha;
    public Setting<Integer> g_alpha1;
    public Setting<Mode> mode;
    public Setting<Integer> backgroundAlpha;
    public Setting<Integer> gb_red;
    public Setting<Integer> gb_green;
    public Setting<Integer> gb_blue;
    private int color;
    
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Category.CLIENT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.Gui));
        this.prefix = (Setting<String>)this.register(new Setting("Prefix", (T)".", v -> this.setting.getValue() == Settings.Gui));
        this.customFov = (Setting<Boolean>)this.register(new Setting("CustomFov", (T)false, v -> this.setting.getValue() == Settings.Gui));
        this.fov = (Setting<Float>)this.register(new Setting("Fov", (T)150.0f, (T)(-180.0f), (T)180.0f, v -> this.setting.getValue() == Settings.Gui && this.customFov.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gui));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)30, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gui));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)80, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gui));
        this.hoverAlpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)180, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gui));
        this.alphaBox = (Setting<Integer>)this.register(new Setting("AlphaBox", (T)150, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gui));
        this.alpha = (Setting<Integer>)this.register(new Setting("HoverAlpha", (T)240, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gui));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)true, v -> this.setting.getValue() == Settings.Gui));
        this.rainbowModeHud = (Setting<rainbowMode>)this.register(new Setting("HUD", (T)rainbowMode.Static, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Gui));
        this.rainbowModeA = (Setting<rainbowModeArray>)this.register(new Setting("ArrayList", (T)rainbowModeArray.Static, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Gui));
        this.rainbowHue = (Setting<Integer>)this.register(new Setting("Delay", (T)200, (T)0, (T)600, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Gui));
        this.rainbowBrightness = (Setting<Float>)this.register(new Setting("Brightness ", (T)255.0f, (T)1.0f, (T)255.0f, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Gui));
        this.rainbowSaturation = (Setting<Float>)this.register(new Setting("Saturation", (T)100.0f, (T)1.0f, (T)255.0f, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Gui));
        this.rainbowg = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)true, v -> this.setting.getValue() == Settings.Gradient));
        this.guiComponent = (Setting<Boolean>)this.register(new Setting("Gui Component", (T)true, v -> this.setting.getValue() == Settings.Gradient));
        this.g_red = (Setting<Integer>)this.register(new Setting("RedL", (T)105, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_green = (Setting<Integer>)this.register(new Setting("GreenL", (T)162, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_blue = (Setting<Integer>)this.register(new Setting("BlueL", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_red1 = (Setting<Integer>)this.register(new Setting("RedR", (T)143, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_green1 = (Setting<Integer>)this.register(new Setting("GreenR", (T)140, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_blue1 = (Setting<Integer>)this.register(new Setting("BlueR", (T)213, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_alpha = (Setting<Integer>)this.register(new Setting("AlphaL", (T)0, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_alpha1 = (Setting<Integer>)this.register(new Setting("AlphaR", (T)0, (T)0, (T)255, v -> this.setting.getValue() == Settings.Gradient));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.COLOR, v -> this.setting.getValue() == Settings.Background));
        this.backgroundAlpha = (Setting<Integer>)this.register(new Setting("Background Alpha", (T)160, (T)0, (T)255, v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
        this.gb_red = (Setting<Integer>)this.register(new Setting("RedBG", (T)20, (T)0, (T)255, v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
        this.gb_green = (Setting<Integer>)this.register(new Setting("GreenBG", (T)20, (T)0, (T)255, v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
        this.gb_blue = (Setting<Integer>)this.register(new Setting("BlueBG", (T)20, (T)0, (T)255, v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
        this.setInstance();
    }
    
    public static ClickGui getInstance() {
        if (ClickGui.INSTANCE == null) {
            ClickGui.INSTANCE = new ClickGui();
        }
        return ClickGui.INSTANCE;
    }
    
    private void setInstance() {
        ClickGui.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.customFov.getValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, (float)this.fov.getValue());
        }
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + OyVey.commandManager.getPrefix());
            }
            OyVey.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }
    
    @Override
    public void onEnable() {
        ClickGui.mc.displayGuiScreen((GuiScreen)OyVeyGui.getClickGui());
    }
    
    @Override
    public void onLoad() {
        OyVey.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        OyVey.commandManager.setPrefix(this.prefix.getValue());
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        this.drawBackground();
    }
    
    public void drawBackground() {
        if (this.mode.getValue() == Mode.COLOR) {
            if (getInstance().isEnabled()) {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(this.gb_red.getValue(), this.gb_green.getValue(), this.gb_blue.getValue(), this.backgroundAlpha.getValue()));
            }
            else {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(0, 0, 0, 0));
            }
        }
        if (this.mode.getValue() == Mode.NONE) {
            if (getInstance().isEnabled()) {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(this.gb_red.getValue(), this.gb_green.getValue(), this.gb_blue.getValue(), this.backgroundAlpha.getValue()));
            }
            else {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(0, 0, 0, 0));
            }
        }
    }
    
    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof OyVeyGui)) {
            this.disable();
        }
    }
    
    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof OyVeyGui) {
            Util.mc.displayGuiScreen((GuiScreen)null);
        }
    }
    
    public final int getColor() {
        return this.color;
    }
    
    static {
        ClickGui.INSTANCE = new ClickGui();
    }
    
    public enum rainbowModeArray
    {
        Static, 
        Up;
    }
    
    public enum rainbowMode
    {
        Static, 
        Sideway;
    }
    
    public enum Settings
    {
        Gui, 
        Gradient, 
        Background;
    }
    
    public enum Mode
    {
        COLOR, 
        BLUR, 
        NONE;
    }
}
