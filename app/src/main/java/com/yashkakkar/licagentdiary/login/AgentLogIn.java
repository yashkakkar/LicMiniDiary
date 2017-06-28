package com.yashkakkar.licagentdiary.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yashkakkar.licagentdiary.ActivityDashboard;
import com.yashkakkar.licagentdiary.network.AppStatus;
import com.yashkakkar.licagentdiary.R;

public class AgentLogIn extends AppCompatActivity implements View.OnClickListener{
    // get email and password to logIn, this is for existing user
    // check if its an existing user or a new user from other device
    // if existing user the database will be same
    // if not exiting user delete all the database created by previous user

    private EditText emailId;
    private EditText password;
    private Button logIn;
    private TextView forgetPassword;
    private TextView registerHere;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_log_in);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        emailId = (EditText) findViewById(R.id.mEmail);
        password = (EditText) findViewById(R.id.pass_word);
        logIn = (Button) findViewById(R.id.log_in);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        registerHere = (TextView) findViewById(R.id.new_account);

        progressDialog = new ProgressDialog(this);

        logIn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        registerHere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == logIn){
            logIn();
        }

        if (v == forgetPassword){
            forgetPassword();
        }

        if (v == registerHere){
            registerHere();
        }
    }

    private void registerHere() {
        // go to NewAgentLogin Activity
        Intent intent = new Intent(AgentLogIn.this,AgentRegistration.class);
        startActivity(intent);
        finish();
    }

    private void forgetPassword() {
        // go to forget password activity
        Intent intent = new Intent(AgentLogIn.this,ForgetPassword.class);
        startActivity(intent);
        finish();
    }

    private void logIn() {
        String  email = emailId.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter your Email id",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if internet is working or not
        if (AppStatus.getInstance(this).isOnline()) {

            Toast.makeText(this,"You are online!!!!",Toast.LENGTH_SHORT).show();

            progressDialog.setMessage("LogIn please wait...");
            progressDialog.show();
            // if working login via online
            firebaseAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AgentLogIn.this,"You are successfully LoggedIn!",Toast.LENGTH_SHORT).show();
                                // Go to Profile Activity
                                finish();
                                startActivity(new Intent(getApplicationContext(),ActivityDashboard.class));
                                // sync the database to online
                            }else {
                                Toast.makeText(AgentLogIn.this,"Unable to LogIn! Please try again!",Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

        } else {

            Log.v("Home", "############################You are not online!!!!");
            // login with existing user

            // if not working login via existing user
            SharedPreferences sd = getSharedPreferences("User", Context.MODE_PRIVATE);
            String e, p;
            e = sd.getString("name","admin");
            p = sd.getString("pass","admin");
            if ((email.equals(e)) && (pass.equals(p))){
                Toast.makeText(AgentLogIn.this,"Welcome Back!! You are successfully LoggedIn!",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),ActivityDashboard.class));
            }else {
                Toast.makeText(AgentLogIn.this,"Unable to LogIn! Please try again!",Toast.LENGTH_SHORT).show();
            }
        }


    }

}
