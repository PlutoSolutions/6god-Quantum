// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.command.commands;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help");
    }
    
    @Override
    public void execute(final String[] commands) {
        Command.sendMessage("Commands: ");
        for (final Command command : OyVey.commandManager.getCommands()) {
            Command.sendMessage(ChatFormatting.GRAY + OyVey.commandManager.getPrefix() + command.getName());
        }
    }
}
