package com.example.bkmerchant.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.MainActivity
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (firebaseAuth.currentUser != null) {
            startMainActivity()
        }

        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.loginButton.setOnClickListener { login() }
        binding.registerTextView.setOnClickListener { navigateToRegisterFragment() }

        return binding.root;
    }

    private fun login() {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if (email.isEmpty()) {
            binding.emailText.setError("Empty email")
            binding.emailText.requestFocus()
        } else if (password.isEmpty()) {
            binding.passwordText.setError("Empty password")
            binding.passwordText.requestFocus()
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null) {
                        currentUser.email?.let { email ->
                            firestore.collection("userTypes")
                                .whereEqualTo("email", email)
                                .limit(1)
                                .get()
                                .addOnSuccessListener { query ->
                                    if (query.isEmpty) {
                                        firebaseAuth.signOut()
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
                                                if (!userType.update) {
                                                    firestore.collection("users")
                                                        .document(currentUser.uid)
                                                        .update(
                                                            "accountType",
                                                            AccountType.VENDOR_OWNER
                                                        )
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
                                                firebaseAuth.signOut()
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
                    }
                } else {
                    Toast.makeText(context, "Login failed, please try again", Toast.LENGTH_SHORT)
                        .show()
                }
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