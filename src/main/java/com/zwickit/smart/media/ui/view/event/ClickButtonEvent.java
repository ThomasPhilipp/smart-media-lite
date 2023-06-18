
package com.zwickit.smart.media.ui.view.event;

/**
 * Created by Thomas Philipp Zwick
 *
 * Examples:
 * http://codingjunkie.net/guava-eventbus/
 * https://github.com/google/guava/wiki/EventBusExplained
 *
 */
public class ClickButtonEvent {

    private String buttonId;

    public ClickButtonEvent(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getButtonId() {
        return buttonId;
    }

    @Override
    public String toString() {
        return "ClickButtonEvent{" +
                "buttonId='" + buttonId + '\'' +
                '}';
    }
}
