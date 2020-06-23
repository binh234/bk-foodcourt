package com.example.bk_foodcourt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AddRemoveVendorActivity : AppCompatActivity() {
    private val PICKIMAGEREQUEST : Int = 1
    private var viewPager: ViewPager2? = null
    private val chooseVendorImageButton : Button = findViewById(R.id.chooseVendorImage)
    private val vendorImageView : ImageView = findViewById(R.id.vendorImageView)
    private val vendorImageUpload : Button = findViewById(R.id.uploadImageButton)
    private lateinit var imageUri : Uri
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
        chooseVendorImageButton.setOnClickListener(){
            openFileChooser()
        }

    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        ActivityCompat.startActivityForResult(this,intent,PICKIMAGEREQUEST,null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKIMAGEREQUEST && resultCode == RESULT_OK
            && data != null && data.data != null) {
            imageUri = data.data!!
            vendorImageView.setImageURI(imageUri)
        }
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
