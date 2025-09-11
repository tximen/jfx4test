package com.jfx4test.framework.service.query;

import java.io.Serial;

public class EmptyNodeQueryException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 144186214574614145L;

    public EmptyNodeQueryException(String message) {
        super(message);
    }
}
