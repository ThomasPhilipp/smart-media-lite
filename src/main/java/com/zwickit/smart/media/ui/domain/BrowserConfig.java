package com.zwickit.smart.media.ui.domain;

/**
 * Created by Thomas Philipp Zwick
 */
public class BrowserConfig {

    /**
     * The URL of the home-page which is loaded by opening the app.
     */
    private String homeUrl;

    /**
     * The URL of the hospital which is loaded by clicking on a specific carousel-button.
     */
    private String hospitalUrl;

    /**
     * The URL of the weather-page which is loaded by clicking on the Weather-button.
     */
    private String weatherUrl;

    /**
     * The URL of the home-page which is loaded by opening the Browser-view.
     */
    private String homepageUrl;

    /**
     * The URL of the media-library.
     */
    private String mediathekUrl;

    /**
     * The setting to enable/disable proprietary codecs.
     */
    private boolean proprietaryCodecs = true;

    /**
     * The setting to set the rendering-mode: off-screen (default) or hardware
     * accelerated. More details see https://jxbrowser-support.teamdev.com/docs/guides/browser-view.html#rendering.
     */
    private boolean hardwareAccelerated = false; // optional

    public BrowserConfig() {
        // empty
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getHospitalUrl() {
        return hospitalUrl;
    }

    public void setHospitalUrl(String hospitalUrl) {
        this.hospitalUrl = hospitalUrl;
    }

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getMediathekUrl() {
        return mediathekUrl;
    }

    public void setMediathekUrl(String mediathekUrl) {
        this.mediathekUrl = mediathekUrl;
    }

    public boolean isProprietaryCodecs() {
        return proprietaryCodecs;
    }

    public void setProprietaryCodecs(boolean proprietaryCodecs) {
        this.proprietaryCodecs = proprietaryCodecs;
    }

    public boolean isHardwareAccelerated() {
        return hardwareAccelerated;
    }

    public void setHardwareAccelerated(boolean hardwareAccelerated) {
        this.hardwareAccelerated = hardwareAccelerated;
    }

    @Override
    public String toString() {
        return "BrowserConfig{" +
                "homeUrl='" + homeUrl + '\'' +
                ", hospitalUrl='" + hospitalUrl + '\'' +
                ", weatherUrl='" + weatherUrl + '\'' +
                ", homepageUrl='" + homepageUrl + '\'' +
                ", mediathekUrl='" + mediathekUrl + '\'' +
                ", proprietaryCodecs=" + proprietaryCodecs +
                ", hardwareAccelerated=" + hardwareAccelerated +
                '}';
    }
}
