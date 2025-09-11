package com.jfx4test.framework.robot;

import java.util.concurrent.TimeUnit;

public class SleepRobot {

    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void sleep(long duration, TimeUnit timeUnit) {
        sleep(timeUnit.toMillis(duration));
    }

}
