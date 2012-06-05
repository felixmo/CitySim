/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.HashMap;

/**
 * Write a description of class ZoneTilesDBInsertThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneTileDBInsertThread extends CSThread
{

    private HashMap[] zoneTiles;    

    public ZoneTileDBInsertThread(HashMap[] zoneTiles) {
        super("ZoneTileDBInsertThread");
        this.zoneTiles = zoneTiles;
    }

    public void run() {
        DataSource.getInstance().insertZoneTiles(this.zoneTiles);
    }
}
