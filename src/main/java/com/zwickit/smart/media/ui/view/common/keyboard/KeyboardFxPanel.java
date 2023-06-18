package com.zwickit.smart.media.ui.view.common.keyboard;

import com.google.common.eventbus.EventBus;
import com.zwickit.smart.media.ui.view.detail.AbstractFxView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * This class is displayed on the glass-pane.
 *
 * Created by Thomas Philipp Zwick
 */
public class KeyboardFxPanel extends AbstractFxView {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyboardFxPanel.class);

    /**
     * The content of the panel
     */
    private StackPane panel;

    /**
     * The keyboard-control
     */
    private SmartMediaKeyboard keyboard;

    /**
     * Track visibility of keyboard-control
     */
    private boolean keyboardVisibility = false;

    /**
     * Constructor
     *
     * @param eventBus
     */
    public KeyboardFxPanel(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void createView() {
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("Create: {}", this.getClass().getSimpleName());

        keyboard = new SmartMediaKeyboard(eventBus, bundle, smartMediaConfig.getDisplayTypeEnum());

        panel = new StackPane();
        panel.getStyleClass().add(STYLE_KEYBOARD_VIEW);

        // Resize view to whole screen-size
        setPreferredSize(new Dimension(resizer.getWindowWidth(), resizer.getWindowHeight()));

        // JavaFX scene
        scene = new Scene(panel);
        scene.getStylesheets().add(loadCssFile(smartMediaConfig.getDisplayTypeEnum()));
        scene.setFill(Color.TRANSPARENT);

        // Finally, set scene on Java Swing
        setScene(scene);
    }

    public void showKeyboard(KeyboardType keyboardType, String predefinedText) {
        LOGGER.trace("Show keyboard");

        keyboard.initialize(keyboardType, predefinedText);

        keyboardVisibility = true;

        Platform.runLater(() -> panel.getChildren().add(keyboard.view()));
    }

    public void hideKeyboard() {
        LOGGER.trace("Hide keyboard");

        keyboard.destroy();

        keyboardVisibility = false;
    }

    public boolean isKeyboardShown() {
        return keyboardVisibility;
    }

}
