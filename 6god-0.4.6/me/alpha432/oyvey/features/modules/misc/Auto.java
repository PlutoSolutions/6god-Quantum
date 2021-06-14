//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraft.client.multiplayer.GuiConnecting;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.client.multiplayer.ServerData;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Auto extends Module
{
    private static Auto INSTANCE;
    public Setting<Mode> mode;
    private final Setting<Boolean> logtrue;
    private final Setting<Float> health;
    private final Setting<Boolean> bed;
    private final Setting<Float> range;
    private final Setting<Boolean> logout;
    private final Setting<Boolean> reconnecttrue;
    private final Setting<Integer> delay;
    private static ServerData serverData;
    private final Setting<Boolean> respawntrue;
    public Setting<Boolean> antiDeathScreen;
    public Setting<Boolean> deathCoords;
    public Setting<Boolean> respawn;
    
    public Auto() {
        super("Auto", "a", Category.MISC, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Reconnect));
        this.logtrue = (Setting<Boolean>)this.register(new Setting("Log", (T)false, v -> this.mode.getValue() == Mode.Log));
        this.health = (Setting<Float>)this.register(new Setting("Health", (T)16.0f, (T)0.1f, (T)36.0f, v -> this.mode.getValue() == Mode.Log && this.logtrue.getValue()));
        this.bed = (Setting<Boolean>)this.register(new Setting("Beds", (T)true, v -> this.mode.getValue() == Mode.Log && this.logtrue.getValue()));
        this.range = (Setting<Float>)this.register(new Setting("BedRange", (T)6.0f, (T)0.1f, (T)36.0f, v -> this.mode.getValue() == Mode.Log && this.logtrue.getValue() && this.bed.getValue()));
        this.logout = (Setting<Boolean>)this.register(new Setting("LogoutOff", (T)true, v -> this.mode.getValue() == Mode.Log && this.logtrue.getValue()));
        this.reconnecttrue = (Setting<Boolean>)this.register(new Setting("Reconnect", (T)false, v -> this.mode.getValue() == Mode.Reconnect && !this.logtrue.getValue()));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)5, (T)1, (T)15, v -> this.mode.getValue() == Mode.Reconnect && this.reconnecttrue.getValue()));
        this.respawntrue = (Setting<Boolean>)this.register(new Setting("Respawn", (T)false, v -> this.mode.getValue() == Mode.Respawn));
        this.antiDeathScreen = (Setting<Boolean>)this.register(new Setting("AntiDeathScreen", (T)true, v -> this.mode.getValue() == Mode.Respawn && this.respawntrue.getValue()));
        this.deathCoords = (Setting<Boolean>)this.register(new Setting("DeathCoords", (T)false, v -> this.mode.getValue() == Mode.Respawn && this.respawntrue.getValue()));
        this.respawn = (Setting<Boolean>)this.register(new Setting("Respawn", (T)true, v -> this.mode.getValue() == Mode.Respawn && this.respawntrue.getValue()));
        this.setInstance();
    }
    
    public static Auto getInstance() {
        if (Auto.INSTANCE == null) {
            Auto.INSTANCE = new Auto();
        }
        return Auto.INSTANCE;
    }
    
    private void setInstance() {
        Auto.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (!Feature.nullCheck() && Auto.mc.player.getHealth() <= this.health.getValue()) {
            Auto.mc.player.connection.sendPacket((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        final SPacketBlockChange packet;
        if (event.getPacket() instanceof SPacketBlockChange && this.bed.getValue() && (packet = event.getPacket()).getBlockState().getBlock() == Blocks.BED && Auto.mc.player.getDistanceSqToCenter(packet.getBlockPosition()) <= MathUtil.square(this.range.getValue())) {
            Auto.mc.player.connection.sendPacket((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("Logged")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }
    
    @SubscribeEvent
    public void sendPacket(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiDisconnected) {
            this.updateLastConnectedServer();
            final GuiDisconnected disconnected = (GuiDisconnected)event.getGui();
            event.setGui((GuiScreen)new GuiDisconnectedHook(disconnected));
        }
    }
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        this.updateLastConnectedServer();
    }
    
    public void updateLastConnectedServer() {
        final ServerData data = Auto.mc.getCurrentServerData();
        if (data != null) {
            Auto.serverData = data;
        }
    }
    
    @SubscribeEvent
    public void onDisplayDeathScreen(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (this.deathCoords.getValue() && event.getGui() instanceof GuiGameOver) {
                Command.sendMessage(String.format("You died at x %d y %d z %d", (int)Auto.mc.player.posX, (int)Auto.mc.player.posY, (int)Auto.mc.player.posZ));
            }
            if ((this.respawn.getValue() && Auto.mc.player.getHealth() <= 0.0f) || (this.antiDeathScreen.getValue() && Auto.mc.player.getHealth() > 0.0f)) {
                event.setCanceled(true);
                Auto.mc.player.respawnPlayer();
            }
        }
    }
    
    static {
        Auto.INSTANCE = new Auto();
    }
    
    private class GuiDisconnectedHook extends GuiDisconnected
    {
        private final Timer timer;
        
        public GuiDisconnectedHook(final GuiDisconnected disconnected) {
            super(disconnected.parentScreen, disconnected.reason, disconnected.message);
            (this.timer = new Timer()).reset();
        }
        
        public void updateScreen() {
            if (this.timer.passedS(Auto.this.delay.getValue())) {
                this.mc.displayGuiScreen((GuiScreen)new GuiConnecting(this.parentScreen, this.mc, (Auto.serverData == null) ? this.mc.currentServerData : Auto.serverData));
            }
        }
        
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            final String s = "Reconnecting in " + MathUtil.round((Auto.this.delay.getValue() * 1000 - this.timer.getPassedTimeMs()) / 1000.0, 1);
            Auto.this.renderer.drawString(s, (float)(this.width / 2 - Auto.this.renderer.getStringWidth(s) / 2), (float)(this.height - 16), 16777215, true);
        }
    }
    
    public enum Mode
    {
        Log, 
        Reconnect, 
        Respawn;
    }
}
