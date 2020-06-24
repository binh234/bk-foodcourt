package com.example.bkmerchant.accountActivity

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
import com.example.bkmerchant.databinding.PasswordFragmentBinding
import com.example.bkmerchant.login.AccountType
import com.example.bkmerchant.login.UserType
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class PasswordFragment : Fragment() {
    private lateinit var binding: PasswordFragmentBinding
    private lateinit var currentUser: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.password_setting)

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        binding = PasswordFragmentBinding.inflate(inflater, container, false)
        binding.forgotPassword.setOnClickListener { resetPassword() }
        binding.saveButton.setOnClickListener { savePassword() }

        return binding.root;
    }

    private fun savePassword() {
        val currentPassword = binding.currentPassword.text.toString()
        val newPassword = binding.newPassword.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        currentUser.email?.let {
            val credential = EmailAuthProvider.getCredential(it, currentPassword)
            currentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    if (newPassword.length < 6) {
                        binding.newPassword.error = "Password must have at least 6 characters"
                    } else if (newPassword != confirmPassword) {
                        binding.confirmPassword.error = "Password not match"
                    } else {
                        currentUser.updatePassword(newPassword)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Update password successful",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    binding.currentPassword.error = "Wrong password"
                }
        }
    }

    private fun resetPassword() {
        currentUser.email?.let {
            firebaseAuth.sendPasswordResetEmail(it)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Please check your email to set new password",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Some error occurred, please try again", Toast.LENGTH_LONG)
                        .show()
                }
        }
    }
}