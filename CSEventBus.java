/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import com.google.common.eventbus.EventBus;

/**
 * CSEventBus
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * Manages the shared EventBus
 * 
 */

public class CSEventBus  
{
    private static EventBus sharedEventBus = null;
    
    static {
        if (sharedEventBus == null) {
            sharedEventBus = new EventBus("com.felixmo.CitySim.CSEventBus.EventBus");
            
            // Register listeners
            sharedEventBus.register(new MenuItemEventListener());
            sharedEventBus.register(new SelectionEventListener());
        }
    }
    
    public static void post(Object event) {
        sharedEventBus.post(event);
    }
}
