package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 14-09-2017.
 */

public class DeleteMemberEvent {
    boolean result;

    public DeleteMemberEvent(boolean result){
        this.result = result;
    }

    public boolean isMembersDelete(){
        return result;
    }

}
