package com.zwickit.smart.media.ui.base;

/**
 * Created by Thomas Philipp Zwick
 */
public class Resizer {

    /**
     * Singleton instance
     */
    public static Resizer INSTANCE;

    /**
     * Display-type
     */
    private final DisplayType displayType;

    /**
     * Constructor
     *
     * @param displayType
     */
    private Resizer(DisplayType displayType) {
        this.displayType = displayType;
    }

    /**
     * Creates or returns singleton instance.
     *
     * @param displayType
     * @return
     */
    public static Resizer getInstance(DisplayType displayType) {
        if (INSTANCE == null) {
            INSTANCE = new Resizer(displayType);
        }
        return INSTANCE;
    }

    public int getWindowWidth() {
        return (int) displayType.getWindowWidth();
    }

    public int getWindowHeight() {
        return (int) displayType.getWindowHeight();
    }

    public int getContentWidth() {
        return getWindowWidth() - get1ColumnWidth();
    }

    public int getContentHeight() {
        return getWindowHeight() - get1ColumnHeight();
    }

    public int getColumnWidth(int multiplier) {
        if (multiplier >= displayType.getNumberOfWidthColumns())
            return (int) displayType.getWindowWidth();

        return (int) displayType.getColumnWidth() * multiplier;
    }

    public int getColumnHeight(int multiplier) {
        if (multiplier >= displayType.getNumberOfHeightColumns())
            return (int) displayType.getWindowHeight();

        return (int) displayType.getColumnHeight() * multiplier;
    }

    public int get1ColumnWidth() {
        return getColumnWidth(1);
    }

    public int get1ColumnHeight() {
        return getColumnHeight(1);
    }

    public int get2ColumnWidth() {
        return getColumnWidth(2);
    }

    public int get2ColumnHeight() {
        return getColumnHeight(2);
    }

    public int get3ColumnWidth() {
        return getColumnWidth(3);
    }

    public int get3ColumnHeight() {
        return getColumnHeight(3);
    }

    public int get4ColumnWidth() {
        return getColumnWidth(4);
    }

    public int get4ColumnHeight() {
        return getColumnHeight(4);
    }

    public int get5ColumnWidth() {
        return getColumnWidth(5);
    }

    public int get5ColumnHeight() {
        return getColumnHeight(5);
    }

    public int get6ColumnWidth() {
        return getColumnWidth(6);
    }

    public int get6ColumnHeight() {
        return getColumnHeight(6);
    }

    public int get7ColumnWidth() {
        return getColumnWidth(7);
    }

    public int get7ColumnHeight() {
        return getColumnHeight(7);
    }

    public int get8ColumnWidth() {
        return getColumnWidth(8);
    }

    public int get8ColumnHeight() {
        return getColumnHeight(8);
    }

    public int get9ColumnWidth() {
        return getColumnWidth(9);
    }

    public int get9ColumnHeight() {
        return getColumnHeight(9);
    }

    public int get10ColumnWidth() {
        return getColumnWidth(10);
    }

    public int get11ColumnWidth() {
        return getColumnWidth(11);
    }

    public int get12ColumnWidth() {
        return getColumnWidth(12);
    }

    public int get13ColumnWidth() {
        return getColumnWidth(13);
    }

    public int get14ColumnWidth() {
        return getColumnWidth(14);
    }

    public int get15ColumnWidth() {
        return getColumnWidth(15);
    }

    public int get16ColumnWidth() {
        return getColumnWidth(16);
    }

}
