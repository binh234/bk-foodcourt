package com.example.bkmerchant.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bkmerchant.login.User
import com.example.bkmerchant.storeActivity.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeViewModel : ViewModel() {
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var firestore = FirebaseFirestore.getInstance()

    var errorMessage = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var currentStore = MutableLiveData(Store())

    init {
        getUserName()
        getCurrentStore()
    }

    private fun getUserName() {
        if (null != currentUser) {
            firestore.collection("users")
                .document(currentUser!!.uid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    userName.value = "Welcome,\n " + user?.name
                }

        } else {
            userName.value = "Error occurred"
        }
    }

    private fun getCurrentStore() {
        if (null != currentUser) {
            firestore.collection("stores")
                .whereEqualTo("ownerID", currentUser!!.uid)
                .orderBy("isFocus", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener {query ->
                    if (query.isEmpty) {
                        errorMessage.value = "You don't have any store!"
                    } else {
                        for (document in query) {
                            currentStore.value = document.toObject(Store::class.java)
                            currentStore.value!!.id = document.id
                            if (!currentStore.value!!.isFocus) {
                                firestore.collection("stores")
                                    .document(document.id)
                                    .update("isFocus", true)
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    errorMessage.value = it.toString()
                }
        }
    }
}