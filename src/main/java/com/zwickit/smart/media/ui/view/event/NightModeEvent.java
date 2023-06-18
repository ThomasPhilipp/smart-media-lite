package com.zwickit.smart.media.ui.view.event;

public class NightModeEvent {

    private boolean nightMode;

    public NightModeEvent(boolean nightMode) {
        this.nightMode = nightMode;
    }

    public boolean isNightMode() {
        return nightMode;
    }

    @Override
    public String toString() {
        return "NightModeEvent{" +
                "nightMode=" + nightMode +
                '}';
    }
}
