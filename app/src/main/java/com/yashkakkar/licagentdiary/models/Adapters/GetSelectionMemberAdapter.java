package com.yashkakkar.licagentdiary.models.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joooonho.SelectableRoundedImageView;
import com.yashkakkar.licagentdiary.CreateNewPolicy;
import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.models.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class GetSelectionMemberAdapter extends RecyclerView.Adapter<GetSelectionMemberAdapter.MyViewHolder> implements Filterable{


    private Context context;
    private LayoutInflater inflater;
    private List<Member> members = Collections.emptyList();
    private List<Member> filterMemberList = Collections.emptyList();
    private MemberFilter memberFilter;

    public GetSelectionMemberAdapter(Context context, List<Member> members) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.members = members;
        this.filterMemberList = members;
    }

    @Override
    public GetSelectionMemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.member_row,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Member member = members.get(position);
        //Log.v("name ", member.getMemberName());
        holder.mName.setText(member.getMemberName());
        holder.mPhoneNumber.setText(member.getMemberPhoneNumber());
        holder.mEmail.setText(member.getMemberEmailId());

        int resId = context.getResources().getIdentifier("ic_member_profile_pic", "drawable", context.getPackageName());
        holder.mImage.setImageResource(resId);

        holder.mCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + member.getMemberPhoneNumber()));
                Toast.makeText(context, "Calling " + member.getMemberName(), Toast.LENGTH_SHORT).show();
                try {
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "this item clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,CreateNewPolicy.class);
                intent.putExtra("selectedMember",(Parcelable) member);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public Filter getFilter() {
        if (memberFilter == null)
            memberFilter = new MemberFilter(this, members);
        return memberFilter;
    }

    private static class MemberFilter extends Filter{

        private final GetSelectionMemberAdapter adapter;
        private final List<Member> orignalList;
        private final List<Member> filteredList;
        private MemberFilter(GetSelectionMemberAdapter adapter, List<Member> orignalList) {
            super();
            this.adapter = adapter;
            this.orignalList = new LinkedList<>(orignalList);
            this.filteredList = new ArrayList<>(orignalList);
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if(charSequence.length() == 0){
                filteredList.addAll(orignalList);
            }else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Member member : orignalList){

                    String name = member.getMemberName().toLowerCase();
                    if(name.contains(filterPattern)){
                        filteredList.add(member);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.filterMemberList.clear();
            adapter.filterMemberList.addAll((ArrayList<Member>) filterResults.values);
            adapter.notifyDataSetChanged();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.memberNameView) TextView mName;
        @BindView(R.id.memberEmailView) TextView mEmail;
        @BindView(R.id.memberPhoneNumberView) TextView mPhoneNumber;
        @BindView(R.id.memberImageView)
        SelectableRoundedImageView mImage;
        @BindView(R.id.callMeBtnView) ImageButton mCallBtn;
        @BindView(R.id.mainHorizontalLayout) LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}

