import greenfoot.*;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;

/**
 * Write a description of class QueryModalWindow here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class QueryModalWindow extends Actor
{
    /*
     * CONSTANTS *
     */
    private static final Color FONTCOLOR = new Color(55, 55, 55);                  // Default font colour
    private static final float INFO_FONTSIZE = 14.5f;                // Font size for text
    private static final float HEADER_FONTSIZE = 25.0f;
    private static final Font FONT_HEADER = CSFont.cabin(HEADER_FONTSIZE);
    private static final Font FONT_INFO = CSFont.cabin(INFO_FONTSIZE);  // Font used by elements

    private Zone zone;

    public QueryModalWindow(Zone zone) {

        CSLogger.sharedLogger().info("Querying zone (" + zone.dbID() + ")");
        this.zone = zone;

        GreenfootImage image = new GreenfootImage("images/modal_query.png");
        image.setColor(FONTCOLOR);
        this.setImage(image);

        City.getInstance().addObject(this, 550, 290);

        draw();	
    }

    public void act() {

        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("escape")) {
            City.getInstance().removeObject(this);
        }
    }

    private void draw() {

        GreenfootImage image = this.getImage();
        FontMetrics headerFontMetrics = image.getAwtImage().getGraphics().getFontMetrics(FONT_HEADER); 

        int midpoint = (int)(383/2);
        int infoOffset = 235;

        // Header
        String header = stringForZone(this.zone);
        image.setFont(CSFont.cabin((float)HEADER_FONTSIZE));
        image.drawString(header, midpoint-(int)(headerFontMetrics.stringWidth(header)/2), 41); 

        image.setFont(CSFont.cabin((float)INFO_FONTSIZE));

        // Density
        int d = 0;
        if (zone.capacity() > 0) d = (int)((zone.allocation() / zone.capacity()) * 100);
        String density = "n/a";
        if (d > 0 && d <= 33) {
            density = "Low";
        }
        else if (d > 33 && d <= 66) {
            density = "Medium";
        }
        else if (d > 66) {
            density = "High";
        }
        density += (" (" + d + ")");
        image.drawString(density, infoOffset, 84);

        // Value
        int v = zone.score();
        String value = "n/a";
        if (v > 0 && v <= 25) {
            value = "Low";
        }
        else if (v > 25 && v <= 50) {
            value = "Medium";
        }
        else if (v > 50) {
            value = "High";
        }
        value += (" (" + v + ")");
        image.drawString(value, infoOffset, 108);

        // Crime
        int c = zone.crime();
        String crime = "n/a";
        crime += (" (" + c + ")");
        image.drawString(value, infoOffset, 133);

        // Pollution
        int p = zone.pollution();
        String pollution = "n/a";
        pollution += (" (" + p + ")");
        image.drawString(pollution, infoOffset, 155);

        // A / C
        String ac = (zone.allocation() + " / " + zone.capacity());
        image.drawString(ac, infoOffset, 180);
    }

    private String stringForZone(Zone zone) {
        System.out.println(zone.zone());
        
        switch (zone.zone()) {
            case CommercialZone.TYPE_ID: return "Commercial zone";
            case IndustrialZone.TYPE_ID: return "Industrial zone";
            case ResidentialZone.TYPE_ID: return "Residential zone";
            case FireStation.TYPE_ID: return "Fire station";
            case PoliceStation.TYPE_ID: return "Police station";
            case Stadium.TYPE_ID: return "Stadium";
            case CoalPowerPlant.TYPE_ID: return "Coal power plant";
            case NuclearPowerPlant.TYPE_ID: return "Nuclear power plant";
            default: return "Not available";
        }
    }
}
