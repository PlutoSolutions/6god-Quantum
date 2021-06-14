//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.client;

import java.util.Iterator;
import me.alpha432.oyvey.mixin.mixins.accessors.IC00Handshake;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.SPacketKeepAlive;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChat;
import me.alpha432.oyvey.event.events.PacketEvent;
import java.util.ArrayList;
import me.alpha432.oyvey.features.setting.Setting;
import java.util.List;
import me.alpha432.oyvey.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.features.modules.Module;

public class ServerModule extends Module
{
    private static ServerModule instance;
    private final AtomicBoolean connected;
    private final Timer pingTimer;
    private final List<Long> pingList;
    public Setting<String> ip;
    public Setting<String> port;
    public Setting<String> serverIP;
    public Setting<Boolean> noFML;
    public Setting<Boolean> getName;
    public Setting<Boolean> average;
    public Setting<Boolean> clear;
    public Setting<Boolean> oneWay;
    public Setting<Integer> delay;
    private long currentPing;
    private long serverPing;
    private StringBuffer name;
    private long averagePing;
    private String serverPrefix;
    
    public ServerModule() {
        super("PingBypass", "Manages Phobos`s internal Server", Category.CLIENT, false, false, true);
        this.connected = new AtomicBoolean(false);
        this.pingTimer = new Timer();
        this.pingList = new ArrayList<Long>();
        this.ip = (Setting<String>)this.register(new Setting("PhobosIP", (T)"0.0.0.0.0"));
        this.port = (Setting<String>)this.register(new Setting<String>("Port", "0").setRenderName(true));
        this.serverIP = (Setting<String>)this.register(new Setting("ServerIP", (T)"AnarchyHvH.eu"));
        this.noFML = (Setting<Boolean>)this.register(new Setting("RemoveFML", (T)false));
        this.getName = (Setting<Boolean>)this.register(new Setting("GetName", (T)false));
        this.average = (Setting<Boolean>)this.register(new Setting("Average", (T)false));
        this.clear = (Setting<Boolean>)this.register(new Setting("ClearPings", (T)false));
        this.oneWay = (Setting<Boolean>)this.register(new Setting("OneWay", (T)false));
        this.delay = (Setting<Integer>)this.register(new Setting("KeepAlives", (T)10, (T)1, (T)50));
        this.currentPing = 0L;
        this.serverPing = 0L;
        this.name = null;
        this.averagePing = 0L;
        this.serverPrefix = "idk";
        ServerModule.instance = this;
    }
    
    public static ServerModule getInstance() {
        if (ServerModule.instance == null) {
            ServerModule.instance = new ServerModule();
        }
        return ServerModule.instance;
    }
    
    public String getPlayerName() {
        if (this.name == null) {
            return null;
        }
        return this.name.toString();
    }
    
    public String getServerPrefix() {
        return this.serverPrefix;
    }
    
    @Override
    public void onLogout() {
        this.averagePing = 0L;
        this.currentPing = 0L;
        this.serverPing = 0L;
        this.pingList.clear();
        this.connected.set(false);
        this.name = null;
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = event.getPacket();
            if (packet.chatComponent.getUnformattedText().startsWith("@Clientprefix")) {
                final String prefix = this.serverPrefix = packet.chatComponent.getFormattedText().replace("@Clientprefix", "");
            }
        }
    }
    
    @Override
    public void onTick() {
        if (ServerModule.mc.getConnection() != null && this.isConnected()) {
            if (this.getName.getValue()) {
                ServerModule.mc.getConnection().sendPacket((Packet)new CPacketChatMessage("@Servername"));
                this.getName.setValue(false);
            }
            if (this.serverPrefix.equalsIgnoreCase("idk") && ServerModule.mc.world != null) {
                ServerModule.mc.getConnection().sendPacket((Packet)new CPacketChatMessage("@Servergetprefix"));
            }
            if (this.pingTimer.passedMs(this.delay.getValue() * 1000)) {
                ServerModule.mc.getConnection().sendPacket((Packet)new CPacketKeepAlive(100L));
                this.pingTimer.reset();
            }
            if (this.clear.getValue()) {
                this.pingList.clear();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packetChat = event.getPacket();
            if (packetChat.getChatComponent().getFormattedText().startsWith("@Client")) {
                this.name = new StringBuffer(TextUtil.stripColor(packetChat.getChatComponent().getFormattedText().replace("@Client", "")));
                event.setCanceled(true);
            }
        }
        else {
            final SPacketKeepAlive alive;
            if (event.getPacket() instanceof SPacketKeepAlive && (alive = event.getPacket()).getId() > 0L && alive.getId() < 1000L) {
                this.serverPing = alive.getId();
                this.currentPing = (this.oneWay.getValue() ? (this.pingTimer.getPassedTimeMs() / 2L) : this.pingTimer.getPassedTimeMs());
                this.pingList.add(this.currentPing);
                this.averagePing = this.getAveragePing();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final IC00Handshake packet;
        final String ip;
        if (event.getPacket() instanceof C00Handshake && (ip = (packet = event.getPacket()).getIp()).equals(this.ip.getValue())) {
            packet.setIp(this.serverIP.getValue());
            System.out.println(packet.getIp());
            this.connected.set(true);
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.averagePing + "ms";
    }
    
    private long getAveragePing() {
        if (!this.average.getValue() || this.pingList.isEmpty()) {
            return this.currentPing;
        }
        int full = 0;
        for (final long i : this.pingList) {
            full += (int)i;
        }
        return full / this.pingList.size();
    }
    
    public boolean isConnected() {
        return this.connected.get();
    }
    
    public int getPort() {
        int result;
        try {
            result = Integer.parseInt(this.port.getValue());
        }
        catch (NumberFormatException e) {
            return -1;
        }
        return result;
    }
    
    public long getServerPing() {
        return this.serverPing;
    }
}
