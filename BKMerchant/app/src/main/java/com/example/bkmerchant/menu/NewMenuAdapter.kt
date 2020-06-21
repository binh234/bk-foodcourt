package com.example.bkmerchant.menu

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.bkmerchant.databinding.MenuItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NewMenuAdapter(options: FirestoreRecyclerOptions<Category>,
                     val viewModel: MenuViewModel,
                     private val owner: LifecycleOwner,
                     private val storeId: String):
    FirestoreRecyclerAdapter<Category, NewMenuAdapter.MenuViewHolder>(options) {

    class MenuViewHolder private constructor(val binding: MenuItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var currentAdapter: DishAdapter? = null

        fun bind(item: Category, viewModel: MenuViewModel, owner: LifecycleOwner) {
            Log.d("MenuAdapter", "Create Adapter")
            binding.category = item
            binding.viewModel = viewModel

            val query: Query = FirebaseFirestore.getInstance()
                .collection("stores")
                .document(item.storeId)
                .collection("categories")
                .document(item.id)
                .collection("items")
                .orderBy("availability", Query.Direction.DESCENDING)
            val options = FirestoreRecyclerOptions.Builder<Dish>()
                .setQuery(query, Dish::class.java)
                .setLifecycleOwner(owner)
                .build()

            currentAdapter?.stopListening()

            currentAdapter = DishAdapter(options, viewModel, item)
            currentAdapter!!.startListening()

            binding.dishRecycler.adapter = currentAdapter
        }

        companion object {
            fun from(parent: ViewGroup): MenuViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MenuItemBinding.inflate(layoutInflater, parent, false)

                return MenuViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int, item: Category) {
        item.id = snapshots.getSnapshot(position).id
        item.storeId = storeId
        holder.bind(item, viewModel, owner)
    }
}