package com.example.bk_foodcourt

import android.app.ActivityManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.bk_foodcourt.R
import com.example.foodcourtreport_man.AddRemoveVendorActivity
import com.example.foodcourtreport_man.StatReportActivity

class Manager : AppCompatActivity() {
    private val openStatReport = 1
    private val openAddRemoveVendor = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statReportImageButton : ImageButton =findViewById(R.id.statReport)
        statReportImageButton.setOnClickListener{
            val intent = Intent(this, StatReportActivity::class.java)
            startActivityForResult(intent, openStatReport)
        }
        val addRemoveVendorButton : ImageButton = findViewById(R.id.addRemoveVendorButton)
        addRemoveVendorButton.setOnClickListener{
            val intent = Intent(this, AddRemoveVendorActivity::class.java)
            startActivityForResult(intent,openAddRemoveVendor)
        }

    }



}