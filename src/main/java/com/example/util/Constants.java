package com.example.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Constants {
    public static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages", Locale.getDefault());

    public static String getMessage(String key) {
        return MESSAGES.getString(key);
    }
}
