package com.yashkakkar.licagentdiary;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.yashkakkar.licagentdiary.models.Policy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewPolicy extends AppCompatActivity {

    @BindView(R.id.policyNameView) TextView policyNameView;
    @BindView(R.id.policyNumberView) TextView policyNumberView;
    @BindView(R.id.policyDocDateView) TextView policyDocDateView;
    @BindView(R.id.policyDobDateView) TextView policyDobDateView;
    @BindView(R.id.policyDomDateView) TextView policyDomDateView;
    @BindView(R.id.policyDlpDateView) TextView policyDlpDateView;
    @BindView(R.id.policyTermView) TextView policyTermView;
    @BindView(R.id.policySaAmtView) TextView policySaAmtView;
    @BindView(R.id.policyPremiumAmtView) TextView policyPremiumAmtView;
    @BindView(R.id.policyNomineeView) TextView policyNomineeView;
    @BindView(R.id.policyStatusView) TextView policyStatusView;
    @BindView(R.id.policyBookedMarkedView) TextView policyBookedMarkedView;

    private Policy policy;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_policy);
        unbinder = ButterKnife.bind(this);
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null){
         policy = savedInstanceState.getParcelable("selectedPolicy");
        }

        Log.v("policyname",policy.getPolicyName());
        // set existing policy Information
        if (policy!= null){
            policyNameView.setText(policy.getPolicyName());
            policyNumberView.setText(policy.getPolicyNumber());
            policyDocDateView.setText(policy.getDocDate());
            policyDobDateView.setText(policy.getDobDate());
            policyDomDateView.setText(policy.getDomDate());
            policyDlpDateView.setText(policy.getDlpDate());
            policyTermView.setText(policy.getTermTable());
            policySaAmtView.setText(policy.getSaAmount());
            policyPremiumAmtView.setText(policy.getPremiumAmount());
            policyNomineeView.setText(policy.getNomineeName());
           // policyStatusView.setText(policy.getPolicyStatus());
           // policyBookedMarkedView.setText(policy.getBookMarked());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_policy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_policy_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Policy Details ---> " +
                        "Policy Name: " + policy.getPolicyName() +
                        ", Number: #" + policy.getPolicyNumber() +
                        ", Date of commencement: " + policy.getDocDate() +
                        ", Date of Maturity: " + policy.getDomDate() +
                        ", Date of last payment: " + policy.getDlpDate() +
                        ", Date of birth: " + policy.getDobDate() +
                        ", Term (in year): " + policy.getTermTable() +
                        ", Sum Assured Amount: " + policy.getSaAmount() +
                        ", Premium Amount: " + policy.getPremiumAmount() +
                        ", Nominee Name: " + policy.getNomineeName() +".");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.action_policy_edit:
                Intent intentEdit = new Intent(this,EditPolicy.class);
                intentEdit.putExtra("selectedPolicy",(Parcelable) policy);
                startActivity(intentEdit);
                break;
            case R.id.action_policy_delete:

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
