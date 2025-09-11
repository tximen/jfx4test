package com.jfx4test.framework.api;


import com.jfx4test.framework.robot.BaseRobot;

import com.jfx4test.framework.service.finder.NodeFinder;
import com.jfx4test.framework.service.finder.WindowFinder;

import com.jfx4test.framework.service.support.CaptureSupport;

/**
 * Stores the following objects:
 * <ul>
 *     <li>a {@link WindowFinder}</li>
 *     <li>a {@link NodeFinder}</li>
 *     <li>a {@link BaseRobot}</li>
 *     <li>{@link CaptureSupport}</li>
 * </ul>
 */
public record FxApiContext(NodeFinder nodeFinder, CaptureSupport captureSupport) {

    public FxApiContext() {
        this (new NodeFinder(), new CaptureSupport());
    }

    public WindowFinder windowFinder() {
        return this.nodeFinder.getWindowFinder();
    }


}
