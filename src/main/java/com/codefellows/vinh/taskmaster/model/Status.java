package com.codefellows.vinh.taskmaster.model;

public enum Status {
    Available, Assigned, Accepted, Finished;

    public static Status nextValue(Status status)
    {
        Status[] statuses = Status.values();
        return statuses[(status.ordinal()+1)%statuses.length];
    }
}
