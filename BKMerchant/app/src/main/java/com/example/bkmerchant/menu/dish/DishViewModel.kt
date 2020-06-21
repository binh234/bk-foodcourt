package com.example.bkmerchant.menu.dish

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bkmerchant.menu.Category
import com.example.bkmerchant.menu.Dish
import com.example.bkmerchant.menu.category.CategoryViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage

class DishViewModel(val dish: Dish): ViewModel() {
    companion object {
        const val TAG = "DishViewModel"
    }

    private val firestore = FirebaseFirestore.getInstance()

    var name = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var price = MutableLiveData<String>()
    var imageUrl = ""

    var categories = mutableListOf<Category>()
    var catList = MutableLiveData<List<String>>()
    var currentCategoryId = ""

    val emptyNameField = MutableLiveData<String>()
    val emptyPriceField = MutableLiveData<String>()

    var navigateToMenuFragment = MutableLiveData<Boolean>()

    init {
        if (dish.id.isNotEmpty()) {
            bind()
        }
        getCategoryList()
    }

    private fun bind() {
        Log.d(TAG, dish.id)
        Log.d(TAG, dish.categoryId)
        Log.d(TAG, dish.storeId)
        name.value = dish.name
        description.value = dish.description
        price.value = dish.price.toString()
        imageUrl = dish.imageUrl
        currentCategoryId = dish.categoryId
    }

    fun saveDish(url: String, index: Int) {
        if (url.isNotEmpty() && imageUrl.isNotEmpty()) {
            FirebaseStorage.getInstance()
                .getReferenceFromUrl(imageUrl)
                .delete()
        }
        dish.name = name.value ?: ""
        dish.description = description.value ?: ""
        dish.price = (price.value ?: "0").toDouble()
        dish.availability = true
        dish.categoryId = categories[index].id
        dish.imageUrl = url
        if (dish.id.isNotEmpty()) {
            updateDish()
        } else {
            addDish()
        }
    }

    private fun addDish() {
        firestore.collection("stores")
            .document(dish.storeId)
            .collection("categories")
            .document(dish.categoryId)
            .collection("items")
            .add(dish)
            .addOnSuccessListener {
                navigateToMenuFragment.value = true
            }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            }
    }

    private fun updateDish() {
        if (currentCategoryId != dish.categoryId) {
            firestore.collection("stores")
                .document(dish.storeId)
                .collection("categories")
                .document(currentCategoryId)
                .collection("items")
                .document(dish.id)
                .delete()
        }
        firestore.collection("stores")
            .document(dish.storeId)
            .collection("categories")
            .document(dish.categoryId)
            .collection("items")
            .document(dish.id)
            .set(dish)
            .addOnSuccessListener {
                navigateToMenuFragment.value = true
            }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            }
    }

    private fun getCategoryList(){
        val returnList = mutableListOf<String>()
        firestore.collection("stores")
            .document(dish.storeId)
            .collection("categories")
            .get()
            .addOnSuccessListener {querySnapshot ->
                if (querySnapshot != null) {
                    for (snapshot in querySnapshot) {
                        val item = snapshot.toObject(Category::class.java)
                        item.id = snapshot.id

                        categories.add(item)
                        returnList.add(item.name)
                        Log.d(TAG, returnList[returnList.size-1])
                    }
                    catList.value = returnList.toList()
                } else {
                    Log.d(TAG, "empty result")
                }
            }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            }
    }
}