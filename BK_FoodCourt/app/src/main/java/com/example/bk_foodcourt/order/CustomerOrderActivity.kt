package com.example.bk_foodcourt.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.bk_foodcourt.R
import com.example.bk_foodcourt.databinding.ActivityCustomerOrderBinding
import com.example.bk_foodcourt.home.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomerOrderActivity : AppCompatActivity() {
    private lateinit var mOrderNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_order)
        mOrderNav = findViewById(R.id.orderNav)
        mOrderNav.selectedItemId = R.id.nav_orderlist
        mOrderNav.setOnNavigationItemSelectedListener { bottomNavigationItemSelected(it) }

        val navController = this.findNavController(R.id.order_nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.order_nav_host_fragment)
        return navController.navigateUp()
    }

    private fun bottomNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                finish()
            }
        }
        return true
    }
}