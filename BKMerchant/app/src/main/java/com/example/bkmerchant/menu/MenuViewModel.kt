package com.example.bkmerchant.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class MenuViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var menuListener: ListenerRegistration

    companion object {
        const val TAG = "MenuViewModel"
    }

    init {
        Log.d("MenuViewModel", "Create view model")
    }

    val openCategoryEvent = MutableLiveData<Category>()
    val openDishEvent = MutableLiveData<Dish>()

    fun deleteCategory(category: Category) {
        Log.d(TAG, "id: ${category.id}")
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

    fun openCategory(category: Category) {
        openCategoryEvent.value = category
    }

    fun openDish(dish: Dish) {
        openDishEvent.value = dish
    }

    fun changeDishStatus(dish: Dish) {
        firestore.collection("stores")
            .document(dish.storeId)
            .collection("categories")
            .document(dish.categoryId)
            .collection("items")
            .document(dish.id)
            .update("availability", !dish.availability)
    }

}