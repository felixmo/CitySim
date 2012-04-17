import greenfoot.*;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.InputStream;

/**
 * Label
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-17-2012
 * 
 * A 'label' UI element
 * 
 */

public class Label extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES & CONSTANTS *
     */

    // Font and font properties
    private static final Color FONT_COLOR = new Color(55, 55, 55);                  // Default font colour
    private static final String FONT_CABIN = "fonts/cabin/Cabin-Regular-TTF.ttf";   // Default font
    private static Font font;                                                       // Font obj. containing the font itself and the default font properties

    // Label properties
    private Rectangle frame;    // Location and dimensions of the label
    private String text;        // Label text

    private GreenfootImage image;   // Label view

    // ---------------------------------------------------------------------------------------------------------------------

    public Label(Rectangle frame) {

        this.frame = frame;

        image = new GreenfootImage(frame.width(), frame.height());
        image.setFont(loadFont(FONT_CABIN).deriveFont((float)frame.height()/2));
        image.setColor(FONT_COLOR);
        setImage(image);
    }

    /*
     * HELPERS *
     */
    
    // Loads a TTF font from the specified path and creates a 'Font' object from it
    private Font loadFont(String path) {

        try {
            InputStream is = Label.class.getResourceAsStream(path);
            return Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * ACCESSORS *
     */
    
    public void setText(String value) {

        this.text = value;

        image.clear();
        image.drawString(this.text, 1, frame.height()/2);
    }

    public Rectangle frame() {
        return frame;
    }
}
