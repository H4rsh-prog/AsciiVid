package com.aar.videostream.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FPSServiceTest {
    private FPSCounter fpsCounter;

    @BeforeEach
    void setUp() throws InterruptedException {
        fpsCounter = new FPSCounter();
        fpsCounter.init();
    }

    @Test
    void keepsFpsStableAsTheThirtyFrameWindowAdvances() {
        double fps = 0.0;
        for (int frame = 0; frame < 35; frame++) {
            long timestamp = 1_000_000_000L + frame * 33_333_333L;
            fps = fpsCounter.getFPS(timestamp);
        }
        assertEquals(29.0, fps, 0.1);
    }
}