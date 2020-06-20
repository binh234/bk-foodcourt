package com.example.foodcourtreport_man

import android.app.ActivityManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private val openStatReport = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statReportImageButton : ImageButton =findViewById(R.id.statReport)
        statReportImageButton.setOnClickListener{
            val intent = Intent(this, StatReportActivity::class.java)
            startActivityForResult(intent, openStatReport)

        }

    }



}