package com.jfx4test.core.internal;

import java.util.Locale;

/**
 * Provides an API for platform specific features.
 */
public class PlatformAdapter {

    /**
     * Stores the operating system we are running on. Shouldn't change during
     * execution, so singleton.
     */
    private static OS os;

    public static OS getOs() {
        if (os == null) {
            if (System.getProperty("os.name").toLowerCase(Locale.US).contains("nux")) {
                os = OS.UNIX;
            } else if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("win")) {
                os = OS.WINDOWS;
            } else {
                os = OS.MAC;
            }
        }
        return os;
    }

}
