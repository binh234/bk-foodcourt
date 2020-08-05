package com.example.managertab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private String account;
    private Button btn_login;
    private TextView link_to_register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        btn_login = findViewById(R.id.btnLogin);
        link_to_register = findViewById(R.id.tvLinkToRegister);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Login failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        link_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
            mFirestore.collection("userTypes").whereEqualTo("email", user.getEmail()).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(LoginActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    User userType = document.toObject(User.class);
                                    Toast.makeText(LoginActivity.this, userType.toString(), Toast.LENGTH_SHORT).show();
                                    account = userType.getAccountType();
                                }
                                if (account == "COOK") {
                                    Toast.makeText(LoginActivity.this, "Cook login!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, CookActivity.class);
                                    startActivity(intent);

                                }
                                else if (account == "MANAGER") {
                                    Toast.makeText(LoginActivity.this, "Manager login!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, Manager.class);
                                    startActivity(intent);
                                }
                                Toast.makeText(LoginActivity.this, "HERE2", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}
