package com.example.bkmerchant.menu

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @get: Exclude
    var id: String="",
    var name: String="",
    @get: Exclude
    var storeId: String=""
): Parcelable

@Parcelize
data class Dish(
    @get: Exclude
    var id: String="",
    var name: String="",
    var description: String = "",
    var price: Double=0.0,
    var availability: Boolean = true,
    var imageUrl: String="",
    @get: Exclude
    var categoryId: String="",
    @get: Exclude
    var storeId: String=""
): Parcelable