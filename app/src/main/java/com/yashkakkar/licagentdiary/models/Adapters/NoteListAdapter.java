package com.yashkakkar.licagentdiary.models.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.models.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yash Kakkar on 13-06-2017.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyNoteViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Note> notes;

    public NoteListAdapter(Context context, List<Note> notes) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.notes = notes;
    }

    @Override
    public MyNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_row, parent, false);
        return new MyNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyNoteViewHolder holder, int position) {

        Note note = notes.get(position);
        Log.v("NOTE ITEM", note.getNoteTitle());
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteContents.setText(note.getNoteContents());
        holder.noteTimeCreated.setText(note.getTimeCreated());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyNoteViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.note_title_view) TextView noteTitle;
    @BindView(R.id.note_contents_view) TextView noteContents;
    @BindView(R.id.note_time_created_view) TextView noteTimeCreated;
    public MyNoteViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}


}
