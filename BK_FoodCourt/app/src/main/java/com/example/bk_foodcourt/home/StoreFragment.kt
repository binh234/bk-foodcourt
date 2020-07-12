package com.example.bk_foodcourt.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bk_foodcourt.OrderActivity
import com.example.bk_foodcourt.databinding.StoreFragmentBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class StoreFragment: Fragment() {
    private lateinit var binding: StoreFragmentBinding
    private lateinit var adapter: StoreAdapter
    private lateinit var viewModel: StoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StoreViewModel::class.java)

        binding = StoreFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupRecyclerView()

        viewModel.openStoreMenuEvent.observe(viewLifecycleOwner, Observer { store->
            if (store != null) {
                openStoreMenu(store)
                viewModel.openStoreMenuEvent.value = null
            }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
//        FirebaseFirestore.getInstance().collection("stores")
//            .get()
//            .addOnSuccessListener {
//                for (doc in it) {
//                    Log.d("StoreFragment", doc.id)
//                    val store = doc.toObject(Store::class.java)
//
//                    Log.d("StoreFragment", (store.openTime+1).toString())
//                }
//            }
        val query = FirebaseFirestore.getInstance()
            .collection("stores")
        val options = FirestoreRecyclerOptions.Builder<Store>()
            .setQuery(query, Store::class.java)
            .build()

        adapter = StoreAdapter(options, viewModel)
        binding.storeRecycler.adapter = adapter
    }

    private fun openStoreMenu(store: Store) {
        val intent = Intent(requireContext(), OrderActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
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