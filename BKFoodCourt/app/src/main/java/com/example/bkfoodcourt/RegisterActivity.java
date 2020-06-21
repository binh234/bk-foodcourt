package com.example.bkfoodcourt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText mUsername, mPassword, mFullname, mPhonenumber, mAddress, mBirthday;
    RadioGroup mGender;
    RadioButton mMale, mFemale, mOther;
    Button mButtonSignUp;
    FirebaseAuth fAuth;
    TextView mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialization
        mUsername = findViewById(R.id.etUsername);
        mPassword = findViewById(R.id.etPassword);
        mFullname = findViewById(R.id.etFullname);
        mPhonenumber = findViewById(R.id.etPhoneNumber);
        mAddress = findViewById(R.id.etAddress);
        mBirthday = findViewById(R.id.etBirthday);
        mGender = findViewById(R.id.rgGender);
        mMale = findViewById(R.id.rbMale);
        mFemale = findViewById(R.id.rbFemale);
        mOther = findViewById(R.id.rbOther);
        mButtonSignUp = findViewById(R.id.btnSignUp);

        // authentication
        fAuth = FirebaseAuth.getInstance();

        // check Login or not
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        // Register button click
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)) {
                    mUsername.setError("You must enter a username.");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("You must enter password.");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Your password must be at least 6 characters.");
                    return;
                }

                // register the user in firebase
                fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}