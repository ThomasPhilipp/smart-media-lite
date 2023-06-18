package com.zwickit.smart.media.ui.base;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Thomas Philipp Zwick
 */
public class FontSwing {

    private static final Font ROBOTO_BOLD = loadFont("Roboto-Bold.ttf").deriveFont(Font.BOLD);
    private static final Font ROBOTO_LIGHT = loadFont("Roboto-Light.ttf").deriveFont(Font.PLAIN);
    private static final Font ROBOTO_REGULAR = loadFont("Roboto-Regular.ttf").deriveFont(Font.PLAIN);

    public static final Font ROBOTO_BOLD_30 = ROBOTO_BOLD.deriveFont(30.0f);
    public static final Font ROBOTO_LIGHT_30 = ROBOTO_LIGHT.deriveFont(30.0f);
    public static final Font ROBOTO_REGULAR_30 = ROBOTO_REGULAR.deriveFont(30.0f);

    private static final Font WEBFONT = loadFont("fontawesome-webfont.ttf").deriveFont(Font.TRUETYPE_FONT);
    public static final Font WEBFONT_75 = WEBFONT.deriveFont(75.0f);



    private static Font loadFont(String resourceName) {
        try (InputStream inputStream = FontSwing.class.getResourceAsStream("/fonts/" + resourceName)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException("Could not load " + resourceName, e);
        }
    }

}
