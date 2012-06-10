/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.lang.Thread;

/**
 * Write a description of class CSThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class CSThread extends Thread
{

    private static CSThreadGroup threadGroup = new CSThreadGroup();
    private static Thread mainThread = null;
    
    private static int count = 0;
    private int id = 0;
    
    public CSThread(String name) {
        super(threadGroup, name + "(" + (count+=1) + ")");
        this.id = count;
    }
    
    public int id() {
        return this.id;
    }
    
    public static int count() {
        return threadGroup.activeCount();
    }
    
    public static Thread mainThread() {
        return mainThread;
    }
    
    public static void setMainThread(Thread thread) {
        mainThread = thread;
    }
}
