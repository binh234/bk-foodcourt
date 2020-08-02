package com.example.bk_foodcourt.menu.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bk_foodcourt.R
import com.example.bk_foodcourt.home.Store
import com.example.bk_foodcourt.login.User
import com.example.bk_foodcourt.menu.CartItem
import com.example.bk_foodcourt.menu.Dish
import com.example.bk_foodcourt.menu.MenuViewModel
import com.example.bk_foodcourt.menu.Promotion
import com.example.bk_foodcourt.order.Order
import com.example.bk_foodcourt.order.OrderItem
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.util.*

class CartViewModel : ViewModel() {
    private var firestore = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser!!
    private var userInfo = User()
    private var storeInfo = Store()
    private var storeId = ""

    var total = MutableLiveData(0.0)
    var applicableFee = MutableLiveData(0.0)
    var promotion = MutableLiveData(0.0)

    val cartList = MutableLiveData<List<CartItem>>()
    val promotionList = mutableListOf<Promotion>()
    val codeList = MutableLiveData<List<String>>()

    val showCartItemDetailEvent = MutableLiveData<Dish>()
    val goToHomeEvent = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<Int>()

    init {
        getCart()
        getUserInfo()
    }

    private fun getCart() {
        firestore.collection("users")
            .document(currentUser.uid)
            .collection("cart")
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.d(MenuViewModel.TAG, exception.toString())
                } else if (querySnapshot != null) {
                    val list = mutableListOf<CartItem>()
                    var subTotal = 0.0
                    for (document in querySnapshot) {
                        val item = document.toObject(CartItem::class.java)
                        item.id = document.id
                        subTotal += item.total
                        list.add(item)
                    }
                    if (list.isNotEmpty()) {
                        storeId = list[0].storeId
                        getStoreInfo()
                        getPromotionCodes()
                        Log.d("CartViewModel", list[0].storeId)
                    }
                    total.value = subTotal * 1.1
                    applicableFee.value = subTotal / 10
                    cartList.value = list

                    Log.d("CartViewModel", subTotal.toString())
                }
            }
    }

    private fun getUserInfo() {
        firestore.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                userInfo = document.toObject(User::class.java)!!
                userInfo.id = document.id
            }
    }

    private fun getStoreInfo() {
        if (storeId.isNotEmpty()) {
            firestore.collection("stores")
                .document(storeId)
                .get()
                .addOnSuccessListener { document ->
                    storeInfo = document.toObject(Store::class.java)!!
                    storeInfo.id = document.id
                }
        }
    }

    private fun getPromotionCodes() {
        if (storeId.isNotEmpty()) {
            val list = mutableListOf<String>()
            val todayString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(Date())
            val today = DateFormat.getDateInstance(DateFormat.DATE_FIELD).parse(todayString)!!
            val todayTimestamp = Timestamp(today)
            firestore.collection("stores")
                .document(storeId)
                .collection("promotions")
                .whereEqualTo("scope", 0)
                .whereLessThanOrEqualTo("activateDay", todayTimestamp)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val promotion = document.toObject(Promotion::class.java)
                        promotion.id = document.id
                        list.add(promotion.code)
                        promotionList.add(promotion)
                        Log.d("CartViewModel", promotion.id)
                        Log.d("CartViewModel", promotion.code)
                    }
                    codeList.value = list
                }
        }
    }

    fun showCartItemDetail(item: CartItem) {
        Log.d("CartViewModel", item.options)
        val dish = Dish(
            name = item.name,
            price = item.total / item.quantity,
            description = item.description,
            options = item.options,
            quantity = item.quantity,
            imageUrl = item.imageUrl,
            cartItemId = item.id,
            storeId = item.storeId
        )
        showCartItemDetailEvent.value = dish
    }

    fun checkout(orderType: String, promotionCode: String) {
        cartList.value?.let { list ->
            if (storeId.isNotEmpty()) {
                val order = Order(
                    userID = userInfo.id,
                    userName = userInfo.name,
                    userPhoneNumber = userInfo.phoneNumber,
                    storeID = storeInfo.id,
                    storeName = storeInfo.name,
                    storeHotline = storeInfo.hotline,
                    promotion = promotion.value!!,
                    promotionCode = promotionCode,
                    total = total.value!!,
                    applicableFee = applicableFee.value!!,
                    type = orderType,
                    status = 0,
                    time = Timestamp.now()
                )
                firestore.collection("order")
                    .add(order)
                    .addOnSuccessListener { ref ->
                        for (item in list) {
                            val orderItem = OrderItem(
                                orderId = ref.id,
                                name = item.name,
                                quantity = item.quantity,
                                total = item.total,
                                options = item.options
                            )
                            firestore.collection("order")
                                .document(ref.id)
                                .collection("orderItems")
                                .add(orderItem)
                                .addOnSuccessListener {
                                    firestore.collection("users")
                                        .document(currentUser.uid)
                                        .collection("cart")
                                        .document(item.id)
                                        .delete()
                                }
                        }
                        goToHomeEvent.value = true
                    }
                    .addOnFailureListener {
                        Log.d("CartViewModel", it.toString())
                    }
            } else {
                errorMessage.value = R.string.empty_cart
            }
        }
    }
}