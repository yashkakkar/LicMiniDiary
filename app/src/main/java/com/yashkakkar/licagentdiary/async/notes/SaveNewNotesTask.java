package com.yashkakkar.licagentdiary.async.notes;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.NoteCreatedEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Note;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class SaveNewNotesTask extends AsyncTask<Note,Void,Long>{
    private Context context;
    private Note note;
    private Long rowId;

    public SaveNewNotesTask(Context context, Note note) {
        this.context = context;
        this.note = note;
    }


    @Override
    protected Long doInBackground(Note... notes) {
        rowId = DatabaseHelper.getInstance().addNewNote(note);
        return rowId;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        EventBus.getDefault().post(new NoteCreatedEvent("Note create event", aLong));
    }
}
