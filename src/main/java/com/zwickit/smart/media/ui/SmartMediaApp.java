package com.zwickit.smart.media.ui;

import com.zwickit.smart.media.ui.base.SmartMediaConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import static com.zwickit.smart.media.ui.base.Constants.LICENSE_JXBROWSER;

/**
 * The entry point for the smart-media-app.
 *
 * Created by Thomas Philipp Zwick
 */
public class SmartMediaApp {

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartMediaApp.class);

    public static void main(String[] args) {
        // Set JxBrowser license key
        System.setProperty("jxbrowser.license.key", LICENSE_JXBROWSER);

        // Check arguments
        if (args != null || args.length == 1) {
            LOGGER.info("Application launch with given arguments: {}", args);
        }
        else {
            LOGGER.warn("Application launch failed! Argument(s) are missing.");
            exit();
        }

        // Load config files
        SmartMediaConfigLoader.load(args[0]);

        // Create user-interface
        SwingUtilities.invokeLater(() -> {
            LOGGER.info("Initialize user-interface.");
            new SmartMediaUI();
        });
    }

    private static void exit() {
        LOGGER.warn("Application launch failed! For more details please have a look to previous log-entries.");
        System.exit(0);
    }

}
