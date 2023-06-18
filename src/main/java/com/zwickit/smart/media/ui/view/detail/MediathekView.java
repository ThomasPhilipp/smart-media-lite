package com.zwickit.smart.media.ui.view.detail;

import com.google.common.eventbus.EventBus;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Thomas Philipp Zwick
 */
public class MediathekView extends AbstractSwingBrowserView {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(MediathekView.class);

    /** A component that displays content of the resources loaded in the Browser instance */
    private BrowserView browserView;

    /**
     * Constructor
     *
     * @param eventBus
     */
    public MediathekView(JFrame frame, EventBus eventBus) {
        super(frame, eventBus);

        // Load configured URL
        loadURL(smartMediaConfig.getBrowserConfig().getMediathekUrl());
    }

    @Override
    public void createView() {
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("Create: {}", this.getClass().getSimpleName());

        // Initialize browser and its engine
        initBrowserEngine();

        // disable zooming
        browser.zoom().disable();

        setLayout(new BorderLayout());

        browserView = BrowserView.newInstance(browser);

        add(browserView, BorderLayout.CENTER);

        setPreferredSize(new Dimension(resizer.getContentWidth(), resizer.getContentHeight()));
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
