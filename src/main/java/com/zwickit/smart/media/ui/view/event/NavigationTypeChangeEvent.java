package com.zwickit.smart.media.ui.view.event;

import com.zwickit.smart.media.ui.view.common.NavigationType;

import java.util.Map;

/**
 * Created by Thomas Philipp Zwick
 *
 * Examples:
 * http://codingjunkie.net/guava-eventbus/
 * https://github.com/google/guava/wiki/EventBusExplained
 *
 */
public class NavigationTypeChangeEvent {

    private NavigationType type;

    private Map<String, Object> context;

    public NavigationTypeChangeEvent(NavigationType type) {
        this(type, null);
    }

    public NavigationTypeChangeEvent(NavigationType type, Map<String, Object> context) {
        this.type = type;
        this.context = context;
    }

    public NavigationType getType() {
        return type;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "NavigationTypeChangeEvent{" +
                "type=" + type +
                ", context='" + context + '\'' +
                '}';
    }
}
