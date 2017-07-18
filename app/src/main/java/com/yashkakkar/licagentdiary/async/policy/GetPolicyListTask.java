package com.yashkakkar.licagentdiary.async.policy;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.GetPolicyListEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Policy;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class GetPolicyListTask extends AsyncTask<Void,Void, List<Policy>>{

    private Context context;
    private List<Policy> policies;


    public GetPolicyListTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Policy> doInBackground(Void... lists) {
        policies = new ArrayList<>();
        policies = DatabaseHelper.getInstance().getAllPolicies();
        return policies;
    }

    @Override
    protected void onPostExecute(List<Policy> policies) {
        super.onPostExecute(policies);
        EventBus.getDefault().post(new GetPolicyListEvent(policies));
    }
}
