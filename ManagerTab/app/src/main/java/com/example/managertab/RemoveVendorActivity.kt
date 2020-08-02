package com.example.managertab

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_remove_vendor.*


class RemoveVendorActivity : AppCompatActivity() {
    private lateinit var firebaseFireStore : FirebaseFirestore

    private lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_vendor)
        firebaseFireStore = FirebaseFirestore.getInstance()
        val storeItemList = mutableListOf<StoreItem>()
        // Codes to get stores from firebase and store it into the list
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        firebaseFireStore.collection("stores").orderBy("name")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val storeItem: StoreItem = document.toObject(StoreItem::class.java)
                        Log.d("STORENAME:","$storeItem.name" )
                        Toast.makeText(this, "$storeItem.name", Toast.LENGTH_SHORT).show()
                        storeItemList.add(storeItem)
                        Toast.makeText(this, "list size is:${storeItemList.size}", Toast.LENGTH_SHORT).show()
                    }
                    recycler_view.adapter = StoreItemAdapter(storeItemList)

                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to connect to Firebase.Check your connection", Toast.LENGTH_SHORT).show()
            }
        //
        Toast.makeText(this, "list: ${storeItemList.size}", Toast.LENGTH_SHORT).show()


    }
}
