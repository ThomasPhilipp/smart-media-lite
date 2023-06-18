package com.zwickit.smart.media.ui.view.event;

public class KeyboardCloseEvent {

    private String inputText;

    public KeyboardCloseEvent(String inputText) {
        this.inputText = inputText;
    }

    public String getMode() {
        return inputText;
    }

    @Override
    public String toString() {
        return "KeyboardCloseEvent{" +
                "mode=" + inputText +
                '}';
    }
}
