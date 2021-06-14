// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.command.commands;

import me.alpha432.oyvey.util.WorldUtil;
import me.alpha432.oyvey.features.command.Command;

public class KickCommand extends Command
{
    public KickCommand() {
        super("kick");
    }
    
    @Override
    public void execute(final String[] commands) {
        WorldUtil.disconnectFromWorld(this);
    }
}
