/**
 * MenuItemEvent
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * The message sent to 'MenuItemEventListener' via 'CSEventBus'
 * 
 */
public class MenuItemEvent extends CSEvent
{
    
    /*                       
     * MESSAGES / MENU ITEMS *
     */                      
    // Zoning
    public static final String RESIDENTIAL = "Residential";
    public static final String INDUSTRIAL = "Industrial";
    public static final String COMMERCIAL = "Commercial";
    
    // Transportation
    public static final String STREETS = "Roads";
 
    // Utilities
    //
    
    // Civic
    //
    
    // Tools
    public static final String BULLDOZER = "Bulldozer";
    
    public MenuItemEvent(String message) {
        super(message);
    }
}

