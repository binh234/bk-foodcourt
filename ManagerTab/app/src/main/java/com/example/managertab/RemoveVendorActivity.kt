package com.example.managertab

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_remove_vendor.*


class RemoveVendorActivity : AppCompatActivity() {
    private lateinit var  firebaseDatabase : FirebaseDatabase

    private lateinit var  firebaseStorage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_vendor)
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        //val storeItemList = readData()
        //recycler_view.adapter = StoreItemAdapter(storeItemList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    /*
    interface MyCallback {
        fun onCallback(value: ArrayList<StoreItem>)
    }
    fun readData(myCallback : MyCallback) {
        storeReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list = ArrayList<StoreItem>()
                for (document in task.result!!) {
                    val storeName = document.data["name"].toString()
                    val storeOpenTime : String = document.data["openTime"].toString()
                    val storeCloseTime : String = document.data["closeTime"].toString()
                    val storeEmail : String = document.data["supportEmail"].toString()
                    val storeToAdd : StoreItem = StoreItem(storeName,storeOpenTime.toInt(),storeCloseTime.toInt(),storeEmail)
                    list.add(storeToAdd)
                }
                myCallback.onCallback(list)
            }
        }
    }
    private suspend fun getListOfStores(): List<DocumentSnapshot> {
        val snapshot = storeReference.get().await()
        return snapshot.documents
    }
    private suspend fun getDataFromFirestore() : ArrayList<StoreItem> {
        val storeList : ArrayList<StoreItem>  = getListOfStores()
    }
    */


    /*
    private fun getStoreList() : List<StoreItem>{
        val list = ArrayList<StoreItem>()
        var imageUri : String = ""
        var imageRef = firebaseStorage.reference.child("1593356786662.jpg")
        imageRef.downloadUrl.addOnSuccessListener {
            imageUri = it.toString()
        }

        storeReference.orderByChild("storeId")
        var store : StoreItem = StoreItem("",0,0,"")
        val valueEventListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children) {
                    store.ImageResource = imageUri
                    store.CloseTime = ds.child("closeTime").value as Int
                    store.OpenTime = ds.child("opentTime").value as Int
                    store.SupportEmail = ds.child("supportEamil").value.toString()
                    list.add(store)
                }
            }

        }
        return list
    }
     */
}

