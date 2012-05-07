import java.awt.Font;
import java.io.InputStream;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

/**
 * Write a description of class CSFont here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSFont  
{
    private static final String CABIN = "fonts/cabin/Cabin-Regular-TTF.ttf";   // Relative path to 'Cabin' font
    
    private static LoadingCache<String, Font> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .build(
            new CacheLoader<String, Font>() {
                public Font load(String path) throws Exception {
                    CSLogger.sharedLogger().info("Caching font at path: " + path);
                    return loadFont(path);
                }
            }
        );
    
    public static Font cabin(float size) {
        try {
            return cache.get(CABIN).deriveFont(size);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static Font loadFont(String path) {

        try {
            InputStream is = Label.class.getResourceAsStream(path);
            return Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
