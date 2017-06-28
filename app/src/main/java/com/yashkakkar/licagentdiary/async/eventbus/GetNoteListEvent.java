package com.yashkakkar.licagentdiary.async.eventbus;

import com.yashkakkar.licagentdiary.models.Note;

import java.util.List;

/**
 * Created by Yash Kakkar on 13-06-2017.
 */

public class GetNoteListEvent {
    private List<Note> notes;

    public GetNoteListEvent(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getNotes() {
        return notes;
    }
}
