package com.zwickit.smart.media.ui.view.event;

import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardButtonType;
import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardType;

import java.util.HashMap;
import java.util.Map;

public class KeyboardButtonActionEvent {

    private final KeyboardType keyboardType;

    private final KeyboardButtonType buttonType;

    private final Map<String, String> parameters;

    public KeyboardButtonActionEvent(KeyboardType keyboardType, KeyboardButtonType buttonType) {
        this(keyboardType, buttonType, null);
    }

    public KeyboardButtonActionEvent(KeyboardType keyboardType, KeyboardButtonType buttonType, Map<String, String> parameters) {
        this.keyboardType = keyboardType;
        this.buttonType = buttonType;
        this.parameters = parameters;
    }

    public KeyboardType getKeyboardType() {
        return keyboardType;
    }

    public KeyboardButtonType getButtonType() {
        return buttonType;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "KeyboardButtonActionEvent{" +
                "buttonType=" + buttonType +
                ", parameters=" + parameters +
                '}';
    }
}
