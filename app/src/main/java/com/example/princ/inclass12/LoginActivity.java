package com.example.princ.inclass12;

/*
  Author : Sujanth Babu Guntupalli
*/

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "demoLogin";
    private FirebaseAuth mAuth;
    EditText emailET, pwdET;
    Button loginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        emailET = findViewById(R.id.emailET);
        pwdET = findViewById(R.id.pwdET);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()==null) {
            Log.d(TAG, "onCreate: " + mAuth.getCurrentUser());
        }else{
            Log.d(TAG, "onCreate: " + mAuth.getCurrentUser().getDisplayName());
            Intent intent = new Intent(LoginActivity.this, ThreadsActivity.class);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = pwdET.getText().toString();
                if (email.isEmpty()) {
                    emailET.setError("Email field empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailET.setError("Invalid Email pattern");
                } else if (password.isEmpty()) {
                    pwdET.setError("Password field empty");
                } else {
                    performLogin(email, password);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void performLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            if(mAuth.getCurrentUser()!=null) {
                                Log.d(TAG, "Current User: " + mAuth.getCurrentUser().getDisplayName());
                            }
                            Intent intent = new Intent(LoginActivity.this, ThreadsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if(task.getException()!=null) {
                                Toast.makeText(LoginActivity.this, "LoginActivity: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}

