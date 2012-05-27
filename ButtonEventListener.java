/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import com.google.common.eventbus.Subscribe;

/**
 * ButtonEventListener
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * Listens for button events and acts accordingly
 * 
 */

public class ButtonEventListener extends CSEventListener
{

    @Subscribe
    public void listen(ButtonEvent event) {
        System.out.println(event.message());   
    }
}
