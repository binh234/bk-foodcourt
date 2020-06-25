package com.example.bk_foodcourt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI


class AddRemoveVendorActivity : AppCompatActivity() {
    private var viewPager: ViewPager2? = null
    private var filepath : Uri? = null
    private var storage: FirebaseStorage? = null
       private var storageReference : StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remove_vendor)
        viewPager = findViewById<ViewPager2>(R.id.pager)
        //Init firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        //Setup button

        viewPager!!.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return if (position == 0) {
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
            if (position == 0) {
                tab.text = "Add New Vendor"
            } else {
                tab.text = "Remove a Vendor"
            }
        }.attach()
    }

}

class AddVendorFragment : Fragment() {
    private val pickImageRequest : Int = 1
    private var vendorImageView : ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater!!.inflate(R.layout.fragment_add_vendor, container, false)

        val chooseImageButton : Button = view.findViewById(R.id.chooseVendorImage)
        vendorImageView = view.findViewById(R.id.vendorImageView)
        val progressBar : ProgressBar = view.findViewById(R.id.progress_image)
        chooseImageButton.setOnClickListener{
            openFileChooser()
        }
        return inflater.inflate(R.layout.fragment_add_vendor, container, false)
    }
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, pickImageRequest)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            val imageURI : Uri = data.data!!
            vendorImageView!!.setImageURI(imageURI)
        }
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
