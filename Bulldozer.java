import java.awt.Dimension;
import java.util.ArrayList;

/**
 * Write a description of class Bulldozer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bulldozer extends Tool
{
    public static final int ID = 1;
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;

    public static void bulldoze(Tile tile) {

        tile.setType(Tile.GROUND);
        tile.setRoad(0);
        tile.setZone(0);
        tile.setZoneID(-1);
        
        Tool.updateTile(tile);
    }

    public static void bulldoze(ArrayList<ArrayList<Tile>> tiles) {

        int width = tiles.size();
        int height = ((ArrayList)tiles.get(0)).size();

        if (((Tile)tiles.get(0).get(0)).zoneID() > -1) {
            Data.deleteZoneWithID(((Tile)tiles.get(0).get(0)).zoneID());
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                ((Tile)tiles.get(i).get(j)).setType(Tile.GROUND);
                ((Tile)tiles.get(i).get(j)).setRoad(0);
                ((Tile)tiles.get(i).get(j)).setZone(0);
                ((Tile)tiles.get(i).get(j)).setZoneID(-1);
            }
        }

        Tool.updateTiles(tiles);
    }
}
