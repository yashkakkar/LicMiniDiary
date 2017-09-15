package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 16-09-2017.
 */

public class UpdateNotesEvent {
    boolean result;

    public UpdateNotesEvent(Boolean result){
        this.result = result;
    }

    public boolean isNoteUpdated(){
        return result;
    }

}
