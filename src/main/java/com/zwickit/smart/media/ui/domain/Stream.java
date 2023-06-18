package com.zwickit.smart.media.ui.domain;

/**
 * Created by Thomas Philipp Zwick
 */
public class Stream {

    private String name;
    private String url;
    private String icon;
    private int groupNumber;

    public Stream() {
        // empty
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @Override
    public String toString() {
        return "Stream{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", groupNumber=" + groupNumber +
                '}';
    }
}
