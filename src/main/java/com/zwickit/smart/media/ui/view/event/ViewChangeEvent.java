package com.zwickit.smart.media.ui.view.event;

import com.zwickit.smart.media.ui.view.common.NavigationType;

/**
 * The event is triggered after the displayed view has changed to notify other
 * view-parts about this change.
 *
 * Created by Thomas Philipp Zwick
 */
public class ViewChangeEvent {

    private final NavigationType navigationType;

    public ViewChangeEvent(NavigationType navigationType) {
        this.navigationType = navigationType;
    }

    public NavigationType getNavigationType() {
        return navigationType;
    }

    @Override
    public String toString() {
        return "ViewChangeEvent{" +
                "navigationType=" + navigationType +
                '}';
    }
}
