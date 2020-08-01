package com.example.bkmerchant.accountActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.PasswordFragmentBinding
import com.example.bkmerchant.login.LoginActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
                        binding.newPassword.error = getString(R.string.password_condition)
                    } else if (newPassword != confirmPassword) {
                        binding.confirmPassword.error = getString(R.string.password_not_match)
                    } else {
                        currentUser.updatePassword(newPassword)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    getString(R.string.update_success),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                logout()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    binding.currentPassword.error = getString(R.string.wrong_password)
                }
        }
    }

    private fun resetPassword() {
        currentUser.email?.let {
            firebaseAuth.sendPasswordResetEmail(it)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        getString(R.string.reset_password),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    logout()
                }
                .addOnFailureListener {
                    Toast.makeText(context, getString(R.string.error_occur), Toast.LENGTH_LONG)
                        .show()
                }
        }
    }
    private fun logout() {
        firebaseAuth.signOut()
        startLoginActivity()
        requireActivity().finish()
    }

    private fun startLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}