//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class FakePlayer extends Module
{
    public static final String[][] OyVeyInfo;
    private final String name = "BigNigger";
    public Setting<Boolean> multi;
    private final Setting<Integer> players;
    private EntityOtherPlayerMP _fakePlayer;
    private final List<EntityOtherPlayerMP> fakeEntities;
    public List<Integer> fakePlayerIdList;
    
    public FakePlayer() {
        super("FakePlayer", "fp", Category.PLAYER, false, false, false);
        this.multi = (Setting<Boolean>)this.register(new Setting("Multi", (T)false));
        this.players = (Setting<Integer>)this.register(new Setting("Players", (T)1, (T)1, (T)9, v -> this.multi.getValue(), "Amount of other players."));
        this.fakeEntities = new ArrayList<EntityOtherPlayerMP>();
        this.fakePlayerIdList = new ArrayList<Integer>();
    }
    
    public static String getUuid(final String name) {
        final JsonParser parser = new JsonParser();
        final String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            final String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) {
                return "invalid name";
            }
            final JsonObject UUIDObject = (JsonObject)parser.parse(UUIDJson);
            return reformatUuid(UUIDObject.get("id").toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    private static String reformatUuid(final String uuid) {
        String longUuid = "";
        longUuid = longUuid + uuid.substring(1, 9) + "-";
        longUuid = longUuid + uuid.substring(9, 13) + "-";
        longUuid = longUuid + uuid.substring(13, 17) + "-";
        longUuid = longUuid + uuid.substring(17, 21) + "-";
        longUuid += uuid.substring(21, 33);
        return longUuid;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        if (this.multi.getValue()) {
            int amount = 0;
            int entityId = -101;
            for (final String[] data : FakePlayer.OyVeyInfo) {
                this.addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                if (++amount >= this.players.getValue()) {
                    return;
                }
                entityId -= amount;
            }
        }
        else {
            this._fakePlayer = null;
            if (FakePlayer.mc.player != null) {
                try {
                    final WorldClient world = FakePlayer.mc.world;
                    this.getClass();
                    final UUID fromString = UUID.fromString(getUuid("BigNigger"));
                    this.getClass();
                    this._fakePlayer = new EntityOtherPlayerMP((World)world, new GameProfile(fromString, "BigNigger"));
                }
                catch (Exception e) {
                    final WorldClient world2 = FakePlayer.mc.world;
                    final UUID fromString2 = UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03");
                    this.getClass();
                    this._fakePlayer = new EntityOtherPlayerMP((World)world2, new GameProfile(fromString2, "BigNigger"));
                    Command.sendMessage("Failed to load uuid, setting another one.");
                }
                final String format = "%s has been spawned.";
                final Object[] args = { null };
                final int n = 0;
                this.getClass();
                args[n] = "BigNigger";
                Command.sendMessage(String.format(format, args));
                this._fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
                this._fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
                FakePlayer.mc.world.addEntityToWorld(-100, (Entity)this._fakePlayer);
            }
        }
    }
    
    private void addFakePlayer(final String uuid, final String name, final int entityId, final int offsetX, final int offsetZ) {
        final GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
        final EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, profile);
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        final EntityOtherPlayerMP entityOtherPlayerMP = fakePlayer;
        entityOtherPlayerMP.posX += offsetX;
        final EntityOtherPlayerMP entityOtherPlayerMP2 = fakePlayer;
        entityOtherPlayerMP2.posZ += offsetZ;
        fakePlayer.setHealth(FakePlayer.mc.player.getHealth() + FakePlayer.mc.player.getAbsorptionAmount());
        this.fakeEntities.add(fakePlayer);
        FakePlayer.mc.world.addEntityToWorld(entityId, (Entity)fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }
    
    @Override
    public void onDisable() {
        if (FakePlayer.mc.world != null && FakePlayer.mc.player != null) {
            super.onDisable();
            FakePlayer.mc.world.removeEntity((Entity)this._fakePlayer);
        }
    }
    
    @SubscribeEvent
    public void onClientDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (this.isEnabled()) {
            this.disable();
        }
    }
    
    static {
        OyVeyInfo = new String[][] { { "8af022c8-b926-41a0-8b79-2b544ff00fcf", "3arthqu4ke", "3", "0" }, { "0aa3b04f-786a-49c8-bea9-025ee0dd1e85", "zb0b", "-3", "0" }, { "19bf3f1f-fe06-4c86-bea5-3dad5df89714", "3vt", "0", "-3" }, { "e47d6571-99c2-415b-955e-c4bc7b55941b", "Phobos_eu", "0", "3" }, { "b01f9bc1-cb7c-429a-b178-93d771f00926", "bakpotatisen", "6", "0" }, { "b232930c-c28a-4e10-8c90-f152235a65c5", "948", "-6", "0" }, { "ace08461-3db3-4579-98d3-390a67d5645b", "Browswer", "0", "-6" }, { "5bead5b0-3bab-460d-af1d-7929950f40c2", "fsck", "0", "6" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "Fit", "0", "9" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "deathcurz0", "0", "-9" } };
    }
}
