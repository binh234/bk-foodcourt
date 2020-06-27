package com.example.bkmerchant.order.orderDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bkmerchant.order.Order

class OrderDetailFactory(private val order: Order): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            return OrderDetailViewModel(order) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}