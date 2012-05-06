import java.awt.Font;
import java.io.InputStream;

/**
 * Write a description of class CSFont here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSFont  
{
    private static final String CABIN_PATH = "fonts/cabin/Cabin-Regular-TTF.ttf";   // Relative path to 'Cabin' font

    public static Font cabin(float size) {
        return CSFont.loadFont(CABIN_PATH).deriveFont(size);
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
