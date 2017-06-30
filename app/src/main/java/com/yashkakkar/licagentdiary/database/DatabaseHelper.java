package com.yashkakkar.licagentdiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.media.Image;
import android.util.Log;

import com.yashkakkar.licagentdiary.ActivityDashboard;
import com.yashkakkar.licagentdiary.async.UpgradeMethod;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.models.Note;
import com.yashkakkar.licagentdiary.models.Policy;
import com.yashkakkar.licagentdiary.utils.AssetUtils;
import com.yashkakkar.licagentdiary.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yash Kakkar on 18-03-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "LIC_AGENT_DIARY";
    // Database version aligned if possible to the software version
    private static final int DATABASE_VERSION = 1;
    // SQL Query file directory
    private static final String SQL_DIR = "sql";
    // Queries
    private static final String CREATE_QUERY = "create.sql";
    private static final String UPGRADE_QUERY_PREFIX = "upgrade-";
    private static final String UPGRADE_QUERY_SUFFIX = ".sql";


    // Members Table Name
    public static final String TABLE_MEMBERS = "Members";
    // Members Table columns
    public static final String KEY_MEMBER_ID = "member_id";
    public static final String KEY_MEMBER_NAME = "name";
    public static final String KEY_MEMBER_PHONE_NUMBER = "phone_number";
    public static final String KEY_MEMBER_EMAIL_ID = "email_id";
    public static final String KEY_MEMBER_GENDER = "gender";
    public static final String KEY_MEMBER_IMAGE_PATH = "image_path";
    public static final String KEY_MEMBER_IMAGE_NAME = "image_name";
    public static final String KEY_MEMBER_IMAGE_FILE = "image_file";
    public static final String KEY_MEMBER_FAV = "fav";


    // Policies Table Name
    public static final String TABLE_POLICIES = "Policies";
    // Policies Table columns
    public static final String KEY_POLICY_ID = "policy_id";
    public static final String KEY_POLICY_NAME = "policy_name";
    public static final String KEY_POLICY_NUMBER = "policy_number";
    public static final String KEY_POLICY_DOC_DATE = "doc_date";
    public static final String KEY_POLICY_DLP_DATE = "dlp_date";
    public static final String KEY_POLICY_DOM_DATE = "dom_date";
    public static final String KEY_POLICY_DOB_DATE = "dob_date";
    public static final String KEY_POLICY_FUP = "fup";
    public static final String KEY_POLICY_TERM = "term";
    public static final String KEY_POLICY_MODE = "mode";
    public static final String KEY_POLICY_SUM_ASSURED_AMOUNT = "sum_assured_amount";
    public static final String KEY_POLICY_PREMIUM_AMOUNT = "premium_amount";
    public static final String KEY_POLICY_NOMINEE_NAME = "nominee_name";
    public static final String KEY_POLICY_MARKED = "marked";
    public static final String KEY_POLICY_BOOK_MARKED = "book_marked";
    public static final String KEY_POLICY_STATUS = "policy_status";

    // Notes Table Name
    public static final String TABLE_NOTES = "Notes";
    // Notes Table columns
    public static final String KEY_NOTE_ID = "note_id";
    public static final String KEY_NOTE_TITLE = "title";
    public static final String KEY_NOTE_CONTENT = "content";
    public static final String KEY_NOTE_DATE_CREATED = "date_created";
    public static final String KEY_NOTE_TIME_CREATED = "time_created";
    public static final String KEY_NOTE_LAST_MODIFICATION = "last_modification";




    private static DatabaseHelper instance = null;
    private SQLiteDatabase db;

    public static synchronized DatabaseHelper getInstance() {
        return getInstance(ActivityDashboard.getAppContext());
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private final Context mContext;
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public SQLiteDatabase getDatabase() {
        return getDatabase(false);
    }

    public SQLiteDatabase getDatabase(boolean forceWritable) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            if (forceWritable && db.isReadOnly()) {
                throw new SQLiteReadOnlyDatabaseException("Required writable database, obtained read-only");
            }
            return db;
        } catch (IllegalStateException e) {
            return this.db;
        }
    }

    // Creating Tables for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            execSqlFile(CREATE_QUERY, db);
            Log.v(Constants.TAG, "Create database tables ");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Constants.TAG, "Fail to create database tables ");
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // not used, but you could upgrade the database with ALTER
        // scripts
        this.db = db;
        // 2 ways to upgrade the database

        // 1 upgrading database via methods using UpgradeMethod in background
        // UpgradeMethod.process(oldVersion,newVersion);

        // 2 upgrading database via executing upgrade-2.sql
        //   Create a new upgrade file with the name of upgrade-new version number  upgrade function runs after changing the old database version to new database version
        //   step 1 change upgrade file version to newVersion
        //   step 2 change the database version to newVersion
        try {
            for (String sqlFile : AssetUtils.listOfAllFiles(SQL_DIR,mContext.getAssets())){
                if (sqlFile.startsWith(UPGRADE_QUERY_PREFIX)){
                    int fileVersion = Integer.parseInt(sqlFile.substring(UPGRADE_QUERY_PREFIX.length(),sqlFile.length()-UPGRADE_QUERY_SUFFIX.length()));
                    if (fileVersion > oldVersion && fileVersion <= newVersion){
                        execSqlFile(sqlFile,db);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // execSqlFile function create the all the tables in the database by executing
    // each sql create statement written in 'create.sql' file
    // WHICH IS IN DIRECTORY  --->  assets/sql/create.sql
    protected void execSqlFile(String sqlFile, SQLiteDatabase db) throws SQLException, IOException {
        Log.i(Constants.TAG, "  executing sql file: {}" + sqlFile);
        // here parseSqlFile() function returns query one by one
        for (String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR+"/"+sqlFile, mContext.getAssets())) {
            Log.v(Constants.TAG, " execute sql>: {}" + sqlInstruction);
            try {
                db.execSQL(sqlInstruction);
            } catch (Exception e) {
                Log.e(Constants.TAG, "Error executing command: " + sqlInstruction, e);
            }
        }
    }


    /**
     * Queries for Members Table
     * */

    // member_id, name, phone_number,email_id

    // addMembers
    public Long addMember(Member member){
        SQLiteDatabase db = getDatabase(true);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MEMBER_ID, member.getMemberId());
        initialValues.put(KEY_MEMBER_NAME, member.getMemberName());
        initialValues.put(KEY_MEMBER_PHONE_NUMBER, member.getMemberPhoneNumber());
        initialValues.put(KEY_MEMBER_EMAIL_ID, member.getMemberEmailId());
        initialValues.put(KEY_MEMBER_GENDER, member.getMemberGender());
        initialValues.put(KEY_MEMBER_IMAGE_PATH, member.getMemberImagePath());
        initialValues.put(KEY_MEMBER_IMAGE_NAME, member.getMemberImageName());
        initialValues.put(KEY_MEMBER_IMAGE_FILE, member.getMemberImageFile());
        initialValues.put(KEY_MEMBER_FAV, member.getMemberFav());
        return db.insert(TABLE_MEMBERS, null, initialValues);
    }

    // fetchMembers
    public Cursor fetchAllMembers(){
        SQLiteDatabase db = getDatabase(true);
        Cursor wrapped  = db.query(TABLE_MEMBERS,new String[]{KEY_MEMBER_ID, KEY_MEMBER_NAME,
                KEY_MEMBER_PHONE_NUMBER, KEY_MEMBER_EMAIL_ID, KEY_MEMBER_GENDER, KEY_MEMBER_IMAGE_PATH,
                KEY_MEMBER_IMAGE_PATH, KEY_MEMBER_IMAGE_FILE, KEY_MEMBER_FAV},null,null,null,null, KEY_MEMBER_NAME + " ASC");
       // Log.e("cursor returned", wrapped.toString());
        return wrapped;
    }

    // fetchMembers
    public List<Member> getAllMembers(){
        List<Member> members = new ArrayList<>();
        SQLiteDatabase db = getDatabase(true);
        Cursor cursor  = db.query(TABLE_MEMBERS,new String[]{KEY_MEMBER_ID, KEY_MEMBER_NAME,
                KEY_MEMBER_PHONE_NUMBER, KEY_MEMBER_EMAIL_ID,
                KEY_MEMBER_GENDER, KEY_MEMBER_IMAGE_NAME,
                KEY_MEMBER_IMAGE_PATH, KEY_MEMBER_IMAGE_FILE,
                KEY_MEMBER_FAV},null,null,null,null, KEY_MEMBER_NAME + " ASC");

        if (cursor.moveToFirst()){
            do{
                String member_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_ID));
                String member_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_NAME));
                // Log.v("Member Name ", member_name);
                String member_phone_number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_PHONE_NUMBER));
                String member_email_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_EMAIL_ID));
                String member_gender = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_GENDER));
                String member_image_path = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_IMAGE_PATH));
                String member_image_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_IMAGE_NAME));
                byte[] member_image_file = null; //cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_IMAGE_FILE));
                int member_fav = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_FAV));
                Member member = new Member(member_id,member_name,member_phone_number,member_email_id,member_gender,member_image_path,member_image_name,member_image_file,member_fav);
                members.add(member);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }

    // updateMembers
    public boolean updateMember(Member member){
        SQLiteDatabase db = getDatabase(true);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MEMBER_ID, member.getMemberId());
        initialValues.put(KEY_MEMBER_NAME, member.getMemberName());
        initialValues.put(KEY_MEMBER_PHONE_NUMBER, member.getMemberPhoneNumber());
        initialValues.put(KEY_MEMBER_EMAIL_ID, member.getMemberEmailId());
        initialValues.put(KEY_MEMBER_GENDER, member.getMemberGender());
        initialValues.put(KEY_MEMBER_IMAGE_PATH, member.getMemberImagePath());
        initialValues.put(KEY_MEMBER_IMAGE_NAME, member.getMemberImageName());
        initialValues.put(KEY_MEMBER_IMAGE_FILE, member.getMemberImageFile());
        initialValues.put(KEY_MEMBER_FAV, member.getMemberFav());
        db.update(TABLE_MEMBERS, initialValues, KEY_MEMBER_ID + "=" + member.getMemberId(), null);
        return true;
    }

    // deleteMembers
    public boolean deleteMembers(String memberId) {
        SQLiteDatabase db = getDatabase(true);
        return db.delete(TABLE_MEMBERS, KEY_MEMBER_ID + "=" + memberId, null) > 0;
    }



    /**
     * Queries for Policies Table
     * */
    // addNewPolicy
    // updatePolicy
    // deletePolicy
    // fetchPolicies
    // policy_id TEXT PRIMARY KEY, name TEXT,number TEXT, doc_date TEXT,
    // dlp_date TEXT, dom_date TEXT,dob_date TEXT, term TEXT,mode TEXT,
    // sum_assured_amount TEXT, premium_amount TEXT,nominee_name TEXT,
    // marked INTEGER DEFAULT 0,book_marked INTEGER DEFAULT 0, policy_status INTEGER DEFAULT 5,
    public Long addNewPolicy(Policy policy){
        SQLiteDatabase db = getDatabase(true);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MEMBER_ID, policy.getMemberId());
        initialValues.put(KEY_POLICY_NAME, policy.getPolicyName());
        initialValues.put(KEY_POLICY_NUMBER, policy.getPolicyNumber());
        initialValues.put(KEY_POLICY_DOC_DATE, policy.getDobDate());
        initialValues.put(KEY_POLICY_DLP_DATE, policy.getDlpDate());
        initialValues.put(KEY_POLICY_DOM_DATE, policy.getDomDate());
        initialValues.put(KEY_POLICY_DOB_DATE, policy.getDobDate());
        initialValues.put(KEY_POLICY_MODE, policy.getMode());
        initialValues.put(KEY_POLICY_TERM, policy.getTermTable());
        initialValues.put(KEY_POLICY_SUM_ASSURED_AMOUNT, policy.getSaAmount());
        initialValues.put(KEY_POLICY_PREMIUM_AMOUNT, policy.getPremiumAmount());
        initialValues.put(KEY_POLICY_NOMINEE_NAME, policy.getNomineeName());
        initialValues.put(KEY_POLICY_MARKED, policy.getMarked());
        initialValues.put(KEY_POLICY_BOOK_MARKED, policy.getBookMarked());
        initialValues.put(KEY_POLICY_STATUS, policy.getPolicyStatus());
        return db.insert(TABLE_POLICIES, null, initialValues);
    }

    // fetch all policies
    public List<Policy> getAllPolicies(){
        List<Policy>  policies = new ArrayList<>();
        SQLiteDatabase db = getDatabase(true);
        Cursor cursor = db.query(TABLE_POLICIES, new String[]{
                KEY_MEMBER_ID, KEY_POLICY_ID, KEY_POLICY_NAME, KEY_POLICY_NUMBER,
                KEY_POLICY_DOC_DATE, KEY_POLICY_DLP_DATE, KEY_POLICY_DOM_DATE,
                KEY_POLICY_DOB_DATE, KEY_POLICY_FUP, KEY_POLICY_TERM, KEY_POLICY_MODE,
                KEY_POLICY_SUM_ASSURED_AMOUNT, KEY_POLICY_PREMIUM_AMOUNT, KEY_POLICY_NOMINEE_NAME,
                KEY_POLICY_MARKED, KEY_POLICY_BOOK_MARKED, KEY_POLICY_STATUS},null,null,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                String member_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MEMBER_ID));
                String policy_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_ID));
                String policy_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_NAME));
                String policy_number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_NUMBER));
                String policy_docdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_DOC_DATE));
                String policy_dlpdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_DLP_DATE));
                String policy_domdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_DOM_DATE));
                String policy_dobdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_DOB_DATE));
                String policy_fup = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_FUP));
                String policy_term = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_TERM));
                String policy_mode = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_MODE));
                String policy_sa_amount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_SUM_ASSURED_AMOUNT));
                String policy_premium_amount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_PREMIUM_AMOUNT));
                String policy_nominee_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_NOMINEE_NAME));
                int policy_marked = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_MARKED));
                int policy_bookmarked = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_BOOK_MARKED));
                int policy_status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_POLICY_STATUS));
                Policy policy = new
                        Policy(member_id,policy_id,policy_name,
                        policy_number,policy_docdate,policy_dlpdate,
                        policy_domdate,policy_dobdate,policy_fup,policy_term,policy_mode,
                        policy_sa_amount,policy_premium_amount,policy_nominee_name,
                        policy_marked,policy_bookmarked,policy_status);
                policies.add(policy);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return policies;
    }

    public boolean updatePolicy(Policy policy, String policy_id){
        SQLiteDatabase db = getDatabase(true);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MEMBER_ID, policy.getMemberId());
        initialValues.put(KEY_POLICY_NAME, policy.getPolicyName());
        initialValues.put(KEY_POLICY_NUMBER, policy.getPolicyNumber());
        initialValues.put(KEY_POLICY_DOC_DATE, policy.getDobDate());
        initialValues.put(KEY_POLICY_DLP_DATE, policy.getDlpDate());
        initialValues.put(KEY_POLICY_DOM_DATE, policy.getDomDate());
        initialValues.put(KEY_POLICY_DOB_DATE, policy.getDobDate());
        initialValues.put(KEY_POLICY_MODE, policy.getMode());
        initialValues.put(KEY_POLICY_TERM, policy.getTermTable());
        initialValues.put(KEY_POLICY_SUM_ASSURED_AMOUNT, policy.getSaAmount());
        initialValues.put(KEY_POLICY_PREMIUM_AMOUNT, policy.getPremiumAmount());
        initialValues.put(KEY_POLICY_NOMINEE_NAME, policy.getNomineeName());
        initialValues.put(KEY_POLICY_MARKED, policy.getMarked());
        initialValues.put(KEY_POLICY_BOOK_MARKED, policy.getBookMarked());
        initialValues.put(KEY_POLICY_STATUS, policy.getPolicyStatus());
        db.update(TABLE_POLICIES, initialValues, KEY_POLICY_ID + "=" + policy_id, null);
        return true;
    }

    public boolean deletePolicy(String policy_id){
        SQLiteDatabase db = getDatabase(true);
        return db.delete(TABLE_POLICIES, KEY_POLICY_ID + "=" + policy_id, null) > 0;
    }



    /**
     * Queries for Notes Table
     * */
    // addNewNote
    // updateNote
    // deleteNote
    // fetchNotes

    public Long addNewNote(Note note) {
        SQLiteDatabase db = getDatabase(true);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOTE_ID, note.getNoteId());
        initialValues.put(KEY_NOTE_TITLE, note.getNoteTitle());
        initialValues.put(KEY_NOTE_CONTENT,note.getNoteContents());
        initialValues.put(KEY_NOTE_TIME_CREATED, note.getTimeCreated());
        initialValues.put(KEY_NOTE_DATE_CREATED, note.getDateCreated());
        initialValues.put(KEY_NOTE_LAST_MODIFICATION, note.getLastModification());
        return db.insert(TABLE_NOTES, null, initialValues);
    }

    public List<Note> fetchAllNotes(){

        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getDatabase(true);
        Cursor cursor  = db.query(TABLE_NOTES,new String[]{KEY_NOTE_ID, KEY_NOTE_TITLE,
                KEY_NOTE_CONTENT, KEY_NOTE_TIME_CREATED, KEY_NOTE_DATE_CREATED,
                KEY_NOTE_LAST_MODIFICATION},null,null,null,null,null);

        if (cursor.moveToFirst()){
            do{
                String note_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOTE_ID));
                String note_title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOTE_TITLE));
                String note_contents = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOTE_CONTENT));
                String note_time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOTE_TIME_CREATED));
                String note_date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOTE_DATE_CREATED));
                String note_last_modification = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NOTE_LAST_MODIFICATION));
                Note note = new Note(note_id, note_title, note_contents, note_time, note_date, note_last_modification);
                notes.add(note);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public boolean updateNote(Note note, Long note_id){
        SQLiteDatabase db = getDatabase(true);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOTE_ID, note.getNoteId());
        initialValues.put(KEY_NOTE_TITLE, note.getNoteTitle());
        initialValues.put(KEY_NOTE_CONTENT,note.getNoteContents());
        initialValues.put(KEY_NOTE_TIME_CREATED, note.getTimeCreated());
        initialValues.put(KEY_NOTE_DATE_CREATED, note.getDateCreated());
        initialValues.put(KEY_NOTE_LAST_MODIFICATION, note.getLastModification());
        db.update(TABLE_NOTES, initialValues, KEY_MEMBER_ID + "=" + note_id, null);
        return true;
    }

    public boolean deleteNote(String note_id){
        SQLiteDatabase db = getDatabase(true);
        return db.delete(TABLE_NOTES, KEY_NOTE_ID + "=" + note_id, null) > 0;
    }


}

/*
    public boolean checkIfPhoneNumberExist(String phone_number){

            Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE PHONE_NUMBER =?", new String[]{phone_number});
            if (mCursor != null)
                return true; */
                          /* record exist *//*
            else
                return false; */
                         /* record not exist *//*

    }

     public static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + "("
            + KEY_SERIAL_NO + " integer primary key autoincrement, "
            + KEY_MEMBER_NAME + " text not null, "
            + KEY_PHONE_NUMBER + " text not null, "
            + KEY_EMAIL_ID + " text, "
            + KEY_POLICY_NAME + " text, "
            + KEY_POLICY_NUMBER + " text, "
            + KEY_DOC + " text ,"
            + KEY_DLP + " text ,"
            + KEY_DOM + " text ,"
            + KEY_DOB + " text ,"
            + KEY_TERM_TABLE + " text ,"
            + KEY_SA_AMOUNT + " text ,"
            + KEY_MODE + " text ,"
            + KEY_PREMIUM_AMOUNT + " text ,"
            + KEY_NOMINEE_NAME + " text);";
*/
