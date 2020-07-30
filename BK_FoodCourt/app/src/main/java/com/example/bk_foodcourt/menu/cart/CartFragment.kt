package com.example.bk_foodcourt.menu.cart

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bk_foodcourt.R
import com.example.bk_foodcourt.databinding.CartFragmentBinding
import com.example.bk_foodcourt.menu.CartItem
import com.example.bk_foodcourt.order.Order
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CartFragment : Fragment() {
    private lateinit var binding: CartFragmentBinding
    private lateinit var adapter: CartItemAdapter

    private lateinit var viewModel: CartViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    private lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.order_detail)

        firestore = FirebaseFirestore.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser!!

        binding = CartFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        binding.checkoutButton.setOnClickListener { checkOut() }

        viewModel.applicableFee.observe(viewLifecycleOwner, Observer {
            binding.orderFee.text = String.format("%1$,.0f", it) + "đ"
            binding.orderTotal.text = String.format("%1$,.0f", viewModel.total.value) + "đ"
            binding.orderSubtotal.text = String.format(
                "%1$,.0f",
                viewModel.total.value!! + viewModel.promotion.value!! - viewModel.applicableFee.value!!
            ) + "đ"
        })

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val query: Query = firestore.collection("users")
            .document(currentUser.uid)
            .collection("cart")

        val options = FirestoreRecyclerOptions.Builder<CartItem>()
            .setQuery(query, CartItem::class.java)
            .build()

        adapter = CartItemAdapter(options, viewModel)

        binding.orderDetail.adapter = adapter
    }

    private fun checkOut() {
        navigateToPaymentFragment()
    }

    private fun navigateToPaymentFragment() {
//        findNavController().navigateUp()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}