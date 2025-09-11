package com.jfx4test.framework.api;

public final class FxApiFxApiContextHolder {

    private static final FxApiFxApiContextHolder SOLE_INSTANCE = new FxApiFxApiContextHolder();

    private final FxApiContext apiContext;

    private FxApiFxApiContextHolder() {
        this.apiContext = new FxApiContext();
    }

    public FxApiContext getApiContext() {
        return apiContext;
    }

    public static FxApiFxApiContextHolder getInstance() {
        return FxApiFxApiContextHolder.SOLE_INSTANCE;
    }
}
