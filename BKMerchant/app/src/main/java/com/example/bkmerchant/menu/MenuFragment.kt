package com.example.bkmerchant.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.databinding.MenuFragmentBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MenuFragment : Fragment() {

    companion object {
        const val TAG = "MenuFragment"
    }

    private lateinit var binding: MenuFragmentBinding
    private lateinit var viewModel: MenuViewModel
    private lateinit var adapter: NewMenuAdapter
    private lateinit var storeId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val arguments = MenuFragmentArgs.fromBundle(requireArguments())
        storeId = arguments.storeId

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        binding = MenuFragmentBinding.inflate(inflater, container, false)
        binding.fabAddDish.setOnClickListener {
            onAddDish(Dish(storeId = storeId))
        }
        binding.fabAddCategory.setOnClickListener {
            onAddCategory(Category(storeId = storeId))
        }

        binding.lifecycleOwner = this

        setupRecyclerView()

        viewModel.openCategoryEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                onAddCategory(it)
                viewModel.openCategoryEvent.value = null
            }
        })

        viewModel.openDishEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                onAddDish(it)
                viewModel.openDishEvent.value = null
            }
        })

        return binding.root
    }

    private fun onAddCategory(category: Category) {
        val action = MenuFragmentDirections.actionMenuFragmentToCategoryFragment(category)
        findNavController().navigate(action)
    }

    private fun onAddDish(dish: Dish) {
        val action = MenuFragmentDirections.actionMenuFragmentToDishFragment(dish)
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        val query: Query = FirebaseFirestore.getInstance()
            .collection("stores")
            .document(storeId)
            .collection("categories")
        val options = FirestoreRecyclerOptions.Builder<Category>()
            .setQuery(query, Category::class.java)
            .build()

        adapter = NewMenuAdapter(options, viewModel, viewLifecycleOwner, storeId)
        binding.menuItemRecycler.adapter = adapter
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