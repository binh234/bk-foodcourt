package com.example.bkmerchant

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.bkmerchant.order.OrderStatus
import com.google.firebase.Timestamp
import kotlin.math.round

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String) {
    if (url.isNotEmpty()) {
        Glide.with(view.context).load(url).into(view)
    } else {
        view.setImageResource(R.drawable.ic_food_default)
    }
}

@BindingAdapter("imageDetailUrl")
fun setImageDetailUrl(view: ImageView, url: String) {
    if (url.isNotEmpty()) {
        Glide.with(view.context).load(url).into(view)
    } else {
        view.setImageResource(R.drawable.ic_add)
    }
}

@BindingAdapter("priceText")
fun setPriceFormatted(view: TextView, price: Double) {
    val text = String.format("%1$,.0f", price) + "đ"
    view.text = text
}

@BindingAdapter("quantityText")
fun setQuantityText(view: TextView, quantity: Int) {
    val text = "${quantity}x"
    view.text = text
}

@BindingAdapter("time")
fun convertIntToTime(view:TextView, time:Int) {
    val hours: Int = time / 60
    val minutes: Int = time % 60
    val timeText = String.format("%02d", hours) + ":" + String.format("%02d", minutes)
    view.text = timeText
}

@BindingAdapter(value = ["total_rating", "total_star"])
fun setRatingStar(view:TextView, totalRatings: Int, totalStars: Int) {
    if (totalRatings !=0 ) {
        val rating = round(totalStars * 10.0 / totalRatings + 0.5) / 10
        val text = String.format("%.1f", rating)
        view.text = text
    } else {
        view.text = "No ratings"
    }
}

@BindingAdapter(value = ["order_type", "user_name"])
fun setOrderDescription(view: TextView, orderType: String, userName: String) {
    val text = "$userName ($orderType)"
    view.text = text
}

@BindingAdapter("order_status")
fun setOrderStatus(view: TextView, orderStatus: Int) {
    view.text = when(orderStatus) {
        OrderStatus.CONFIRMED.value -> "CONFIRMED"
        OrderStatus.PROCESSING.value -> "PROCESSING"
        OrderStatus.DONE_PROCESSING.value -> "DONE_PROCESSING"
        OrderStatus.FINISH.value -> "FINISH"
        OrderStatus.CANCEL.value -> "CANCELED"
        else -> "PENDING"
    }
}

@BindingAdapter("order_time")
fun setOrderTime(view: TextView, orderTime: Timestamp) {
    view.text = orderTime.toDate().toString()
}