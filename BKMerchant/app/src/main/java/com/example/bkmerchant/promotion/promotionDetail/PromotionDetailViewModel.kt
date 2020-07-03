package com.example.bkmerchant.promotion.promotionDetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bkmerchant.promotion.Promotion
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class PromotionDetailViewModel(val promotion: Promotion) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private var lastCode = ""

    var categories = arrayOf<String>()
    var items = arrayOf<String>()
    var categoryIds = arrayOf<String>()
    var itemIds = arrayOf<String>()
    var checkedCategories = BooleanArray(0)
    var checkedItems = BooleanArray(0)

    val label = MutableLiveData("")
    var discountType = 0
    var discountScope = 0
    val value = MutableLiveData("")
    val numAllowed = MutableLiveData("1000000000")
    val activateDay = MutableLiveData(Timestamp.now())
    val expireDay = MutableLiveData(Timestamp.now())
    val activateDayTime = MutableLiveData(0)
    val expireDayTime = MutableLiveData(1439)

    var discountList = HashMap<String, String>()

    val code = MutableLiveData("")
    val orderFrom = MutableLiveData("")
    val orderTo = MutableLiveData("")

    val message = MutableLiveData<String>()
    val navigateToPromotionEvent = MutableLiveData<Boolean>()

    init {
        getCategoryList(promotion.storeId)
        getItemList(promotion.storeId)
        if (promotion.id.isNotEmpty()) {
            bind(promotion)
        }
    }

    private fun bind(promotion: Promotion) {
        label.value = promotion.label
        discountType = promotion.type
        discountScope = promotion.scope
        value.value = promotion.value.toString()
        numAllowed.value = promotion.numAllowed.toString()
        activateDay.value = promotion.activateDay
        expireDay.value = promotion.expireDay
        activateDayTime.value = promotion.activateDayTime
        expireDayTime.value = promotion.expireDayTime

        discountList = promotion.discountList

        code.value = promotion.code
        orderFrom.value = promotion.orderFrom.toString()
        orderTo.value = promotion.orderTo.toString()

        lastCode = promotion.code
    }

    fun savePromotion() {
        if (activateDay.value!! > expireDay.value!!) {
            message.value = "Activate day must be less than expired day"
        } else if (activateDayTime.value!! >= expireDayTime.value!!) {
            message.value = "Activate time must be less than expired time"
        } else if (discountScope == 0 && orderFrom.value!! >= orderTo.value!!) {
            message.value = "Order from must be less than order to"
        } else {
            val map = HashMap<String, Any>()

            map["label"] = label.value!!
            map["type"] = discountType
            map["scope"] = discountScope
            map["numUsed"] = promotion.numUsed
            map["numAllowed"] = numAllowed.value!!.toInt()
            map["value"] = value.value!!.toDouble()
            map["activateDay"] = activateDay.value!!
            map["expireDay"] = expireDay.value!!
            map["activateDayTime"] = activateDayTime.value!!
            map["expireDayTime"] = expireDayTime.value!!
            map["status"] = promotion.status

            if (discountScope == 0) {
                map["orderFrom"] = orderFrom.value!!.toInt()
                map["orderTo"] = orderTo.value!!.toInt()
                if (lastCode != code.value!!) {
                    firestore.collectionGroup("promotions")
                        .whereEqualTo("code", code.value!!)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (querySnapshot.isEmpty) {
                                map["code"] = code.value!!

                                if (promotion.id.isEmpty()) {
                                    addPromotion(map)
                                } else {
                                    updatePromotion(map)
                                }
                            } else {
                                message.value = "Promotion code had been used before!"
                            }
                        }
                } else {
                    updatePromotion(map)
                }
            } else {
                if (discountScope > 1) {
                    val itemMap = HashMap<String, String>()
                    if (discountScope == 2) {
                        for (i in categories.indices) {
                            if (checkedCategories[i]) {
                                itemMap[categoryIds[i]] = categories[i]
                            }
                        }
                    } else {
                        for (i in items.indices) {
                            if (checkedItems[i]) {
                                itemMap[itemIds[i]] = items[i]
                            }
                        }
                    }
                    if (itemMap.isEmpty()) {
                        map["discountList"] = discountList
                    } else {
                        map["discountList"] = itemMap
                    }
                }

                if (promotion.id.isEmpty()) {
                    addPromotion(map)
                } else {
                    updatePromotion(map)
                }
            }
        }
    }

    private fun addPromotion(map: HashMap<String, Any>) {
        firestore.collection("stores")
            .document(promotion.storeId)
            .collection("promotions")
            .add(map)
            .addOnSuccessListener {
                message.value = "Add successful"
                navigateToPromotionEvent.value = true
            }
    }

    private fun updatePromotion(map: HashMap<String, Any>) {
        firestore.collection("stores")
            .document(promotion.storeId)
            .collection("promotions")
            .document(promotion.id)
            .update(map)
            .addOnSuccessListener {
                message.value = "Update successful"
                navigateToPromotionEvent.value = true
            }
    }

    private fun getCategoryList(storeId: String) {
        firestore.collection("stores")
            .document(storeId)
            .collection("categories")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val list = mutableListOf<String>()
                val idList = mutableListOf<String>()
                for (document in querySnapshot) {
                    val name = document.getString("name")!!
                    idList.add(document.id)
                    list.add(name)
                }
                categories = list.toTypedArray()
                categoryIds = idList.toTypedArray()
                checkedCategories = BooleanArray(list.size) { false }
            }
    }

    private fun getItemList(storeId: String) {
        firestore.collectionGroup("items")
            .whereEqualTo("storeId", storeId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val list = mutableListOf<String>()
                val idList = mutableListOf<String>()
                Log.d("PromotionDetail", "Get item success")
                for (document in querySnapshot) {
                    val name = document.getString("name")!!
                    list.add(name)
                    idList.add(document.id)
                }
                items = list.toTypedArray()
                itemIds = idList.toTypedArray()
                checkedItems = BooleanArray(list.size) { false }
            }
            .addOnFailureListener {
                Log.d("PromotionDetail", it.toString())
            }
    }
}