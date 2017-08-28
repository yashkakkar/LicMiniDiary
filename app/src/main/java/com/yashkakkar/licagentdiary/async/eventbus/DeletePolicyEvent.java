package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 28-08-2017.
 */

public class DeletePolicyEvent {
    boolean result;

    public DeletePolicyEvent(boolean result) {
        this.result = result;
    }

    public boolean isPolicyDeleted(){
        return result;
    }

}
