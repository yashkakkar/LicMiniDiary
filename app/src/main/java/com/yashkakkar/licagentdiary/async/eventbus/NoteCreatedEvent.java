package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 13-06-2017.
 */

public class NoteCreatedEvent {

    private final String message;
    private final Long rowId;

    public NoteCreatedEvent(String message, Long rowId) {
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
