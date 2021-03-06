package com.example.authproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPswd;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.rgstr);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.btSignIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.etemail);
        editTextEmail.setOnClickListener(this);

        editTextPswd = (EditText) findViewById(R.id.etpassword);
        editTextPswd.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.frgtpswd);
        forgotPassword.setOnClickListener(this);

        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.rgstr:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.btSignIn:
                userLogin();
                break;

            case R.id.frgtpswd:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPswd.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Pls enter email!");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid Email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPswd.setError("Full Name is required");
            editTextPswd.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPswd.setError("Min pswd length should be 6 chars!");
            editTextPswd.requestFocus();
            return;


        }
        progressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify your account.",Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! pls check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}