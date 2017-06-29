package com.yashkakkar.licagentdiary.async;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.yashkakkar.licagentdiary.async.eventbus.IsWhatsAppNumberEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Yash Kakkar on 28-06-2017.
 */

public class IsWhatsAppNumber extends AsyncTask<String,Void,Boolean>{

    private Context context;
    private boolean isWhatsAppNumber = false;
    private String whatsAppId;
    // ArrayList for Store Whatsapp Contact
    // private ArrayList<String> myWhatsappContacts = new ArrayList<>();

    public IsWhatsAppNumber(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String phoneNumber = strings[0];
        //This class provides applications access to the content model.
        ContentResolver cr = context.getContentResolver();

        //RowContacts for filter Account Types
        Cursor contactCursor = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID,
                        ContactsContract.RawContacts.CONTACT_ID},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

        if (contactCursor != null) {
            if (contactCursor.getCount() > 0) {
                if (contactCursor.moveToFirst()) {
                    do {
                        //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                        String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                        if (whatsappContactId != null) {
                            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                            Cursor whatsAppContactCursor = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{whatsappContactId}, null);

                            if (whatsAppContactCursor != null) {
                                whatsAppContactCursor.moveToFirst();

                                String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                /* String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                */
                                String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                whatsAppContactCursor.close();

                                //Add Number to ArrayList
                                //myWhatsappContacts.add(number);

                                // Log.v("WhatsApp"," WhatsApp contact id  :  "+ id);
                                // Log.v("WhatsApp"," WhatsApp contact name :  "+ name);
                                // Log.v("WhatsApp"," WhatsApp contact number :  "+ number);

                                if (phoneNumber.contains(number)) {
                                    isWhatsAppNumber = true;
                                    whatsAppId = id;
                                    Log.v("WhatsApp"," WhatsApp contact id  :  "+ id);
                                }
                            }
                        }
                    } while (contactCursor.moveToNext());
                    contactCursor.close();
                }
            }
        }
        //Log.v("WhatsApp", " WhatsApp contact size :  " + myWhatsappContacts.size());
        return isWhatsAppNumber;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        EventBus.getDefault().post(new IsWhatsAppNumberEvent(aBoolean, whatsAppId));
    }

}
