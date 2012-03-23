import greenfoot.*;
import java.awt.Font;
import java.awt.Color;
import java.io.File;

/**
 * Write a description of class Label here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Label extends Actor
{
    private static final Color FONT_COLOR = new Color(55, 55, 55);
    private static Font FONT = new Font("Arial", Font.PLAIN, 22);

    private Rectangle frame;
    private GreenfootImage image;
    private String text;

    public Label(Rectangle frame) {
// 
//         try {
//             font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/cabin/Cabin-Bold-TTF.ttf"));
//         } 
//         catch (Exception e) {
//             e.printStackTrace();
//         }

        this.frame = frame;

        image = new GreenfootImage(frame.width(), frame.height());
        image.setFont(FONT);
        image.setColor(FONT_COLOR);
        setImage(image);
    }

    public void setText(String value) {
        this.text = value;
        
        image.clear();
        image.drawString(this.text, 1, frame.height()/2);
    }

    public Rectangle frame() {
        return frame;
    }
}
