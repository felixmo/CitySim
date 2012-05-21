import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class Street here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Street extends Road
{
    public static final int TYPE_ID = 1;
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;

    public static void buildStreet(Tile tile, int type) {

        tile.setType(type);
        tile.setRoad(TYPE_ID);
        addToCount(1);

        Road.updateTile(tile);
    }

    public static void buildStreets(ArrayList<ArrayList<Tile>> selectedTiles, int type) {

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();
        addToCount(width*height);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = selectedTiles.get(j).get(i);
                tile.setType(type);
                tile.setRoad(TYPE_ID);
            }
        }

        Road.updateTiles(selectedTiles);
    }

    public static int count() {
        return ((Integer)Data.roadStats().get(Data.ROADSTATS_STREETCOUNT)).intValue();
    }

    public static void addToCount(int more) {
        HashMap roadStats = Data.roadStats();
        roadStats.put(Data.ROADSTATS_STREETCOUNT, new Integer(count()+more));
        Data.updateRoadStats(roadStats);
    }

    public static void subtractFromCount(int less) {
        HashMap roadStats = Data.roadStats();
        roadStats.put(Data.ROADSTATS_STREETCOUNT, new Integer(count()-less));
        Data.updateRoadStats(roadStats);
    }
}
