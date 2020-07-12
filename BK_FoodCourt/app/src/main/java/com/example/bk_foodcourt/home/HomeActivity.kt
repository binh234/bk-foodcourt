package com.example.bk_foodcourt.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.bk_foodcourt.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private var mMainNav: BottomNavigationView? = null
    private var mMainFrame: FrameLayout? = null
    private var storeFragment: Fragment? = null
    private var orderListFragment: Fragment? = null
    private var notiFragment: Fragment? = null
    private var profileFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mMainNav = findViewById(R.id.mainNav)
        mMainFrame = findViewById(R.id.mainFrame)
        storeFragment = StoreFragment()
        orderListFragment = OrderListFragment()
        notiFragment = NotificationFragment()
        profileFragment = ProfileFragment()
        setFragment(storeFragment)
        mMainNav!!.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    setFragment(storeFragment)
                    true
                }
                R.id.nav_orderlist -> {
                    setFragment(orderListFragment)
                    true
                }
                R.id.nav_notification -> {
                    setFragment(notiFragment)
                    true
                }
                R.id.nav_profile -> {
                    setFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setFragment(fragment: Fragment?) {
        val fragmentTransaction =
            supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, fragment!!)
        fragmentTransaction.commit()
    }
}