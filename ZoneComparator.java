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
 * Write a description of class ZoneComparator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneComparator implements Comparator<Zone>
{
    private Point origin = null;

    public ZoneComparator() {
        
    }
    
    public ZoneComparator(Point origin) {
        this.origin = origin;   
    }

    public int compare(Zone a, Zone b) {

        Point a_pos = a.origin();
        Point b_pos = b.origin();

        // If origin was specified, compare the points' distances from the origin.
        if (origin != null) {
            
            Point a_d = distanceFromOrigin(a.origin());
            Point b_d = distanceFromOrigin(b.origin());
            
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
