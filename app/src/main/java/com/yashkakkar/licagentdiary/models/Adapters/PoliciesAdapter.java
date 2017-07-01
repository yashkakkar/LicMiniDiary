package com.yashkakkar.licagentdiary.models.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.models.Policy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yash Kakkar on 01-07-2017.
 */

public class PoliciesAdapter extends RecyclerView.Adapter<PoliciesAdapter.MyMemberPolicyViewHolder>{

    private Context context;
    private List<Policy> policies;
    private LayoutInflater inflater;

    public PoliciesAdapter(Context context, List<Policy> policies) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.policies = policies;
    }

    @Override
    public MyMemberPolicyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.member_policy_row, parent);
        return new MyMemberPolicyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyMemberPolicyViewHolder holder, int position) {
        Policy policy = policies.get(position);
        holder.policyName.setText(policy.getPolicyName());
        holder.policyNumber.setText(policy.getPolicyNumber());

        holder.saAmt.setText("₹ "+policy.getSaAmount());
        holder.premiumAmt.setText("₹ "+policy.getPremiumAmount());
        holder.mode.setText(policy.getMode());
        holder.docDate.setText(policy.getDobDate());
        holder.dlpDate.setText(policy.getDlpDate());
        holder.domDate.setText(policy.getDomDate());
        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                        .items(R.array.policy_menu)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch (position){
                                    case 0:// view

                                        break;
                                    case 1:// edit

                                        break;
                                    case 2:// share

                                        break;
                                    case 3:// delete

                                        break;
                                    default:

                                        break;
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return policies.size();
    }

    class MyMemberPolicyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.m_policy_name) TextView policyName;
        @BindView(R.id.m_menu) ImageButton cardMenu;
        @BindView(R.id.m_policy_number) TextView policyNumber;
        @BindView(R.id.m_doc_date) TextView docDate;
        @BindView(R.id.m_dlp_date) TextView dlpDate;
        @BindView(R.id.m_dom_date) TextView domDate;
        @BindView(R.id.m_sa_amt) TextView saAmt;
        @BindView(R.id.m_premium_amt) TextView premiumAmt;
        @BindView(R.id.m_mode) TextView mode;

        public MyMemberPolicyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
