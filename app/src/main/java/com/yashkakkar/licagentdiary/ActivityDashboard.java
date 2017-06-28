package com.yashkakkar.licagentdiary;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.yashkakkar.licagentdiary.fargments.ListDuePoliciesFragment;
import com.yashkakkar.licagentdiary.fargments.ListMembersFragment;
import com.yashkakkar.licagentdiary.fargments.ListNotesFragment;
import com.yashkakkar.licagentdiary.fargments.ListNotificationsFragment;
import com.yashkakkar.licagentdiary.fargments.ListPoliciesFragment;
import com.yashkakkar.licagentdiary.login.AgentRegistration;
import com.yashkakkar.licagentdiary.models.User;
import com.yashkakkar.licagentdiary.receivers.MyReceiver;

import java.util.Map;

public class ActivityDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ObservableScrollViewCallbacks {

    private static Context mContext;

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private final String LIST_NOTIFICATIONS_FRAGMENT = "LIST_OF_NOTIFICATIONS";
    private final String LIST_POLICIES_FRAGMENT = "LIST_OF_POLICIES";
    private final String LIST_DUE_POLICIES_FRAGMENT = "LIST_OF_DUE_POLICIES";
    private final String LIST_MEMBERS_FRAGMENT = "LIST_OF_MEMBERS";
    private final String LIST_NOTES_FRAGMENT = "LIST_OF_NOTES";

    android.app.FragmentManager fragmentManager;
    private Fragment listNotificationsFragment;
    private Fragment listDuePoliciesFragment;
    private Fragment listPoliciesFragment;
    private Fragment listNotesFragment;
    private Fragment listMembersFragment;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mRef;

    private TextView userName;
    private TextView userEmailId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_dashboard);
        // initializing user interface
        initUI();
        // asking for user permissions
        userPermissions();
        // Fire base authentication
        firebaseFunction();

    }

    private void initUI() {

        // Setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_20170605_191525);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Setting Navigation bar
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView  = navigationView.inflateHeaderView(R.layout.nav_dashboard);
        userName = (TextView) headerView.findViewById(R.id.profile_name);
        userEmailId = (TextView) headerView.findViewById(R.id.profile_email);
        setUserProfileData();
        TextView editProfile = (TextView) headerView.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDashboard.this,UserProfile.class);
                startActivity(intent);
            }
        });

        // Setting Bottom Navigation
        bottomNavigation();
    }

    private void setUserProfileData() {

    }

    public static Context getAppContext(){
        return ActivityDashboard.mContext;
    }


    private void bottomNavigation() {

        // initializing 3 fab button show when user click on a particular fragment
        final FloatingActionButton addMember = (FloatingActionButton) findViewById(R.id.fab_add_members);
        final FloatingActionButton addPolicies = (FloatingActionButton) findViewById(R.id.fab_add_policies);
        final FloatingActionButton addNotes = (FloatingActionButton) findViewById(R.id.fab_add_notes);

        // hide all FAB buttons
        addMember.hide();
        addPolicies.hide();
        addNotes.hide();

        // Initializing bottom navigation bar
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_notifications_black_24dp, "Notifications"))
                .addItem(new BottomNavigationItem(R.drawable.ic_policy_due_icon, "Dues"))
                .addItem(new BottomNavigationItem(R.drawable.ic_all_policies_24dp, "Policies"))
                .addItem(new BottomNavigationItem(R.drawable.ic_notes_icon, "Notes"))
                .addItem(new BottomNavigationItem(R.drawable.ic_round_account_button_with_user_inside, "Contacts"))
                .setFirstSelectedPosition(2)
                .initialise();
        //bottomNavigationBar.setFab(addPolicies);
        //bottomNavigationBar.setFab(addNotes);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar.setBackgroundColor(getResources().getColor(R.color.white));
        bottomNavigationBar
                .setActiveColor(R.color.white)
                .setInActiveColor(R.color.colorPrimaryDark)
                .setBarBackgroundColor(R.color.white);
        bottomNavigationBar.setAutoHideEnabled(true);

        // Action Listener for bottom navigation
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {

            @Override
            public void onTabSelected(int position) {
                //Log.d("BOTTOM NAVIGATION ", "onTabSelected() called with: " + "position = [" + position + "]");
                fragmentManager = getFragmentManager();
                android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (position == 0) {

                    // Notification fragment
                    if ( listNotificationsFragment == null){
                        listNotificationsFragment = ListNotificationsFragment.newInstance(LIST_NOTIFICATIONS_FRAGMENT);
                    }
                    transaction.replace(R.id.fragment_content, listNotificationsFragment,LIST_NOTIFICATIONS_FRAGMENT).commit();
                    addMember.hide();
                    addPolicies.hide();
                    addNotes.hide();
                }
                if (position == 1) {
                    // premium dues fragment
                    if ( listDuePoliciesFragment == null){
                        listDuePoliciesFragment = ListDuePoliciesFragment.newInstance(LIST_DUE_POLICIES_FRAGMENT);
                    }
                    transaction.replace(R.id.fragment_content, listDuePoliciesFragment,LIST_DUE_POLICIES_FRAGMENT).commit();
                    addPolicies.show();
                    addMember.hide();
                    addNotes.hide();
                }
                if (position == 2) {
                    // All Policies fragment
                    if ( listPoliciesFragment == null){
                        listPoliciesFragment = ListPoliciesFragment.newInstance();
                    }
                    transaction.replace(R.id.fragment_content, listPoliciesFragment,LIST_POLICIES_FRAGMENT).commit();
                    addPolicies.show();
                    addMember.hide();
                    addNotes.hide();
                }

                if (position == 3) {
                    // Notes fragment
                    if ( listNotesFragment == null){
                        listNotesFragment = ListNotesFragment.newInstance(LIST_NOTES_FRAGMENT);
                    }
                    transaction.replace(R.id.fragment_content,listNotesFragment,LIST_NOTES_FRAGMENT).commit();
                    addNotes.show();

                    addMember.hide();
                    addPolicies.hide();

                }
                if (position == 4) {
                    // All policy Holders List
                    if ( listMembersFragment == null){
                        listMembersFragment = ListMembersFragment.newInstance(LIST_MEMBERS_FRAGMENT);
                    }
                    transaction.replace(R.id.fragment_content, listMembersFragment,LIST_MEMBERS_FRAGMENT).commit();
                    addMember.show();

                    addPolicies.hide();
                    addNotes.hide();
                    Snackbar.make(findViewById(android.R.id.content),"Tap + Add New Member", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }

            @Override
            public void onTabUnselected(int position) {
               // Log.d("BOTTOM NAVIGATION", "onTabUnselected() called with: " + "position = [" + position + "]");
        /*
               if ( position == 0){

               }
               if (position == 1){

               }
               if (position == 2){

               }
               if (position == 3){

               }

                if (position == 4){

                }*/
            }

            @Override
            public void onTabReselected(int position) {
              //  Log.d("BOTTOM NAVIGATION", "onTabReselected() called with: " + "position = [" + position + "]");
            }

        });

        fragmentManager = getFragmentManager();
        final android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Load List Policies fragment for the first time
        // TAGGING the fragment with an appropriate name
        listPoliciesFragment = ListPoliciesFragment.newInstance();
        transaction.replace(R.id.fragment_content,listPoliciesFragment,LIST_POLICIES_FRAGMENT)
                .commit();
        addPolicies.show();


        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Add Member activity
                Intent intent = new Intent(ActivityDashboard.this, CreateNewMember.class);
                startActivity(intent);

            }
        });
        addPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Add Policy activity
                Intent intent = new Intent(ActivityDashboard.this, CreateNewPolicy.class);
                startActivity(intent);
            }
        });
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Add Notes Activity
                Intent intent = new Intent(ActivityDashboard.this, AddNewNote.class);
                startActivity(intent);
            }
        });

    }

    private void userPermissions() {
        // grant permission for making a call
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCallingPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if(hasCallingPermission != PackageManager.PERMISSION_GRANTED){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    showMessageOKCancel("You need to allow access to Calling Phone",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[] {Manifest.permission.CALL_PHONE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else {Toast.makeText(this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();}
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_policy_holders, menu);

        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              //  ListAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.this_month) {
            Intent intent = new Intent(ActivityDashboard.this,UserProfile.class);
            startActivity(intent);

            // generate notification
            scheduleNotification(getNotification("Call Policy Holders"), 5000);
            return true;
        }

        if (id == R.id.log_out){
            // before logout sync all the data to the web
            logOut();
            Intent intent = new Intent(ActivityDashboard.this,AgentRegistration.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

     private void logOut() {
        firebaseAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, MyReceiver.class);
        notificationIntent.putExtra(MyReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("LIC POLICY DUE THIS MONTH");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_user);
        return builder.build();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.all_policy_holders:
                break;
            case R.id.premium_dues:
                break;
            case R.id.manage_contacts:
                break;
            case R.id.notes:
                break;
            case R.id.notifications:
                break;
            case R.id.sync_now:
                break;
            case R.id.upgrade_account:
                break;
            case R.id.share:
                break;
            case R.id.setting:
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        /*ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }*/
    }

    private void firebaseFunction() {
        firebaseAuth = FirebaseAuth.getInstance();
        final User u = new User();

        SharedPreferences sd = getSharedPreferences("User",MODE_PRIVATE);
        u.setName(sd.getString("name","Profile Name"));
        u.setEmail(sd.getString("email","username@gmail.com"));
        u.setPass(sd.getString("pass","null"));

        String name = u.getName();
        String email = u.getEmail();
        userName.setText(name);
        userEmailId.setText(email);

        //get Fire base User details
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // getting user session id as emailId
            String userId = user.getEmail();
            userId = userId.replace(".",",");
            // get user data from fire base database /users/$userId/
            mRef = FirebaseDatabase.getInstance().getReference("users");

           mRef.child(userId).addValueEventListener(new ValueEventListener() {
                private static final String TAG = "User Node" ;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                    u.setName(map.get("name"));
                    u.setEmail(map.get("email"));
                   // u.setPass(map.get("pass"));
                    String n,e;
                    n = u.getName();
                    e = u.getEmail();
                    // update ui in navigation view
                    userName.setText(n);
                    userEmailId.setText(e);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
            userName.setText(user.getDisplayName());
            userEmailId.setText(user.getEmail());

            Toast.makeText(ActivityDashboard.this, "Welcome! " +user.getDisplayName(), Toast.LENGTH_SHORT).show();
        }

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ActivityDashboard.this, AgentRegistration.class));
                    finish();
                }
            }
        };

    }

}