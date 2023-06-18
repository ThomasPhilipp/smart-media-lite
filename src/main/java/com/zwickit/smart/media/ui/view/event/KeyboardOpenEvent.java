package com.zwickit.smart.media.ui.view.event;

public class KeyboardOpenEvent {

    private String mode;

    public KeyboardOpenEvent(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "KeyboardOpenEvent{" +
                "mode=" + mode +
                '}';
    }
}
