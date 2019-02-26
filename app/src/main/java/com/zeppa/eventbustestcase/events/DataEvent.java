package com.zeppa.eventbustestcase.events;

import java.util.concurrent.atomic.AtomicInteger;

public class DataEvent {
    private static final AtomicInteger lastRevision = new AtomicInteger();
    private final int revision;
    private final long threadId;
    private final long timestamp;

    private DataEvent(int revision) {
        this.revision = revision;
        this.threadId = Thread.currentThread().getId();
        this.timestamp = System.nanoTime();
    }

    public static DataEvent newInstance() {
        return new DataEvent(lastRevision.incrementAndGet());
    }

    public int getRevision() {
        return revision;
    }

    @Override
    public String toString() {
        String timestamp = String.valueOf(this.timestamp);
        int len = timestamp.length();
        return "DataEvent{" +
                "revision=" + revision +
                ", threadId=" + threadId +
                ", timestamp=" + timestamp.substring(len - 7, len) +
                '}';
    }
}
