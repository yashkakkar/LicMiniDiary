package com.yashkakkar.licagentdiary.fargments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.async.eventbus.GetMemberListEvent;
import com.yashkakkar.licagentdiary.async.eventbus.GetPolicyListEvent;
import com.yashkakkar.licagentdiary.async.member.GetMemberListTask;
import com.yashkakkar.licagentdiary.async.policy.GetPolicyListTask;
import com.yashkakkar.licagentdiary.models.Adapters.PolicyListAdapter;
import com.yashkakkar.licagentdiary.models.Adapters.PolicyListMonthWiseSortAdapter;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.models.Policy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Yash Kakkar on 26-05-2017.
 */

public class ListPoliciesFragment extends Fragment {


    @BindView(R.id.empty_list_policies_fragment) RelativeLayout emptyListPolicy;
    @BindView(R.id.policiesListView) RecyclerView policyListView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    Unbinder unbinder;
    List<Member> members;
    List<Policy> policies;

    PolicyListAdapter policyListAdapter;
    PolicyListMonthWiseSortAdapter policyListMonthWiseSortAdapter;

    Handler handler;
    public static ListPoliciesFragment newInstance(){
        return new ListPoliciesFragment();
    }

    public ListPoliciesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_policies,container,false);
        unbinder = ButterKnife.bind(this,v);
        EventBus.getDefault().register(this);
        progressBar = new ProgressBar(getActivity());
        progressBar.setVisibility(View.VISIBLE);
        handler = new Handler();
        /*
        members = Collections.emptyList();
        policies = Collections.emptyList();
        policyListAdapter = new PolicyListAdapter(getActivity(),members,policies);
        policyListView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        policyListView.setAdapter(policyListAdapter);
        */

        GetMemberListTask getMemberListTask = new GetMemberListTask(getActivity());
        getMemberListTask.execute();
        GetPolicyListTask getPolicyListTask = new GetPolicyListTask(getActivity());
        getPolicyListTask.execute();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(GetMemberListEvent event){
        // get the member from the database
        members = event.getMembers();
    }

    @Subscribe
    public void onEvent(GetPolicyListEvent event) throws ParseException {
        // get the policies from the database
        policies = event.getPolicies();
        // make adapter class
        policyListAdapter = new PolicyListAdapter(getActivity(),members,policies);
        policyListView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        policyListView.setAdapter(policyListAdapter);
        if (policies.isEmpty()){
            // Create and start a new Thread
            new Thread(new Runnable() {
                public void run() {
                    try{Thread.sleep(1000);}
                    catch (Exception e) { } // Just catch the InterruptedException
                    // Now we use the Handler to post back to the main thread
                    handler.post(new Runnable() {
                        public void run() {
                            // Set the View's visibility back on the main UI Thread
                            progressBar.setVisibility(View.INVISIBLE);
                            emptyListPolicy.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }).start();
        }else{
            // Create and start a new Thread
            new Thread(new Runnable() {
                public void run() {
                    try{Thread.sleep(3000);}
                    catch (Exception e) { } // Just catch the InterruptedException
                    // Now we use the Handler to post back to the main thread
                    handler.post(new Runnable() {
                        public void run() {
                            // Set the View's visibility back on the main UI Thread
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();
        }
        handler.removeCallbacksAndMessages(null);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.list_policies_fragment_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.policies_menu_log_out:
                // logout from the app
                //System.exit(1);
                break;
            case R.id.policies_menu_settings:
                // Go to notes setting menu
                break;
        }
        return true;
    }

}
