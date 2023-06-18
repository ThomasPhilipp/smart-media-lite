package com.zwickit.smart.media.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zwickit.smart.media.ui.base.SmartMediaConfigLoader;
import com.zwickit.smart.media.ui.base.DisplayType;
import com.zwickit.smart.media.ui.base.Resizer;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import com.zwickit.smart.media.ui.view.HeaderView;
import com.zwickit.smart.media.ui.view.NavigationView;
import com.zwickit.smart.media.ui.view.ViewContainer;
import com.zwickit.smart.media.ui.view.event.*;
import javafx.embed.swing.JFXPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Locale;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Created by Thomas Philipp Zwick
 */
public class SmartMediaUI {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartMediaUI.class);

    /** The smart-media-config loaded from file which is specified as argument */
    private SmartMediaConfig smartMediaConfig;

    /** The shared event-bus instance */
    private EventBus eventBus;

    /** The window of the application which contains the user-interface */
    private JFrame frame;

    /** The header-panel */
    private JFXPanel headerPanel;

    /** The navigation-panel */
    private JFXPanel navigationPanel;

    /** The view-container which displays the single sub-views */
    private JPanel viewContainer;

    /**
     * Constructor
     */
    public SmartMediaUI() {
        // Initialize event-bus for inter-view-communication
        eventBus = new EventBus();

        smartMediaConfig = SmartMediaConfigLoader.getSmartMediaConfig();

        setLocale();

        // Create user-interface
        createClient();

        // Lazy initialization
        lazyInitialization();
    }

    private void setLocale() {
        final Locale locale = smartMediaConfig.getLocaleEnum();
        LOGGER.info("Set default locale: {}", locale);
        Locale.setDefault(locale);
    }

    private void createClient() {
        // Initialize resizer
        final DisplayType displayType = smartMediaConfig.getDisplayTypeEnum();

        if (displayType != null) {
            LOGGER.info("Set display type: {}", displayType.name());
            Resizer.getInstance(displayType);
        }
        else {
            LOGGER.warn("No or invalid display-type defined. Using the default one ({})", DisplayType.TABLET.name());
            Resizer.getInstance(DisplayType.TABLET);
        }

        // Create frame
        frame = new JFrame(WINDOW_TITLE);
        frame.setSize(Resizer.INSTANCE.getWindowWidth(), Resizer.INSTANCE.getWindowHeight());
        // Disable Alt+F4 on Windows
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // Remove window title and borders
        frame.setUndecorated(true);
        // Center frame-window on screen
        frame.setLocationRelativeTo(null);


        // Create view-panels
        headerPanel = new HeaderView(eventBus);
        navigationPanel = new NavigationView(eventBus);
        viewContainer = new ViewContainer(frame, eventBus);

        // Create view-layout
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(headerPanel,      BorderLayout.NORTH);
        mainPanel.add(navigationPanel,  BorderLayout.WEST);
        mainPanel.add(viewContainer,    BorderLayout.CENTER);
        frame.add(mainPanel);

        // Finally, display window
        frame.setVisible(true);

        // Register events
        eventBus.register(new KeyboardOpenEventSubscriber());
        eventBus.register(new KeyboardCloseEventSubscriber());
        eventBus.register(new FullScreenEventSubscriber());
    }

    private void lazyInitialization() {
        // TODO execute it with delay?

        // Programmatically execute click on specified button
        eventBus.post(new ClickButtonEvent("navigation-button-home"));
    }

    // ------------------------------------------------------------------------
    //
    // Event Handlers
    //
    // ------------------------------------------------------------------------

    class KeyboardOpenEventSubscriber {
        @Subscribe
        public void onEventReceived(KeyboardOpenEvent event) {
            LOGGER.debug("Published event: {}", event);

            frame.getGlassPane().setVisible(true);
        }
    }

    class KeyboardCloseEventSubscriber {
        @Subscribe
        public void onEventReceived(KeyboardCloseEvent event) {
            LOGGER.debug("Published event: {}", event);

            frame.getGlassPane().setVisible(false);
        }
    }

    class FullScreenEventSubscriber {
        @Subscribe
        public void onEventReceived(FullScreenEvent event) {
            LOGGER.debug("Published event: {}", event);

            if (event.isFullScreen()) {
                headerPanel.setVisible(!event.isFullScreen());
                headerPanel.setPreferredSize(new Dimension(0, 0));
                navigationPanel.setVisible(!event.isFullScreen());
                navigationPanel.setPreferredSize(new Dimension(0, 0));
            }
            else {
                headerPanel.setVisible(!event.isFullScreen());
                headerPanel.setPreferredSize(new Dimension(Resizer.INSTANCE.get16ColumnWidth(), Resizer.INSTANCE.get1ColumnHeight()));
                navigationPanel.setVisible(!event.isFullScreen());
                navigationPanel.setPreferredSize(new Dimension(Resizer.INSTANCE.get1ColumnWidth(), Resizer.INSTANCE.get9ColumnHeight()));
            }
        }
    }

}
