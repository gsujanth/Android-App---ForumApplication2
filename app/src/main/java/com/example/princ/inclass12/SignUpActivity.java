package com.example.princ.inclass12;

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
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = "demoSignUp";
    private FirebaseAuth mAuth;

    EditText fNameET, lNameET, suEmailET, cPwdET, rPwdET;
    Button cancelButton, signUpButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        fNameET = findViewById(R.id.fNameET);
        lNameET = findViewById(R.id.lNameET);
        suEmailET = findViewById(R.id.suEmailET);
        cPwdET = findViewById(R.id.cPwdET);
        rPwdET = findViewById(R.id.rPwdET);

        cancelButton = findViewById(R.id.cancelButton);
        signUpButton2 = findViewById(R.id.signUpButton2);

        mAuth = FirebaseAuth.getInstance();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUpButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = fNameET.getText().toString();
                String lName = lNameET.getText().toString();
                String email = suEmailET.getText().toString();
                String cPassword = cPwdET.getText().toString();
                String rPassword = rPwdET.getText().toString();
                if(fName.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "First Name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if(lName.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Last Name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }else if(cPassword.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Choose password field is empty", Toast.LENGTH_SHORT).show();
                }else if(rPassword.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Repeat password field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!cPassword.equals(rPassword)) {
                    Toast.makeText(SignUpActivity.this, "Chosen Password does not match Repeated Password", Toast.LENGTH_SHORT).show();
                } else {
                    performSignUp(fName, lName, email, cPassword);
                }
            }
        });
    }

    public void performSignUp(final String fName, final String lName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            Log.d(TAG, "Current User: " + mAuth.getCurrentUser());
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fName + " " + lName)
                                    .build();
                            if (user != null) {
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });
                            }
                            Intent intent = new Intent(SignUpActivity.this, ThreadsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if(task.getException()!=null) {
                                Toast.makeText(SignUpActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}
