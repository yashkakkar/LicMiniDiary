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
import com.yashkakkar.licagentdiary.async.eventbus.GetNoteListEvent;
import com.yashkakkar.licagentdiary.async.notes.GetNotesListTask;
import com.yashkakkar.licagentdiary.models.Adapters.NoteListAdapter;
import com.yashkakkar.licagentdiary.models.Note;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Yash Kakkar on 31-05-2017.
 */

public class ListNotesFragment extends Fragment {

    @BindView(R.id.notesListView) RecyclerView notesListView;
    @BindView(R.id.empty_list_notes_fragment) RelativeLayout emptyNoteFragment;
    @BindView(R.id.progress_bar_notes) ProgressBar progressBar;

    List<Note> notes;
    NoteListAdapter noteListAdapter;
    Unbinder unbinder;
    Handler handler;
    public static ListNotesFragment newInstance(String param1){
        ListNotesFragment fragment = new ListNotesFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ListNotesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_notes,container,false);
        unbinder = ButterKnife.bind(this,v);
        EventBus.getDefault().register(this);

        handler = new Handler();
     /*   notes = Collections.emptyList();
        noteListAdapter = new NoteListAdapter(getActivity(),notes);
        notesListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        notesListView.setAdapter(noteListAdapter);
    */
       // progressBar.setVisibility(View.VISIBLE);
        GetNotesListTask getNotesListTask = new GetNotesListTask(getActivity());
        getNotesListTask.execute();
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
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(GetNoteListEvent event){
        notes = event.getNotes();
        noteListAdapter = new NoteListAdapter(getActivity(),notes);
        notesListView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        notesListView.setAdapter(noteListAdapter);
        if (notes.isEmpty()){
           // Create and start a new Thread
            new Thread(new Runnable() {
                public void run() {
                    try{Thread.sleep(1000);}
                    catch (Exception e) { } // Just catch the InterruptedException
                    // Now we use the Handler to post back to the main thread
                    handler.post(new Runnable() {
                        public void run() {
                            // Set the View's visibility back on the main UI Thread
                         //   progressBar.setVisibility(View.INVISIBLE);
                            emptyNoteFragment.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }).start();
            handler.removeCallbacksAndMessages(null);
        }else {
            // Create and start a new Thread
            new Thread(new Runnable() {
                public void run() {
                    try{Thread.sleep(3000);}
                    catch (Exception e) { } // Just catch the InterruptedException
                    // Now we use the Handler to post back to the main thread
                    handler.post(new Runnable() {
                        public void run() {
                            // Set the View's visibility back on the main UI Thread
                         //  progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.list_notes_fragment_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notes_menu_log_out:
                // logout from the app
                //System.exit(1);
                break;
            case R.id.notes_menu_settings:
                // Go to notes setting menu
                break;
        }
        return true;
    }

}
