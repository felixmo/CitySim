/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * MenuItemEvent
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * The message sent to 'MenuItemEventListener' via 'CSEventBus'
 * 
 */
public class MenuItemEvent extends CSEvent
{
    
    public MenuItemEvent(String message) {
        super(message);
    }
}

