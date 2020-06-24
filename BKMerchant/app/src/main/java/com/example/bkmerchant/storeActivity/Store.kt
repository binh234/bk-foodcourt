package com.example.bkmerchant.storeActivity

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Store(
    @get: Exclude
    var id: String="",
    val name: String = "",
    val imageUrl: String = "",
    val hotline: String = "",
    val website: String = "",
    val supportEmail: String = "",
    val description: String = "",
    val isFocus: Boolean = false,
    val openTime: Int = 0,
    val closeTime: Int = 0,
    val ownerID: String = "",
    val ownerName: String = "",
    val totalRatings: Int = 0,
    val totalStars: Int = 0
): Parcelable