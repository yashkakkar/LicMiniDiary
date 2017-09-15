package com.yashkakkar.licagentdiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.yashkakkar.licagentdiary.async.eventbus.DeleteNotesEvent;
import com.yashkakkar.licagentdiary.async.eventbus.UpdateNotesEvent;
import com.yashkakkar.licagentdiary.async.notes.DeleteNotesTask;
import com.yashkakkar.licagentdiary.async.notes.UpdateNotesTask;
import com.yashkakkar.licagentdiary.models.Note;
import com.yashkakkar.licagentdiary.utils.CurrentDateTimeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditMyNote extends AppCompatActivity {

    @BindView(R.id.edit_note_title) EditText editNoteTitle;
    @BindView(R.id.edit_note_contents) EditText editNoteContents;
    private Unbinder unbinder;
    private Note note;
    private Note updateNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null){
            note = savedInstanceState.getParcelable("note");
        }
        setContentView(R.layout.activity_edit_my_note);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (note != null) {
            editNoteTitle.setText(note.getNoteTitle());
            Log.v("note title ", note.getNoteTitle());
            editNoteContents.setText(note.getNoteContents());
        }

    }

    public void updateNote(){
        updateNote = new Note();
        updateNote.setNoteId(note.getNoteId());
        if (!editNoteTitle.getText().toString().equals(note.getNoteTitle()))
            updateNote.setNoteTitle(editNoteTitle.getText().toString());
        else
            updateNote.setNoteTitle(note.getNoteTitle());

        if (!editNoteContents.getText().toString().equals(note.getNoteContents()))
            updateNote.setNoteContents(editNoteContents.getText().toString());
        else
            updateNote.setNoteContents(note.getNoteContents());
        // created date and time will not change
        updateNote.setTimeCreated(note.getTimeCreated());
        updateNote.setDateCreated(note.getDateCreated());
        // get current date and time
        String note_date = CurrentDateTimeUtils.newInstance().getCurrentDate("dd MMM, yyyy");
        String note_time = CurrentDateTimeUtils.newInstance().getCurrentTime("K:mm a");
        String note_last_modification = note_date+" "+note_time;
        updateNote.setLastModification(note_last_modification);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.save_edit_note:
                // update notes
                updateNote();
                // save notes
                UpdateNotesTask updateNotesTask = new UpdateNotesTask(updateNote, this);
                updateNotesTask.execute();
                break;
            case R.id.delete_edit_note:
                DeleteNotesTask deleteNotesTask = new DeleteNotesTask(note.getNoteId(), this);
                deleteNotesTask.execute();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onEvent(UpdateNotesEvent event){
        if (event.isNoteUpdated()){
            Toast.makeText(this,"Note saved successfully!!",Toast.LENGTH_SHORT);
        }else {
            Toast.makeText(this,"Unable to save note! Try Again!",Toast.LENGTH_SHORT);
        }
        finish();
    }

    @Subscribe
    public void onEvent(DeleteNotesEvent event){
        if (event.isNotesDeleted()){
            Toast.makeText(this,"Note deleted successfully!!",Toast.LENGTH_SHORT);
        }else {
            Toast.makeText(this, "Unable to delete note!\nTry Again!", Toast.LENGTH_SHORT);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }
}
