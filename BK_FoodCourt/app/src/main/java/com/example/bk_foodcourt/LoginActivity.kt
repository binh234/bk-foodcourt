package com.example.bk_foodcourt

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), OnItemSelectedListener {
    var mUsername: EditText? = null
    var mPassword: EditText? = null
    var mSignUp: TextView? = null
    var mButtonLogin: Button? = null
    var mUserChoice: Spinner? = null
    var fAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // start Login
        mUsername = findViewById(R.id.etUsername)
        mPassword = findViewById(R.id.etPassword)
        mButtonLogin = findViewById(R.id.btnLogin)
        mUserChoice = findViewById(R.id.spinnerUserChoice)
        mSignUp = findViewById(R.id.tvSignUp)
        fAuth = FirebaseAuth.getInstance()
        // Spinner
        val Adapter = ArrayAdapter.createFromResource(
            this,
            R.array.user_choice,
            R.layout.support_simple_spinner_dropdown_item
        )
        Adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        mUserChoice!!.setAdapter(Adapter)
        mUserChoice!!.setOnItemSelectedListener(this)

        // Login button click
        mButtonLogin!!.setOnClickListener(View.OnClickListener {
            val username = mUsername!!.getText().toString().trim { it <= ' ' }
            val password = mPassword!!.getText().toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(username)) {
                mUsername!!.setError("You must enter a username.")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                mPassword!!.setError("You must enter password.")
                return@OnClickListener
            }
            // authentication
            fAuth!!.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Sign in successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val user = mUserChoice!!.getSelectedItem().toString()
                        if (user == "Cook") {
                            startActivity(Intent(applicationContext, Cook::class.java))
                            finish()
                        }
                        else if (user == "Manager") {
                            startActivity(Intent(applicationContext, Manager::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error! " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })

        // Sign up nevigation
        mSignUp!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()
        })
    }

    // method for Spinner
    override fun onItemSelected(
        adapterView: AdapterView<*>,
        view: View,
        i: Int,
        l: Long
    ) {
        val choice = adapterView.getItemAtPosition(i).toString()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
}