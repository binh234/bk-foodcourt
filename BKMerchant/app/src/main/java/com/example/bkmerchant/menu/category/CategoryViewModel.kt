package com.example.bkmerchant.menu.category

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bkmerchant.menu.Category
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CategoryViewModel(val category: Category): ViewModel() {
    companion object {
        const val TAG = "CategoryViewModel"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val categoryCollection = FirebaseFirestore.getInstance()
        .collection("stores")
        .document(category.storeId)
        .collection("categories")

    var name = MutableLiveData("")

    var nameFieldError = MutableLiveData<String>()

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
                    } else if (category.id.isEmpty()){
                        nameFieldError.value = "Duplicate item"
                    } else {
                        navigateToMenuFragment.value = true
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, it.toString())
                }
        } else {
            nameFieldError.value = "Please enter this field"
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
    }
}