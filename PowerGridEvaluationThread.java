/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class PowerGridEvaluationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGridEvaluationThread extends CSThread
{
    public PowerGridEvaluationThread()
    {
        super("PowerGridEvaluationThread");
    }
    
    public void run() {
        PowerGrid.evaluate();
    }
}
