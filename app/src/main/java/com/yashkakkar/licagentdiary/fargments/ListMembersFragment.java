package com.yashkakkar.licagentdiary.fargments;

import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.async.eventbus.DeleteMemberEvent;
import com.yashkakkar.licagentdiary.async.eventbus.GetMemberListEvent;
import com.yashkakkar.licagentdiary.async.member.DeleteMemberTask;
import com.yashkakkar.licagentdiary.async.member.GetMemberListTask;
import com.yashkakkar.licagentdiary.models.Adapters.MemberListAdapter;
import com.yashkakkar.licagentdiary.models.Adapters.TouchListener.RecyclerItemClickListener;
import com.yashkakkar.licagentdiary.models.Member;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Yash Kakkar on 26-05-2017.
 */
public class ListMembersFragment extends Fragment {

    @BindView(R.id.membersListView) RecyclerView memberListView;
    @BindView(R.id.empty_list_members_fragment) RelativeLayout relativeLayout;
    private Unbinder unbinder;

    // Create a List of members
    private List<Member> members = new ArrayList<>();
    private List<Member> multi_select_member = new ArrayList<>();
    private MemberListAdapter memberListAdapter;
    // private Paint p = new Paint();

    boolean isMultiSelect = false;
    ActionMode mActionMode;

    public static ListMembersFragment newInstance(String param1){
        ListMembersFragment fragment = new ListMembersFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ListMembersFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fetch members in a separate thread using Async Task
        GetMemberListTask getMemberListTask = new GetMemberListTask(getActivity());
        getMemberListTask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_members,container,false);
        unbinder = ButterKnife.bind(this,v);
        EventBus.getDefault().register(this);
        // setting menu
        setHasOptionsMenu(true);
        // set the recycler view adapter
        members = Collections.emptyList();
        memberListAdapter = new MemberListAdapter(getActivity(),members);
        memberListView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        memberListView.setAdapter(memberListAdapter);
        /* initSwipe();*/
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.list_member_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.member_menu_log_out:
                // logout from the app
                //System.exit(1);
                break;
            case R.id.member_menu_settings:
                // Go to member setting menu
                break;
        }
        return true;
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
        members = event.getMembers();
        // make adapter class
        memberListAdapter = new MemberListAdapter(getActivity(),members);
        memberListView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        memberListView.setAdapter(memberListAdapter);
        if (members.isEmpty()){
           // Toast.makeText(getActivity(),"LIST NOT NULL!",Toast.LENGTH_SHORT).show();
            relativeLayout.setVisibility(View.VISIBLE);
        }

        memberListView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),memberListView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (isMultiSelect)
                    multi_select(position);
               // else
               //     Toast.makeText(getActivity(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View v, int position) {
                if (!isMultiSelect) {
                    multi_select_member = new ArrayList<>();
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode =  getActivity().startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
            }

        }));

    }

    // Add/Remove the item from/to the list
    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multi_select_member.contains(members.get(position)))
                multi_select_member.remove(members.get(position));
            else
                multi_select_member.add(members.get(position));
            if (multi_select_member.size() > 0)
                mActionMode.setTitle("" + multi_select_member.size()+"/"+members.size());
            else
                mActionMode.setTitle("" + multi_select_member.size()+"/"+members.size());
            refreshAdapter();
        }
    }

    public void refreshAdapter() {
        memberListAdapter.selectedMembers = multi_select_member;
        memberListAdapter.members = members;
        memberListAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback(){

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.list_select_member_fragment_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()){
                case R.id.action_delete_selected_member:
                    MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                            .content("Delete "+multi_select_member.size()+" contacts? \n It may delete all policies associated with the contacts!")
                            .positiveText("DELETE")
                            .negativeText("CANCEL")
                            .callback(new MaterialDialog.ButtonCallback() {

                                @Override
                                public void onPositive(MaterialDialog dialog) {

                                    // Delete the members
                                    DeleteMemberTask deleteMemberTask = new DeleteMemberTask(multi_select_member,getActivity());
                                    deleteMemberTask.execute();
                                    if(multi_select_member.size()>0) {
                                        for(int i=0; i < multi_select_member.size(); i++){
                                            members.remove(multi_select_member.get(i));
                                        }
                                        memberListAdapter.notifyDataSetChanged();
                                        if (mActionMode != null) {
                                            mActionMode.finish();
                                        }
                                        Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    if (mActionMode != null) {
                                        mActionMode.finish();
                                    }
                                    dialog.dismiss();
                                }

                            }).build();
                    dialog.show();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multi_select_member = new ArrayList<>();
            refreshAdapter();
        }

    };


    @Subscribe
    public void onEvent(DeleteMemberEvent event){
    if (event.isMembersDelete()){
        Toast.makeText(getActivity(),"Contacts deleted Successfully!",Toast.LENGTH_LONG).show();
    }else {
        Toast.makeText(getActivity(),"Problem deleting members",Toast.LENGTH_LONG).show();
    }
    }
}