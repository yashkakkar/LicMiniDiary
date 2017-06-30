package com.yashkakkar.licagentdiary.async.member;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Member;

/**
 * Created by Yash Kakkar on 29-05-2017.
 */

public class SaveNewMemberTask extends AsyncTask<Member,Void,Member> {

    private Context context;
    private Member member;

    public SaveNewMemberTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Member doInBackground(Member... params) {
       member = params[0];
        // calling database function
        Long rowId = DatabaseHelper.getInstance().addMember(member);
        return member;
    }

    @Override
    protected void onPostExecute(Member member) {
        super.onPostExecute(member);
    }
}
