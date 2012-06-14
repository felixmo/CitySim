/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Write a description of class Employment here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Employment  
{
    public static ArrayList<Zone> employers = new ArrayList<Zone>();

    private static void reset() {
        DataSource.getInstance().resetJobAllocations();

        employers.clear();
        employers.addAll(Arrays.asList(Data.industrialZones()));
        employers.addAll(Arrays.asList(Data.commercialZones()));
    }
    
    public static void simulate() {
        
        reset();
        
        int totalCap = DataSource.getInstance().totalIndustrialCapacity() + DataSource.getInstance().totalCommercialCapacity();
        int pool = Math.min(Population.size(), totalCap);
        
        while (pool > 0) {
            for (Zone employer : employers) {
                if (employer.allocation() < employer.capacity()) {
                    employer.setAllocation(employer.allocation() + 1);
                    pool--;
                }
            }
        }
        
        Zone[] needUpdate = new Zone[employers.size()];
        employers.toArray(needUpdate);
        DataSource.getInstance().updateZones(needUpdate);
    }
}
