// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.command.commands;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.manager.FriendManager;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new String[] { "<add/del/name/clear>", "<name>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            if (OyVey.friendManager.getFriends().isEmpty()) {
                Command.sendMessage("Friend list empty D:.");
            }
            else {
                String f = "Friends: ";
                for (final FriendManager.Friend friend : OyVey.friendManager.getFriends()) {
                    try {
                        f = f + friend.getUsername() + ", ";
                    }
                    catch (Exception ex) {}
                }
                Command.sendMessage(f);
            }
            return;
        }
        if (commands.length != 2) {
            if (commands.length >= 2) {
                final String s = commands[0];
                switch (s) {
                    case "add": {
                        OyVey.friendManager.addFriend(commands[1]);
                        Command.sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                    }
                    case "del": {
                        OyVey.friendManager.removeFriend(commands[1]);
                        Command.sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                    }
                    default: {
                        Command.sendMessage("Unknown Command, try friend add/del (name)");
                        break;
                    }
                }
            }
            return;
        }
        final String s2 = commands[0];
        switch (s2) {
            case "reset": {
                OyVey.friendManager.onLoad();
                Command.sendMessage("Friends got reset.");
            }
            default: {
                Command.sendMessage(commands[0] + (OyVey.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            }
        }
    }
}
