/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.sql.Connection;

/**
 * Write a description of class CSSQLCallable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class CSSQLCallable extends CSCallable
{

    protected Connection connection;
    protected String sql;
    
    public CSSQLCallable(Connection connection) {
        this.connection = connection;
    }
    
    public CSSQLCallable(Connection connection, String sql) {
        this.connection = connection;
        this.sql = sql;
    }
}
