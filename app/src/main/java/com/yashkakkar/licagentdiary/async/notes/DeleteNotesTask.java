package com.yashkakkar.licagentdiary.async.notes;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.DeleteNotesEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yash Kakkar on 16-09-2017.
 */

public class DeleteNotesTask extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private String note_id;
    private boolean flag;

    public DeleteNotesTask(String note_id, Context context){
        this.note_id = note_id;
        this.context = context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        flag = false;
        flag = DatabaseHelper.getInstance(context).deleteNote(note_id);
        return flag;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        EventBus.getDefault().post(new DeleteNotesEvent(aBoolean));
    }
}
