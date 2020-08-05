package com.example.managertab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firestore = FirebaseFirestore.getInstance()
        val signUpButton : Button = findViewById(R.id.custom_signup_button)
        val emailEditText : EditText = findViewById(R.id.email_edittext)
        val passwordEditText: EditText = findViewById(R.id.password_edittext)
        val inputEmail : String = emailEditText.text.toString()
        val inputPassword : String = passwordEditText.text.toString()
        signUpButton.setOnClickListener{
                pushAccount(inputEmail,inputPassword)
        }
        val signInButton : Button = findViewById(R.id.custom_signin_button)
        signInButton.setOnClickListener{
            checkPermissionAndLogin(inputEmail,inputPassword)
        }
    }
    private fun pushAccount(email:String,password:String){
    }
    private fun checkPermissionAndLogin(email: String, password: String) {
        firestore.collection("userTypes")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) {
                    Toast.makeText(
                        this,
                        "No permission",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    for (document in query) {
                        val userType = document.toObject(UserType::class.java)
                        if (userType.accountType == AccountType.VENDOR_OWNER) {
                            firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        if (!userType.update) {
                                            val currentUser = firebaseAuth.currentUser
                                            if (currentUser != null) {
                                                firestore.collection("users")
                                                    .document(currentUser.uid)
                                                    .update(
                                                        "accountType",
                                                        AccountType.VENDOR_OWNER
                                                    )
                                            }
                                            firestore.collection("userTypes")
                                                .document(document.id)
                                                .update("update", true)
                                        }
                                        Toast.makeText(
                                            this,
                                            "Login Success",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        startManagerActivity()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Wrong password",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "No permission. Try again",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            }
    }
    private fun startManagerActivity(){
        val intent = Intent(this, Manager::class.java)
        startActivity(intent)
        this?.finish()
    }
}