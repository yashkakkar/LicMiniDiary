package com.yashkakkar.licagentdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserProfile extends AppCompatActivity {

    @BindView(R.id.user_profile_email) TextView userEmail;
    @BindView(R.id.user_profile_password) TextView userPass;
    @BindView(R.id.user_profile_address) TextView userAddress;
    @BindView(R.id.user_proflie_gender) TextView userGender;
    @BindView(R.id.user_profile_phone_number) TextView userPhoneNumber;
    @BindView(R.id.user_profile_image_icon) SelectableRoundedImageView userProfileImage;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        unbinder = ButterKnife.bind(this);
        // fetch session data username and email id

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Yash Kakkar");
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // fetch user data from user database table or shared preferences


        // or fetch user data from User file offline


        // set data to the user profile

        userEmail.setText("");
        userPass.setText("");
        userAddress.setText("");
        userGender.setText("");
        userPhoneNumber.setText("");

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.user_profile_edit){
            // open new activity to edit existing user information
            Intent intent = new Intent(this,UserProfileEdit.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.user_profile_logout){
            // logout the user from the app

        }
        if (id == R.id.user_profile_settings){
            // open user settings

        }
        return super.onOptionsItemSelected(item);
    }
}
