import java.util.ArrayList;

/**
 * Write a description of class Tool here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tool  
{

    private static int pendingOp = 0; // ID of zone type

    protected static void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        Data.updateTiles(tiles);
    }
    
    protected static void updateTile(Tile tile) {
        
        Data.updateTile(tile);
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
}
