package com.jfx4test.framework.api;


import java.io.Serial;

public class FxRobotException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7630635682567257825L;

    public FxRobotException(String message) {
        super(message);
    }

}