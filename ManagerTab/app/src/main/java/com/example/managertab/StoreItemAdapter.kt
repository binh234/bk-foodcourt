package com.example.managertab
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.managertab.R
import com.example.managertab.Store
import com.example.managertab.StoreItem
import kotlinx.android.synthetic.main.store_item.view.*

class StoreItemAdapter(private val StoreItemList : List<StoreItem>) : Adapter<StoreItemAdapter.StoreItemViewHolder>(){
    // Each of the View Holder is called when each of the view is created (for each Item)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreItemViewHolder {
        val storeItemObject = LayoutInflater.from(parent.context).inflate(R.layout.store_item,
        parent, false)

        return StoreItemViewHolder(storeItemObject)
    }

    // This function is call when any data is updated and when each item is recycled
    // This method is called over and over again
    override fun onBindViewHolder(holder: StoreItemViewHolder, position: Int) {
        val currentItem = StoreItemList[position]
        holder.nameTextView.text = currentItem.Name
        holder.closeTimeTextView.text = currentItem.CloseTime.toString()
        holder.openTimeTextView.text = currentItem.OpenTime.toString()
        holder.supportEmail.text = currentItem.SupportEmail

    }


    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    // This class is used to avoid calling FindViewByID over and over again (Cache the view in the xml
    // so they can be updated faster and easier)
    class StoreItemViewHolder(StoreItemView: View) : RecyclerView.ViewHolder(StoreItemView){
        val nameTextView : TextView = StoreItemView.store_name
        val openTimeTextView : TextView = StoreItemView.open_time
        val closeTimeTextView  : TextView = StoreItemView.close_time
        val supportEmail : TextView = StoreItemView.support_email
    }
}
