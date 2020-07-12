package com.example.bk_foodcourt

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
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