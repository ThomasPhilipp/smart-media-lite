package com.zwickit.smart.media.ui.domain;

import com.zwickit.smart.media.ui.base.DisplayType;

import java.util.*;

/**
 * Created by Thomas Philipp Zwick
 */
public class SmartMediaConfig {

    private Optional<String> client; // optional: empty
    private String locale; // optional: default = English
    private String displayType;
    private String dataPath;
    private String iconPath;
    private String helpVideoPath;
    private String pinHash;
    private String pinMasterHash;

    private boolean browserEnabled;
    private boolean kioskEnabled;
    private boolean mediathekEnabled;

    private BrowserConfig browserConfig;

    public SmartMediaConfig() {
        // empty
    }

    public Optional<String> getClient() {
        return client != null ? client : Optional.empty();
    }

    public void setClient(String client) {
        this.client = Optional.of(client);
    }

    public String getLocale() {
        return locale;
    }

    public Locale getLocaleEnum() {
        if (locale == null)
            return Locale.ENGLISH;

        return Locale.forLanguageTag(locale);
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDisplayType() {
        return displayType;
    }

    public DisplayType getDisplayTypeEnum() {
        return DisplayType.valueOf(displayType);
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getHelpVideoPath() {
        return helpVideoPath;
    }

    public void setHelpVideoPath(String helpVideoPath) {
        this.helpVideoPath = helpVideoPath;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public String getPinMasterHash() {
        return pinMasterHash;
    }

    public void setPinMasterHash(String pinMasterHash) {
        this.pinMasterHash = pinMasterHash;
    }

    public boolean isBrowserEnabled() {
        return browserEnabled;
    }

    public void setBrowserEnabled(boolean browserEnabled) {
        this.browserEnabled = browserEnabled;
    }

    public boolean isKioskEnabled() {
        return kioskEnabled;
    }

    public void setKioskEnabled(boolean kioskEnabled) {
        this.kioskEnabled = kioskEnabled;
    }

    public boolean isMediathekEnabled() {
        return mediathekEnabled;
    }

    public void setMediathekEnabled(boolean mediathekEnabled) {
        this.mediathekEnabled = mediathekEnabled;
    }

    public BrowserConfig getBrowserConfig() {
        return browserConfig;
    }

    public void setBrowserConfig(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
    }

    public boolean isTablet() {
        return getDisplayTypeEnum() == DisplayType.TABLET;
    }

    public boolean is1080p() {
        return getDisplayTypeEnum() == DisplayType.HD1080;
    }

    @Override
    public String toString() {
        return "SmartMediaConfig{" +
                "locale='" + locale + '\'' +
                ", displayType='" + displayType + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", helpVideoPath='" + helpVideoPath + '\'' +
                ", pinHash='" + pinHash + '\'' +
                ", pinMasterHash='" + pinMasterHash + '\'' +
                ", browserEnabled='" + browserEnabled + '\'' +
                ", kioskEnabled='" + kioskEnabled + '\'' +
                ", browserConfig=" + browserConfig +
                '}';
    }
}
