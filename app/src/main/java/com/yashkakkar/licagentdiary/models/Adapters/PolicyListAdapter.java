package com.yashkakkar.licagentdiary.models.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.yashkakkar.licagentdiary.MemberProfileView;
import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.models.Policy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yash Kakkar on 07-06-2017.
 */

public class PolicyListAdapter extends RecyclerView.Adapter<PolicyListAdapter.MyViewHolder> {

    private Context context;
    private List<Member> members;
    private List<Policy> policies;
    private LayoutInflater inflater;

    public PolicyListAdapter(Context context, List<Member> members, List<Policy> policies) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.members = members;
        this.policies = policies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.policy_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //final Member member = new Member();
        final Policy policy = policies.get(position);
        String member_id = policy.getMemberId();
        for (final Member member : members){
            if (member.getMemberId()!= null && member.getMemberId().contains(member_id)){
                // set member data
                holder.memberName.setText(member.getMemberName());
                // set the profile image
                int resId = context.getResources().getIdentifier("ic_member_profile_pic","drawable",context.getPackageName());
                holder.memberProfileIcon.setImageResource(resId);
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MemberProfileView.class);
                        // sending member and policy details
                        intent.putExtra("selectedMember",(Parcelable) member);
                        intent.putExtra("selectedPolicy",(Parcelable) policy);
                        context.startActivity(intent);
                    }
                });
                break;
            }
            holder.policyName.setText(policy.getPolicyName());
            holder.policyNumber.setText(policy.getPolicyNumber());
            holder.docDate.setText(policy.getDocDate());
            holder.dlpDate.setText(policy.getDlpDate());
            holder.domDate.setText(policy.getDomDate());
            holder.mode.setText(policy.getMode());
            holder.term.setText(policy.getTermTable());
            holder.saAmt.setText("₹ "+policy.getSaAmount());
            holder.premiumAmt.setText("₹ "+policy.getPremiumAmount());
            holder.status.setText(String.valueOf(policy.getPolicyStatus()));
            holder.dob.setText(policy.getDobDate());
            holder.age.setText("("+String.valueOf(policy.calculateAgeFromDob(policy.getDobDate()))+")");
        }

    }

    @Override
    public int getItemCount() {
        return policies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.policy_row_profile_icon) SelectableRoundedImageView memberProfileIcon;
        @BindView(R.id.policy_row_member_name) TextView memberName;
        @BindView(R.id.policy_row_age) TextView age;
        @BindView(R.id.policy_row_dobdate) TextView dob;
        @BindView(R.id.policy_row_policy_name) TextView policyName;
        @BindView(R.id.policy_row_policy_number) TextView policyNumber;
        @BindView(R.id.policy_row_docdate) TextView docDate;
        @BindView(R.id.policy_row_dlpdate) TextView dlpDate;
        @BindView(R.id.policy_row_domdate) TextView domDate;
        @BindView(R.id.policy_row_mode) TextView mode;
        @BindView(R.id.policy_row_term) TextView term;
        @BindView(R.id.policy_row_sa_amount) TextView saAmt;
        @BindView(R.id.policy_row_premium_amount) TextView premiumAmt;
        @BindView(R.id.policy_row_status) TextView status;

        @BindView(R.id.profile_row_relative_layout) RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
