// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager.Identify;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class UtilThree
{
    private static final Gson gson;
    private static final Gson PRETTY_PRINTING;
    
    public String toJson() {
        return UtilThree.gson.toJson((Object)this);
    }
    
    public String toJson(final boolean prettyPrinting) {
        return prettyPrinting ? UtilThree.PRETTY_PRINTING.toJson((Object)this) : UtilThree.gson.toJson((Object)this);
    }
    
    static {
        gson = new Gson();
        PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();
    }
}
