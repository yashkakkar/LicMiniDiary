package com.yashkakkar.licagentdiary;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.yashkakkar.licagentdiary.async.eventbus.GetMemberListEvent;
import com.yashkakkar.licagentdiary.async.member.GetMemberListTask;
import com.yashkakkar.licagentdiary.models.Adapters.GetSelectionMemberAdapter;
import com.yashkakkar.licagentdiary.models.Member;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SelectMember extends AppCompatActivity {


    @BindView(R.id.empty_list_members_fragment)
    RelativeLayout emptyLayoutContent;
    @BindView(R.id.selectMemberListView)
    RecyclerView recyclerView;
    List<Member> memberLists;
    GetSelectionMemberAdapter memberListAdapter;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        memberLists = new ArrayList<>();
        // get list of members using async task
        GetMemberListTask  getMemberListTask = new GetMemberListTask(this);
        //getMemberListTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        getMemberListTask.execute();
        // listen to the posted event from asyncTask thread

        if (recyclerView!= null){
            emptyLayoutContent.removeAllViews();
        }

     // select 1 member and return member object to parent activity
     // on selecting member return back to add new policy holder activity
    }


    // This method will be called when a GetMemberListEvent is posted
    @Subscribe
    public void onEvent(GetMemberListEvent event){
        memberLists = event.getMembers();
        // getting List<Member> list
        memberListAdapter = new GetSelectionMemberAdapter(this, memberLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(memberListAdapter);

        // your implementation
        // Toast.makeText(getActivity(), event.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_member_menu, menu);

        /*
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_policy_holders).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        */


        final MenuItem searchItem = menu.findItem(R.id.search_policy_holders);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO write your code what you want to perform on search
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //TODO write your code what you want to perform on search text change
                memberListAdapter.getFilter().filter(query);
                return true;
            }
        });




        return true;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==R.id.create_new_member){
            Intent intent = new Intent(SelectMember.this, CreateNewMember.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
