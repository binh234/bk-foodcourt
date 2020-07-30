package com.example.bk_foodcourt.menu.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bk_foodcourt.databinding.CartItemBinding
import com.example.bk_foodcourt.menu.CartItem
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CartItemAdapter(options: FirestoreRecyclerOptions<CartItem>, var viewModel: CartViewModel):
    FirestoreRecyclerAdapter<CartItem, CartItemAdapter.CartItemViewHolder>(options) {

    class CartItemViewHolder private constructor(val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem, viewModel: CartViewModel) {
            binding.cartItem = item
            binding.viewModel = viewModel
        }

        companion object {
            fun from(parent: ViewGroup): CartItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CartItemBinding.inflate(layoutInflater, parent, false)

                return CartItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        Log.d("OrderItemAdapter", "Create order view holder")
        return CartItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int, item: CartItem) {
        item.id = snapshots.getSnapshot(position).id
        holder.bind(item, viewModel)
    }
}