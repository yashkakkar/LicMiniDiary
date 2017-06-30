package com.yashkakkar.licagentdiary.async.member;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.UpdateMemberEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Member;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yash Kakkar on 01-07-2017.
 */

public class UpdateMemberTask extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private Member member;

    public UpdateMemberTask(Context context, Member member) {
        this.context = context;
        this.member = member;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result;
        result = DatabaseHelper.getInstance(context).updateMember(this.member);
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        EventBus.getDefault().post(new UpdateMemberEvent(aBoolean));
    }

}
