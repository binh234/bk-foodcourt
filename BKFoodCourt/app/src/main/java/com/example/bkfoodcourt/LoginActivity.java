package com.example.bkfoodcourt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText mUsername, mPassword;
    TextView mSignUp;
    Button mButtonLogin;
    Spinner mUserChoice;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // start Login
        mUsername = findViewById(R.id.etUsername);
        mPassword = findViewById(R.id.etPassword);
        mButtonLogin = findViewById(R.id.btnLogin);
        mUserChoice = findViewById(R.id.spinnerUserChoice);
        mSignUp = findViewById(R.id.tvSignUp);
        fAuth = FirebaseAuth.getInstance();
        // Spinner
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this, R.array.user_choice, R.layout.support_simple_spinner_dropdown_item);
        Adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mUserChoice.setAdapter(Adapter);
        mUserChoice.setOnItemSelectedListener(this);

        // Login button click
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
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
                // authentication
                fAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                            String user = mUserChoice.getSelectedItem().toString();
                            if (user.equals(new String("Cook"))) {
                                startActivity(new Intent(getApplicationContext(), Cook.class));
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Sign up nevigation
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }

    // method for Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}

