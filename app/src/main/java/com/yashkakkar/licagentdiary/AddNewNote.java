package com.yashkakkar.licagentdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.async.eventbus.NoteCreatedEvent;
import com.yashkakkar.licagentdiary.async.notes.GetNotesListTask;
import com.yashkakkar.licagentdiary.async.notes.SaveNewNotesTask;
import com.yashkakkar.licagentdiary.models.Note;
import com.yashkakkar.licagentdiary.utils.CurrentDateTimeUtils;
import com.yashkakkar.licagentdiary.utils.GenerateUniqueId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddNewNote extends AppCompatActivity {

    @BindView(R.id.new_note_title) EditText noteTitle;
    @BindView(R.id.new_note_contents) EditText noteContents;
    Unbinder unbinder;
    private String note_id;
    private String note_title;
    private String note_contents;
    private String note_time;
    private String note_date;
    private String note_last_modification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        /*
        private String noteId; //note id is timestamp---> note_title_date_time
        private String noteTitle;
        private String noteContents;
        private String timeCreated;
        private String dateCreated;
        */



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_note:
                if (isNoteTitle()){
                    // generate new noteId
                    note_contents = noteContents.getText().toString().trim();
                    if (TextUtils.isEmpty(note_contents)){
                        note_contents = "";
                        // ask user to save an empty note or discard note
                    }

                    //GenerateUniqueId uniqueId = new GenerateUniqueId();
                    note_id = GenerateUniqueId.newInstance().generateUniqueKeyUsingUUID();

                    // get current date and time
                    note_date = CurrentDateTimeUtils.newInstance().getCurrentDate("dd/mm/yyyy");
                    note_time = CurrentDateTimeUtils.newInstance().getCurrentTime("HH:mm:ss");
                    note_last_modification = note_date+" "+note_time;
                    Note note = new Note(note_id,note_title,note_contents,note_time,note_date,note_last_modification);

                    // save all notes
                    SaveNewNotesTask saveNewNotesTask = new SaveNewNotesTask(this,note);
                    saveNewNotesTask.execute();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNoteTitle() {
        note_title = noteTitle.getText().toString().trim();
        if (TextUtils.isEmpty(note_title)){
            Toast.makeText(this,"Please give a title to this note!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Subscribe
    public void onEvent(NoteCreatedEvent event) {
        if (event.getRowId() > 0) {
            Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddNewNote.this, ActivityDashboard.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Some thing went wrong! Try Again!", Toast.LENGTH_SHORT).show();
        }
    }
}
