/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.awt.Point;
import java.util.Comparator;
import java.lang.Math;

/**
 * Write a description of class PointComparator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileComparator implements Comparator<Tile>
{
    private Point origin = null;

    public TileComparator() {
        
    }
    
    public TileComparator(Point origin) {
        this.origin = origin;   
    }

    public int compare(Tile a, Tile b) {

        Point a_pos = a.position();
        Point b_pos = b.position();

        // If origin was specified, compare the points' distances from the origin.
        if (origin != null) {
            
            Point a_d = distanceFromOrigin(a.position());
            Point b_d = distanceFromOrigin(b.position());
            
            if (a_d.x == b_d.x) {
                return (int)(a_d.y - b_d.y);
            }
            else {
                return (int)(a_d.x - b_d.y);
            }
        }
        else {
            if (a_pos.x == b_pos.x) {
                return (int)(a_pos.y - b_pos.y);
            }
            else {
                return (int)(a_pos.x - b_pos.x);
            }
        }
    }
    
    private Point distanceFromOrigin(Point coordinate) {
        return new Point(Math.abs(coordinate.x - this.origin.x), Math.abs(coordinate.y - this.origin.y));
    }
}
