package com.zeppa.eventbustestcase.events;

public class DataEvent {
    private static volatile int lastRevision = 1;
    private final int revision;
    private final long threadId;
    private final long timestamp;

    private DataEvent(int revision) {
        this.revision = revision;
        this.threadId = Thread.currentThread().getId();
        this.timestamp = System.nanoTime();
    }

    public static synchronized DataEvent newInstance() {
        return new DataEvent(lastRevision++);
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
                ", timestamp=" + timestamp +
                '}';
    }
}
