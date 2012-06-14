/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

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

    private static QueryModalWindow activeWindow = null;

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

        if (QueryModalWindow.activeWindow == null && zone != null) {

            QueryModalWindow.activeWindow = this;

            City.getInstance().enableOverlay();

            CSLogger.sharedLogger().info("Querying zone (" + zone.dbID() + ")");
            this.zone = zone;

            GreenfootImage image = new GreenfootImage("images/modal_query.png");
            image.setColor(FONTCOLOR);
            this.setImage(image);

            City.getInstance().addObject(this, 550, 290);

            draw(); 
        }
        else {
            return;
        }
    }

    public void act() {

        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("escape")) {
            QueryModalWindow.activeWindow = null;
            City.getInstance().removeOverlay();
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
        if (!(zone.zone() == CoalPowerPlant.TYPE_ID || zone.zone() == NuclearPowerPlant.TYPE_ID)) {
            if (zone.capacity() > 0 && zone.allocation() > 0) {
                d = (int)((((float)zone.allocation()) / zone.capacity()) * 100);
            }
        }
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
//         density += (" (" + d + ")");
        image.drawString(density, infoOffset, 84);

        // Value
        int v = zone.score();
        String value = "n/a";
        if (v > 0 && v <= 45) {
            value = "Low";
        }
        else if (v > 45 && v <= 90) {
            value = "Medium";
        }
        else if (v > 90) {
            value = "High";
        }
//         value += (" (" + v + ")");
        image.drawString(value, infoOffset, 108);

        // Crime
        int c = zone.crime();
        String crime = "Safe";
        if (c >= 6 && c <= 12) {
            crime = "Moderate";
        }
        else if (c > 12) {
            crime = "High";
        }
//         crime += (" (" + c + ")");
        image.drawString(crime, infoOffset, 133);

        // Pollution
        int p = zone.pollution();
        String pollution = "None";
        if (zone.zone() == PoliceStation.TYPE_ID || zone.zone() == FireStation.TYPE_ID || zone.zone() == Stadium.TYPE_ID) {
            pollution = "n/a";
        }
        else if (zone.zone() == CoalPowerPlant.TYPE_ID) {
            pollution = "High";
        }
        else if (zone.zone() == NuclearPowerPlant.TYPE_ID) {
            pollution = "Medium";
        }

        if (p >= 1 && p <= 30) {
            pollution = "Low";
        }
        else if (p >= 31 && p <= 50) {
            pollution = "Medium";
        }
        else if (p >= 51 && p < 100) {
            pollution = "High";
        }
        else if (p == 100) {
            pollution = "Toxic";
        }
//         pollution += (" (" + p + ")");
        image.drawString(pollution, infoOffset, 155);

        // A / C
        String ac = "n/a";
        //         System.out.println(PowerGrid.allocationForPlant(zone));
        if (zone.zone() == CommercialZone.TYPE_ID || zone.zone() == IndustrialZone.TYPE_ID || zone.zone() == ResidentialZone.TYPE_ID) {
            ac = (zone.allocation() + " / " + zone.capacity());
        }
        else if (zone.zone() == CoalPowerPlant.TYPE_ID || zone.zone() == NuclearPowerPlant.TYPE_ID) {
            ac = (PowerGrid.allocationForPlant(zone) + " / " + zone.capacity());
        }
        image.drawString(ac, infoOffset, 180);
    }

    private String stringForZone(Zone zone) {
        //         System.out.println(zone.zone());

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
