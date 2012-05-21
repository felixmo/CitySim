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
    public static final int TYPE_ID = 1;
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;

    public static void bulldoze(Tile tile) {

        if (tile.road() == Street.TYPE_ID) Street.subtractFromCount(1);

        tile.setType(Tile.GROUND);
        tile.setRoad(0);
        tile.setZone(0);
        tile.setZoneID(-1);

        Tool.updateTile(tile);
    }

    public static void bulldoze(ArrayList<ArrayList<Tile>> tiles) {

        int width = tiles.size();
        int height = ((ArrayList)tiles.get(0)).size();

        int zone_type = ((Tile)tiles.get(0).get(0)).zone();
        switch (zone_type) {
            case CommercialZone.TYPE_ID: CommercialZone.subtractFromCount(width*height);
            break;
            case IndustrialZone.TYPE_ID: IndustrialZone.subtractFromCount(width*height);
            break;
            case ResidentialZone.TYPE_ID: ResidentialZone.subtractFromCount(width*height);
            break;
            default:
            break;
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (((Tile)tiles.get(i).get(j)).road() == Street.TYPE_ID) Street.subtractFromCount(1);

                ((Tile)tiles.get(i).get(j)).setType(Tile.GROUND);
                ((Tile)tiles.get(i).get(j)).setRoad(0);
                ((Tile)tiles.get(i).get(j)).setZone(0);
                ((Tile)tiles.get(i).get(j)).setZoneID(-1);
            }
        }

        Tool.updateTiles(tiles);
    }
}
