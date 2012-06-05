/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;

/**
 * Write a description of class DBWriteThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileDBUpdateThread extends CSThread
{
    private Tile tile;
    private ArrayList<ArrayList<Tile>> tiles_list;
    private Tile[] tiles;

    public TileDBUpdateThread(Tile tile) {
        super("TileDBUpdateThread");
        this.tile = tile;
    }

    public TileDBUpdateThread(ArrayList<ArrayList<Tile>> tiles) {
        super("TileDBUpdateThread");
        this.tiles_list = tiles;
    }

    public TileDBUpdateThread(Tile[] tiles) {
        super("TileDBUpdateThread");
        this.tiles = tiles;
    }

    public void run() {
        if (tile != null) {
            DataSource.getInstance().updateTile(tile);
        }
        else if (tiles_list != null) {
            DataSource.getInstance().updateTiles(tiles_list);
        }
        else if (tiles != null) {
            DataSource.getInstance().updateTiles(tiles);
        }
    }
}
