package com.yashkakkar.licagentdiary.async.policy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.yashkakkar.licagentdiary.async.eventbus.PolicyCreatedEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Policy;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class SaveNewPolicyTask extends AsyncTask<Policy,Void,Long>{

    private Context context;
    private Policy policy;
    private Long rowId;

    public SaveNewPolicyTask(Context context, Policy policy) {
        this.context = context;
        this.policy = policy;
    }

    @Override
    protected Long doInBackground(Policy... policies) {
        rowId = DatabaseHelper.getInstance().addNewPolicy(policy);
        return rowId;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        EventBus.getDefault().post(new PolicyCreatedEvent("Policy create event",rowId));
    }
}
