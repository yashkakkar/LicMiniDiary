package com.yashkakkar.licagentdiary.async.policy;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.GetMemberPoliciesListEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Policy;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yash Kakkar on 01-07-2017.
 */

public class GetMemberPoliciesListTask extends AsyncTask<String,Void,List<Policy>>{

    private Context context;
    private List<Policy> policies = new ArrayList<>();

    public GetMemberPoliciesListTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Policy> doInBackground(String... strings) {
        String memberId = strings[0];
        policies = DatabaseHelper.getInstance(context).getMemberPolicies(memberId);
        return policies;
    }

    @Override
    protected void onPostExecute(List<Policy> policies) {
        super.onPostExecute(policies);
        EventBus.getDefault().post(new GetMemberPoliciesListEvent(policies));
    }
}
