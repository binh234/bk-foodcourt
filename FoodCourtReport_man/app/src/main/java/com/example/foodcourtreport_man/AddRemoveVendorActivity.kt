package com.example.foodcourtreport_man

import android.app.ActivityManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AddRemoveVendorActivity : AppCompatActivity() {
    var viewPager: ViewPager2? = null
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remove_vendor)

        viewPager = findViewById<ViewPager2>(R.id.pager)
        viewPager!!.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int) : Fragment {
                return if (position == 0){
                    AddVendorFragment.newInstance()
                } else {
                    RemoveVendorFragment.newInstance()
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager!!) { tab, position ->
            if (position == 0){
                tab.text = "Add New Vendor"
            }
            else {
                tab.text = "Remove a Vendor"
            }
        }.attach()
    }
}
class AddVendorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_add_vendor, container, false)
    }
    companion object {
        fun newInstance(): AddVendorFragment = AddVendorFragment()
    }
}
class RemoveVendorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_remove_vendor, container, false)
    }
    companion object {
        fun newInstance(): RemoveVendorFragment = RemoveVendorFragment()
    }
}