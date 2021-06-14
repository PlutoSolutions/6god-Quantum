// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.command.commands;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        OyVey.reload();
    }
}
