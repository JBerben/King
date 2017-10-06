package src.main.org.bot.client;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

public class Stub implements AppletStub {

    private final Hashtable<String, String> parameters = new Hashtable<String, String>();
    private boolean isActive = true;

    public Stub() {
        
    	parameters.put("offline", "true");
        parameters.put("cache_dir", "locopk");
        parameters.put("main_class", "com.locopk.client.EmbedPanel.class");
        parameters.put("jar_download", "http://www.locopk.com/play/client.jar");
        parameters.put("code_base", "http://www.locopk.com");
        parameters.put("jar_savepath", "client");
        
    
    	//parameters.put("offline", "true");
    	//parameters.put("cache_dir", "Cache");
    	//parameters.put("main_class", "client");
    	//parameters.put("jar_download", "C:\\Users\\JBerben\\Documents\\Programming\\RSPS\\ReflectionClient\\KingClient\\Galkons.jar");
    	//parameters.put("code_base", "");
    	//parameters.put("jar_savepath", "client");
    }

    public boolean isDownloaded() {
         File f = new File("./Client.jar");
         return (f.exists() && !f.isDirectory());
    }

    public void downloadJar() {
        try {
            URLConnection jarURL = new URL(parameters.get("jar_download")).openConnection();
            FileOutputStream out = new FileOutputStream("./Client.jar");
            InputStream in = jarURL.getInputStream();
            byte[] info = new byte[1024];
            int ln;
            while ((ln = in.read(info)) != -1){
                out.write(info, 0, ln);
            }
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public final void appletResize(int width, int height) {
    }

    @Override
    public final AppletContext getAppletContext() {
        return null;
    }

    @Override
    public final URL getCodeBase() {
        try {
            return new URL(parameters.get("codebase"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public final URL getDocumentBase() {
        return getCodeBase();
    }

    @Override
    public final String getParameter(final String paramString) {
        return parameters.get(paramString);
    }

    public void setActive(boolean bool) {
        isActive = bool;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }
}
