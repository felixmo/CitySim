import java.util.LinkedHashMap;

/**
 * Write a description of class Road here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Road  
{
    private static int pendingOp = 0;
    private static int activeType = 0;

    protected static LinkedHashMap<Integer, Integer> counts = new LinkedHashMap<Integer, Integer>();

    protected static void updateTile(Tile selectedTile) {

        CSLogger.sharedLogger().info("Building road on tile (" + selectedTile.position().x + ", " + selectedTile.position().y + ") of type " + pendingOp);

        selectedTile.setRoad(pendingOp);
        Integer value = counts.get((Integer)pendingOp);
        counts.put((Integer)pendingOp, value == null ? (Integer)1 : (Integer)(value + 1));
        
        Data.updateTile(selectedTile);
    }

    /*
     * ACCESSORS *
     */

    public static int pendingOp() {
        return pendingOp;
    }

    public static void setPendingOp(int value) {
        pendingOp = value;
    }
    
    public static int activeType() {
        return activeType;
    }
    
    public static void setActiveType(int value) {
        activeType = value;
    }
}
