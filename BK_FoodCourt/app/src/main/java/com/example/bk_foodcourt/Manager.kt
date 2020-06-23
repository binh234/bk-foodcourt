package com.example.bk_foodcourt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageButton


class Manager() : AppCompatActivity() {
    private val openStatReport = 1
    private val openAddRemoveVendor = 1

    constructor(parcel: Parcel) : this() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        // Open Stat Report from the corresponding Image Button and Button
        val statReportImageButton: ImageButton = findViewById(R.id.statReport)
        statReportImageButton.setOnClickListener {
            openStatReportClick()
        }
        val statReportButton: Button = findViewById(R.id.stat_button)
        statReportButton.setOnClickListener(){
            openStatReportClick()
        }
        // Open Add Remove Vendor from the corresponding Image Button and Button
        val addRemoveVendorImageButton: ImageButton = findViewById(R.id.addRemoveVendorButton)
        addRemoveVendorImageButton.setOnClickListener {
            openAddRemoveClick()
        }
        val addRemoveVendorButton : Button = findViewById(R.id.addRemoveButton)
        addRemoveVendorButton.setOnClickListener(){
            openAddRemoveClick()
        }
        // Open Top 5 Vendor List from the corresponding Image Button and Button


        // Open Manage Vendor from the corresponding Image Button and Button
    }

    private fun openAddRemoveClick() {
        val intent = Intent(this, AddRemoveVendorActivity::class.java)
        startActivityForResult(intent, openAddRemoveVendor)
    }

    private fun openStatReportClick() {
        val intent = Intent(this, StatReportActivity::class.java)
        startActivityForResult(intent, openStatReport)
    }
}
