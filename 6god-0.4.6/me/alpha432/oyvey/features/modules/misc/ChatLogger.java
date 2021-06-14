//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import java.io.Writer;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.client.Minecraft;
import java.io.BufferedWriter;
import java.io.File;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ChatLogger extends Module
{
    public Setting<Boolean> numbers;
    File mainFolder;
    File txt;
    File folder;
    BufferedWriter out;
    
    public ChatLogger() {
        super("ChatLogger", "cl", Category.MISC, true, false, false);
        this.numbers = (Setting<Boolean>)this.register(new Setting("OnlyNumbers", (T)false));
        this.mainFolder = new File(Minecraft.getMinecraft().gameDir + File.separator + "Quantum");
    }
    
    @Override
    public void onEnable() {
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
            final Date date = new Date();
            this.folder = new File(this.mainFolder + File.separator + "logs");
            if (!this.folder.exists()) {
                this.folder.mkdirs();
            }
            final String fileName = formatter.format(date) + "-chatlogs.txt";
            (this.txt = new File(this.folder + File.separator + fileName)).createNewFile();
            this.out = new BufferedWriter(new FileWriter(this.txt));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDisable() {
        if (this.out != null) {
            try {
                this.out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @SubscribeEvent
    public void onChatRecieved(final ClientChatReceivedEvent event) {
        try {
            final String message = event.getMessage().getUnformattedText();
            if (this.numbers.getValue()) {
                if (message.matches(".*\\d.*")) {
                    this.out.write(message);
                    this.out.write(endLine());
                    this.out.flush();
                }
            }
            else {
                this.out.write(message);
                this.out.write(endLine());
                this.out.flush();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String endLine() {
        return "\r\n";
    }
}
