package com.example.bk_foodcourt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageButton


class Manager() : AppCompatActivity(){
    private val openStatReport = 1
    private val openAddRemoveVendor = 1

    constructor(parcel: Parcel) : this() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        val statReportImageButton : ImageButton =findViewById(R.id.statReport)
        statReportImageButton.setOnClickListener{
            val intent = Intent(this, StatReportActivity::class.java)
            startActivityForResult(intent, openStatReport)
        }
        val addRemoveVendorButton : ImageButton = findViewById(R.id.addRemoveVendorButton)
        addRemoveVendorButton.setOnClickListener{
            val intent = Intent(this,AddRemoveVendorActivity::class.java)
            startActivityForResult(intent,openAddRemoveVendor)
        }
}
}
