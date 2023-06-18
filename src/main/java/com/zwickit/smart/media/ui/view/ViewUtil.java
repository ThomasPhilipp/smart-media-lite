package com.zwickit.smart.media.ui.view;

import com.zwickit.smart.media.ui.base.DisplayType;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Created by Thomas Philipp Zwick
 */
public class ViewUtil {

    /**
     * Creates a button with given arguments. The graphic is loaded from the
     * graphics-folder.
     *
     * @param id
     * @param style
     * @param imageName
     * @return
     */
    public static Button createButton(String id, String style, String imageName) {
        final Button button = new Button();

        if (id != null) {
            button.setId(id);
        }

        if (style != null) {
            button.getStyleClass().add(style);
        }

        final ImageView imageView = new ImageView(DIR_GRAPHICS + imageName);
        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return button;
    }

    /**
     * Creates a button with given arguments.
     *
     * @param id
     * @param imageName
     * @return
     */
    public static Button createButton(String id, String imageName) {
        return createButton(id, id, imageName);
    }

    /**
     * Creates a button with given arguments.
     *
     * @param id
     * @param style
     * @param fontAwesomeIcon
     * @return
     */
    public static Button createButton(String id, String style, FontAwesomeIcon fontAwesomeIcon) {
        return createButton(id, style, fontAwesomeIcon, ICON_SIZE_4_2EM, ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * Creates a button with given arguments.
     *
     * @param id
     * @param style
     * @param fontAwesomeIcon
     * @param iconSize
     * @return
     */
    public static Button createButton(String id, String style, FontAwesomeIcon fontAwesomeIcon, String iconSize) {
        return createButton(id, style, fontAwesomeIcon, iconSize, ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * Creates a button with given id (plus style-class) and image.
     *
     * @param id
     * @param fontAwesomeIcon the name of the file in the 'graphics'-folder
     * @return
     */
    public static Button createButton(String id, String style, FontAwesomeIcon fontAwesomeIcon, String iconSize, ContentDisplay contentDisplay) {
        final Button button = new Button();

        if (id != null) {
            button.setId(id);
        }

        if (style != null) {
            button.getStyleClass().add(style);
        }

        FontAwesomeIconFactory.get().setIcon(button, fontAwesomeIcon, iconSize, contentDisplay);

        return button;
    }

    /**
     * Creates a button for the control-panel with the following style-class
     * name: control-button
     *
     * @param id
     * @param fontAwesomeIcon
     * @param iconSize
     * @return
     */
    public static Button createControlButton(String id, FontAwesomeIcon fontAwesomeIcon, String iconSize) {
        return createButton(id, ID_CONTROL_BUTTON, fontAwesomeIcon, iconSize);
    }

    /**
     * Creates a toggle-button with given arguments.
     *
     * @param id
     * @param style
     * @param fontAwesomeIcon
     * @param iconSize
     * @return
     */
    public static ToggleButton createToggleButton(String id, String style, FontAwesomeIcon fontAwesomeIcon, String iconSize) {
        final ToggleButton button = new ToggleButton();
        button.setId(id);
        button.getStyleClass().add(style);
        FontAwesomeIconFactory.get().setIcon(button, fontAwesomeIcon, iconSize, ContentDisplay.GRAPHIC_ONLY);
        return button;
    }

    /**
     * Creates a hyperlink with given image-name.
     *
     * @param image the name of the file in the 'graphics'-folder
     * @return
     */
    public static Hyperlink createHyperlink(String image, String name, DisplayType displayType) {
        final ImageView imageView = loadExternalGraphic(image, displayType);

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.getStyleClass().add(ID_HYPERLINK);
        hyperlink.setGraphic(imageView);
        hyperlink.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        hyperlink.setText(name);
        return hyperlink;
    }

    /**
     * See also http://stackoverflow.com/questions/31271800/fontawesomefx-only-rectangles-displayed
     *
     * @param fontAwesomeIcon
     * @return
     */
    public static GlyphsStack createCircleButton(FontAwesomeIcon fontAwesomeIcon) {
        return GlyphsStack.create().addAll(
                GlyphsBuilder.create(FontAwesomeIconView.class)
                        .glyph(FontAwesomeIcon.CIRCLE)
                        .size("12em")
                        .style("-fx-fill: linear-gradient(#70b4e5 0%, #247cbc 70%, #2c85c1 85%);")
                        .build(),
                GlyphsBuilder.create(FontAwesomeIconView.class)
                        .glyph(fontAwesomeIcon)
                        .size(ICON_SIZE_4_2EM)
                        .style("-fx-fill: linear-gradient(orange 0%, darkred); "
                                + "-fx-effect: dropshadow( one-pass-box , rgba(0,0,0,0.8) , 4 , 0.0 , 1 , 1 );")
                        .build());
    }

    /**
     * Loads the specified image and returns it. If it is not found, then
     * the default-icon.png image is returned.
     *
     * @param image
     * @return
     */
    private static ImageView loadExternalGraphic(String image, DisplayType displayType) {
        final File file = new File(image);
        if (file.exists()) {
            return new ImageView(file.toURI().toString());
        }
        else {
            return new ImageView(buildPath(PNG_DEFAULT_LOGO, displayType));
        }
    }

    /**
     * Loads given file-name from the "graphics/[displayType]"-directory.
     *
     * @param fileName
     * @param displayType
     * @return
     */
    public static URL loadGraphic(String fileName, DisplayType displayType) {
        return ClassLoader.getSystemResource(buildPath(fileName, displayType));
    }

    /**
     * Builds the path to the given file-name which depends on the display-type.
     *
     * @param fileName
     * @param displayType
     * @return
     */
    public static String buildPath(String fileName, DisplayType displayType) {
        return new StringBuilder()
                .append(DIR_GRAPHICS)
                .append((displayType == DisplayType.HD1080) ? DIR_1080P : DIR_TABLET)
                .append(fileName)
                .toString();
    }
}
