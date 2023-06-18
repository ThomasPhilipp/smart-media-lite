package com.zwickit.smart.media.ui.view.event;

/**
 * Created by Thomas Philipp Zwick
 */
public class PadChangeEvent {

    private String value;

    public PadChangeEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PadChangeEvent{" +
                "value='" + value + '\'' +
                '}';
    }
}
