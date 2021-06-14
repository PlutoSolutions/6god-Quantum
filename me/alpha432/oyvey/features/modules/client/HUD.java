//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.client;

import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.event.events.ClientEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import java.util.function.ToIntFunction;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.EntityUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import net.minecraft.potion.PotionEffect;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import me.alpha432.oyvey.util.HudUtil;
import me.alpha432.oyvey.util.ItemUtil;
import net.minecraft.init.Items;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.event.events.Render2DEvent;
import java.util.HashMap;
import java.util.Map;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import me.alpha432.oyvey.features.modules.Module;

public class HUD extends Module
{
    private static final ResourceLocation box;
    private static final ItemStack totem;
    private static RenderItem itemRender;
    private static HUD INSTANCE;
    private final Setting<Boolean> grayNess;
    private final Setting<Boolean> renderingUp;
    private final Setting<Boolean> watermark;
    public final Setting<Watermark> mode;
    private final Setting<String> customWatermark;
    private final Setting<Integer> wateX;
    private final Setting<Integer> wateY;
    private final Setting<Boolean> arrayList;
    private final Setting<Float> arraylistx;
    private final Setting<Integer> arraylisty;
    public Setting<Integer> animationHorizontalTime;
    public Setting<Integer> animationVerticalTime;
    private final Setting<Boolean> pvpInfo;
    private final Setting<Integer> pvpY;
    private final Setting<Boolean> friendlist;
    private final Setting<Integer> friendlistx;
    private final Setting<Integer> friendlisty;
    private final Setting<Boolean> coords;
    private final Setting<Integer> coordsposy;
    private final Setting<Boolean> direction;
    private final Setting<Integer> directionposy;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> totems;
    private final Setting<Greeter> greeter;
    private final Setting<String> spoofGreeter;
    private final Setting<Integer> greeterposy;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> potions;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Setting<Boolean> lag;
    public Setting<String> command;
    public Setting<TextUtil.Color> bracketColor;
    public Setting<TextUtil.Color> commandColor;
    public Setting<String> commandBracket;
    public Setting<String> commandBracket2;
    public Setting<Boolean> notifyToggles;
    public Setting<Boolean> magenDavid;
    public Setting<RenderingMode> renderingMode;
    public Setting<Boolean> time;
    public Setting<Integer> lagTime;
    public static Entity target;
    private int color;
    public float hue;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    private final Timer timer;
    private final Map<String, Integer> players;
    private int startcolor1;
    private int endcolor1;
    
    public HUD() {
        super("HUD", "hud", Category.CLIENT, true, false, false);
        this.grayNess = (Setting<Boolean>)this.register(new Setting("Gray", (T)true));
        this.renderingUp = (Setting<Boolean>)this.register(new Setting("RenderingUp", (T)false));
        this.watermark = (Setting<Boolean>)this.register(new Setting("Watermark", (T)false));
        this.mode = (Setting<Watermark>)this.register(new Setting("Mode", (T)Watermark.None, v -> this.watermark.getValue()));
        this.customWatermark = (Setting<String>)this.register(new Setting("WatermarkName", (T)"Quantum v0.4.5", v -> this.watermark.getValue()));
        this.wateX = (Setting<Integer>)this.register(new Setting("WPosX", (T)10, (T)0, (T)600, v -> this.watermark.getValue()));
        this.wateY = (Setting<Integer>)this.register(new Setting("WPosY", (T)10, (T)0, (T)530, v -> this.watermark.getValue()));
        this.arrayList = (Setting<Boolean>)this.register(new Setting("ActiveModules", (T)true));
        this.arraylistx = (Setting<Float>)this.register(new Setting("PosX", (T)2.0f, (T)0.0f, (T)40.0f, v -> this.arrayList.getValue()));
        this.arraylisty = (Setting<Integer>)this.register(new Setting("PosY", (T)10, (T)0, (T)530, v -> this.arrayList.getValue()));
        this.animationHorizontalTime = (Setting<Integer>)this.register(new Setting("HorizontalTime", (T)500, (T)1, (T)1000, v -> this.arrayList.getValue()));
        this.animationVerticalTime = (Setting<Integer>)this.register(new Setting("VerticalTime", (T)50, (T)1, (T)500, v -> this.arrayList.getValue()));
        this.pvpInfo = (Setting<Boolean>)this.register(new Setting("PvpInfo", (T)false));
        this.pvpY = (Setting<Integer>)this.register(new Setting("PPosY", (T)250, (T)0, (T)250, v -> this.pvpInfo.getValue()));
        this.friendlist = (Setting<Boolean>)this.register(new Setting("FriendList", (T)false));
        this.friendlistx = (Setting<Integer>)this.register(new Setting("FPosX", (T)2, (T)0, (T)600, v -> this.friendlist.getValue()));
        this.friendlisty = (Setting<Integer>)this.register(new Setting("FPosY", (T)10, (T)0, (T)530, v -> this.friendlist.getValue()));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", (T)false));
        this.coordsposy = (Setting<Integer>)this.register(new Setting("CPosY", (T)2, (T)0, (T)530, v -> this.coords.getValue()));
        this.direction = (Setting<Boolean>)this.register(new Setting("Direction", (T)false));
        this.directionposy = (Setting<Integer>)this.register(new Setting("DPosY", (T)4, (T)0, (T)530, v -> this.direction.getValue()));
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", (T)false));
        this.totems = (Setting<Boolean>)this.register(new Setting("Totems", (T)false));
        this.greeter = (Setting<Greeter>)this.register(new Setting("Greeter", (T)Greeter.None));
        this.spoofGreeter = (Setting<String>)this.register(new Setting("GreeterName", (T)"Welcome to Quantum", v -> this.greeter.getValue() == Greeter.Custom));
        this.greeterposy = (Setting<Integer>)this.register(new Setting("GPosY", (T)2, (T)0, (T)20, v -> this.greeter.getValue() == Greeter.Custom));
        this.speed = (Setting<Boolean>)this.register(new Setting("Speed", (T)false));
        this.potions = (Setting<Boolean>)this.register(new Setting("Potions", (T)false));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", (T)false));
        this.tps = (Setting<Boolean>)this.register(new Setting("TPS", (T)false));
        this.fps = (Setting<Boolean>)this.register(new Setting("FPS", (T)false));
        this.lag = (Setting<Boolean>)this.register(new Setting("LagNotifier", (T)false));
        this.command = (Setting<String>)this.register(new Setting("Command", (T)"6ixGod+"));
        this.bracketColor = (Setting<TextUtil.Color>)this.register(new Setting("BracketColor", (T)TextUtil.Color.RED));
        this.commandColor = (Setting<TextUtil.Color>)this.register(new Setting("NameColor", (T)TextUtil.Color.GRAY));
        this.commandBracket = (Setting<String>)this.register(new Setting("BracketL", (T)"["));
        this.commandBracket2 = (Setting<String>)this.register(new Setting("BracketR", (T)"]"));
        this.notifyToggles = (Setting<Boolean>)this.register(new Setting("ToggleNotifs", (T)false));
        this.magenDavid = (Setting<Boolean>)this.register(new Setting("FutureGUI", (T)false));
        this.renderingMode = (Setting<RenderingMode>)this.register(new Setting("Ordering", (T)RenderingMode.Alphabet));
        this.time = (Setting<Boolean>)this.register(new Setting("Time", (T)false, "The time"));
        this.lagTime = (Setting<Integer>)this.register(new Setting("LagTime", (T)1000, (T)0, (T)2000));
        this.timer = new Timer();
        this.players = new HashMap<String, Integer>();
        this.setInstance();
    }
    
    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }
    
    private void setInstance() {
        HUD.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.friendlist.getValue()) {
            this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            this.renderFriends();
        }
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.watermark.getValue()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(this.customWatermark.getValue(), this.wateX.getValue(), this.wateY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = this.customWatermark.getValue().toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), this.wateX.getValue() + f, this.wateY.getValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(this.customWatermark.getValue(), this.wateX.getValue(), this.wateY.getValue(), this.color, true);
            }
        }
        if (this.pvpInfo.getValue()) {
            OyVey.textManager.drawStringWithShadow("Quantum", 1.0f, (float)(this.pvpY.getValue() - 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color);
            final String caOn = "CA" + ChatFormatting.GREEN + " ON";
            final String caOff = "CA" + ChatFormatting.RED + " OFF";
            this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            if (OyVey.moduleManager.getModuleByName("AutoCrystal").isEnabled() || OyVey.moduleManager.getModuleByName("CrystalAura").isEnabled()) {
                if (ClickGui.getInstance().rainbow.getValue()) {
                    if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                        this.renderer.drawString(caOn, 2.0f, this.pvpY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    }
                    else {
                        final int[] arrayOfInt2 = { 1 };
                        final char[] stringToCharArray2 = caOn.toCharArray();
                        float f2 = 0.0f;
                        for (final char c2 : stringToCharArray2) {
                            this.renderer.drawString(String.valueOf(c2), 2.0f + f2, this.pvpY.getValue(), ColorUtil.rainbow(arrayOfInt2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                            f2 += this.renderer.getStringWidth(String.valueOf(c2));
                            ++arrayOfInt2[0];
                        }
                    }
                }
                else {
                    this.renderer.drawString(caOn, 2.0f, this.pvpY.getValue(), this.color, true);
                }
            }
            if ((OyVey.moduleManager.getModuleByName("AutoCrystal").isDisabled() && !OyVey.moduleManager.getModuleByName("CrystalAura").isEnabled()) || (OyVey.moduleManager.getModuleByName("CrystalAura").isDisabled() && !OyVey.moduleManager.getModuleByName("AutoCrystal").isEnabled())) {
                if (ClickGui.getInstance().rainbow.getValue()) {
                    if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                        this.renderer.drawString(caOff, 2.0f, this.pvpY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    }
                    else {
                        final int[] arrayOfInt2 = { 1 };
                        final char[] stringToCharArray2 = caOff.toCharArray();
                        float f2 = 0.0f;
                        for (final char c2 : stringToCharArray2) {
                            this.renderer.drawString(String.valueOf(c2), 2.0f + f2, this.pvpY.getValue(), ColorUtil.rainbow(arrayOfInt2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                            f2 += this.renderer.getStringWidth(String.valueOf(c2));
                            ++arrayOfInt2[0];
                        }
                    }
                }
                else {
                    this.renderer.drawString(caOff, 2.0f, this.pvpY.getValue(), this.color, true);
                }
            }
            final String totnull = "" + ChatFormatting.RED + "0";
            final String totslot = "" + ChatFormatting.GREEN + String.valueOf(ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING));
            if (ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING) != 0) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static || ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) {
                    this.renderer.drawString(totslot, 2.0f, (float)(this.pvpY.getValue() + 10), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
            }
            else if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static || ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) {
                this.renderer.drawString(totnull, 2.0f, (float)(this.pvpY.getValue() + 10), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            final String pingg = "" + ChatFormatting.GREEN + HudUtil.getPingSatus();
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(pingg, 2.0f, (float)(this.pvpY.getValue() + 20), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                this.renderer.drawString(pingg, 2.0f, (float)(this.pvpY.getValue() + 20), this.color, true);
            }
        }
        final int[] counter1 = { 1 };
        int j = (HUD.mc.currentScreen instanceof GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                if (this.renderingMode.getValue() == RenderingMode.Alphabet) {
                    for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                        final String str = OyVey.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(2 + j * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        ++counter1[0];
                    }
                }
                else {
                    for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                        final Module module = OyVey.moduleManager.sortedModules.get(k);
                        final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(2 + j * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        ++counter1[0];
                    }
                }
            }
            else if (this.renderingMode.getValue() == RenderingMode.Alphabet) {
                for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                    final String str = OyVey.moduleManager.sortedModulesABC.get(k);
                    j += 10;
                    this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                    final Module module = OyVey.moduleManager.sortedModules.get(k);
                    final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    j += 10;
                    this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (HUD.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.getMinecraft().player.getActivePotionEffects());
                for (final PotionEffect potionEffect : effects) {
                    final String str3 = OyVey.potionManager.getColoredPotionString(potionEffect);
                    i += 10;
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(height - 2 - i), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String str4 = grayString + "Ping " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str4) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        else {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.getMinecraft().player.getActivePotionEffects());
                for (final PotionEffect potionEffect : effects) {
                    final String str3 = OyVey.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(2 + i++ * 10), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String str4 = grayString + "Ping " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str4) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int)HUD.mc.player.posX;
        final int posY = (int)HUD.mc.player.posY;
        final int posZ = (int)HUD.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(HUD.mc.player.posX * nether);
        final int hposZ = (int)(HUD.mc.player.posZ * nether);
        i = ((HUD.mc.currentScreen instanceof GuiChat) ? 14 : 0);
        final String coordinates = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]"));
        final String direction = this.direction.getValue() ? OyVey.rotationManager.getDirection4D(false) : "";
        final String coords = this.coords.getValue() ? coordinates : "";
        i += 10;
        if (ClickGui.getInstance().rainbow.getValue()) {
            final String rainbowCoords = this.coords.getValue() ? ("XYZ " + (inHell ? (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]"))) : "";
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(rainbowCoords, 2.0f, (float)(height - i), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter2 = { 1 };
                final char[] stringToCharArray3 = direction.toCharArray();
                float s = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    this.renderer.drawString(String.valueOf(c3), 2.0f + s, (float)(height - i - 11), ColorUtil.rainbow(counter2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    s += this.renderer.getStringWidth(String.valueOf(c3));
                    ++counter2[0];
                }
                final int[] counter3 = { 1 };
                final char[] stringToCharArray4 = rainbowCoords.toCharArray();
                float u = 0.0f;
                for (final char c4 : stringToCharArray4) {
                    this.renderer.drawString(String.valueOf(c4), 2.0f + u, (float)(height - i), ColorUtil.rainbow(counter3[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c4));
                    ++counter3[0];
                }
            }
        }
        else {
            this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), this.color, true);
            this.renderer.drawString(coords, 2.0f, (float)(height - i), this.color, true);
        }
        if (this.armor.getValue()) {
            this.renderArmorHUD(true);
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue() != Greeter.None) {
            this.renderGreeter();
        }
        if (this.lag.getValue()) {
            this.renderLag();
        }
    }
    
    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }
    
    public void renderGreeter() {
        final int width = this.renderer.scaledWidth;
        String text = "";
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        switch (this.greeter.getValue()) {
            case Custom: {
                text += this.spoofGreeter.getValue();
                break;
            }
        }
        if (ClickGui.getInstance().rainbow.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) {
                this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, this.greeterposy.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, this.greeterposy.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] arrayOfInt = { 1 };
                final char[] stringToCharArray = text.toCharArray();
                float f = 0.0f;
                for (final char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    f += this.renderer.getStringWidth(String.valueOf(c));
                    ++arrayOfInt[0];
                }
            }
        }
        else {
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, this.color, true);
        }
    }
    
    public void renderLag() {
        final int width = this.renderer.scaledWidth;
        if (OyVey.serverManager.isServerNotResponding()) {
            final String text = ChatFormatting.RED + "Server lagging for " + MathUtil.round(OyVey.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }
    
    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    public void renderArmorHUD(final boolean percent) {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
        for (final ItemStack is : HUD.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            this.renderer.drawStringWithShadow(s, (float)(x + 19 - 2 - this.renderer.getStringWidth(s)), (float)(y + 9), 16777215);
            if (!percent) {
                continue;
            }
            int dmg = 0;
            final int itemDurability = is.getMaxDamage() - is.getItemDamage();
            final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
            final float red = 1.0f - green;
            if (percent) {
                dmg = 100 - (int)(red * 100.0f);
            }
            else {
                dmg = itemDurability;
            }
            this.renderer.drawStringWithShadow(dmg + "", (float)(x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final AttackEntityEvent event) {
        this.shouldIncrement = true;
    }
    
    @Override
    public void onLoad() {
        OyVey.commandManager.setClientMessage(this.getCommandMessage());
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && this.equals(event.getSetting().getFeature())) {
            OyVey.commandManager.setClientMessage(this.getCommandMessage());
        }
    }
    
    public String getCommandMessage() {
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command.getPlannedValue(), this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
    }
    
    public String getRainbowCommandMessage() {
        final StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
        stringBuilder.insert(0, "§+");
        stringBuilder.append("§r");
        return stringBuilder.toString();
    }
    
    public String getRawCommandMessage() {
        return this.commandBracket.getValue() + this.command.getValue() + this.commandBracket2.getValue();
    }
    
    public void drawTextRadar(final int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (final Map.Entry<String, Integer> player : this.players.entrySet()) {
                final String text = player.getKey() + " ";
                final int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, (float)y, this.color, true);
                y += textheight;
            }
        }
    }
    
    private void renderFriends() {
        final List<String> friends = new ArrayList<String>();
        for (final EntityPlayer player : HUD.mc.world.playerEntities) {
            if (OyVey.friendManager.isFriend(player.getName())) {
                friends.add(player.getName());
            }
        }
        if (ClickGui.getInstance().rainbow.getValue()) {
            int y = this.friendlisty.getValue();
            final int x = this.friendlistx.getValue();
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                if (friends.isEmpty()) {
                    this.renderer.drawString("No friends online", (float)x, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    this.renderer.drawString("Friends:", (float)x, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    y += 12;
                    for (final String friend : friends) {
                        this.renderer.drawString(friend, (float)x, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        y += 12;
                    }
                }
            }
        }
        else {
            int y = this.friendlisty.getValue();
            final int x = this.friendlistx.getValue();
            if (friends.isEmpty()) {
                this.renderer.drawString("No friends online", (float)x, (float)y, this.color, true);
            }
            else {
                this.renderer.drawString("Friends:", (float)x, (float)y, this.color, true);
                y += 12;
                for (final String friend : friends) {
                    this.renderer.drawString(friend, (float)x, (float)y, this.color, true);
                    y += 12;
                }
            }
        }
    }
    
    static {
        box = new ResourceLocation("textures/gui/container/shulker_box.png");
        totem = new ItemStack(Items.TOTEM_OF_UNDYING);
        HUD.INSTANCE = new HUD();
    }
    
    public enum Page
    {
        Default, 
        SemiCustom, 
        Custom;
    }
    
    public enum RenderingMode
    {
        Length, 
        Alphabet;
    }
    
    public enum Greeter
    {
        None, 
        Custom;
    }
    
    public enum Watermark
    {
        None, 
        Custom;
    }
}
