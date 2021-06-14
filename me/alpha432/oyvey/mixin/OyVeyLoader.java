// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import me.alpha432.oyvey.OyVey;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class OyVeyLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public OyVeyLoader() {
        OyVey.LOGGER.info("\n\nLoading mixins by Alpha432");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.oyvey.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        OyVey.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        OyVeyLoader.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        OyVeyLoader.isObfuscatedEnvironment = false;
    }
}
