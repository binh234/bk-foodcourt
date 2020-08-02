package com.example.bk_foodcourt.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bk_foodcourt.Cook
import com.example.bk_foodcourt.Manager
import com.example.bk_foodcourt.R
import com.example.bk_foodcourt.databinding.LoginFragmentBinding
import com.example.bk_foodcourt.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

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
            binding.emailText.error = getString(R.string.empty_field)
            binding.emailText.requestFocus()
        } else if (password.length < 6) {
            binding.passwordText.error = getString(R.string.password_condition)
            binding.passwordText.requestFocus()
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val accountType = binding.accountType.selectedItem.toString()
            checkPermissionAndLogin(email, password, accountType.toUpperCase(Locale.ROOT))
        } else {
            binding.emailText.error = getString(R.string.invalid_email)
            binding.emailText.requestFocus()
        }
    }

    private fun checkPermissionAndLogin(email: String, password: String, accountType: String) {
        if (accountType == "CUSTOMER") {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            getString(R.string.login_success),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        startMainActivity(accountType)
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.wrong_password),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        } else {
            firestore.collection("userTypes")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener { query ->
                    if (query.isEmpty) {
                        Toast.makeText(
                            context,
                            getString(R.string.no_permission),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {
                        for (document in query) {
                            val userType = document.toObject(UserType::class.java)
                            if (userType.accountType == accountType) {
                                firebaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            if (!userType.update) {
                                                val currentUser = firebaseAuth.currentUser!!
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
                                                getString(R.string.login_success),
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            startMainActivity(accountType)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                getString(R.string.wrong_password),
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.no_permission),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                }
        }
    }

    private fun resetPassword() {
        val email = binding.emailText.text.toString()
        if (email.isEmpty()) {
            binding.emailText.error = getString(R.string.empty_field)
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        getString(R.string.reset_password),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, getString(R.string.invalid_email), Toast.LENGTH_LONG)
                        .show()
                }
        } else {
            binding.emailText.error = getString(R.string.invalid_email)
            binding.emailText.requestFocus()
        }
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(R.id.registerFragment)
    }

    private fun startMainActivity(accountType: String) {
        var intent = Intent()
        if (accountType == "CUSTOMER") {
            intent = Intent(context, HomeActivity::class.java)
        } else if (accountType == "COOK") {
            intent = Intent(context, Cook::class.java)
        } else if (accountType == "MANAGER"){
            intent = Intent(context, Manager::class.java)
        }
        startActivity(intent)
        activity?.finish()
    }
}