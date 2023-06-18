package com.zwickit.smart.media.ui.view;

import com.google.common.eventbus.EventBus;
import com.zwickit.smart.media.ui.base.AppColor;
import com.zwickit.smart.media.ui.base.FontFx;
import com.zwickit.smart.media.ui.base.Resizer;
import com.zwickit.smart.media.ui.base.SmartMediaConfigLoader;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import com.zwickit.smart.media.ui.view.common.NavigationType;
import com.zwickit.smart.media.ui.view.common.keyboard.NavigationTypeContext;
import com.zwickit.smart.media.ui.view.event.NavigationTypeChangeEvent;
import com.zwickit.smart.media.ui.view.event.NightModeEvent;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Created by Thomas Philipp Zwick
 *
 * Embed JavaFX in Swing examples:
 * https://blog.codecentric.de/2015/06/integration-von-javafx-in-swing-applikationen/
 *
 * Styling examples:
 * https://harmoniccode.blogspot.co.at/2016/11/medusa-industrial-clock-skin.html
 *
 */
public class HeaderView extends JFXPanel {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderView.class);

    /** The loaded resource-bundle */
    protected final ResourceBundle bundle;

    /** The shared event-bus instance */
    private final EventBus eventBus;

    /** The resizer to help building a dynamic user-interface */
    private final Resizer resizer = Resizer.INSTANCE;

    /** The loaded configuration */
    private final SmartMediaConfig smartMediaConfig = SmartMediaConfigLoader.getSmartMediaConfig();

    /**
     * Constructor
     *
     * @param eventBus
     */
    public HeaderView(EventBus eventBus) {
        this.eventBus = eventBus;

        bundle = ResourceBundle.getBundle("message");

        init();
    }

    private void init() {
        final Button weatherButton = ViewUtil.createButton(ID_WEATHER_BUTTON, STYLE_HEADER_BUTTON, FontAwesomeIcon.CLOUD, smartMediaConfig.isTablet() ? ICON_SIZE_4_2EM : ICON_SIZE_7EM);
        weatherButton.setOnAction(event -> onWeatherButtonClicked());

        final Clock dateTimeClock = ClockBuilder.create().skinType(Clock.ClockSkinType.TEXT /*.DIGITAL*/)
                .maxSize(resizer.get1ColumnHeight() * 3, resizer.get1ColumnHeight())
                .running(true)
                .locale(Locale.getDefault())
                .dateVisible(true)
                .textVisible(true)
                .secondsVisible(false)
                .textColor(AppColor.APP_GRAY)
                .dateColor(AppColor.APP_GRAY)
                .build();

        final Button settingButton = ViewUtil.createButton(ID_SETTING_BUTTON, STYLE_HEADER_BUTTON, FontAwesomeIcon.COGS, smartMediaConfig.isTablet() ? ICON_SIZE_4_2EM : ICON_SIZE_7EM);
        settingButton.setOnAction(event -> onSettingButtonClicked());

        final HBox viewPane = new HBox();
        viewPane.getStyleClass().add(ID_HEADER_PANEL);
        viewPane.setPrefHeight(Resizer.INSTANCE.get1ColumnHeight());
        viewPane.setAlignment(Pos.CENTER_RIGHT);
        viewPane.setSpacing(35);
        //viewPane.setPadding(new Insets(5, 5, 5, 5));
        viewPane.getChildren().addAll(weatherButton, settingButton, dateTimeClock);

        setSize(new Dimension(Resizer.INSTANCE.get16ColumnWidth(), Resizer.INSTANCE.get1ColumnHeight()));

        // Finally, set scene
        Scene scene = new Scene(viewPane);
        scene.getStylesheets().add(loadCssFile(smartMediaConfig.getDisplayTypeEnum()));

        setScene(scene);
    }

    private void onSettingButtonClicked() {
        System.exit(0);
    }

    private void onWeatherButtonClicked() {
        Map<String, Object> context = new HashMap<>(1);
        context.put(NavigationTypeContext.KEY_URL, smartMediaConfig.getBrowserConfig().getWeatherUrl());

        eventBus.post(new NavigationTypeChangeEvent(NavigationType.BROWSER, context));
    }
}
