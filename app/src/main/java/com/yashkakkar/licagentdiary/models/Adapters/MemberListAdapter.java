package com.yashkakkar.licagentdiary.models.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
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
import com.yashkakkar.licagentdiary.MemberProfileView;
import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.utils.BitmapUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yash Kakkar on 22-03-2017.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    public List<Member> members = Collections.emptyList();
    public List<Member> selectedMembers = Collections.emptyList();
    private List<Member> filterMemberList = Collections.emptyList();
    private MemberFilter memberFilter;
    private Bitmap bitmap;
    private final static String PATH = "/LIC DIARY";

    public MemberListAdapter(Context context, List<Member> members) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.members = members;
        this.filterMemberList = members;
    }

    @Override
    public MemberListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.member_row,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MemberListAdapter.MyViewHolder holder, final int position) {
        final Member member = members.get(position);
       // Log.v("name ", member.getMemberName());
        holder.mName.setText(member.getMemberName());
        holder.mPhoneNumber.setText(member.getMemberPhoneNumber());
        holder.mEmail.setText(member.getMemberEmailId());

        bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+PATH+"/"+member.getMemberImageName()+".jpg");
        if (bitmap!= null) {

            BitmapUtility bitmapUtility = new BitmapUtility();
            bitmap = bitmapUtility.getScaledBitmap(bitmap, 300);
            holder.mImage.setImageBitmap(bitmap);

        }else {
            int resId = context.getResources().getIdentifier("ic_member_profile_pic", "drawable", context.getPackageName());
            holder.mImage.setImageResource(resId);
        }

        holder.mCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+member.getMemberPhoneNumber()));
                Toast.makeText(context,"Calling " + member.getMemberName(),Toast.LENGTH_SHORT).show();
                try{
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(context,"Could not find an activity to place the call.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(context, MemberProfileView.class);
                i.putExtra("selectedMember",(Parcelable) member);
                context.startActivity(i);
            /*
            Toast.makeText(context,"this item clicked",Toast.LENGTH_SHORT).show();
            */
            }
        });
        if (selectedMembers.contains(members.get(position))) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.lightPrimaryColor));
            int resId = context.getResources().getIdentifier("ic_check_symbol", "drawable", context.getPackageName());
            holder.rightTick.setImageResource(resId);
        }
        else {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
            holder.rightTick.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public Filter getFilter() {
        if(memberFilter == null)
            memberFilter = new MemberFilter(this,members);
        return memberFilter;
    }

    private static class MemberFilter extends Filter{

        private final MemberListAdapter adapter;
        private final List<Member> orignalList;
        private final List<Member> filteredList;
        private MemberFilter(MemberListAdapter adapter, List<Member> orignalList) {
            super();
            this.adapter = adapter;
            this.orignalList = new LinkedList<>(orignalList);
            this.filteredList = new ArrayList<>();
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
        @BindView(R.id.memberImageView) SelectableRoundedImageView mImage;
        @BindView(R.id.callMeBtnView) ImageButton mCallBtn;
        @BindView(R.id.mainHorizontalLayout) LinearLayout linearLayout;
        @BindView(R.id.rightTick) ImageView rightTick;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
