package com.example.bkmerchant.order.orderDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.OrderDetailBinding
import com.example.bkmerchant.order.Order
import com.example.bkmerchant.order.OrderItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrderDetailFragment : Fragment() {
    private lateinit var binding: OrderDetailBinding
    private lateinit var adapter: OrderItemAdapter

    private lateinit var viewModelFactory: OrderDetailFactory
    private lateinit var viewModel: OrderDetailViewModel

    private lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.order_detail)

        binding = OrderDetailBinding.inflate(inflater, container, false)

        val args = OrderDetailFragmentArgs.fromBundle(requireArguments())
        order = args.order
        viewModelFactory = OrderDetailFactory(args.order)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrderDetailViewModel::class.java)

        binding.order = order
        binding.cancelOrderButton.setOnClickListener { navigateToOrderFragment() }
        binding.phoneButton.setOnClickListener { startCall() }

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        Log.d("OrderDetailFragment", order.id)
        val query: Query = FirebaseFirestore.getInstance()
            .collection("order")
            .document(order.id)
            .collection("orderItems")

        val options = FirestoreRecyclerOptions.Builder<OrderItem>()
            .setQuery(query, OrderItem::class.java)
            .build()

        adapter = OrderItemAdapter(options)

        binding.orderDetail.adapter = adapter
    }

    private fun navigateToOrderFragment() {
        findNavController().navigateUp()
    }

    private fun startCall() {
        val phone = order.userPhoneNumber
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
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