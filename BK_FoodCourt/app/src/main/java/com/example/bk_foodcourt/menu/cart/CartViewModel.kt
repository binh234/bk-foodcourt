package com.example.bk_foodcourt.menu.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bk_foodcourt.menu.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartViewModel: ViewModel() {
    private var firestore = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser!!

    var total = MutableLiveData(0.0)
    var applicableFee = MutableLiveData(0.0)
    var promotion = MutableLiveData(0.0)

    init {
        calOrderTotal()
    }

    fun calOrderTotal() {
        firestore.collection("users")
            .document(currentUser.uid)
            .collection("cart")
            .get()
            .addOnSuccessListener { querySnapshot ->
                var subTotal = 0.0
                for (document in querySnapshot) {
                    val item = document.toObject(CartItem::class.java)
                    subTotal += item.total
                }
                total.value = subTotal * 1.1
                applicableFee.value = subTotal / 10
                Log.d("CartViewModel", total.value.toString())
                Log.d("CartViewModel", subTotal.toString())
            }
    }
}