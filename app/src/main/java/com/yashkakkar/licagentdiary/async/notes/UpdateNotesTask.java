package com.yashkakkar.licagentdiary.async.notes;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.yashkakkar.licagentdiary.async.eventbus.UpdateNotesEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Note;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yash Kakkar on 16-09-2017.
 */

public class UpdateNotesTask extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private Note note;
    private boolean flag;
    public UpdateNotesTask(Note note, Context context){
        this.context = context;
        this.note = note;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        flag = false;
        flag = DatabaseHelper.getInstance(context).updateNote(note);
        return flag;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        EventBus.getDefault().post(new UpdateNotesEvent(aBoolean));
    }
}
