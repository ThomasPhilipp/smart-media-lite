package com.zwickit.smart.media.ui.view.common;

import com.google.common.eventbus.Subscribe;

/**
 * Created by Thomas Philipp Zwick
 */
public abstract class EventBusSubscriber<T> {

    @Subscribe
    public abstract void onEventReceived(T event);

}

// ------------------------------------------------------------------------
//
// Event-Bus Handlers
//
// ------------------------------------------------------------------------


/*


        // Register UI-events
        subscribe(new NightModeEventSubscriber());



 extends EventBusSubscriber<NightModeEvent>


            LOGGER.debug("Published event: {}", event);


 */
