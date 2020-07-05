package com.example.bkmerchant.menu.category

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bkmerchant.R
import com.example.bkmerchant.menu.Category
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CategoryViewModel(val category: Category) : ViewModel() {
    companion object {
        const val TAG = "CategoryViewModel"
    }

    private val promotionCollection = FirebaseFirestore.getInstance()
        .collection("stores")
        .document(category.storeId)
        .collection("promotions")
    private val categoryCollection = FirebaseFirestore.getInstance()
        .collection("stores")
        .document(category.storeId)
        .collection("categories")

    var name = MutableLiveData("")

    var nameFieldError = MutableLiveData<Int>()

    var navigateToMenuFragment = MutableLiveData<Boolean>()

    init {
        Log.d(TAG, "Create ViewModel")
        if (category.id.isNotEmpty()) {
            name.value = category.name
        }
    }

    fun saveCategory() {
        val catName = name.value ?: ""

        if (catName.trim().isNotEmpty()) {
            if (catName.trim() == category.name) {
                navigateToMenuFragment.value = true
            } else {
                categoryCollection
                    .whereEqualTo("name", name.value)
                    .limit(1)
                    .get()
                    .addOnSuccessListener {
                        if (it.isEmpty) {
                            category.name = catName
                            if (category.id.isNotEmpty()) {
                                updateCategory()
                            } else {
                                addCategory()
                            }
                            Log.i(TAG, catName)
                        } else {
                            nameFieldError.value = R.string.duplicate_category
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, it.toString())
                    }
            }
        } else {
            nameFieldError.value = R.string.empty_field
        }
    }

    private fun addCategory() {
        categoryCollection
            .add(category)
            .addOnSuccessListener {
                navigateToMenuFragment.value = true
            }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            }
    }

    private fun updateCategory() {
        categoryCollection
            .document(category.id)
            .set(category)
            .addOnSuccessListener {
                navigateToMenuFragment.value = true
            }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            }

        promotionCollection.whereGreaterThan("discountList.${category.id}", "")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    document.reference.update("discountList.${category.id}", category.name)
                }
            }
    }
}