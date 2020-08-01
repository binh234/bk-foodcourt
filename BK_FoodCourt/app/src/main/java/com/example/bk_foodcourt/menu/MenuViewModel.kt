package com.example.bk_foodcourt.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage

class MenuViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    val categories = MutableLiveData<List<String>>()
    var orderDiscountValue = 0.0
    var orderDiscountPercent = 0.0

    companion object {
        const val TAG = "MenuViewModel"
    }

    init {
        Log.d("MenuViewModel", "Create view model")
    }

    val loadDiscountDone = MutableLiveData<Boolean>()
    val openDishEvent = MutableLiveData<Dish>()

    fun deleteCategory(category: Category) {
        Log.d(TAG, "id: ${category.id}")
        val task1 = firestore.collection("stores")
            .document(category.storeId)
            .collection("categories")
            .document(category.id)
            .collection("items")
            .get()

        task1.addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot) {
                val imageUrl = document.getString("imageUrl")
                if (imageUrl != null) {
                    if (imageUrl.isNotEmpty()) {
                        FirebaseStorage.getInstance()
                            .getReferenceFromUrl(imageUrl)
                            .delete()
                            .addOnSuccessListener {
                                document.reference.delete()
                            }
                    } else {
                        document.reference.delete()
                    }
                }
            }
            Log.d(TAG, "delete item list success")
        }

        Tasks.whenAllSuccess<Task<QuerySnapshot>>(task1)
            .addOnSuccessListener {
                firestore.collection("stores")
                    .document(category.storeId)
                    .collection("categories")
                    .document(category.id)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "delete item success")
                    }
                    .addOnFailureListener {
                        Log.d(TAG, it.toString())
                    }
            }
    }

    fun deleteDish(dish: Dish) {
        Log.d(TAG, "id: ${dish.id}")
        if (dish.imageUrl.isNotEmpty()) {
            FirebaseStorage.getInstance()
                .getReferenceFromUrl(dish.imageUrl)
                .delete()
        }
        firestore.collection("stores")
            .document(dish.storeId)
            .collection("categories")
            .document(dish.categoryId)
            .collection("items")
            .document(dish.id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "delete item success")
            }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
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