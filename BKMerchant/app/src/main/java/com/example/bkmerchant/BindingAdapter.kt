package com.example.bkmerchant

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

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

@BindingAdapter("time")
fun convertIntToTime(view:TextView, time:Int) {
    val hours: Int = time / 60
    val minutes: Int = time % 60
    val timeText = String.format("%02d", hours) + ":" + String.format("%02d", minutes)
    view.text = timeText
}