package com.zwickit.smart.media.ui.view.common;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Based on https://github.com/HanSolo/chargecontrol/
 */
public class VolumeControl extends Region {

    private static final double PREFERRED_WIDTH  = 200;
    private static final double PREFERRED_HEIGHT = 43; // PREFERRED_WIDTH / 4,63

    private static final double MINIMUM_WIDTH    = 150;
    private static final double MINIMUM_HEIGHT   = 32; // MINIMUM_WIDTH / 4,63

    private static final double MAXIMUM_WIDTH    = 250;
    private static final double MAXIMUM_HEIGHT   = 54; // MAXIMUM_WIDTH / 4,63

    private        Gauge    model;
    private        Region[] bars;
    private static double   aspectRatio;
    private        double   width;
    private        double   height;
    private        HBox     pane;
    private        Paint    backgroundPaint;
    private        Paint    borderPaint;
    private        double   borderWidth;

    // ******************** Constructors **************************************
    public VolumeControl() {
        getStylesheets().add(getClass().getResource("/styles/volume-control.css").toExternalForm());
        getStyleClass().add("volume-control");

        aspectRatio     = PREFERRED_HEIGHT / PREFERRED_WIDTH;
        backgroundPaint = Color.TRANSPARENT;
        borderPaint     = Color.TRANSPARENT;
        borderWidth     = 0;
        model           = GaugeBuilder.create().maxValue(1.0)/*.animated(true)*/.build();

        init();
        initGraphics();
        registerListeners();
    }

    // ******************** Initialization ************************************
    private void init() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 ||
                Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0) {
            setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0) {
            setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }
    }

    private void initGraphics() {
        bars = new Region[] {
                createBar(8, "gray-bar"),
                createBar(16, "gray-bar"),
                createBar(22, "gray-bar"),
                createBar(28, "gray-bar"),
                createBar(34, "gray-bar"),
                createBar(40, "gray-bar"),
                createBar(46, "gray-bar"),
                createBar(52, "gray-bar"),
                createBar(58, "gray-bar"),
                createBar(64, "gray-bar"),
                //createBar(60, "gray-bar"),
                //createBar(64, "gray-bar")
        };

        pane = new HBox(bars);
        pane.setSpacing(PREFERRED_WIDTH * 0.01960784);
        pane.setAlignment(Pos.CENTER);
        pane.setFillHeight(false);
        pane.setBackground(new Background(new BackgroundFill(backgroundPaint, new CornerRadii(1024), Insets.EMPTY)));
        pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, new CornerRadii(1024), new BorderWidths(borderWidth))));

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        model.currentValueProperty().addListener(o -> handleControlPropertyChanged("VALUE"));
    }

    // ******************** Methods *******************************************
    private void handleControlPropertyChanged(final String PROPERTY) {
        if ("VALUE".equals(PROPERTY)) {
            int chargedBars = (int) (model.getCurrentValue() * 11);
            for (int i = 0 ; i < 10 ; i++) {
                if (i < chargedBars) {
                    if (i < 3) {
                        bars[i].getStyleClass().setAll("red-bar");
                    } else if (i < 7) {
                        bars[i].getStyleClass().setAll("orange-bar");
                    } else {
                        bars[i].getStyleClass().setAll("green-bar");
                    }
                } else {
                    bars[i].getStyleClass().setAll("gray-bar");
                }
            }
        }
    }

    public double getValue() { return model.getValue(); }

    public void setValue(final double VALUE) {
        double value = VALUE / 100;
        model.setValue(value);
    }

    private Region createBar(final double HEIGHT, final String STYLE_CLASS) {
        Region region = new Region();
        region.setPrefSize(20, HEIGHT);
        region.getStyleClass().setAll(STYLE_CLASS);
        return region;
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (aspectRatio * width > height) {
            width = 1 / (aspectRatio / height);
        } else if (1 / (aspectRatio / height) > width) {
            height = aspectRatio * width;
        }

        if (width > 0 && height > 0) {
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);
            pane.setSpacing(width * 0.01960784);
            for (int i = 0 ; i < 10 ; i++) {
                bars[i].setPrefSize(0.3030303 * height, (0.3030303 * height + i * 0.06060606 * height));
            }
        }
    }
}
