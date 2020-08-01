package com.example.bk_foodcourt

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.bk_foodcourt.order.OrderStatus
import com.google.firebase.Timestamp
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String) {
    if (url.isNotEmpty()) {
        Glide.with(view.context).load(url).into(view)
    } else {
        view.setImageResource(R.drawable.ic_food_default)
    }
}

@BindingAdapter("avatarUrl")
fun setAvatarUrl(view: CircleImageView, url: String) {
    if (url.isNotEmpty()) {
        Glide.with(view.context).load(url).into(view)
    } else {
        view.setImageResource(R.drawable.ic_food_default)
    }
}

@BindingAdapter(value = ["total_rating", "star_rating"])
fun setRatingStar(view: TextView, totalRatings: Int, starRating: Double) {
    if (totalRatings != 0) {
        val text = String.format("%.1f", starRating)
        view.text = text
    } else {
        view.text = view.resources.getString(R.string.no_rating)
    }
}

fun convertIntToTime(time: Int): String {
    val hours: Int = time / 60
    val minutes: Int = time % 60
    return String.format("%02d", hours) + ":" + String.format("%02d", minutes)
}

@BindingAdapter(value = ["open_time", "close_time"])
fun setOpeningHours(view: TextView, open_time: Int, close_time: Int) {
    val openingHours = convertIntToTime(open_time) + " - " + convertIntToTime(close_time)
    view.text = openingHours
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

@BindingAdapter(value = ["order_type", "store_name"])
fun setOrderDescription(view: TextView, orderType: String, storeName: String) {
    var text = "$storeName ("
    text += when (orderType) {
        "Pickup" -> view.resources.getString(R.string.pickup)
        else -> view.resources.getString(R.string.take_away)
    }
    text += ")"
    view.text = text
}

@BindingAdapter("order_status")
fun setOrderStatus(view: TextView, orderStatus: Int) {
    view.text = when (orderStatus) {
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

@BindingAdapter("item_option")
fun setItemOption(view: TextView, option: String) {
    view.text = option.replace("\\n", "\n")
}