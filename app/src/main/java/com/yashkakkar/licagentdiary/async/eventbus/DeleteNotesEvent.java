package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 16-09-2017.
 */

public class DeleteNotesEvent {
    boolean result;

    public DeleteNotesEvent(Boolean result){
        this.result = result;
    }

    public boolean isNotesDeleted(){
        return result;
    }

}
