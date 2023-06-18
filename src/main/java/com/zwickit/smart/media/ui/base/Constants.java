package com.zwickit.smart.media.ui.base;

/**
 * Created by Thomas Philipp Zwick
 */
public class Constants {

    public static final String FILE_JXBROWSER = ".jxbrowser"; // hidden file
    public static final String LICENSE_JXBROWSER = "invalid";

    public static final String DIR_FONTS =     "fonts/";
    public static final String DIR_GRAPHICS =  "graphics/";
    public static final String DIR_TABLET =     "tablet/";
    public static final String DIR_1080P =      "hd1080/";

    public static final String PNG_CONTENT_LOADING = "content-loading.jpg";
    public static final String PNG_DEFAULT_LOGO = "default-logo.png";

    public static final String CSS_TABLET = "style.css";
    public static final String CSS_HD1080 = "style-1080p.css";

    public static final String ICON_SIZE_4_2EM = "4.2em";
    public static final String ICON_SIZE_4_5EM = "4.5em";
    public static final String ICON_SIZE_7EM = "7em";
    public static final String ICON_SIZE_12EM = "12em";

    public static final double HD1080_WIDTH_WINDOW =    1920.0;
    public static final double HD1080_WIDTH =            120.0;
    public static final double HD10880_HEIGHT_WINDOW =  1080.0;
    public static final double HD1080_HEIGHT =           120.0;

    public static final double TABLET_WIDTH_WINDOW =    1366.0;
    public static final double TABLET_WIDTH =             85.375;
    public static final double TABLET_HEIGHT_WINDOW =    768.0;
    public static final double TABLET_HEIGHT =            85.333;

    public static final double HD720_WIDTH_WINDOW =     1280.0;
    public static final double HD720_WIDTH =              80.0;
    public static final double HD720_HEIGHT_WINDOW =     720.0;
    public static final double HD720_HEIGHT =             80.0;

    public static final String WINDOW_TITLE = "Smart Media UI";

    /* ****************************************************************************
     * Header
     * ****************************************************************************/

    public static final String ID_HEADER_PANEL = "header-panel";

    public static final String ID_SETTING_BUTTON = "setting-button";
    public static final String ID_WEATHER_BUTTON = "weather-button";

    public static final String STYLE_HEADER_BUTTON = "header-button";

    /* ****************************************************************************
     * Navigation
     * ****************************************************************************/

    public static final String ID_NAVIGATION_PANEL = "navigation-panel";
    public static final String ID_NAVIGATION_BUTTON = "navigation-button";

    /* ****************************************************************************
     * Common
     * ****************************************************************************/

    public static final String ID_CONTROL_PANEL = "control-panel";
    public static final String ID_CONTROL_BUTTON = "control-button";
    public static final String ID_CONTROL_FIELD = "control-field";

    public static final String ID_HYPERLINK = "hyperlink";
    public static final String STYLE_DIAL_PAD_PANEL = "dial-pad-panel";
    public static final String STYLE_PHONE_BUTTON = "phone-button";

    public static final String ID_HOME_BUTTON = "home-button";
    public static final String ID_REFRESH_BUTTON = "refresh-button";
    public static final String ID_ZOOM_IN_BUTTON = "zoom-in-button";
    public static final String ID_ZOOM_OUT_BUTTON = "zoom-out-button";

    /* ****************************************************************************
     * Keyboard
     * ****************************************************************************/

    public static final String STYLE_KEYBOARD_VIEW = "keyboard-view";
    public static final String STYLE_KEYBOARD_PANEL = "keyboard-panel";
    public static final String STYLE_KEYBOARD_HEADER = "keyboard-header";
    public static final String STYLE_KEYBOARD_TEXT = "keyboard-text";
    public static final String STYLE_KEYBOARD_BUTTON = "keyboard-button";
    public static final String STYLE_KEYBOARD_OPTION_BUTTON = "keyboard-option-button";
    public static final String STYLE_KEYBOARD_EXIT_BUTTON = "keyboard-exit-button";

    /* ****************************************************************************
     * Others
     * ****************************************************************************/

    public static final String PARAM_URL = "url";

    public static final String HTML_TAG_BUTTON = "button";
    public static final String HTML_ATTRIBUTE_ID = "id";

    public static String loadCssFile(DisplayType displayType) {
        if (displayType == DisplayType.HD1080) {
            return CSS_HD1080;
        }
        else {
            return CSS_TABLET;
        }
    }
}
