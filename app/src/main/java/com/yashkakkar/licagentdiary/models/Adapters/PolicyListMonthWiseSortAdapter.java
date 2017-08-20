package com.yashkakkar.licagentdiary.models.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.models.Policy;
import com.yashkakkar.licagentdiary.utils.DateTimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yash Kakkar on 08-06-2017.
 */

public class PolicyListMonthWiseSortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private DateTimeUtils dateTimeUtils;
    private Context context;
    private List<Member> members;
    private List<Policy> policiesSortedByDate;
    private LayoutInflater inflater;
    private List<List<Policy>> listOfPolicies = new ArrayList<>();
    // get list of policies at position 0,1,2,3...
    private int pointer = 0;

    private int offset = -1;
    private int nextpos = 0;
    private boolean flag = false;
    private int listOffset = 0;
    private static final int VIEW_TYPE_FIRST  = 0;
    private static final int VIEW_TYPE_SECOND = 1;

    public PolicyListMonthWiseSortAdapter(Context context, List<Member> members, List<Policy> policiesSortedByDate) throws ParseException {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.members = members;
        this.policiesSortedByDate = policiesSortedByDate;
        this.listOfPolicies = splitByDateAcive(policiesSortedByDate);
    }

    public class MyDateViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.date_row_text) TextView dateText;
        public MyDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class MyPolicyViewHolder extends RecyclerView.ViewHolder{
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
        public MyPolicyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_FIRST){
            View view1 = inflater.inflate(R.layout.my_date_row,parent,false);
            return new MyDateViewHolder(view1);
        }else if(viewType == VIEW_TYPE_SECOND){
            View view = inflater.inflate(R.layout.policy_row,parent,false);
            return new MyPolicyViewHolder(view);
        }
      return null;
    }


    @Override
    public int getItemViewType(int position) {
      int viewType = VIEW_TYPE_FIRST;
        if (getDateViewHolderSection(position)){
            return viewType;
        }
        else {
            viewType = VIEW_TYPE_SECOND;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        int size_of_policies_holder = policiesSortedByDate.size();
        Log.v("Total no of policies",String.valueOf(size_of_policies_holder));
        int size_of_date_holder = listOfPolicies.size();
        Log.v("List of policy",String.valueOf(size_of_date_holder));
        return size_of_policies_holder+size_of_date_holder;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case VIEW_TYPE_FIRST:   MyDateViewHolder myDateViewHolder = (MyDateViewHolder) holder;
                                    Log.e("index",String.valueOf(position));
                                    //int index1 = position - nextpos;
                                    List<List<Policy>> lp = listOfPolicies;
                                    List<Policy> p = lp.get(offset); // 0 1 2 3 4
                                    Policy policy = p.get(0);        // 0--> 0
                                    // showing only month, year in desc order
                                    myDateViewHolder.dateText.setText(policy.getDocDate());
                                    listOffset++;
                                    pointer = 0;
                                    break;
            case VIEW_TYPE_SECOND:  MyPolicyViewHolder myPolicyViewHolder = (MyPolicyViewHolder) holder;
                                    List<List<Policy>> lp1 = listOfPolicies;
                                    Log.e("offset of policy",String.valueOf(offset));
                                    List<Policy> p1 = lp1.get(offset); // 0 1 2 3 4
                                    Log.e("Pointer size",String.valueOf(p1.size()));

                                     // 0 1, 0 , 0 , 0

                                    if (pointer < p1.size()) {
                                        Policy policy1 = p1.get(pointer);
                                        Log.e("Pointer value", String.valueOf(pointer));
                                        String member_id = policy1.getMemberId();
                                        for (Member member : members) {
                                            if (member.getMemberId() != null && member.getMemberId().contains(member_id)) {
                                                // set member data
                                                myPolicyViewHolder.memberName.setText(member.getMemberName());
                                                // set the profile image
                                                int resId = context.getResources().getIdentifier("ic_member_profile_pic", "drawable", context.getPackageName());
                                                myPolicyViewHolder.memberProfileIcon.setImageResource(resId);
                                                break;
                                            }
                                        }
                                        myPolicyViewHolder.policyName.setText(policy1.getPolicyName());
                                        myPolicyViewHolder.policyNumber.setText(policy1.getPolicyNumber());
                                        myPolicyViewHolder.docDate.setText(policy1.getDocDate());
                                        myPolicyViewHolder.dlpDate.setText(policy1.getDlpDate());
                                        myPolicyViewHolder.domDate.setText(policy1.getDomDate());
                                        myPolicyViewHolder.mode.setText(policy1.getMode());
                                        myPolicyViewHolder.term.setText(policy1.getTermTable());
                                        myPolicyViewHolder.saAmt.setText("₹ " + policy1.getSaAmount());
                                        myPolicyViewHolder.premiumAmt.setText("₹ " + policy1.getPremiumAmount());
                                        myPolicyViewHolder.status.setText(String.valueOf(policy1.getPolicyStatus()));
                                        myPolicyViewHolder.dob.setText(policy1.getDobDate());
                                        myPolicyViewHolder.age.setText("(" + String.valueOf(policy1.calculateAgeFromDob(policy1.getDobDate())) + ")");
                                        pointer++;
                                    }
                                    break;
            default:
                                    break;
        }



        // 0 -->3 sizeof listOfPolicies at 0 position
        // 4 -->3 sizeof listOfPolicies at 1 position
        // 7 -->5 ...
        // 12 -->2 ...

    }

    public boolean getDateViewHolderSection(int position) {
        if (position == nextpos){ // 0
            nextpos = getPositionOfDateHolder(position); // 4 9 15
            flag = false;
        }
        else { // 1 2 3   5 6 7 8   10 11 12 13 14    16 17 18
            flag = true;
        }
        return flag;
    }

    public int getPositionOfDateHolder(int position){
        int list_size = 0;
        if (nextpos == position && offset < listOfPolicies.size()){
            offset = offset+1;
            List<Policy> policyList = listOfPolicies.get(offset);
            list_size = policyList.size(); // 3
            nextpos = position+list_size+1; //4
        }
        Log.e("nextpos ",String.valueOf(nextpos));
        Log.v("offset value ",String.valueOf(offset));
        return nextpos; //4 9 15
    }

    public int getOffsetOfDateHolder(int pos){


        return offset;
    }



    private List<List<Policy>> getListOfPolicies(List<Policy> policies) throws ParseException {
        // collect List<List<policies>> of policies of same month,year
        // ex jan, 2017 --> List[0] --> list of policies with same month and year
        //    feb, 2017 --> List[1]
        //    mar, 2017 --> List[2]
        // add list of policy which have dated same month and year
        dateTimeUtils = new DateTimeUtils();

                /*for (int i = 0; i < policies.size(); i++) {
                    Log.v("index i" ,String.valueOf(i));
                    List<Policy> list = new ArrayList<>();
                    Policy p1 = policies.get(i);
                    list.add(p1);//
                    Calendar c1 = dateTimeUtils.getCalenderFromDate(p1.getDocDate());
                    int doc_month1 = c1.get(Calendar.MONTH) + 1;
                    int doc_year1 = c1.get(Calendar.YEAR);
                    for (int j = i+1; j< policies.size();j++){
                        Policy p2 = policies.get(j);
                        dateTimeUtils = new DateTimeUtils();
                        Calendar c2 = dateTimeUtils.getCalenderFromDate(p2.getDocDate());
                        int doc_month2 = c2.get(Calendar.MONTH) + 1;
                        int doc_year2 = c2.get(Calendar.YEAR);
                        if (doc_month1 == doc_month2 && doc_year1 == doc_year2) {
                            Log.e("index j" ,String.valueOf(j));
                            list.add(p2);
                        }else{
                            int lastIndexOf = policies.lastIndexOf(p2);
                            if (lastIndexOf != -1){
                                list.add(p2);
                            }
                            i = j; // 5
                            i--; //4
                            break;
                        }
                    }
                    listOfPolicies.add(list);

                }*/
       /* String size= String.valueOf(listOfPolicies.size());
        Log.v("Size Of list",size);
       */ return listOfPolicies;
    }


    private static List<List<Policy>> splitByDateAcive(List<Policy> policies){
        Map<String, List<Policy>> map = new TreeMap<String, List<Policy>>();
        for (Policy p : policies){
            List<Policy> list = map.get(p.getDobDate());
            if (list == null){
                list = new ArrayList<Policy>();
                map.put(p.getDocDate(),list);
            }
            list.add(p);
        }
        List<List<Policy>> lists = new ArrayList<List<Policy>>();
        for (List<Policy> policyList: map.values()){
            lists.add(policyList);
        }
        return  lists;
    }

}
