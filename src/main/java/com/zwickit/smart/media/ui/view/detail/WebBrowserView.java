package com.zwickit.smart.media.ui.view.detail;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.teamdev.jxbrowser.dom.*;
import com.teamdev.jxbrowser.dom.event.*;
import com.teamdev.jxbrowser.engine.event.EngineCrashed;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.navigation.Navigation;
import com.teamdev.jxbrowser.navigation.event.*;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import com.zwickit.smart.media.ui.view.common.keyboard.NavigationTypeContext;
import com.zwickit.smart.media.ui.base.Constants;
import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardButtonType;
import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardFxPanel;
import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardType;
import com.zwickit.smart.media.ui.view.event.KeyboardButtonActionEvent;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Created by Thomas Philipp Zwick
 */
public class WebBrowserView extends AbstractSwingBrowserView {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebBrowserView.class);

    /** A map of URLs (key) matching specific inputs by id (value) mapping to OTHER */
    private static final Map<String, String> OTHER_URLS_MAP;

    /** teletext.at definition */
    private static final String OTHER_TELETEXT_URL = "https://teletext.orf.at/";

    /** A component that displays content of the resources loaded in the Browser instance */
    private BrowserView browserView;

    /** The view to control (URL, buttons, etc) the web-browser */
    private JFXPanel controlView;

    /** The URL address text-field */
    private TextField urlField;

    /** The keyboard-panel displayed on glass-pane */
    private KeyboardFxPanel keyboardPanel;

    /** A registered event */
    private KeyboardButtonActionEventSubscriber keyboardButtonActionEventSubscriber;

    /** Registered elements */
    private List<Element> registeredElements = new ArrayList<>();

    /** Current used DOM element (input or textarea) */
    private FormControlElement currentDomElement;

    static {
        OTHER_URLS_MAP = new HashMap<>();
        OTHER_URLS_MAP.put(OTHER_TELETEXT_URL, "inputPageNumber");
    }

    /**
     * Constructor
     *
     * @param frame
     * @param eventBus
     */
    public WebBrowserView(JFrame frame, EventBus eventBus, Map<String, Object> context) {
        super(frame, eventBus);

        String url;

        if (context != null && !context.isEmpty()) {
            url = (String) context.get(NavigationTypeContext.KEY_URL);
        }
        else {
            url = smartMediaConfig.getBrowserConfig().getHomepageUrl();
        }

        loadURL(url);
    }

    /**
     * Initializes the view on the FX application thread (which must be
     * synchronized).
     */
    @Override
    public void createView() {
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("Create: {}", this.getClass().getSimpleName());

        // Initialize browser and its engine
        initBrowserEngine();

        engine.on(EngineCrashed.class, event -> {
            LOGGER.warn("Browser engine crashed (exit code: {})", event.exitCode());
        });

        // See https://jxbrowser-support.teamdev.com/docs/guides/navigation.html
        final Navigation navigation = browser.navigation();

        navigation.on(FrameLoadFinished.class, frameEvent -> {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Event: FrameLoadFinished");

            final Optional<Frame> frame = browser.mainFrame();

            // Register for <input> elements to show keyboard
            frame.ifPresent(f ->
                    f.document().ifPresent(d ->
                            registerInputElements(d)));
        });

        setLayout(new BorderLayout());

        // Initialize browser-view
        browserView = BrowserView.newInstance(browser);
        add(browserView, BorderLayout.CENTER);

        controlView = createControlPanel();
        add(controlView, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(resizer.getContentWidth(), resizer.getContentHeight()));

        // Initialize glass-pane
        keyboardPanel = new KeyboardFxPanel(eventBus);
        frame.setGlassPane(keyboardPanel);

        // Register event(s)
        keyboardButtonActionEventSubscriber = new KeyboardButtonActionEventSubscriber();
        eventBus.register(keyboardButtonActionEventSubscriber);
    }

    private void registerInputElements(Document document) {
        if (OTHER_URLS_MAP.containsKey(document.baseUri())) {
            final String searchId = OTHER_URLS_MAP.get(document.baseUri());
            registeredElements = document.findElementsById(searchId);
        } else {
            // find input elements in document
            registeredElements = new ArrayList<>();

            // register (HTML) input elements
            registeredElements.addAll(document.findElementsByTagName(TAG_INPUT)
                    .stream()
                    // filter for specific type-attributes only
                    .filter(e -> {
                        final String val = e.attributes().get(ATTRIBUTE_TYPE);
                        return KeyboardType.toEnum(val) != null;
                    })
                    .collect(Collectors.toList()));

            // register (HTML) textarea elements
            registeredElements.addAll(document.findElementsByTagName(TAG_TEXTAREA)
                    .stream()
                    // filter for specific type-attributes only
                    .filter(e -> {
                        final String val = e.attributes().get(ATTRIBUTE_TYPE);
                        return KeyboardType.toEnum(val) != null;
                    })
                    .collect(Collectors.toList()));
        }

        LOGGER.debug("Register elements: {}", registeredElements.size());

        // add click listener to registered elements
        registeredElements.forEach(domElement -> {
            // add CLICK listener to each input element for Keyboard
            domElement.addEventListener(EventType.CLICK, e -> {

                if (domElement instanceof FormControlElement) {
                    // save current referenced input element for keyboard
                    currentDomElement = (FormControlElement) domElement;

                    // get type of HTML element
                    final String attr = currentDomElement.attributes().get(ATTRIBUTE_TYPE);

                    // map type to enum
                    final KeyboardType keyboardType = KeyboardType.toEnum(attr);

                    // open keyboard with specific layout and pre-define text
                    openKeyboardPopup((keyboardType != null)
                            ? keyboardType
                            : KeyboardType.OTHER, currentDomElement.value());

                } else {
                    LOGGER.warn("Could not handle registered element: {}", e.getClass().getSimpleName());
                }
            }, false);
        });
    }

    @Override
    public void destroyView() {
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("Destroy: {}", this.getClass().getSimpleName());

        // Dispose engine to release all allocated memory and resources
        engine.close();

        engine = null;

        // Unregister event(s)
        eventBus.unregister(keyboardButtonActionEventSubscriber);

        super.destroyView();
    }

    private JFXPanel createControlPanel() {
        urlField = new TextField();
        urlField.setId(ID_CONTROL_FIELD);
        urlField.getStyleClass().add(ID_CONTROL_FIELD);
        urlField.setOnMouseClicked(event -> openKeyboardPopup(KeyboardType.URL, urlField.getText()));
        HBox.setHgrow(urlField, Priority.ALWAYS);

        final Button refreshButton = createControlButton(ID_HOME_BUTTON, FontAwesomeIcon.REFRESH);
        refreshButton.setId(ID_REFRESH_BUTTON);
        refreshButton.disableProperty().bind(urlField.textProperty().isEmpty());
        refreshButton.setOnAction(event -> loadURL(urlField.getText()));

        final Button homeButton = createControlButton(ID_HOME_BUTTON, FontAwesomeIcon.HOME);
        homeButton.setOnAction(event -> loadURL(smartMediaConfig.getBrowserConfig().getHomepageUrl()));

        final Button zoomInButton = createControlButton(ID_ZOOM_IN_BUTTON, FontAwesomeIcon.SEARCH_PLUS);
        zoomInButton.setOnAction(e -> increaseZoomLevel());

        final Button zoomOutButton = createControlButton(ID_ZOOM_OUT_BUTTON, FontAwesomeIcon.SEARCH_MINUS);
        zoomOutButton.setOnAction(e -> decreaseZoomLevel());

        final HBox panel = new HBox(urlField, refreshButton, homeButton, zoomOutButton, zoomInButton);
        panel.getStyleClass().add(ID_CONTROL_PANEL);
        panel.setAlignment(Pos.CENTER);
        panel.setSpacing(10.0);
        panel.setPrefWidth(resizer.get15ColumnWidth());
        panel.setPrefHeight(resizer.get1ColumnHeight());

        // Finally, set scene
        final Scene scene = new Scene(panel);
        scene.getStylesheets().add(loadCssFile(smartMediaConfig.getDisplayTypeEnum()));

        final JFXPanel swingPanel = new JFXPanel();
        swingPanel.setScene(scene);

        return swingPanel;
    }

    /**
     * Loads specified URL in browser.
     *
     * @param url
     */
    private void loadURL(String url) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Load URL in browser: {}", url);

        // Set url (again) to update UI
        urlField.setText(url);

        // Load url in browser
        browser.navigation().loadUrl(url);
    }

    /**
     * Increases the zoom-level of given browser-view.
     */
    private void increaseZoomLevel() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Zoom: in");

        browser.zoom().in();
    }

    /**
     * Decreases the zoom-level of given browser-view.
     */
    private void decreaseZoomLevel() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Zoom: out");

        browser.zoom().out();
    }

    private void openKeyboardPopup(KeyboardType keyboardType, String predefinedText) {
        showGlassPane(keyboardType, predefinedText);
    }

    private void toggleViewVisibility() {
        browserView.setVisible(!browserView.isVisible());
        controlView.setVisible(!controlView.isVisible());
    }

    private void onKeyboardTextChange(KeyboardButtonActionEvent event) {
        if (event.getButtonType() == KeyboardButtonType.ENTER) {
            currentDomElement.value(event.getParameters().get(Constants.PARAM_URL));
        }

        hideGlassPane();
    }

    private void onKeyboardUrlChange(KeyboardButtonActionEvent event) {
        if (event.getButtonType() == KeyboardButtonType.ENTER) {
            loadURL(event.getParameters().get(Constants.PARAM_URL));
        }

        hideGlassPane();
    }

    private void onKeyboardOtherClick(KeyboardButtonActionEvent event) {
        if (event.getButtonType() == KeyboardButtonType.ENTER) {
            // TODO: must be changed when other websites than https://teletext.orf.at/ are handled here
            final String pageNumber = event.getParameters().get(PARAM_URL);
            currentDomElement.value(pageNumber);
            loadURL(OTHER_TELETEXT_URL + "channel/orf1/page/" + pageNumber);
        }

        hideGlassPane();
    }

    private void showGlassPane(KeyboardType keyboardType, String predefinedText) {
        if (!keyboardPanel.isKeyboardShown()) {
            toggleViewVisibility();

            // Show control on sub-panel
            keyboardPanel.showKeyboard(keyboardType, predefinedText);

            // Set glass-pane visible
            frame.getGlassPane().setVisible(true);
        }
    }
    private void hideGlassPane() {
        if (keyboardPanel.isKeyboardShown()) {
            toggleViewVisibility();

            // Hide control on sub-panel
            keyboardPanel.hideKeyboard();

            // Set glass-pane invisible
            frame.getGlassPane().setVisible(false);
        }
    }

    // ------------------------------------------------------------------------
    //
    // Event-Bus Handlers
    //
    // ------------------------------------------------------------------------

    class KeyboardButtonActionEventSubscriber {
        @Subscribe
        public void onEventReceived(KeyboardButtonActionEvent event) {
            LOGGER.debug("Published event: {}", event);

            if (event == null || event.getKeyboardType() == null || event.getButtonType() == null) {
                return;
            } else if (event.getKeyboardType() == KeyboardType.OTHER) {
                onKeyboardOtherClick(event);
            } else if (event.getKeyboardType() == KeyboardType.URL) {
                onKeyboardUrlChange(event);
            } else {
                onKeyboardTextChange(event);
            }
        }
    }

}
