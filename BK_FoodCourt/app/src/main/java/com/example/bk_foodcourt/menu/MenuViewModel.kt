package com.example.bk_foodcourt.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MenuViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser!!
    val categories = MutableLiveData<List<String>>()
    var orderDiscountValue = 0.0
    var orderDiscountPercent = 0.0

    val isEmptyCart = MutableLiveData<Boolean>()

    companion object {
        const val TAG = "MenuViewModel"
    }

    init {
        Log.d("MenuViewModel", "Create view model")
    }

    val loadDiscountDone = MutableLiveData<Boolean>()
    val openDishEvent = MutableLiveData<Dish>()

    fun checkEmptyCart() {
        firestore.collection("users")
            .document(currentUser.uid)
            .collection("cart")
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    isEmptyCart.value = true
                } else {
                    isEmptyCart.value = false
                }
            }
    }

    fun openDish(dish: Dish) {
        openDishEvent.value = dish
    }

    fun getCategoryList(storeId: String) {
        firestore.collection("stores")
            .document(storeId)
            .collection("categories")
            .orderBy("priority", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.d(TAG, exception.toString())
                } else if (querySnapshot != null) {
                    val list = mutableListOf<String>()
                    for (document in querySnapshot) {
                        val name = document.getString("name") ?: ""
                        list.add(name)
                    }
                    categories.value = list
                }
            }
    }

    fun getOrderDiscount(storeId: String) {
        firestore.collection("stores")
            .document(storeId)
            .collection("promotions")
            .whereEqualTo("scope", 0)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val promo = document.toObject(Promotion::class.java)
                    if (promo.type == 0) {
                        orderDiscountPercent += promo.value
                    } else {
                        orderDiscountValue += promo.value
                    }
                }
                loadDiscountDone.value = true
            }
    }
}