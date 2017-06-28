package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 29-05-2017.
 */

public class MemberCreatedEvent {
    private final String message;

    public MemberCreatedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
