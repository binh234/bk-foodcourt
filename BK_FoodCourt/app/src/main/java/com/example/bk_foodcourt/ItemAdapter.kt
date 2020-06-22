package com.example.bk_foodcourt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemAdapter(var itemList: List<Row>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.orderID.text = itemList[position].getOrderID()
        holder.userID.text = itemList[position].getUserID()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var orderID: TextView
        var userID: TextView

        init {
            orderID = itemView.findViewById(R.id.tvOrderID)
            userID = itemView.findViewById(R.id.tvUserID)
        }
    }

}
