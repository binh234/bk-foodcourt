package com.example.bkmerchant.promotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.databinding.PromotionFragmentBinding
import com.example.bkmerchant.order.Order
import com.example.bkmerchant.order.OrderFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PromotionFragment: Fragment() {
    private lateinit var binding: PromotionFragmentBinding
    private lateinit var adapter: PromotionAdapter
    private lateinit var viewModel: PromotionViewModel
    private lateinit var storeId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val args = PromotionFragmentArgs.fromBundle(requireArguments())
        storeId = args.storeId

        binding = PromotionFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(PromotionViewModel::class.java)

        binding.addPromotionFab.setOnClickListener {
            navigateToPromotionDetailFragment(Promotion(storeId = storeId))
        }

        setupRecyclerView()

        viewModel.showPromotionDetailEvent.observe(viewLifecycleOwner, Observer {promotion ->
            promotion?.let {
                navigateToPromotionDetailFragment(it)
                viewModel.showPromotionDetailEvent.value = null
            }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        val query: Query = FirebaseFirestore.getInstance()
            .collection("stores")
            .document(storeId)
            .collection("promotions")
            .orderBy("status", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<Promotion>()
            .setQuery(query, Promotion::class.java)
            .build()

        adapter = PromotionAdapter(options, viewModel, storeId)

        binding.promotionRecycler.adapter = adapter
    }

    private fun navigateToPromotionDetailFragment(promotion: Promotion) {
        val action = PromotionFragmentDirections.actionPromotionFragmentToPromotionDetailFragment(promotion)
        findNavController().navigate(action)
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