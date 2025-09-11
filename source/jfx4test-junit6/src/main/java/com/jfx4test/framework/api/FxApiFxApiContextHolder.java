package com.jfx4test.framework.api;

import com.jfx4test.framework.api.FxApiContext;

public final class FxApiFxApiContextHolder {

    private static final FxApiFxApiContextHolder SOLE_INSTANCE = new FxApiFxApiContextHolder();

    private final com.jfx4test.framework.api.FxApiContext apiContext;

    private FxApiFxApiContextHolder() {
        this.apiContext = new com.jfx4test.framework.api.FxApiContext();
    }

    public FxApiContext getApiContext() {
        return apiContext;
    }

    public static FxApiFxApiContextHolder getInstance() {
        return FxApiFxApiContextHolder.SOLE_INSTANCE;
    }
}
