/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import com.google.common.eventbus.Subscribe;

/**
 * Write a description of class TileSelectorEventListener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileSelectorEventListener extends CSEventListener
{

    @Subscribe
    public void listen(TileSelectorEvent event) {
        CSLogger.sharedLogger().debug("\"" + event.message() + "\" was selected.");

        if (Road.pendingOp() > 0) {
            Road.setActiveType(event.type());
        }
        else if (PowerGrid.pendingOp() > 0) {
            PowerGrid.setActiveType(event.type());
        }
    }
}
