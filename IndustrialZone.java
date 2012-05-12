import java.util.ArrayList;
import java.awt.Dimension;

/**
 * Write a description of class IndustrialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IndustrialZone extends Zone
{
    public static final int ID = 3;
    public static final int[] MARKERS = { 500, 501, 502, 503, 504, 505, 506, 507, 508 };
    public static final Dimension SIZE = new Dimension(3, 3);

    public static void zoneTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int k = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = selectedTiles.get(j).get(i);
                tile.setType(IndustrialZone.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Did update types for " + (width*height) + " industrial tiles");

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
