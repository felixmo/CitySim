/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class PowerGridEvaluationTileSearchThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGridEvaluationTileSearchThread extends CSThread
{
    private Tile tile;
    private PowerGridZone plant;
    
    public PowerGridEvaluationTileSearchThread(Tile tile, PowerGridZone plant) {
        super("PowerGridEvaluationTileSearchThread");
        this.tile = tile;
        this.plant = plant;
    }
    
    public void run() {
        PowerGrid.searchTile(this.tile, this.plant);
    }
}
