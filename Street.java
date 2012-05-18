import java.awt.Dimension;
import java.util.ArrayList;

/**
 * Write a description of class Street here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Street extends Road
{
    public static final int ID = 1;
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;

    public static void buildStreet(Tile tile, int type) {

        // System.out.println("Building street");

        tile.setType(type);

        Road.updateTile(tile);
    }

    public static void buildStreets(ArrayList<ArrayList<Tile>> selectedTiles, int type) {

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = selectedTiles.get(j).get(i);
                tile.setType(type);
            }
        }
        
        Road.updateTiles(selectedTiles);
    }

    /*
     * ACCESSORS *
     */

    public static int count() {
        Integer count = Zone.counts.get((Integer)ID);
        return count == null ? 0 : count;
    }
}
