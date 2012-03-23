import greenfoot.*;
import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * HUD
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-17-2012
 * 
 * 
 * 
 */

public class HUD extends Actor {

    private static final Rectangle CITYNAME_RECT = new Rectangle(new Point(345, 615), 135, 48);
    private static final Rectangle POPNUM_RECT = new Rectangle(new Point(370, 685), 135, 48);
    private static final Rectangle TIME_RECT = new Rectangle(new Point(430, 735), 135, 48);
    private static final Rectangle DATE_RECT = new Rectangle(new Point(320, 735), 135, 48);

    private LinkedHashMap labels = new LinkedHashMap();
    private DataSource dataSource;
    private Minimap minimap;
    
    public HUD(DataSource dataSource) {

        this.dataSource = dataSource;
        this.minimap = new Minimap(this.dataSource);
        
        setImage("hud.png");

        // init labels
        labels.put("name", new Label(CITYNAME_RECT));
        labels.put("population", new Label(POPNUM_RECT));
        labels.put("time", new Label(TIME_RECT));
        labels.put("date", new Label(DATE_RECT));
    }
    
    protected void addedToWorld(World world) {
        
//         System.out.println("Added to world");
        
        // Add minimap to HUD
        world.addObject(minimap, minimap.frame().origin().x(), minimap.frame().origin().y());   
    }

    public void refresh(LinkedHashMap values) {
        
        Iterator iterator = values.keySet().iterator();
        while (iterator.hasNext()) {
            
            String key = (String)iterator.next();
            Object object = values.get(key);
            String className = object.getClass().getName();
            String value = null;
            
            if (className.equals("java.lang.String")) {
                value = (String)object;   
            }
            else if (className.equals("java.lang.Integer")) {
                value = Integer.toString((Integer)object);
            }
            
            refreshLabel(key, value);
        }
    }

    private void refreshLabel(String key, String value) {

        Label label = (Label)labels.get(key);
        
        if (label.getWorld() == null) {
            getWorld().addObject(label, label.frame().origin().x(), label.frame().origin().y());
        }

        label.setText(value);
    }
    
    // ACCESSORS
    
    public Minimap minimap() {
        return minimap;
    }
}
