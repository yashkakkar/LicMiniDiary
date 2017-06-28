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

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yashkakkar.licagentdiary.ActivityDashboard;
import com.yashkakkar.licagentdiary.network.AppStatus;
import com.yashkakkar.licagentdiary.R;
import com.yashkakkar.licagentdiary.models.User;

public class AgentRegistration extends AppCompatActivity implements View.OnClickListener {

    private EditText userName;
    private EditText emailId;
    private EditText password;
    private Button registerBtn;
    private TextView forgetPassword;
    private TextView logInHere;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private SignInButton signInButton;// google sign in
    private DatabaseReference mRef;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_agent_log_in);

        // get Fire base Auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // user session tracking --> registered or not
        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(AgentRegistration.this,ActivityDashboard.class));
            finish();
        }

        mRef = FirebaseDatabase.getInstance().getReference();


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();*/
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/
        progressDialog = new ProgressDialog(this);
        userName = (EditText) findViewById(R.id.user_name);
        emailId = (EditText) findViewById(R.id.mEmail);
        password = (EditText) findViewById(R.id.pass_word);
        registerBtn = (Button) findViewById(R.id.register);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        logInHere =(TextView) findViewById(R.id.log_in);

        // google sign in button instantiate
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        logInHere.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton){
            signInWithGoogle();
        }

        if (v == registerBtn){
            registerNewUser();
        }

        if (v == logInHere){
            logInHere();
        }

        if (v == forgetPassword){
            forgetPassword();
        }
    }

    private void signInWithGoogle() {
       /* Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);*/
    }

    private void forgetPassword() {
        // go to forget password activity and enter email to reset password
        Intent intent = new Intent(AgentRegistration.this,ForgetPassword.class);
        startActivity(intent);
        finish();

    }

    private void logInHere() {
        // go to AgentLogIn activity if already registered
        Intent intent = new Intent(AgentRegistration.this,AgentLogIn.class);
        startActivity(intent);
        finish();

    }

    private void registerNewUser() {
        String name = userName.getText().toString().trim();
        String email = emailId.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            // Show message
            Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)){
            // Show message
            Toast.makeText(this,"Please enter email id",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if internet is working or not
        if (AppStatus.getInstance(this).isOnline()) {
            progressDialog.setMessage("Login for the first time please wait...");
            progressDialog.show();
                Toast.makeText(this,"You are online!!!!",Toast.LENGTH_SHORT).show();
                // Create first time database with unique user
                // save user data for offline capabilities name,email,pass
                user = new User(name, email, pass);
                createUserInFireBaseDatabase(user);
                // Authenticate email and password
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // user is successfully registered and logged in
                                    // we will start a profile activity here
                                    // display a toast message only
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(AgentRegistration.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Intent intent = new Intent(AgentRegistration.this, ActivityDashboard.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(AgentRegistration.this, "Registered Successfully!", Toast.LENGTH_SHORT);
                                }
                                progressDialog.dismiss();
                            }
                        });
            }else{
            Toast.makeText(this,"No Internet Connection! If you sign-in before for this app please login with that username & password!",Toast.LENGTH_SHORT).show();
        }
       /*
       private boolean checkAccountEmailExistInFirebase(String email) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final boolean[] b = new boolean[1];
            mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    b[0] = !task.getResult().getProviders().isEmpty();
                }
            });
            return b[0];
        }
        */
    }

    private void createUserInFireBaseDatabase(User user) {

        // new user node would be /users/$userId/
        String userId = user.getEmail();
        userId = userId.replace(".",",");
        Log.v("userid",userId);
        mRef.child("users").child(userId).setValue(user);

        // save user data for offline capabilities
        // save user in SharedPreferences

        SharedPreferences sd = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sd.edit();

        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("pass", user.getPass());

    }

}
