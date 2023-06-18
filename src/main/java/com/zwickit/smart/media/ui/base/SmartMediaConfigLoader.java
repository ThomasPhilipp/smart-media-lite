package com.zwickit.smart.media.ui.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Util class to load smart-media-config.
 *
 * Created by Thomas Philipp Zwick
 */
public class SmartMediaConfigLoader {

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartMediaConfigLoader.class);

    /**
     * The smart-media-config.
     */
    private static SmartMediaConfig smartMediaConfig;

    /**
     * Loads the configuration for the app.
     *
     * @param fileName the name of file
     * @return
     */
    public static void load(String fileName) {
        final File file = new File(fileName);

        if (!file.exists()) {
            LOGGER.warn("Could not found smart-media-config!");
            return;
            // TODO throw exception
        }

        try {
            smartMediaConfig = new ObjectMapper().readValue(file, SmartMediaConfig.class);

            if (LOGGER.isInfoEnabled())
                LOGGER.info("Smart-media-config loaded: {}", smartMediaConfig);

        } catch (IOException e) {
            LOGGER.warn("Could not load or map smart-media-config: {}", e.getMessage());
            // TODO throw exception
        }
    }

    /**
     * Returns processed smart-media-config.
     *
     * @return
     */
    public static synchronized SmartMediaConfig getSmartMediaConfig() {
        return smartMediaConfig;
    }

}
