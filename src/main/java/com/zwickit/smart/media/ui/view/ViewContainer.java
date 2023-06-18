package com.zwickit.smart.media.ui.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zwickit.smart.media.ui.base.Resizer;
import com.zwickit.smart.media.ui.base.SmartMediaConfigLoader;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import com.zwickit.smart.media.ui.view.event.ViewChangeEvent;
import com.zwickit.smart.media.ui.view.detail.*;
import com.zwickit.smart.media.ui.view.event.NavigationTypeChangeEvent;
import com.zwickit.smart.media.ui.view.common.NavigationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

import static com.zwickit.smart.media.ui.base.Constants.PNG_CONTENT_LOADING;

/**
 * Created by Thomas Philipp Zwick
 */
public class ViewContainer extends JPanel {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewContainer.class);

    /** The loaded smart-media-configuration */
    private final SmartMediaConfig smartMediaConfig = SmartMediaConfigLoader.getSmartMediaConfig();

    /** The frame of the app. */
    private final JFrame frame;

    /** The shared event-bus instance */
    private final EventBus eventBus;

    /**
     * Constructor
     *
     * @param eventBus
     */
    public ViewContainer(JFrame frame, EventBus eventBus) {
        this.frame = frame;
        this.eventBus = eventBus;

        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // Register for following events
        eventBus.register(new NavigationTypeChangeEventSubscriber());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            final Image backgroundImage = ImageIO.read(ViewUtil.loadGraphic(PNG_CONTENT_LOADING, smartMediaConfig.getDisplayTypeEnum()));
            g.drawImage(backgroundImage, 0, 0,  null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the view for given navigation-type. Previous views get
     * destroyed.
     *
     * @param navigationType
     */
    protected void displayView(NavigationType navigationType, Map<String, Object> context) {
        // Get current displayed components
        final Component[] components = getComponents();

        if (LOGGER.isTraceEnabled())
            LOGGER.trace("Destroy # of components: {}", components.length);

        for (int i = components.length - 1; i == 0; i--) {
            Component viewComponent = components[i];

            if (viewComponent instanceof IBaseView) {
                ((IBaseView) viewComponent).destroyView();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Remove sub-view
            remove(viewComponent);

            // Refresh window
            repaint();
            validate();
        }

        // Add new view to panel
        final Component view = getView(navigationType, context);
        add(view);

        setSize(new Dimension(Resizer.INSTANCE.getContentWidth(), Resizer.INSTANCE.getContentHeight()));
        //setBorder(BorderFactory.createLineBorder(Color.PINK));

        repaint();
        validate();
    }

    /**
     * Creates and returns the view-component for given navigation-type.
     *
     * @param navigationType
     * @return
     */
    private Component getView(NavigationType navigationType, Map<String, Object> context) {
        Component component = null;

        if (NavigationType.HOME == navigationType) {
            component = new HomeView(frame, eventBus);
        }
        else if (NavigationType.BROWSER == navigationType) {
            component = new WebBrowserView(frame, eventBus, context);
        }
        else if (NavigationType.KIOSK == navigationType) {
            component = new KioskView(frame, eventBus);
        }
        else if (NavigationType.MEDIATHEK == navigationType) {
            component = new MediathekView(frame, eventBus);
        }
        else {
            LOGGER.warn("Unknown navigation type: {}. No view displayed!", navigationType);
        }

        eventBus.post(new ViewChangeEvent(navigationType));

        return component;
    }

    /**
     * Handles navigation-type change events.
     */
    class NavigationTypeChangeEventSubscriber {
        @Subscribe
        public void onEventReceived(NavigationTypeChangeEvent event) {
            LOGGER.debug("Published event: {}", event);
            displayView(event.getType(), event.getContext());
        }
    }

}
