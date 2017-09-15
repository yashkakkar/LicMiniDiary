package com.yashkakkar.licagentdiary.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yash Kakkar on 24-05-2017.
 */

public class Note implements Parcelable {

    private String noteId; //note id is timestamp---> note_title_date_time
    private String noteTitle;
    private String noteContents;
    private String timeCreated;
    private String dateCreated;
    private String lastModification;

    public Note(){

    }
    public Note(String noteId, String noteTitle, String noteContents, String timeCreated, String dateCreated, String lastModification) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContents = noteContents;
        this.timeCreated = timeCreated;
        this.dateCreated = dateCreated;
        this.lastModification = lastModification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Storing the Note data to Parcel object
     **/
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(noteId);
        parcel.writeString(noteTitle);
        parcel.writeString(noteContents);
        parcel.writeString(timeCreated);
        parcel.writeString(dateCreated);
        parcel.writeString(lastModification);
    }

    /**
     * Retrieving Note data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Note(Parcel in){
        this.noteId = in.readString();
        this.noteTitle = in.readString();
        this.noteContents = in.readString();
        this.timeCreated = in.readString();
        this.dateCreated = in.readString();
        this.lastModification = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }

    };

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
