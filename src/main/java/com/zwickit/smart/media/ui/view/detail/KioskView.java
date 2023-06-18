package com.zwickit.smart.media.ui.view.detail;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.teamdev.jxbrowser.dom.Document;
import com.teamdev.jxbrowser.dom.Element;
import com.teamdev.jxbrowser.dom.InputElement;
import com.teamdev.jxbrowser.dom.event.EventType;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.navigation.Navigation;
import com.teamdev.jxbrowser.navigation.event.LoadFinished;
import com.teamdev.jxbrowser.ui.KeyCode;
import com.teamdev.jxbrowser.ui.event.KeyReleased;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import com.zwickit.smart.media.ui.base.Constants;
import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardButtonType;
import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardFxPanel;
import com.zwickit.smart.media.ui.view.common.keyboard.KeyboardType;
import com.zwickit.smart.media.ui.view.event.KeyboardButtonActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.teamdev.jxbrowser.ui.KeyCode.KEY_CODE_SPACE;

/**
 * Created by Thomas Philipp Zwick
 */
public class KioskView extends AbstractSwingBrowserView {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(KioskView.class);

    /** A component that displays content of the resources loaded in the Browser instance */
    private BrowserView browserView;

    /** The keyboard-panel displayed on glass-pane */
    private KeyboardFxPanel keyboardPanel;

    /** A registered event */
    private KeyboardButtonActionEventSubscriber keyboardButtonActionEventSubscriber;

    /** Registered elements */
    private List<Element> registeredElements = new ArrayList<>();

    /** Current used DOM input element */
    private InputElement inputElement;

    /**
     * Constructor
     *
     * @param eventBus
     */
    public KioskView(JFrame frame, EventBus eventBus) {
        super(frame, eventBus);

        // Load url in browser
        loadURL("https://www.kiosk.at");
    }

    @Override
    public void createView() {
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("Create: {}", this.getClass().getSimpleName());

        // Initialize browser and its engine
        initBrowserEngine();

        // See https://jxbrowser-support.teamdev.com/docs/guides/navigation.html
        final Navigation navigation = browser.navigation();

        navigation.on(LoadFinished.class, frameEvent -> {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Event: FrameLoadFinished");

            final Optional<Frame> frame = browser.mainFrame();

            // Register for <input> elements to show keyboard
            frame.ifPresent(f ->
                    f.document().ifPresent(d ->
                            registerInputElements(d)));
        });

        // disable zooming
        browser.zoom().disable();

        setLayout(new BorderLayout());

        browserView = BrowserView.newInstance(browser);
        add(browserView, BorderLayout.CENTER);

        setPreferredSize(new Dimension(resizer.getContentWidth(), resizer.getContentHeight()));

        // Initialize glass-pane
        keyboardPanel = new KeyboardFxPanel(eventBus);
        frame.setGlassPane(keyboardPanel);

        // Register event(s)
        keyboardButtonActionEventSubscriber = new KeyboardButtonActionEventSubscriber();
        eventBus.register(keyboardButtonActionEventSubscriber);
    }

    private void registerInputElements(Document document) {
        // find input elements in document
        registeredElements = document.findElementsByTagName(TAG_INPUT)
                .stream()
                // filter for specific type-attributes only
                .filter(e -> {
                    final String val = e.attributes().get(ATTRIBUTE_TYPE);
                    return KeyboardType.toEnum(val) != null;
                })
                .collect(Collectors.toList());

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Register elements: {}", registeredElements.size());

        // loop through filtered input elements
        registeredElements.forEach(el -> {
            // add CLICK listener to each input element for Keyboard
            el.addEventListener(EventType.CLICK, e -> {
                // save current referenced input element for keyboard
                inputElement = (InputElement) el;

                // get type of HTML element
                final String attr = inputElement.attributes().get(ATTRIBUTE_TYPE);

                // map type to enum
                final KeyboardType keyboardType = KeyboardType.toEnum(attr);

                // open keyboard with specific layout and pre-define text
                openKeyboardPopup((keyboardType != null)
                        ? keyboardType
                        : KeyboardType.OTHER, inputElement.value());
            }, false);
        });
    }

    @Override
    public void destroyView() {
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("Destroy: {}", this.getClass().getSimpleName());

        // Clear cache
        //TODO engine.getCacheStorage().clearCache();

        // Dispose engine to release all allocated memory and resources
        engine.close();

        engine = null;

        // Unregister event(s)
        eventBus.unregister(keyboardButtonActionEventSubscriber);

        super.destroyView();
    }

    /**
     * Loads specified URL in browser.
     *
     * @param url
     */
    private void loadURL(String url) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Load URL in browser: {}", url);

        // Load url in browser
        browser.navigation().loadUrl(url);
    }

    private void openKeyboardPopup(KeyboardType keyboardType, String predefinedText) {
        showGlassPane(keyboardType, predefinedText);
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

    private void toggleViewVisibility() {
        browserView.setVisible(!browserView.isVisible());
    }

    private void onKeyboardTextChange(KeyboardButtonActionEvent event) {
        if (event.getButtonType() == KeyboardButtonType.ENTER) {
            final String searchText = event.getParameters().get(Constants.PARAM_URL);
            inputElement.value(searchText);

            // #32: trigger key event manually
            dispatchKeyEvent('\u0000', KEY_CODE_SPACE);
        }

        hideGlassPane();
    }

    private void dispatchKeyEvent(char character, KeyCode keyCode) {
//        final KeyPressed keyPressed = KeyPressed.newBuilder(keyCode).keyChar(character).build();
//        final KeyTyped keyTyped = KeyTyped.newBuilder(keyCode).keyChar(character).build();
        final KeyReleased keyReleased = KeyReleased.newBuilder(keyCode).build();

//        browser.dispatch(keyPressed);
//        browser.dispatch(keyTyped);
        browser.dispatch(keyReleased);
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
            }
//            else if (event.getKeyboardType() == KeyboardType.OTHER) {
//                onKeyboardOtherClick(event);
//            }
//            else if (event.getKeyboardType() == KeyboardType.URL) {
//                onKeyboardUrlChange(event);
//            }
            else {
                onKeyboardTextChange(event);
            }
        }
    }
}
