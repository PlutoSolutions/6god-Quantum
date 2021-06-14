//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ExtraTab extends Module
{
    private static ExtraTab INSTANCE;
    public Setting<Integer> size;
    
    public ExtraTab() {
        super("ExtraTab", "Extends Tab.", Category.MISC, false, false, false);
        this.size = (Setting<Integer>)this.register(new Setting("Size", (T)250, (T)1, (T)1000));
        this.setInstance();
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String string;
        final String name = string = ((networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName()));
        if (OyVey.friendManager.isFriend(name)) {
            return ChatFormatting.AQUA + name;
        }
        return name;
    }
    
    public static ExtraTab getINSTANCE() {
        if (ExtraTab.INSTANCE == null) {
            ExtraTab.INSTANCE = new ExtraTab();
        }
        return ExtraTab.INSTANCE;
    }
    
    private void setInstance() {
        ExtraTab.INSTANCE = this;
    }
    
    static {
        ExtraTab.INSTANCE = new ExtraTab();
    }
}
