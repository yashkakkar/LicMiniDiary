package com.yashkakkar.licagentdiary.async.member;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.ActivityDashboard;
import com.yashkakkar.licagentdiary.async.eventbus.GetMemberListEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Member;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class GetMemberListTask extends AsyncTask<Void,Void, List<Member>>{

    private Context context;
    private List<Member> members;
    //private ProgressDialog dialog;

    public GetMemberListTask(Context context){
        this.context = context;
    }

    public List<Member> getMembers(){
        return members;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();*/
    }

    @Override
    protected List<Member> doInBackground(Void... lists) {
        members = new ArrayList<>();
        members = DatabaseHelper.getInstance().getAllMembers();
        return members;
    }

    @Override
    protected void onPostExecute(List<Member> members) {
        super.onPostExecute(members);
        //dialog.dismiss();
        // post the event
        EventBus.getDefault().post(new GetMemberListEvent(members));
    }

}
