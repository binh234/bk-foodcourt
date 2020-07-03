package com.example.bkmerchant

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.bkmerchant.order.OrderStatus
import com.example.bkmerchant.promotion.Promotion
import com.google.firebase.Timestamp
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import kotlin.math.round

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
fun convertIntToTime(view: TextView, time: Int) {
    val hours: Int = time / 60
    val minutes: Int = time % 60
    val timeText = String.format("%02d", hours) + ":" + String.format("%02d", minutes)
    view.text = timeText
}

@BindingAdapter("day")
fun setDayFormatted(view: TextView, time: Timestamp) {
    val timeText = SimpleDateFormat("MM/dd/yyyy").format(time.toDate())
    view.text = timeText
}

@BindingAdapter(value = ["total_rating", "total_star"])
fun setRatingStar(view: TextView, totalRatings: Int, totalStars: Int) {
    if (totalRatings != 0) {
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

@BindingAdapter("promotion_description")
fun setPromotionDescription(view: TextView, promotion: Promotion) {
    var text = "Discount "

    text += when (promotion.type) {
        0 -> "${promotion.value}%"
        else -> String.format("%1$,.0f", promotion.value) + "đ"
    }
    text += " for "

    text += when (promotion.scope) {
        0 -> "order from " + String.format(
            "%1$,.0f",
            promotion.orderFrom
        ) + "đ to " + String.format("%1$,.0f", promotion.orderTo) + "đ"
        1 -> "all items in menu"
        2 -> "all items in " + getItemList(promotion.discountList)
        else -> getItemList(promotion.discountList)
    }
    view.text = text
}

fun getItemList(map: HashMap<String, String>): String {
    var res = ""

    for (key in map.keys) {
        res += map[key] + ", "
    }
    return res
}

@BindingAdapter(value = ["promotion_activate_day", "promotion_expire_day"])
fun setPromotionDateRange(view: TextView, activateDay: Timestamp, expireDay: Timestamp) {
    val text = SimpleDateFormat("MM/dd/yyyy").format(activateDay.toDate()) +
            "-" +
            SimpleDateFormat("MM/dd/yyyy").format(expireDay.toDate())
    view.text = text
}

@BindingAdapter(value = ["promotion_activate_time", "promotion_expire_time"])
fun setPromotionTimeRange(view: TextView, activateTime: Int, expireTime: Int) {
    val activateHours: Int = activateTime / 60
    val activateMinutes: Int = activateTime % 60
    val activateTimeText = String.format("%02d", activateHours) + ":" + String.format("%02d", activateMinutes)

    val expireHours: Int = expireTime / 60
    val expireMinutes: Int = expireTime % 60
    val expireTimeText = String.format("%02d", expireHours) + ":" + String.format("%02d", expireMinutes)
    view.text = "${activateTimeText}-${expireTimeText}"
}

@BindingAdapter(value = ["promotion_num_used", "promotion_num_allowed"])
fun setPromotionUseTime(view: TextView, numUsed: Int, numAllowed: Int) {
    view.text = "${numUsed}/${numAllowed}"
}