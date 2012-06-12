/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.lang.Integer;
import java.util.HashMap;

/**
 * Write a description of class Selection here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Selection extends Actor
{

    private Tile activeTile;
    private Rectangle viewport;
    private boolean active = true;
    private boolean selectionMode = false;
    private ArrayList<Integer> acceptedTypes = new ArrayList<Integer>();          // accepted types of tiles for selection
    private ArrayList<Integer> unacceptedTypes = new ArrayList<Integer>();
    private ArrayList<Integer> unacceptedRoads = new ArrayList<Integer>();
    private ArrayList<Integer> unacceptedZones = new ArrayList<Integer>();
    private Dimension size = new Dimension(1, 1);
    private boolean customSize = false;

    private GreenfootImage image;

    public Selection(int width, int height) {

        this.image = new GreenfootImage(width, height);
        setImage(this.image);
    }

    private void draw() {

        if (activeTile == null || viewport == null || !active) return;

        this.image.setTransparency(255);

        this.image.clear();

        if (selectionMode) {

            if (!selectionIsValid()) {
                this.image.setTransparency(100);
                this.image.setColor(Color.RED);
                this.image.fillRect((((activeTile.position().x) * Tile.SIZE) - viewport.x), (((activeTile.position().y-2) * Tile.SIZE) - viewport.y), (int)size.getWidth()*Tile.SIZE, (int)size.getHeight()*Tile.SIZE);
            }
        }

        this.image.setColor(Color.WHITE);
        this.image.drawRect((((activeTile.position().x) * Tile.SIZE) - viewport.x), (((activeTile.position().y-2) * Tile.SIZE) - viewport.y), (int)size.getWidth()*Tile.SIZE, (int)size.getHeight()*Tile.SIZE);
    }

    public void act() {

        if (selectionMode) {

            if (Greenfoot.mouseClicked(this)) {

                if (selectionIsValid()) {
                    if (Zone.pendingOp() > 0) CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED_FOR_ZONING, selectedTiles()));
                    if (Road.pendingOp() > 0) CSEventBus.post(new SelectionEvent(SelectionEvent.TILE_SELECTED_FOR_ROAD, activeTile));
                    if (Tool.pendingOp() > 0) {
                        if (size.getWidth() == 1 && size.getHeight() == 1) {
                            CSEventBus.post(new SelectionEvent(SelectionEvent.TILE_SELECTED_FOR_TOOL, activeTile));
                        }
                        else {
                            CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED_FOR_TOOLS, selectedTiles()));
                        }
                    }
                    if (PowerGrid.pendingOp() > 0) {
                        if (size.getWidth() == 1 && size.getHeight() == 1) {
                            CSEventBus.post(new SelectionEvent(SelectionEvent.TILE_SELECTED_FOR_POWERGRID, activeTile));
                        }
                        else {
                            CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED_FOR_POWERGRID, selectedTiles()));
                        }
                    }
                    if (Recreation.pendingOp() > 0) {
                        if (size.getWidth() == 1 && size.getHeight() == 1) {
                            CSEventBus.post(new SelectionEvent(SelectionEvent.TILE_SELECTED_FOR_RECREATION, activeTile));
                        }
                        else {
                            CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED_FOR_RECREATION, selectedTiles()));   
                        }
                    }
                }
            } 
            /*
            else if (Greenfoot.mouseDragged(this)) {

            if (Road.pendingOp() > 0) CSEventBus.post(new SelectionEvent(SelectionEvent.TILE_SELECTED_FOR_ROAD, activeTile));
            if (Tool.pendingOp() > 0) {
            if (size.getWidth() == 1 && size.getHeight() == 1) {
            CSEventBus.post(new SelectionEvent(SelectionEvent.TILE_SELECTED_FOR_TOOL, activeTile));
            }
            else {
            CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED_FOR_TOOLS, selectedTiles()));
            }
            }
            if (PowerGrid.pendingOp() > 0) {
            if (size.getWidth() == 1 && size.getHeight() == 1) {
            CSEventBus.post(new SelectionEvent(SelectionEvent.TILE_SELECTED_FOR_POWERGRID, activeTile));
            }
            else {
            CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED_FOR_POWERGRID, selectedTiles()));
            }
            }
            }
             */

            // Increases (up key) and decreases (down key) the selection size

            String key = Greenfoot.getKey();
            if (key != null) {
                if (key.equalsIgnoreCase("up")) {
                    size.setSize(size.getWidth()+1, size.getHeight()+1);
                    draw();
                }
                else if (key.equalsIgnoreCase("down")) {
                    if (!(size.getWidth() == 1 && size.getHeight() == 1)) { 
                        size.setSize(size.getWidth()-1, size.getHeight()-1);
                        draw();
                    }
                }
            }

            if (Greenfoot.isKeyDown("escape")) {
                setSelectionMode(false);
                this.customSize = false;

                Zone.setPendingOp(0);
                Road.setPendingOp(0);
                Tool.setPendingOp(0);
                PowerGrid.setPendingOp(0);
                Recreation.setPendingOp(0);

                City.getInstance().removeHint();
            }
        }
        // FOR TESTING
//         else {
//             if (Greenfoot.mouseClicked(this)) {
//                 if (size.width > 1) {
//                     Zone zone = Data.zonesMatchingCriteria("id = " + this.activeTile.zoneID())[0];
//                     System.out.println("ZONE (" + zone.dbID() + ")" + " | SCORE: " + zone.score() + " | POLLUTION : " + zone.pollution() + " | A/C: " + zone.allocation() + "/" + zone.capacity() + " | POWERED: " + (zone.powered() == 1 ? "YES" : "NO"));
//                 }
//                 else {
//                     System.out.println("Type: " + this.activeTile.type() + " | Power grid type: " + this.activeTile.powerGrid() + " | Powered: " + this.activeTile.powered());
//                 }
//             }
//         }
    }

    public ArrayList<ArrayList<Tile>> selectedTiles() {

        ArrayList<ArrayList<Tile>> tiles = Data.tiles();

        ArrayList<ArrayList<Tile>> selectedTiles = new ArrayList<ArrayList<Tile>>((int)size.getWidth());
        for (int i = 0; i < size.getWidth(); i++) {
            selectedTiles.add(new ArrayList<Tile>((int)size.getHeight()));
        }

        int i = 0;

        for (int x = activeTile.position().x; x < activeTile.position().x + (int)size.getWidth(); x++) {
            for (int y = activeTile.position().y; y < activeTile.position().y + (int)size.getHeight(); y++) {
                selectedTiles.get(i).add(tiles.get(x).get(y));
            }
            i++;
        }

        return selectedTiles;
    }

    private boolean selectionIsValid() {

        // Check type

        for (int x = activeTile.position().x; x < (activeTile.position().x + size.getWidth()); x++) {
            for (int y = activeTile.position().y; y < (activeTile.position().y + size.getHeight()); y++) {

                if (this.acceptedTypes.size() > 0) {

                    if (!acceptedTypes.contains(Data.tiles().get(x).get(y).type())) {
                        return false;
                    }
                }

                if (this.unacceptedTypes.size() > 0) {

                    if (unacceptedTypes.contains(Data.tiles().get(x).get(y).type())) {
                        return false;
                    }
                }

                if (this.unacceptedRoads.size() > 0) {
                    if (unacceptedRoads.contains(Data.tiles().get(x).get(y).road())) {
                        return false;
                    }
                }

                if (this.unacceptedZones.size() > 0) {
                    if (unacceptedZones.contains(Data.tiles().get(x).get(y).zone())) {
                        return false;
                    }
                }

            }
        }

        return true;
    }

    /*
     * ACCESSORS *
     */

    public boolean active() {
        return this.active;
    }

    public void setActive(boolean value) {
        this.active = value;

        if (!this.active) {
            this.image.setTransparency(0);
        }
    }

    public boolean selectionMode() {
        return this.selectionMode;
    }

    public void setSelectionMode(boolean value) {

        CSLogger.sharedLogger().fine("Setting selection mode: " + (value ? "ON" : "OFF"));

        this.size.setSize(1, 1);
        this.customSize = false;

        this.active = !value;
        this.selectionMode = value;
        if (!value) {
            this.acceptedTypes.clear();
            this.unacceptedTypes.clear();
            this.unacceptedRoads.clear();
            this.unacceptedZones.clear();
        }
    }

    public Tile activeTile() {
        return this.activeTile;
    }

    public void setActiveTile(Tile tile) {

        try {

            if (this.activeTile == tile) return;

            if ((!selectionMode || Tool.pendingOp() == Bulldozer.TYPE_ID || Tool.pendingOp() == Query.TYPE_ID) && tile.zoneID() > -1) {
                // ONLY SNAP IF:
                // - normal mode
                // - bulldozer

                if (this.activeTile.zoneID() != tile.zoneID()) {
                    int id = Data.tilesInZoneWithID(tile.zoneID())[0];
                    this.activeTile = Data.tileWithID(id);
                }
                if (this.activeTile.zone() > 0) {
                    if (this.activeTile.zone() <= 3 || this.activeTile.zone() > 5) {
                        setSize(3, 3);
                    }
                    else if (this.activeTile.zone() > 3 && this.activeTile.zone() <= 5) {
                        setSize(4, 4);
                    }
                }

                this.customSize = false;
            }
            else {
                this.activeTile = tile;
                if (!this.customSize) {
                    setSize(1, 1);
                }
            }

            draw();

        }
        catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public void setViewport(Rectangle viewport) {
        this.viewport = viewport;
        draw();
    }

    public void setAcceptedTypes(int[] types) {
        this.acceptedTypes.clear();
        for (int type : types) {
            this.acceptedTypes.add(new Integer(type));
        }
    }

    public void setUnacceptedTypes(int[] types) {
        this.unacceptedTypes.clear();
        for (int type : types) {
            this.unacceptedTypes.add(new Integer(type));
        }
    }

    public void setUnacceptedRoads(int[] roads) {
        this.unacceptedRoads.clear();
        for (int road : roads) {
            this.unacceptedRoads.add(new Integer(road));
        }
    }

    public void setUnacceptedZones(int[] zones) {
        this.unacceptedZones.clear();
        for (int zone : zones) {
            this.unacceptedZones.add(new Integer(zone));
        }
    }

    public void setSize(int width, int height) {
        this.size.setSize(width, height);
        this.customSize = true;
    }

    public void setSize(Dimension size) {
        this.size = size;
        this.customSize = true;
    }
}
