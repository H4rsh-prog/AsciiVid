package com.aar.videostream.service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class FPSCounter{
    volatile private long[] timestamps;
    volatile private long previousTimestamp;
    volatile private long currentTimestamp;
    private final int windowLengthIndex = 29;

    @PostConstruct
    public void init() throws InterruptedException{
        timestamps = new long[windowLengthIndex+1];
        for(int i=0; i<windowLengthIndex+1; i++) {
            timestamps[i] = 0L;
        }
    }

    public double getFPS(long currentTimestamp){
        this.currentTimestamp = currentTimestamp;
        updateTimestamps();
        return (windowLengthIndex) / ((currentTimestamp - previousTimestamp) / 1e9);
    }

    synchronized private void updateTimestamps() {
        previousTimestamp = timestamps[0];
        for(int i=0; i<29; i++) {
            timestamps[i] = timestamps[i+1];
        }
        timestamps[29] = currentTimestamp;
    }
}