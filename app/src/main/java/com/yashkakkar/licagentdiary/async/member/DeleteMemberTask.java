package com.yashkakkar.licagentdiary.async.member;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.DeleteMemberEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Member;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Yash Kakkar on 14-09-2017.
 */

public class DeleteMemberTask extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private List<Member> members;
    private boolean res;

    public DeleteMemberTask(List<Member> members, Context context){
        this.members = members;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        res = false;
        for (Member m:members){
            res = DatabaseHelper.getInstance(context).deleteMembers(m.getMemberId());
        }
        return res;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        EventBus.getDefault().post(new DeleteMemberEvent(aBoolean));
    }
}
