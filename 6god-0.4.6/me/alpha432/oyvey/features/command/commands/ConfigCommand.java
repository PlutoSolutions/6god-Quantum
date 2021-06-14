// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.command.commands;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import me.alpha432.oyvey.features.command.Command;

public class ConfigCommand extends Command
{
    public ConfigCommand() {
        super("config", new String[] { "<save/load>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("You`ll find the config files in your gameProfile directory under Quantum/config");
            return;
        }
        if (commands.length == 2) {
            if ("list".equals(commands[0])) {
                String configs = "Configs: ";
                final File file = new File("Quantum//");
                final List<File> directories = Arrays.stream(file.listFiles()).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect((Collector<? super File, ?, List<File>>)Collectors.toList());
                final StringBuilder builder = new StringBuilder(configs);
                for (final File file2 : directories) {
                    builder.append(file2.getName() + ", ");
                }
                configs = builder.toString();
                Command.sendMessage(configs);
            }
            else {
                Command.sendMessage("Not a valid command... Possible usage: <list>");
            }
        }
        if (commands.length >= 3) {
            final String s = commands[0];
            switch (s) {
                case "save": {
                    OyVey.configManager.saveConfig(commands[1]);
                    Command.sendMessage(ChatFormatting.GREEN + "Config '" + commands[1] + "' has been saved.");
                }
                case "load": {
                    if (OyVey.configManager.configExists(commands[1])) {
                        OyVey.configManager.loadConfig(commands[1]);
                        Command.sendMessage(ChatFormatting.GREEN + "Config '" + commands[1] + "' has been loaded.");
                    }
                    else {
                        Command.sendMessage(ChatFormatting.RED + "Config '" + commands[1] + "' does not exist.");
                    }
                }
                default: {
                    Command.sendMessage("Not a valid command... Possible usage: <save/load>");
                    break;
                }
            }
        }
    }
}
