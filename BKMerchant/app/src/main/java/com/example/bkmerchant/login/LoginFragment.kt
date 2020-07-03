package com.example.bkmerchant.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.MainActivity
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.login)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (firebaseAuth.currentUser != null) {
            startMainActivity()
        }

        binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener { login() }
        binding.createAccountButton.setOnClickListener { navigateToRegisterFragment() }
        binding.forgotPassword.setOnClickListener { resetPassword() }

        return binding.root;
    }

    private fun login() {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if (email.isEmpty()) {
            binding.emailText.error = "Empty email"
            binding.emailText.requestFocus()
        } else if (password.length < 6) {
            binding.passwordText.error = "Password must be at least 6 characters"
            binding.passwordText.requestFocus()
        } else {
            checkPermissionAndLogin(email, password)
        }
    }

    private fun checkPermissionAndLogin(email: String, password: String) {
        firestore.collection("userTypes")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) {
                    Toast.makeText(
                        context,
                        "Sorry, you don't have permission to use this app",
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
                                            context,
                                            "Login successful",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        startMainActivity()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Login failed, please try again",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                context,
                                "Sorry, you don't have permission to use this app",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            }
    }

    private fun resetPassword() {
        val email = binding.emailText.text.toString()
        if (email.isEmpty()) {
            binding.emailText.error = "Empty email"
        } else {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Please check your email to set new password",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(R.id.registerFragment)
    }

    private fun startMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}