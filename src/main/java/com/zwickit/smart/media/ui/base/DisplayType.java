package com.zwickit.smart.media.ui.base;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Sources:
 * * http://www.reinmedical.com/de/wissen-u-technik/bildschirmaufloesungen-uebersicht.html
 *
 * Created by Thomas Philipp Zwick
 */
public enum DisplayType {

    HD720 (HD720_WIDTH_WINDOW, HD720_WIDTH, HD720_HEIGHT_WINDOW, HD720_HEIGHT),
    TABLET (TABLET_WIDTH_WINDOW, TABLET_WIDTH, TABLET_HEIGHT_WINDOW, TABLET_HEIGHT),
    HD1080 (HD1080_WIDTH_WINDOW, HD1080_WIDTH, HD10880_HEIGHT_WINDOW, HD1080_HEIGHT);

    /**
     * The width of the window for given display-type.
     */
    private double windowWidth;

    /**
     * The width of a column for given display-type.
     */
    private double columnWidth;

    /**
     * The height of the window for given display-type.
     */
    private double windowHeight;

    /**
     * The height of a column for given display-type.
     */
    private double columnHeight;

    private int numberOfWidthColumns = 16;

    private int numberOfHeightColumns = 9;

    /**
     * Constructor
     *
     * @param windowWidth
     * @param columnWidth
     * @param windowHeight
     * @param columnHeight
     */
    DisplayType(double windowWidth, double columnWidth, double windowHeight, double columnHeight) {
        this.windowWidth = windowWidth;
        this.columnWidth = columnWidth;
        this.windowHeight = windowHeight;
        this.columnHeight = columnHeight;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getColumnWidth() {
        return columnWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public double getColumnHeight() {
        return columnHeight;
    }

    public int getNumberOfWidthColumns() {
        return numberOfWidthColumns;
    }

    public int getNumberOfHeightColumns() {
        return numberOfHeightColumns;
    }

}
