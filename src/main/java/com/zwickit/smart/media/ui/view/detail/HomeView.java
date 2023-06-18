package com.zwickit.smart.media.ui.view.detail;

import com.google.common.eventbus.EventBus;
import com.teamdev.jxbrowser.dom.Element;
import com.teamdev.jxbrowser.dom.event.EventType;
import com.teamdev.jxbrowser.engine.event.EngineCrashed;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.navigation.event.FrameLoadFinished;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import com.zwickit.smart.media.ui.view.common.NavigationType;
import com.zwickit.smart.media.ui.view.common.keyboard.NavigationTypeContext;
import com.zwickit.smart.media.ui.view.event.NavigationTypeChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Created by Thomas Philipp Zwick
 */
public class HomeView extends AbstractSwingBrowserView {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeView.class);

    /** A component that displays content of the resources loaded in the Browser instance */
    private BrowserView browserView;

    /** Registered elements */
    private List<Element> registeredElements = new ArrayList<>();

    /**
     * Constructor
     *
     * @param eventBus
     */
    public HomeView(JFrame frame, EventBus eventBus) {
        super(frame, eventBus);

        // Load configured URL
        loadURL(smartMediaConfig.getBrowserConfig().getHomeUrl());
    }

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
        browser.navigation().on(FrameLoadFinished.class, frameEvent -> {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Event: FrameLoadFinished");

            final Optional<Frame> frame = browser.mainFrame();

            frame.ifPresent(f ->
                    f.document().ifPresent(d ->
                            d.documentElement().ifPresent(e ->
                                    registerElements(e))));
        });

        // disable zooming
        browser.zoom().disable();

        setLayout(new BorderLayout());

        browserView = BrowserView.newInstance(browser);

        add(browserView, BorderLayout.CENTER);

        setPreferredSize(new Dimension(resizer.getContentWidth(), resizer.getContentHeight()));
    }

    private void registerElements(Element element) {
        registeredElements = element.findElementsByTagName(HTML_TAG_BUTTON);

        registeredElements.forEach(el -> {
            el.addEventListener(EventType.CLICK, e -> {
                final String idAttribute = el.attributeValue(HTML_ATTRIBUTE_ID);

                if (LOGGER.isTraceEnabled())
                    LOGGER.trace("Clicked on given button: {}", idAttribute);

                Map<String, Object> context = new HashMap<>(1);

                switch (idAttribute) {
                    case "infoButton":
                        context.put(NavigationTypeContext.KEY_URL, smartMediaConfig.getBrowserConfig().getHospitalUrl());
                        invokeNavigationTypeChange(new NavigationTypeChangeEvent(NavigationType.BROWSER, context));
                        break;
                    case "videoButton":
                        context.put(NavigationTypeContext.KEY_URL, smartMediaConfig.getHelpVideoPath());
                        invokeNavigationTypeChange(new NavigationTypeChangeEvent(NavigationType.TV, context));
                        break;
                    case "calendarButton":
                        break;
                    default:
                        LOGGER.warn("Could not process given id: {}", idAttribute);
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
}
