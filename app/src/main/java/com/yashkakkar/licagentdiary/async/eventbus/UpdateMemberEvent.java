package com.yashkakkar.licagentdiary.async.eventbus;

import android.os.AsyncTask;

/**
 * Created by Yash Kakkar on 01-07-2017.
 */

public class UpdateMemberEvent {
    boolean result;

    public UpdateMemberEvent(boolean result) {
        this.result = result;
    }

    public boolean isMemberUpdated() {
        return result;
    }
}
