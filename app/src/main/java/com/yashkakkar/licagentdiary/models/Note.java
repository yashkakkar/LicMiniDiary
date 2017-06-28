package com.yashkakkar.licagentdiary.models;

/**
 * Created by Yash Kakkar on 24-05-2017.
 */

public class Note {

    private String noteId; //note id is timestamp---> note_title_date_time
    private String noteTitle;
    private String noteContents;
    private String timeCreated;
    private String dateCreated;
    private String lastModification;

    public Note(String noteId, String noteTitle, String noteContents, String timeCreated, String dateCreated, String lastModification) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContents = noteContents;
        this.timeCreated = timeCreated;
        this.dateCreated = dateCreated;
        this.lastModification = lastModification;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContents() {
        return noteContents;
    }

    public void setNoteContents(String noteContents) {
        this.noteContents = noteContents;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }
}
