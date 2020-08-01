package com.example.bk_foodcourt.menu

import com.google.firebase.firestore.Exclude

data class CartItem(
    var storeId: String = "",
    var name: String = "",
    var options: String = "",
    var total: Double = 0.0,
    var quantity: Int = 0,
    @get: Exclude var id: String = "",
    @get: Exclude var description: String ="",
    @get: Exclude var imageUrl: String =""
)