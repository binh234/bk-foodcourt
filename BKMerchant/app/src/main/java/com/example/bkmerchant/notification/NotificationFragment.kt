package com.example.bkmerchant.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bkmerchant.R
import com.example.bkmerchant.menu.Dish
import com.google.firebase.firestore.FirebaseFirestore

class NotificationFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.notification_fragment, container, false)
    }
}