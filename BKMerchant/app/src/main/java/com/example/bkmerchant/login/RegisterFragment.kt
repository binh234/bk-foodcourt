package com.example.bkmerchant.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    companion object {
        const val TAG = "RegisterFragment"
        const val REGISTER_RESULT_CODE = 1003
    }

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userCollection: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.register)

        firebaseAuth = FirebaseAuth.getInstance()
        userCollection = FirebaseFirestore.getInstance().collection("users")

        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        binding.registerButton.setOnClickListener { registerNewAccount() }
        binding.signInTextView.setOnClickListener { navigateToLoginFragment() }
        return binding.root;
    }

    private fun registerNewAccount() {
        val name = binding.nameText.text.toString().trim()
        val email = binding.emailText.text.toString().trim()
        val password = binding.passwordText.text.toString()
        val confirmPassword = binding.confirmPasswordText.text.toString()

        if (email.isEmpty()) {
            binding.nameText.error = getString(R.string.empty_field)
        } else if (email.isEmpty()) {
            binding.emailText.error = getString(R.string.empty_field)
        } else if (password.length < 6) {
            binding.passwordText.error = getString(R.string.password_condition)
        } else if (confirmPassword != password) {
            binding.confirmPasswordText.error = getString(R.string.password_not_match)
            binding.confirmPasswordText.requestFocus()
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = User(name, email = email)
                        task.result?.user?.uid?.let {
                            userCollection.document(it).set(user)
                        }
                        Toast.makeText(
                            context,
                            getString(R.string.register_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToLoginFragment()
                    } else {
                        val exception = task.exception
                        var errorMessage = ""

                        if (exception != null) {
                            when (exception) {
                                is FirebaseAuthWeakPasswordException -> binding.passwordText.error =
                                    getString(
                                        R.string.weak_password
                                    )
                                is FirebaseAuthUserCollisionException -> errorMessage =
                                    getString(R.string.email_registered)
                                is FirebaseAuthInvalidCredentialsException -> errorMessage =
                                    getString(R.string.invalid_email)
                                else -> errorMessage = getString(R.string.register_fail)
                            }
                        }
                        if (errorMessage.isNotEmpty()) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    private fun navigateToLoginFragment() {
        firebaseAuth.signOut()
        findNavController().navigate(R.id.loginFragment)
    }
}