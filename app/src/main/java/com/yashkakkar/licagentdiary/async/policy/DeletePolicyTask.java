package com.yashkakkar.licagentdiary.async.policy;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.DeletePolicyEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yash Kakkar on 28-08-2017.
 */

public class DeletePolicyTask extends AsyncTask<Void,Void,Boolean> {

    private String policy_id;
    private Context context;

    public DeletePolicyTask(Context context, String policyId) {
        this.context = context;
        this.policy_id = policyId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result;
        result = DatabaseHelper.getInstance(context).deletePolicy(policy_id);
        return result;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        EventBus.getDefault().post(new DeletePolicyEvent(aBoolean));
    }
}
