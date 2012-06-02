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

        if (event.message().equals(SelectionEvent.TILES_SELECTED_FOR_ZONING)) {

            CSLogger.sharedLogger().info("Pending zone op: " + Zone.pendingOp());

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
                case FireStation.TYPE_ID: FireStation.build(event.tiles());
                    break;
                case PoliceStation.TYPE_ID: PoliceStation.build(event.tiles());
                    break;
                case CoalPowerPlant.TYPE_ID: 
                    CoalPowerPlant.build(event.tiles());
                    break;
                case NuclearPowerPlant.TYPE_ID:
                    NuclearPowerPlant.build(event.tiles());
                    break;
                default: break;
            }
        }
        else if (event.message().equals(SelectionEvent.TILE_SELECTED_FOR_POWERGRID)) {

            switch (PowerGrid.pendingOp()) {
                case PowerLine.TYPE_ID: PowerLine.buildPowerLine(event.tile(), PowerGrid.activeType());
                    break;
                case PowerNode.TYPE_ID: PowerNode.buildPowerNode(event.tile(), PowerGrid.activeType());
                    break;

                default: break;
            }
        }
        else if (event.message().equals(SelectionEvent.TILE_SELECTED_FOR_ROAD)) {

            switch (Road.pendingOp()) {
                case Street.TYPE_ID: Street.buildStreet(event.tile(), Road.activeType());
                break;

                default: break;
            }
        }
        else if (event.message().equals(SelectionEvent.TILE_SELECTED_FOR_TOOL)) {

            switch (Tool.pendingOp()) {
                case Bulldozer.TYPE_ID: Bulldozer.bulldoze(event.tile());
                break;

                default: break;
            }
        }
        else if (event.message().equals(SelectionEvent.TILES_SELECTED_FOR_TOOLS)) {

            switch (Tool.pendingOp()) {
                case Bulldozer.TYPE_ID: Bulldozer.bulldoze(event.tiles());
                break;

                default: break;
            }
        }
    }
}
