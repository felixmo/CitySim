/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class TaxCollectionThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FinancesThread extends CSThread  
{
    public FinancesThread() {
        super("FinancesThread");
    }
    
    public void run() {
        Taxation.collectTaxesAndDeductExpenses();
    }
}
