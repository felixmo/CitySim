/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.TimerTask;

/**
 * DateIncrementor
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-16-2012
 * 
 * 
 * 
 */

public class DateIncrementor extends TimerTask
{
    public void run() {
        Date.increment();
    }
}
