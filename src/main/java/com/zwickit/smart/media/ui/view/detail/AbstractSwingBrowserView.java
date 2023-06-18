package com.zwickit.smart.media.ui.view.detail;

import com.google.common.eventbus.EventBus;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.CreatePopupCallback;
import com.teamdev.jxbrowser.browser.callback.PrintCallback;
import com.teamdev.jxbrowser.browser.callback.StartDownloadCallback;
import com.teamdev.jxbrowser.engine.*;

import javax.swing.*;
import java.nio.file.Paths;
import java.util.Map;

import static com.zwickit.smart.media.ui.base.Constants.FILE_JXBROWSER;

public abstract class AbstractSwingBrowserView extends AbstractSwingView {

    /** HTML input tag definition */
    public static final String TAG_INPUT = "input";

    /** HTML textarea tag definition */
    public static final String TAG_TEXTAREA = "textarea";

    /** HTML attribute type definition */
    public static final String ATTRIBUTE_TYPE = "type";

    /** Provides access to the Chromium engine functionality */
    protected Engine engine;

    /** A web browser control */
    protected Browser browser;

    public AbstractSwingBrowserView(JFrame frame, EventBus eventBus) {
        super(frame, eventBus);
    }

    public AbstractSwingBrowserView(JFrame frame, EventBus eventBus, Map<String, Object> context) {
        super(frame, eventBus, context);
    }

    protected void initBrowserEngine() {

        // initialize engine depending on settings
        if (smartMediaConfig != null && smartMediaConfig.getBrowserConfig() != null
                && smartMediaConfig.getBrowserConfig().isProprietaryCodecs()) {
            engine = Engine.newInstance(EngineOptions.newBuilder(
                    smartMediaConfig.getBrowserConfig().isHardwareAccelerated()
                            ? RenderingMode.HARDWARE_ACCELERATED
                            : RenderingMode.OFF_SCREEN)
                    // set user dir
                    .userDataDir(Paths.get(smartMediaConfig.getDataPath() + FILE_JXBROWSER))
                    // det default language
                    .language(Language.GERMAN)
                    // enable proprietary features
                    .enableProprietaryFeature(ProprietaryFeature.AAC)
                    .enableProprietaryFeature(ProprietaryFeature.H_264)
                    //.enableProprietaryFeature(ProprietaryFeature.WIDEVINE)
                    // browser switches
                    .addSwitch("--autoplay-policy=no-user-gesture-required")
                    .build());

        } else {
            engine = Engine.newInstance(EngineOptions.newBuilder(
                            smartMediaConfig.getBrowserConfig().isHardwareAccelerated()
                                    ? RenderingMode.HARDWARE_ACCELERATED
                                    : RenderingMode.OFF_SCREEN)
                    // set user dir
                    .userDataDir(Paths.get(smartMediaConfig.getDataPath() + FILE_JXBROWSER))
                    // det default language
                    .language(Language.GERMAN)
                    // w/o proprietary features
                    // browser switches
                    .addSwitch("--autoplay-policy=no-user-gesture-required")
                    .build());
        }

        // initialize browser
        browser = engine.newBrowser();

        // disable downloads
        browser.set(StartDownloadCallback.class, (params, tell) -> tell.cancel());

        // disable popups
        browser.set(CreatePopupCallback.class, params -> CreatePopupCallback.Response.suppress());

        // disable printing
        browser.set(PrintCallback.class, (params, tell) -> tell.cancel());

        // adjust browser settings (https://jxbrowser-support.teamdev.com/docs/guides/content.html#settings)
        browser.settings().enableOverscrollHistoryNavigation();

        // adjust profile settings (https://jxbrowser-support.teamdev.com/docs/guides/profile.html)
        browser.profile().spellChecker().disable();
    }

}
