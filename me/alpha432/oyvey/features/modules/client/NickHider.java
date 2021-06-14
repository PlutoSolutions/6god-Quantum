// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class NickHider extends Module
{
    public final Setting<String> NameString;
    private static NickHider instance;
    
    public NickHider() {
        super("NickHider", "Changes name", Category.CLIENT, false, false, false);
        this.NameString = (Setting<String>)this.register(new Setting("Name", (T)"New Name Here..."));
        NickHider.instance = this;
    }
    
    @Override
    public void onEnable() {
        Command.sendMessage(ChatFormatting.GRAY + "Success! Name succesfully changed to " + ChatFormatting.GREEN + this.NameString.getValue());
    }
    
    public static NickHider getInstance() {
        if (NickHider.instance == null) {
            NickHider.instance = new NickHider();
        }
        return NickHider.instance;
    }
}
