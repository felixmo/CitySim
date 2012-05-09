import java.util.ArrayList;

/**
 * Write a description of class Zoning here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zone  
{
    private static int pendingOp = 0; // ID of zone type
 
    public static void zoneTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

        int x = selectedTiles.get(0).get(0).position().x;
        int y = selectedTiles.get(0).get(0).position().y;
        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        CSLogger.sharedLogger().info("Zoning " + width*height + " tiles as type " + pendingOp);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.println("TEST: Zoning tile: " + selectedTiles.get(i).get(j) + " as TYPE " + pendingOp); 
            }
        }
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
