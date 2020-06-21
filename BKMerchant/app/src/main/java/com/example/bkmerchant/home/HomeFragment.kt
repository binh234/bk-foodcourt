package com.example.bkmerchant.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.R
import com.example.bkmerchant.accountActivity.AccountActivity
import com.example.bkmerchant.databinding.HomeFragmentBinding
import com.example.bkmerchant.storeActivity.StoreActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.changeStoreInfo.setOnClickListener {
            navigateToStoreDetailFragment()
        }
        binding.menuCard.setOnClickListener { navigateToMenuFragment() }
        binding.bottomNav.selectedItemId = R.id.nav_home
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            bottomNavigationItemSelected(item)
        }
        binding.bottomNav.selectedItemId = R.id.nav_home
        return binding.root
    }

    private fun bottomNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_account -> {
                val intent = Intent(context, AccountActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_store -> {
                val intent = Intent(context, StoreActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun navigateToMenuFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToMenuFragment()
        action.storeId = viewModel.currentStore.value?.id ?: ""
        findNavController().navigate(action)
    }

    private fun navigateToStoreDetailFragment() {
    }
}