package com.yashkakkar.licagentdiary.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.yashkakkar.licagentdiary.R;

public class ForgetPassword extends AppCompatActivity  implements View.OnClickListener{

    private EditText recoverEmailId;
    private Button forgetBtn;
    private TextView goLogIn;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        firebaseAuth = FirebaseAuth.getInstance();
        recoverEmailId = (EditText) findViewById(R.id.email_forget_password);
        forgetBtn = (Button) findViewById(R.id.forget_password_btn);
        goLogIn = (TextView) findViewById(R.id.go_to_login_page);

        progressDialog = new ProgressDialog(this);

        forgetBtn.setOnClickListener(this);
        goLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == forgetBtn){
            recoverPassword();
        }
        if (v == goLogIn){
            goLogIn();
        }
    }

    private void recoverPassword() {
        String email = recoverEmailId.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(ForgetPassword.this,"Please enter your Email id!",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Recovering your account... ");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void goLogIn() {
        // go back to login page
        Intent intent = new Intent(ForgetPassword.this,AgentLogIn.class);
        startActivity(intent);
        finish();
    }

}
