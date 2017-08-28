package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 28-08-2017.
 */

public class UpdatePolicyEvent {
    boolean result;

    public UpdatePolicyEvent(boolean result){
        this.result = result;
    }

    public  boolean isPolicyUpdated(){
        return result;
    }

}
