import java.util.ArrayList;
import java.lang.Integer;
import java.util.HashMap;

/**
 * Write a description of class Zoning here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zone  
{   
    private static int pendingOp = 0; // ID of zone type

    // - 

    protected static void updateTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

        int zoneID = Data.idForNewZone();
        
        // TODO: check if zone is powered and has water
        Tile center = (Tile)selectedTiles.get(1).get(1);
        Data.insertZone(zoneID, pendingOp, center.powered(), center.position().x, center.position().y);

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int count = 0;

        CSLogger.sharedLogger().info("Zoning " + width*height + " tiles as type " + pendingOp);

        HashMap[] zoneTiles = new HashMap[width*height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                HashMap zone = new HashMap(2);
                zone.put(Data.ZONETILE_ZONEID, zoneID);
                zone.put(Data.ZONETILE_TILEID, ((Tile)selectedTiles.get(i).get(j)).dbID());
                zoneTiles[count] = zone;

                ((Tile)selectedTiles.get(i).get(j)).setZone(pendingOp);
                ((Tile)selectedTiles.get(i).get(j)).setZoneID(zoneID);

                count++;
            }
        }

        Data.insertZoneTiles(zoneTiles);
        Data.updateTiles(selectedTiles);
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
