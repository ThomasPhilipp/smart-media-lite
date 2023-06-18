package com.zwickit.smart.media.ui.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zwickit.smart.media.ui.base.Resizer;
import com.zwickit.smart.media.ui.base.SmartMediaConfigLoader;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import com.zwickit.smart.media.ui.view.event.ClickButtonEvent;
import com.zwickit.smart.media.ui.view.event.NavigationTypeChangeEvent;
import com.zwickit.smart.media.ui.view.common.NavigationType;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Created by Thomas Philipp Zwick
 *
 * Embed JavaFX in Swing examples:
 * https://blog.codecentric.de/2015/06/integration-von-javafx-in-swing-applikationen/
 *
 * Styling examples:
 * -) https://github.com/Jerady/fontawesomefx-examples
 * -) http://fxexperience.com/2011/12/styling-fx-buttons-with-css/
 * -) http://stackoverflow.com/questions/15819242/how-to-make-a-button-appear-to-have-been-clicked-or-selected-javafx2
 */
public class NavigationView extends JFXPanel {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationView.class);

    /** The shared event-bus instance */
    private final EventBus eventBus;

    /** The loaded smart-media-configuration */
    private final SmartMediaConfig smartMediaConfig;

    /** The scene of the view. */
    private Scene scene;

    /** The Home-button which is initial loaded. */
    private ToggleButton homeButton, browserButton, kioskButton, mediaThekButton;

    /**
     * The currently selected toggle-button to track click-changes and ignore
     * duplicate clicks.
     */
    private ToggleButton selectedToggleButton;

    /**
     * Constructor
     *
     * @param eventBus
     */
    public NavigationView(EventBus eventBus) {
        this.eventBus = eventBus;

        // Register for following events
        eventBus.register(new ClickButtonEventSubscriber());
        eventBus.register(new NavigationTypeChangeEventSubscriber());

        // Load app config
        smartMediaConfig = SmartMediaConfigLoader.getSmartMediaConfig();

        createView();
    }

    private void createView() {
        VBox viewPane = new VBox();
        viewPane.getStyleClass().add(ID_NAVIGATION_PANEL);
        viewPane.setAlignment(Pos.CENTER);
        viewPane.setSpacing(20.0);
        viewPane.setPrefWidth(Resizer.INSTANCE.getColumnWidth(1));

        final ToggleGroup toggleGroup = new ToggleGroup();

        homeButton = createNavigationToggle("Home", FontAwesomeIcon.HOME);
        homeButton.setOnAction(event -> onNavigationButtonClicked(event));
        homeButton.setToggleGroup(toggleGroup);
        viewPane.getChildren().add(homeButton);

        if (smartMediaConfig.isBrowserEnabled()) {
            browserButton = createNavigationToggle("Browser", FontAwesomeIcon.GLOBE);
            browserButton.setOnAction(event -> onNavigationButtonClicked(event));
            browserButton.setToggleGroup(toggleGroup);
            viewPane.getChildren().add(browserButton);
        }

        if (smartMediaConfig.isKioskEnabled()) {
            kioskButton = createNavigationToggle("Kiosk", FontAwesomeIcon.BOOK);
            kioskButton.setOnAction(event -> onNavigationButtonClicked(event));
            kioskButton.setToggleGroup(toggleGroup);
            viewPane.getChildren().add(kioskButton);
        }

        if (smartMediaConfig.isMediathekEnabled()) {
            mediaThekButton = createNavigationToggle("Mediathek", FontAwesomeIcon.PLAY_CIRCLE_ALT);
            mediaThekButton.setOnAction(event -> onNavigationButtonClicked(event));
            mediaThekButton.setToggleGroup(toggleGroup);
            viewPane.getChildren().add(mediaThekButton);
        }

        setSize(new Dimension(Resizer.INSTANCE.get1ColumnWidth(), Resizer.INSTANCE.get9ColumnHeight()));

        // Finally, set scene
        scene = new Scene(viewPane);
        scene.getStylesheets().add(loadCssFile(smartMediaConfig.getDisplayTypeEnum()));

        setScene(scene);
    }

    private ToggleButton createNavigationToggle(String name, FontAwesomeIcon fontAwesomeIcon) {
        final ToggleButton button = new ToggleButton(name);
        button.setId(ID_NAVIGATION_BUTTON + "-" + name.toLowerCase()); // setup ids dynamically
        button.getStyleClass().add(ID_NAVIGATION_BUTTON);
        button.setPrefWidth(Resizer.INSTANCE.get1ColumnWidth());
        FontAwesomeIconFactory.get().setIcon(button, fontAwesomeIcon, smartMediaConfig.isTablet() ? ICON_SIZE_4_5EM : ICON_SIZE_7EM, ContentDisplay.GRAPHIC_ONLY);
        return button;
    }

    // ------------------------------------------------------------------------
    //
    // Event Handlers
    //
    // ------------------------------------------------------------------------

    /**
     * Handles a click on toggle-button.
     *
     * @param event
     */
    private void onNavigationButtonClicked(ActionEvent event) {
        // Re-throw event
        onNavigationTypeChangedEvent((ToggleButton) event.getSource());
    }

    /**
     * Notifies other subscribers about a change of the navigation-type. Either
     * it is directly (by the user) or automatically called by the app.
     *
     * @param button
     */
    private void onNavigationTypeChangedEvent(ToggleButton button) {

        if (selectedToggleButton != null && selectedToggleButton.equals(button)) {
            LOGGER.info("Button is already selected. Further processing will be ignored.");

            // Manually mark button as selected again, otherwise its focus get lost
            selectedToggleButton.selectedProperty().set(true);

            return;
        }

        if (button == browserButton) {
            eventBus.post(new NavigationTypeChangeEvent(NavigationType.BROWSER));
        }
        else if (kioskButton != null && button == kioskButton) {
            eventBus.post(new NavigationTypeChangeEvent(NavigationType.KIOSK));
        }
        else if (mediaThekButton != null && button == mediaThekButton) {
            eventBus.post(new NavigationTypeChangeEvent(NavigationType.MEDIATHEK));
        }
        else {
            eventBus.post(new NavigationTypeChangeEvent(NavigationType.HOME));
        }

        // Track last selected button
        this.selectedToggleButton = button;
    }

    /**
     * Handles an event to manually click on a (navigation-) button. It is used
     * by launching the application to open the Home-view.
     */
    class ClickButtonEventSubscriber {
        @Subscribe
        public void onEventReceived(ClickButtonEvent event) {
            LOGGER.debug("Published event: {}", event);

            // Search control by id
            final ToggleButton button = (ToggleButton) scene.lookup("#" + event.getButtonId());

            // Manually mark button as selected
            button.selectedProperty().set(true);

            // Re-throw event
            onNavigationTypeChangedEvent(button);
        }
    }

    /**
     * Handles all events which are not triggered by this class, like Settings,
     * to correct display state manually.
     */
    class NavigationTypeChangeEventSubscriber {
        @Subscribe
        public void onEventReceived(NavigationTypeChangeEvent event) {
            LOGGER.debug("Published event: {}", event);

            switch (event.getType()) {
                // By clicking on the Setting-button, the toggle-button shall be reset
                case SETTING_PAD:
                case SETTING_LIST:
                    // Manually de-select toggle button
                    if (selectedToggleButton != null)
                        selectedToggleButton.selectedProperty().set(false);

                    // Destroy reference too, otherwise wrong toggle button is kept
                    selectedToggleButton = null;
                    break;

                // Views which are launched via context are not per default selected
                default:
                    // Search control by id
                    final ToggleButton button = (ToggleButton) scene.lookup("#" + ID_NAVIGATION_BUTTON + "-" + event.getType().toString().toLowerCase());

                    // Manually mark button as selected
                    button.selectedProperty().set(true);

                    // Track last selected button
                    selectedToggleButton = button;
                    break;
            }
        }
    }

}
