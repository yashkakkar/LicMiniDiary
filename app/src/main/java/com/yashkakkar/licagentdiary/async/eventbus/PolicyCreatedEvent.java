package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class PolicyCreatedEvent {
    private final String message;
    private final Long rowId;


    public PolicyCreatedEvent(String message, Long rowId) {
        this.message = message;
        this.rowId = rowId;
    }

    public String getMessage() {
        return message;
    }

    public Long getRowId() {
        return rowId;
    }
}
