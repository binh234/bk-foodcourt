package com.example.foodcourtreport_man

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyAdapter(private val myContext: Context, fa: FragmentActivity, private var totalTabs: Int) : FragmentStateAdapter(fa) {

    // this is for fragment tabs
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AddVendorFragment.newInstance()
            }
            1 -> {
                RemoveVendorFragment()
            }
            else ->AddVendorFragment()
        }
    }

    // this counts total number of tabs
    override fun getItemCount(): Int {
        return totalTabs
    }
}