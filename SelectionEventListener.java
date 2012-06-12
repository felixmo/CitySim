/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import com.google.common.eventbus.Subscribe;
import greenfoot.*;
import java.awt.Point;

/**
 * Write a description of class SelectionEventListener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectionEventListener extends CSEventListener
{

    @Subscribe
    public void listen(SelectionEvent event) {
        CSLogger.sharedLogger().fine("Message received: " + event.message());

        if (event.message() == SelectionEvent.TILES_SELECTED_FOR_ZONING) {

            CSLogger.sharedLogger().fine("Pending zone op: " + Zone.pendingOp());

            switch (Zone.pendingOp()) {
                case ResidentialZone.TYPE_ID: 
                    ResidentialZone.zoneTiles(event.tiles());  
                    break;
                case IndustrialZone.TYPE_ID: 
                    IndustrialZone.zoneTiles(event.tiles());
                    break;
                case CommercialZone.TYPE_ID: 
                    CommercialZone.zoneTiles(event.tiles());
                    break;
                case FireStation.TYPE_ID: 
                    FireStation.build(event.tiles());
                    break;
                case PoliceStation.TYPE_ID: 
                    PoliceStation.build(event.tiles());
                    break;
                case CoalPowerPlant.TYPE_ID: 
                    CoalPowerPlant.build(event.tiles());
                    break;
                case NuclearPowerPlant.TYPE_ID:
                    NuclearPowerPlant.build(event.tiles());
                    break;
                case Stadium.TYPE_ID:
                    Stadium.build(event.tiles());
                    break;
                default: 
                    break;
            }
        }
        else if (event.message() == SelectionEvent.TILE_SELECTED_FOR_POWERGRID) {

            PowerLine.buildPowerLine(event.tile(), PowerGrid.activeType());
        }
        else if (event.message() == SelectionEvent.TILE_SELECTED_FOR_ROAD) {

            Street.buildStreet(event.tile(), Road.activeType());
        }
        else if (event.message() == SelectionEvent.TILE_SELECTED_FOR_TOOL) {

            switch (Tool.pendingOp()) {
                case Bulldozer.TYPE_ID: 
                    Bulldozer.bulldoze(event.tile());
                    break;
                case Query.TYPE_ID: 
                    Query.query(Data.zoneWithTile(event.tile()));
                    break;
                default: break;
            }
        }
        else if (event.message() == SelectionEvent.TILES_SELECTED_FOR_TOOLS) {

            switch (Tool.pendingOp()) {
                case Bulldozer.TYPE_ID: 
                    Bulldozer.bulldoze(event.tiles());
                    break;
                case Query.TYPE_ID: 
                    Query.query(Data.zoneWithTile(event.tiles().get(0).get(0)));
                    break;
                default: break;
            }
        }
        else if (event.message() == SelectionEvent.TILE_SELECTED_FOR_RECREATION) {

            Park.build(event.tile());
        }
    }
}
