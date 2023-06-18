package com.zwickit.smart.media.ui.view.event;

public class FullScreenEvent {

    private final boolean fullScreen;

    public FullScreenEvent(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    @Override
    public String toString() {
        return "FullScreenEvent{" +
                "fullScreen=" + fullScreen +
                '}';
    }
}
