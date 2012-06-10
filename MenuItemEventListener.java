/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import com.google.common.eventbus.Subscribe;

/**
 * MenuItemEventListener
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * Listens for menu item events and acts accordingly
 * 
 */

public class MenuItemEventListener extends CSEventListener
{

    @Subscribe
    public void listen(MenuItemEvent event) {
        CSLogger.sharedLogger().finer("\"" + event.message() + "\" was selected.");

        // * ZONING *
        if (event.message() == ResidentialZone.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setUnacceptedTypes( new int[]{ Tile.WATER } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(ResidentialZone.SIZE_WIDTH, ResidentialZone.SIZE_HEIGHT);
            Zone.setPendingOp(ResidentialZone.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as residential. Press 'ESC' when done."));
        }
        else if (event.message() == IndustrialZone.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(IndustrialZone.SIZE_WIDTH, IndustrialZone.SIZE_HEIGHT);
            Zone.setPendingOp(IndustrialZone.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as industrial. Press 'ESC' when done."));
        }
        else if (event.message() == CommercialZone.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(CommercialZone.SIZE_WIDTH, CommercialZone.SIZE_HEIGHT);
            Zone.setPendingOp(CommercialZone.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as commercial. Press 'ESC' when done."));
        }
        // * TRANSPORTATION *
        else if (event.message() == Street.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND, Tile.POWERLINE_H, Tile.POWERLINE_V } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(Street.SIZE_WIDTH, Street.SIZE_HEIGHT);    
            Road.setPendingOp(Street.TYPE_ID);
        }
        // * POWER *
        else if (event.message() == PowerLine.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND, Tile.STREET_H, Tile.STREET_V } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(PowerLine.SIZE_WIDTH, PowerLine.SIZE_HEIGHT);    
            PowerGrid.setPendingOp(PowerLine.TYPE_ID);
        }
        else if (event.message() == CoalPowerPlant.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(CoalPowerPlant.SIZE_WIDTH, CoalPowerPlant.SIZE_HEIGHT);
            Zone.setPendingOp(CoalPowerPlant.TYPE_ID);
        }
        else if (event.message() == NuclearPowerPlant.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(NuclearPowerPlant.SIZE_WIDTH, NuclearPowerPlant.SIZE_HEIGHT);
            Zone.setPendingOp(NuclearPowerPlant.TYPE_ID);
        }
        // * PROTECTION *
        else if (event.message() == FireStation.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(FireStation.SIZE_WIDTH, FireStation.SIZE_HEIGHT);
            Zone.setPendingOp(FireStation.TYPE_ID);
        }
        else if (event.message() == PoliceStation.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(PoliceStation.SIZE_WIDTH, PoliceStation.SIZE_HEIGHT);
            Zone.setPendingOp(PoliceStation.TYPE_ID);
        }
        // * RECREATION *
        else if (event.message() == Park.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(Park.SIZE_WIDTH, Park.SIZE_HEIGHT);    
            Recreation.setPendingOp(Park.TYPE_ID);
        }
        else if (event.message() == Stadium.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setUnacceptedZones( new int[] { ResidentialZone.TYPE_ID, CommercialZone.TYPE_ID, IndustrialZone.TYPE_ID });
            Map.getInstance().selection().setSize(Stadium.SIZE_WIDTH, Stadium.SIZE_HEIGHT);
            Zone.setPendingOp(Stadium.TYPE_ID);
        }
        // * TOOLS *
        else if (event.message() == Bulldozer.NAME) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setUnacceptedTypes( new int[]{ Tile.WATER } );
            Map.getInstance().selection().setSize(Bulldozer.SIZE_WIDTH, Bulldozer.SIZE_HEIGHT);
            Tool.setPendingOp(Bulldozer.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to bulldoze. Press 'ESC' when done."));
        }
    }
}