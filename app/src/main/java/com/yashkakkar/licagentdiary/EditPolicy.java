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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yashkakkar.licagentdiary.async.eventbus.DeletePolicyEvent;
import com.yashkakkar.licagentdiary.async.eventbus.UpdatePolicyEvent;
import com.yashkakkar.licagentdiary.async.policy.UpdatePolicyTask;
import com.yashkakkar.licagentdiary.models.Policy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditPolicy extends AppCompatActivity {

    @BindView(R.id.policyNameEdit) EditText policyNameEdit;
    @BindView(R.id.policyNumberEdit) EditText policyNumberEdit;
    @BindView(R.id.docDateEdit) EditText policyDocDateEdit;
    @BindView(R.id.dobDateEdit) EditText policyDobDateEdit;
    @BindView(R.id.dlpDateEdit) EditText policyDlpDateEdit;
    @BindView(R.id.domDateEdit) EditText policyDomDateEdit;
    @BindView(R.id.policyTermEdit) EditText policyTermEdit;
    @BindView(R.id.modeEdit) Spinner policyModeEdit;
    @BindView(R.id.saAmountEdit) EditText policySumAssuredAmountEdit;
    @BindView(R.id.premiumAmountEdit) EditText policyPremiumAmountEdit;
    @BindView(R.id.nomineeNameEdit) EditText policyNomineeNameEdit;

    private Policy policy;
    private Policy policyEdit;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog docDatePickerDialog;
    private DatePickerDialog dlpDatePickerDialog;
    private DatePickerDialog domDatePickerDialog;
    private DatePickerDialog dobDatePickerDialog;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_edit_policy);
       unbinder = ButterKnife.bind(this);
       EventBus.getDefault().register(this);
       policyEdit = new Policy();
       savedInstanceState  = getIntent().getExtras();
       if (savedInstanceState!= null){
           policy = savedInstanceState.getParcelable("selectedPolicy");
       }


       // Set the policy data
        if (policy != null){
            policyNameEdit.setText(policy.getPolicyName());
            policyNumberEdit.setText(policy.getPolicyNumber());
            policyDocDateEdit.setText(policy.getDocDate());
            policyDlpDateEdit.setText(policy.getDlpDate());
            policyDomDateEdit.setText(policy.getDomDate());
            policyDobDateEdit.setText(policy.getDobDate());
            policyTermEdit.setText(policy.getTermTable());
            policyModeEdit.setSelection(((ArrayAdapter<String>)policyModeEdit.getAdapter()).getPosition(policy.getMode()));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mode_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            policyModeEdit.setAdapter(adapter);
            /*if (!compareValue.equals(null)) {
                int spinnerPosition = adapter.getPosition(compareValue);
                policyModeEdit.setSelection(spinnerPosition);
            }*/
            policySumAssuredAmountEdit.setText(policy.getSaAmount());
            policyPremiumAmountEdit.setText(policy.getPremiumAmount());
            policyNomineeNameEdit.setText(policy.getNomineeName());
        }
       dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.US);
        setDateTimeField();
    }

    private void setDateTimeField() {
        policyDocDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == policyDobDateEdit) {
                    docDatePickerDialog.show();
                }
            }
        });
        policyDlpDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == policyDlpDateEdit) {
                    dlpDatePickerDialog.show();
                }
            }
        });
        policyDomDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == policyDomDateEdit) {
                    domDatePickerDialog.show();
                }
            }
        });
        policyDobDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == policyDobDateEdit) {
                    dobDatePickerDialog.show();
                }
            }
        });
        java.util.Calendar newCalendar = java.util.Calendar.getInstance();
        docDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                policyDocDateEdit.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        dlpDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                policyDlpDateEdit.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        domDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                policyDomDateEdit.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(java.util.Calendar.YEAR),
                newCalendar.get(java.util.Calendar.MONTH),
                newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                policyDobDateEdit.setText(dateFormat.format(newDate.getTime()));
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

            // Update the existing policy details & go back to previous activity
            policyEdit.setMemberId(policy.getMemberId());
            policyEdit.setPolicyId(policy.getPolicyId());
            policyEdit.setPolicyName(policyNameEdit.getText().toString());
            policyEdit.setPolicyNumber(policyNumberEdit.getText().toString());
            policyEdit.setDocDate(policyDocDateEdit.getText().toString());
            policyEdit.setDlpDate(policyDlpDateEdit.getText().toString());
            policyEdit.setDomDate(policyDomDateEdit.getText().toString());
            policyEdit.setDobDate(policyDobDateEdit.getText().toString());
            policyEdit.setTermTable(policyTermEdit.getText().toString());
            policyEdit.setMode(policyModeEdit.getSelectedItem().toString());
            policyEdit.setSaAmount(policySumAssuredAmountEdit.getText().toString());
            policyEdit.setPremiumAmount(policyPremiumAmountEdit.getText().toString());
            policyEdit.setNomineeName(policyNomineeNameEdit.getText().toString());

            // Ask before doing update members
            AlertDialog.Builder alert = new AlertDialog.Builder(EditPolicy.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Are you sure you want to update this policy ?");

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // update policy
                    UpdatePolicyTask updatePolicyTask = new UpdatePolicyTask(EditPolicy.this,policyEdit);
                    updatePolicyTask.execute();
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
            AlertDialog.Builder alert = new AlertDialog.Builder(EditPolicy.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Are you sure you want to delete this member ?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

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
            Intent intent = new Intent(EditPolicy.this,ActivityDashboard.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(UpdatePolicyEvent event){
        // update policy
        if(event.isPolicyUpdated()){
            Toast.makeText(EditPolicy.this,"Policy Updated Successfully!",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(EditPolicy.this,"Unable to update!",Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent(DeletePolicyEvent event){
        if (event.isPolicyDeleted()){
            Toast.makeText(EditPolicy.this, "Policy deleted Successfully!",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(EditPolicy.this,"Unable to delete!",Toast.LENGTH_SHORT).show();
        }
    }

}
