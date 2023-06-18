package com.zwickit.smart.media.ui.base;

import javafx.scene.text.Font;

import java.net.URL;

import static com.zwickit.smart.media.ui.base.Constants.DIR_FONTS;

/**
 * Created by Thomas Philipp Zwick
 */
public class FontFx {

    private static final URL ROBOTO_BOLD = loadFontUrl("Roboto-Bold.ttf");
    private static final URL ROBOTO_LIGHT = loadFontUrl("Roboto-Light.ttf");
    private static final URL ROBOTO_REGULAR = loadFontUrl("Roboto-Regular.ttf");

    public static final Font ROBOTO_REGULAR_30 = loadFont(ROBOTO_REGULAR, 30);
    public static final Font ROBOTO_REGULAR_50 = loadFont(ROBOTO_REGULAR, 50);
    public static final Font ROBOTO_REGULAR_70 = loadFont(ROBOTO_REGULAR, 70);

    private static URL loadFontUrl(String fontName) {
        return FontFx.class.getClassLoader().getResource(DIR_FONTS + fontName);
    }

    private static Font loadFont(URL fontUrl, int size) {
        return Font.loadFont(String.valueOf(fontUrl), size);
    }

}
