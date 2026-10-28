package com.aar.videostream.service;

public class FPSCounter{
    volatile private long[] timestamps;
    volatile private long previousTimestamp = 0L;
    volatile private long currentTimestamp = 0;
    private final int windowLengthIndex = 29;

    FPSCounter(){
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