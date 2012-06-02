/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.List;
import java.util.HashMap;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.QueryRunner;
import java.sql.Connection;

/**
 * Write a description of class TilesMatchingCriteriaQueryCallable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TilesMatchingCriteriaQueryCallable extends CSSQLCallable
{

    public TilesMatchingCriteriaQueryCallable(Connection connection, String criteria) {
        super(connection, criteria);
    }

    public Tile[] call() throws Exception {
        
        CSLogger.sharedLogger().fine("Running query for tiles matching criteria (\"" + this.sql + "\")");
        
        // Run query
        List results = (List)(new QueryRunner().query(connection, "SELECT * FROM tiles WHERE " + this.sql + ";", new MapListHandler()));
        
        // Parse & return results
        Tile[] tiles = new Tile[results.size()];
        for (int i = 0; i <  results.size(); i++) {
            tiles[i] = new Tile((HashMap)results.get(i));
        }
        return tiles;
    }
}
