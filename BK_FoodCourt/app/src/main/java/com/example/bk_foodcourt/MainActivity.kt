package com.example.bk_foodcourt

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    var fAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fAuth = FirebaseAuth.getInstance()

        // check Login or not
        if (fAuth!!.currentUser == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    fun logout(view: View?) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    fun add_to_list(view: View) {}
}