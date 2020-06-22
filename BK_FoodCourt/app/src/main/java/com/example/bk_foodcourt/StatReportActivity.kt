package com.example.bk_foodcourt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.bk_foodcourt.R

class StatReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat_report)
        val vendorChartButton: ImageButton = findViewById(R.id.Vendor_Chart_Button)
        val itemChartButton: ImageButton = findViewById(R.id.Item_Chart_Button)
        vendorChartButton.setOnClickListener{

        }

    }

}