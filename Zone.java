import java.util.ArrayList;
import java.lang.Integer;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Write a description of class Zoning here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zone  
{   
    private static int pendingOp = 0; // ID of zone type

    protected static LinkedHashMap<Integer, Integer> counts = new LinkedHashMap<Integer, Integer>();

    // - 

    protected static void updateTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

        int zoneID = Data.lastZoneID() + 1;
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_LASTZONEID, zoneID);

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

        Integer value = counts.get((Integer)pendingOp);
        counts.put((Integer)pendingOp, value == null ? (Integer)count : (Integer)(value + count));

        switch (pendingOp) {
            case CommercialZone.ID: zoneStats.put(Data.ZONESTATS_COMMERCIALCOUNT, counts.get(pendingOp));
            break;
            case IndustrialZone.ID: zoneStats.put(Data.ZONESTATS_INDUSTRIALCOUNT, counts.get(pendingOp));
            break;
            case ResidentialZone.ID: zoneStats.put(Data.ZONESTATS_RESIDENTIALCOUNT, counts.get(pendingOp));
            break;
            default: break;
        }

        Data.insertZoneWithTiles(zoneTiles);
        Data.updateZoneStats(zoneStats);
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
