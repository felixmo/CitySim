import java.awt.Dimension;
import java.util.ArrayList;

/**
 * Write a description of class ResidentialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ResidentialZone extends Zone
{
    public static final int ID = 1;
    public static final int[] MARKERS = { 400, 401, 402, 403, 404, 405, 406, 407, 408 };
    public static final int SIZE_WIDTH = 3;
    public static final int SIZE_HEIGHT = 3;

    public static void zoneTiles(ArrayList<ArrayList<Tile>> selectedTiles) {
        
        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int k = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = selectedTiles.get(j).get(i);
                tile.setType(ResidentialZone.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Did update types for " + (width*height) + " residential tiles");

        Zone.updateTiles(selectedTiles);
    }

    /*
     * ACCESSORS *
     */

    public static int count() {
        Integer count = Zone.counts.get((Integer)ID);
        return count == null ? 0 : count;
    }
}
