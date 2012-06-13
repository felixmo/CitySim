/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Write a description of class PowerGridZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGridZone extends Zone
{
    public PowerGridZone(HashMap properties) {
        super(properties);
    }

    protected static void updateTiles(ArrayList<ArrayList<Tile>> selectedTiles) {
        // Should only be called for power plants

        int zoneID = Data.idForNewZone();

        // TODO: check if zone is powered and has water
        Tile center = (Tile)selectedTiles.get(0).get(0);
        Data.insertZone(zoneID, pendingOp, center.position().x, center.position().y, zoneID, (pendingOp == CoalPowerPlant.TYPE_ID ? CoalPowerPlant.CAPACITY : NuclearPowerPlant.CAPACITY));

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int count = 0;

//         CSLogger.sharedLogger().info("Zoning " + width*height + " tiles as type " + pendingOp);

        HashMap[] zoneTiles = new HashMap[width*height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                HashMap zone = new HashMap(2);
                zone.put(Data.ZONETILE_ZONEID, zoneID);
                zone.put(Data.ZONETILE_TILEID, ((Tile)selectedTiles.get(i).get(j)).dbID());
                zoneTiles[count] = zone;

                ((Tile)selectedTiles.get(i).get(j)).setPowered(zoneID);
                ((Tile)selectedTiles.get(i).get(j)).setZone(pendingOp);
                ((Tile)selectedTiles.get(i).get(j)).setZoneID(zoneID);

                count++;
            }
        }

        Data.insertZoneTiles(zoneTiles);
        Data.updateTiles(selectedTiles);

        new PowerGridEvaluationThread().start();
    }
    
    public int allocation() {
        return DataSource.getInstance().allocationForPowerPlant(this);
    }
}
