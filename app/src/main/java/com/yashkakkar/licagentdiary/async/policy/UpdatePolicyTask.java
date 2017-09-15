package com.yashkakkar.licagentdiary.async.policy;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.UpdatePolicyEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Policy;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yash Kakkar on 28-08-2017.
 */

public class UpdatePolicyTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private Policy policy;

    public UpdatePolicyTask(Context context, Policy policy) {
        this.context = context;
        this.policy = policy;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result;
        result = DatabaseHelper.getInstance(context).updatePolicy(policy);
        return result;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        EventBus.getDefault().post(new UpdatePolicyEvent(b));
    }
}
