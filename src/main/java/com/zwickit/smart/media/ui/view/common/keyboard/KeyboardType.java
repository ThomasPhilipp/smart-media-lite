package com.zwickit.smart.media.ui.view.common.keyboard;

/**
 * Created by Thomas Philipp Zwick
 */
public enum KeyboardType {
    TEXT("text"),
    SEARCH("search"),
    URL("url"),
    EMAIL("email"),
    PASSWORD("password"),
    TELEPHONE("tel"),
    NUMBER("number"),
    OTHER("other");

    private final String value;

    KeyboardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static KeyboardType toEnum(String value) {
        for (KeyboardType type : KeyboardType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

}
