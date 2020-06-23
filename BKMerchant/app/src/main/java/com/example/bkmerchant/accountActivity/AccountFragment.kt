package com.example.bkmerchant.accountActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.MainActivity
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.AccountFragmentBinding
import com.example.bkmerchant.databinding.StoreFragmentBinding
import com.example.bkmerchant.login.LoginActivity
import com.example.bkmerchant.storeActivity.StoreActivity
import com.example.bkmerchant.storeActivity.store.StoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AccountFragment: Fragment() {
    private lateinit var binding: AccountFragmentBinding
    private lateinit var currentUser: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.account_setting)

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        binding = AccountFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.bottomNav.selectedItemId = R.id.nav_account
        binding.bottomNav.setOnNavigationItemSelectedListener {
            bottomNavigationItemSelected(it)
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
        binding.termPrivacy.setOnClickListener {
            navigateToTermFragment()
        }
        binding.aboutUs.setOnClickListener {
            navigateToAboutFragment()
        }
        binding.changePassword.setOnClickListener {
            navigateToPasswordFragment()
        }

        return binding.root
    }

    private fun bottomNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                activity?.finish()
            }
            R.id.nav_store -> {
                val intent = Intent(context, StoreActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        return true
    }

    private fun logout() {
        firebaseAuth.signOut()
        startLoginActivity()
    }

    private fun startLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToTermFragment() {
        findNavController().navigate(R.id.termFragment)
    }

    private fun navigateToAboutFragment() {
        findNavController().navigate(R.id.aboutFragment)
    }

    private fun navigateToPasswordFragment() {
        findNavController().navigate(R.id.passwordFragment)
    }
}