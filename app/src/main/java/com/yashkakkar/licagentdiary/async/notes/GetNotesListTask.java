package com.yashkakkar.licagentdiary.async.notes;

import android.content.Context;
import android.os.AsyncTask;

import com.yashkakkar.licagentdiary.async.eventbus.GetNoteListEvent;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Note;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class GetNotesListTask extends AsyncTask<Void,Void,List<Note>>{

    private Context context;
    private List<Note> notes;

    public GetNotesListTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Note> doInBackground(Void... voids) {
        notes = new ArrayList<>();
        notes = DatabaseHelper.getInstance().fetchAllNotes();
        return notes;
    }

    @Override
    protected void onPostExecute(List<Note> notes) {
        super.onPostExecute(notes);
        EventBus.getDefault().post(new GetNoteListEvent(notes));
    }
}
