package com.yashkakkar.licagentdiary;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.UUID;

import com.yashkakkar.licagentdiary.async.eventbus.PolicyCreatedEvent;
import com.yashkakkar.licagentdiary.async.policy.SaveNewPolicyTask;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.models.Policy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateNewPolicy extends AppCompatActivity {


    @BindView(R.id.create_new_member)
    TextView createNewPolicyHolder;
    @BindView(R.id.select_existing_policy_holder)
    TextView selectExistingPolicyHolder;
    @BindView(R.id.layout_selected_member)
    LinearLayout linearLayout;

    @BindView(R.id.selected_member_name)
    EditText selectionPolicyHolderName;
    @BindView(R.id.seleced_profile_pic)
    ImageView memberImage;

    @BindView(R.id.policyName)
    EditText policyName;
    @BindView(R.id.policyNumber)
    EditText policyNumber;
    @BindView(R.id.docDate)
    EditText doc;
    @BindView(R.id.dlpDate)
    EditText dlp;
    @BindView(R.id.domDate)
    EditText dom;
    @BindView(R.id.dobDate)
    EditText dob;
    @BindView(R.id.tableTerm)
    EditText termTable;
    @BindView(R.id.saAmount)
    EditText saAmount;
    @BindView(R.id.mode)
    Spinner mode;
    @BindView(R.id.premiumAmount)
    EditText premiumAmount;
    @BindView(R.id.nomineeName)
    EditText nomineeName;
    Unbinder unbinder;

    private java.text.SimpleDateFormat dateFormat;
    private DatePickerDialog docDatePickerDialog;
    private DatePickerDialog dlpDatePickerDialog;
    private DatePickerDialog domDatePickerDialog;
    private DatePickerDialog dobDatePickerDialog;


    private final int REQUEST_TO_CREATE_NEW_MEMBER = 1;
    private final int REQUEST_TO_SELECT_MEMBER = 0;

    private Member selectedMember;
    private Policy policy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_policy);
        unbinder = ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            selectedMember = savedInstanceState.getParcelable("selectedMember");
            Toast.makeText(this, "Selected policy holder " + selectedMember.getMemberName(), Toast.LENGTH_SHORT).show();
        }

        // go to create new member activity
        createNewPolicyHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewPolicy.this, CreateNewMember.class);
                startActivityForResult(intent, REQUEST_TO_CREATE_NEW_MEMBER);
            }
        });

        // select item from list of members
        selectExistingPolicyHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize our fragment object with new instance
                Intent intent = new Intent(CreateNewPolicy.this, SelectMember.class);
                startActivityForResult(intent, REQUEST_TO_SELECT_MEMBER);
            }
        });
        /*
        * This Activity has 2 fragments -->
        * 1. select a member from a member list returning member id/member name associated with it
        *    then add new policy to existing member
        *    --> and go to Member/policyholder profile activity
        *
        * 2. add policy to new member ---> launch create new member activity
        *
        * */

        /*
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!= null){
           userId = user.getEmail();
        }
        // get user offline first
        mRef = FirebaseDatabase.getInstance().getReference("users");
        // saving any new member to an existing user /users/$userId/members/$memberId/
        // here $memberId is "phone number"
        // phone number should be unique
        // each member have several policies
        // /user/$userId/members/$memberId/$policyId/
        // policy id for a member is in "p1,p2,p3...." etc
        */

        dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.US);

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            setDateTimeField();
        }

        if (selectedMember!= null){
            selectionPolicyHolderName.setText(selectedMember.getMemberName());
        }
    }



    private void setDateTimeField() {
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == doc) {
                    docDatePickerDialog.show();
                }
            }
        });
        dlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == dlp) {
                    dlpDatePickerDialog.show();
                }
            }
        });
        dom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == dom) {
                    domDatePickerDialog.show();
                }
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == dob) {
                    dobDatePickerDialog.show();
                }
            }
        });

        java.util.Calendar newCalendar = java.util.Calendar.getInstance();
        docDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                doc.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        dlpDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dlp.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        domDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dom.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TO_CREATE_NEW_MEMBER:
                    break;
                case REQUEST_TO_SELECT_MEMBER:
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_new_policy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create_policy) {
            // check for the values entered correctly


            preparePolicyData();
            // set all the values and create object of policy

            // Save the policy with member_id using async task
            SaveNewPolicyTask saveNewPolicyTask = new SaveNewPolicyTask(this, policy);
            saveNewPolicyTask.execute();


        }

        return super.onOptionsItemSelected(item);
    }

    private void preparePolicyData() {
        policy = new Policy();
        String policy_id, policy_name, policy_number, doc_date, dlp_date, dom_date, dob_date, fup_date,
                term_table, m, sum_assured_amt, premium_amt, nominee_name;

        // setting member id
        policy.setMemberId(selectedMember.getMemberId());
        // Create unique new policyId
        policy_id = generateUniqueKeyUsingUUID();
        policy_name = policyName.getText().toString().trim();
        policy_number = policyNumber.getText().toString().trim();
        doc_date = doc.getText().toString().trim();
        dlp_date = dlp.getText().toString().trim();
        dom_date = dom.getText().toString().trim();
        dob_date = dob.getText().toString().trim();
        //fup_date = fup.getText().toString().trim();
        term_table = termTable.getText().toString().trim();
        m = mode.getSelectedItem().toString();
        sum_assured_amt = saAmount.getText().toString().trim();
        premium_amt = premiumAmount.getText().toString().trim();
        nominee_name = nomineeName.getText().toString().trim();





        policy.setPolicyId(policy_id);
        policy.setPolicyName(policy_name);
        policy.setPolicyNumber(policy_number);
        policy.setDocDate(doc_date);
        policy.setDlpDate(dlp_date);
        policy.setDomDate(dom_date);
        policy.setDobDate(dob_date);
        policy.setMode(m);
        policy.setTermTable(term_table);
        //policy.setFup();
        policy.setSaAmount(sum_assured_amt);
        policy.setPremiumAmount(premium_amt);
        policy.setNomineeName(nominee_name);
        policy.setMarked(0);
        policy.setBookMarked(0);
        policy.setPolicyStatus(5);
    }

    private String generateUniqueKeyUsingUUID() {
        // Static factory to retrieve a type 4 (pseudo randomly generated) UUID
        return UUID.randomUUID().toString();
    }


    @Subscribe
    public void onEvent(PolicyCreatedEvent event){
              if (event.getRowId()>0){
                  // policy created
                  // Show a Toast message and go to MemberProfileView class
                  Toast.makeText(this,"Policy Created Successfully!", Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(this,MemberProfileView.class);
                  // send the member and policy data to MemberProfile Activity
                  startActivity(intent);
                  finish();
              }else {
                  // something went wrong
                  Toast.makeText(this,"Not able to create policy!", Toast.LENGTH_SHORT).show();
                  finish();
              }
    }
}
