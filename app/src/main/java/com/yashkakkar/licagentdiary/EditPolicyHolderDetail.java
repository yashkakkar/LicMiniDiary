package com.yashkakkar.licagentdiary;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.yashkakkar.licagentdiary.models.Member;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditPolicyHolderDetail extends AppCompatActivity {

    Member member;

    TextView person_name_edit_heading;
    EditText personName;
    EditText phoneNumber;
    EditText emailId;
    EditText policyName;
    EditText policyNumber;
    EditText docDate;
    EditText dlpDate;
    EditText domDate;
    EditText dobDate;
    EditText termTable;
    Spinner mode;
    EditText saAmt;
    EditText pAmt;
    EditText nomineeName;

    long rowId;

    private SimpleDateFormat dateFormat;
    private DatePickerDialog docDatePickerDialog;
    private DatePickerDialog dlpDatePickerDialog;
    private DatePickerDialog domDatePickerDialog;
    private DatePickerDialog dobDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_policy_holder_detail);

        savedInstanceState  = getIntent().getExtras();
       if (savedInstanceState!= null){
           rowId = savedInstanceState.getLong("Id");
       }
        // Get data from database
       // membersDbAdapter = new DatabaseAdapter(this);
       // membersDbAdapter.open();

       // memberCursor = membersDbAdapter.fetchMember(rowId);

      /*  if(memberCursor.isFirst()){
            long serial_no = memberCursor.getLong(memberCursor.getColumnIndex(membersDbAdapter.KEY_SERIAL_NO));
            String member_name = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_MEMBER_NAME));
            String phone_number = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_PHONE_NUMBER));
            String email_id = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_EMAIL_ID));
            // Log.v("Member Name ", memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_MEMBER_NAME)));
            String policy_name = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_POLICY_NAME));
            String policy_no = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_POLICY_NUMBER));
            String doc = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_DOC));
            String dlp = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_DLP));
            String dom = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_DOM));
            String dob = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_DOB));
            String term_table = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_TERM_TABLE));
            int sa_amount = memberCursor.getInt(memberCursor.getColumnIndex(membersDbAdapter.KEY_SA_AMOUNT));
            String mode = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_MODE));
            int premium_amount = memberCursor.getInt(memberCursor.getColumnIndex(membersDbAdapter.KEY_PREMIUM_AMOUNT));
            String nominee_name = memberCursor.getString(memberCursor.getColumnIndex(membersDbAdapter.KEY_NOMINEE_NAME));
            member = new Member(serial_no, member_name, phone_number, email_id, policy_name, policy_no, doc, dlp, dom, dob, term_table, sa_amount, mode, premium_amount, nominee_name);
        }
        memberCursor.close();
        String pName = member.nameOfPerson;
        findViewsById();
        person_name_edit_heading.setText(pName);
        personName.setText(pName);
        personName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                person_name_edit_heading.setText(editable);
            }
        });

        phoneNumber.setText(member.phoneNumber);
        emailId.setText(member.eMailId);
        policyName.setText(member.policyName);
        policyNumber.setText(String.valueOf(member.policyNumber));
        docDate.setText(member.docDate);
        dlpDate.setText(member.dlpDate);
        domDate.setText(member.dlpDate);
        dobDate.setText(member.dobDate);
        termTable.setText(member.termTable);
        mode.setSelection(((ArrayAdapter<String>)mode.getAdapter()).getPosition(member.mode));

        *//*   ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mode_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mode.setSelection(spinnerPosition);
        }*//*
        saAmt.setText(String.valueOf(member.saAmount));
        pAmt.setText(String.valueOf(member.premiumAmount));
        nomineeName.setText(member.nomineeName);

     */   dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.US);
        setDateTimeField();
    }

    private void findViewsById() {
        person_name_edit_heading = (TextView) findViewById(R.id.policy_holder_name_edit_heading);
        personName = (EditText) findViewById(R.id.policy_holder_name_edit);
        phoneNumber = (EditText) findViewById(R.id.phone_number_edit);
        emailId = (EditText) findViewById(R.id.email_edit);
        policyName = (EditText) findViewById(R.id.policy_name_edit);
        policyNumber = (EditText) findViewById(R.id.policy_number_edit);
        docDate = (EditText) findViewById(R.id.doc_date_edit);
        dlpDate = (EditText) findViewById(R.id.dlp_date_edit);
        domDate = (EditText) findViewById(R.id.dom_date_edit);
        dobDate = (EditText) findViewById(R.id.dob_date_edit);
        termTable = (EditText) findViewById(R.id.term_table_edit);
        mode = (Spinner) findViewById(R.id.mode_edit);
        saAmt = (EditText) findViewById(R.id.sa_amt_edit);
        pAmt = (EditText) findViewById(R.id.premium_edit);
        nomineeName = (EditText) findViewById(R.id.nominee_name_edit);
    }

    private void setDateTimeField() {
        docDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == docDate) {
                    docDatePickerDialog.show();
                }
            }
        });
        dlpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == dlpDate) {
                    dlpDatePickerDialog.show();
                }
            }
        });
        domDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == domDate) {
                    domDatePickerDialog.show();
                }
            }
        });
        dobDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == dobDate) {
                    dobDatePickerDialog.show();
                }
            }
        });
        java.util.Calendar newCalendar = java.util.Calendar.getInstance();
        docDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                docDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        dlpDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dlpDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        domDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                domDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dobDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_activity_menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update){

            // Update the existing member details & go back to previous activity
            // Get new values from user and update it
            final String m_Name,phone_no, e_id, p_Name, p_No, doc_Date, dlp_Date, dom_Date, dob_Date, t_T, m, sa_Amt, p_Amt, n_Name;
            m_Name = personName.getText().toString();
            phone_no = phoneNumber.getText().toString();
            e_id = emailId.getText().toString();
            p_Name = policyName.getText().toString();
            p_No = policyNumber.getText().toString();
            doc_Date = docDate.getText().toString();
            dlp_Date = dlpDate.getText().toString();
            dom_Date = domDate.getText().toString();
            dob_Date = dobDate.getText().toString();
            t_T = termTable.getText().toString();
            m = mode.getSelectedItem().toString();
            sa_Amt = saAmt.getText().toString();
            p_Amt = pAmt.getText().toString();
            n_Name = nomineeName.getText().toString();

           /* Log.v("Name : ",m_Name);
            Log.v("Policy Name : ",p_Name);
            Log.v("Policy Number : ",p_No);
            Log.v("doc : ",doc_Date);
            Log.v("dlp : ",dlp_Date);
            Log.v("dom : ",dom_Date);
            Log.v("dob : ",dob_Date);
            Log.v("term table : ",t_T);
            Log.v("mode : ",m);
            Log.v("sa amount : ",sa_Amt);
            Log.v("premium amt : ",p_Amt);
            Log.v("nominee name : ",n_Name);
           */ // Ask before doing update members
            AlertDialog.Builder alert = new AlertDialog.Builder(EditPolicyHolderDetail.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Are you sure you want to update this member ?");

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                 //   boolean flag = membersDbAdapter.updateMember(rowId,m_Name,phone_no,e_id,p_Name,p_No,doc_Date,dlp_Date,dom_Date,dob_Date,t_T,sa_Amt,m,p_Amt,n_Name);
                  /*  if (flag == true){
                        // go back to previous view activity
                        // show Toast message that update is done
                        Toast.makeText(EditPolicyHolderDetail.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                        Intent viewScreen = new Intent(EditPolicyHolderDetail.this,ViewPolicyHolderDetail.class);
                        viewScreen.putExtra("Id",rowId);
                        startActivity(viewScreen);
                        finish();
                    }else {
                        Toast.makeText(EditPolicyHolderDetail.this,"Unable to update",Toast.LENGTH_SHORT).show();
                    }*/

                    dialogInterface.dismiss();
                }

            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alert.show();
        }


        if (id == R.id.action_delete){
            // delete the existing member & go back to List activity & update the RecyclerView list
            // delete current member with serial id  & go back to previous activity and refresh the RecyclerView list data
            // Alert the user before deleting the entity
            AlertDialog.Builder alert = new AlertDialog.Builder(EditPolicyHolderDetail.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Are you sure you want to delete this member ?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   /* boolean flag  = membersDbAdapter.deleteMembers(rowId);
                    if(flag == true){
                        // go back to previous activity and update RecyclerView List
                        Intent intent = new Intent(EditPolicyHolderDetail.this,ActivityDashboard.class);
                        startActivity(intent);
                        Toast.makeText(EditPolicyHolderDetail.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(EditPolicyHolderDetail.this,"Unable to delete",Toast.LENGTH_SHORT).show();
                    }*/
                    dialogInterface.dismiss();
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alert.show();
        }

        if(id == R.id.action_discard_changes){
            // go back to view detail activity without editing any entity
            Intent intent = new Intent(EditPolicyHolderDetail.this,ActivityDashboard.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
       // memberCursor.close();
      //  membersDbAdapter.close();
        super.onDestroy();
    }
}
