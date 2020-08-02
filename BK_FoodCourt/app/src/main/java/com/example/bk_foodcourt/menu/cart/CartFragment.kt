package com.example.bk_foodcourt.menu.cart

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bk_foodcourt.R
import com.example.bk_foodcourt.databinding.CartFragmentBinding
import com.example.bk_foodcourt.menu.CartItem
import com.example.bk_foodcourt.menu.Dish
import com.example.bk_foodcourt.order.Order
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

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
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.cart)

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

        adapter = CartItemAdapter(viewModel)
        viewModel.cartList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter.submitList(it)
                binding.orderDetail.adapter = adapter
            }
        })

        viewModel.codeList.observe(viewLifecycleOwner, Observer { codeList ->
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                codeList
            )
            binding.orderCode.adapter = arrayAdapter
        })

        viewModel.showCartItemDetailEvent.observe(viewLifecycleOwner, Observer { dish ->
            dish?.let {
                showCartItemDetail(dish)
                viewModel.showCartItemDetailEvent.value = null
            }
        })

        viewModel.goToHomeEvent.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, "Checkout successfullly", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.storeFragment)
                viewModel.goToHomeEvent.value = null
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
            }
        })

        setItemTouchHelper()

        return binding.root
    }

    private fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeCartItem(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_24)
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorSecondary
                        )
                    )
                    .addSwipeLeftLabel(getString(R.string.delete))
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.orderDetail)
    }

    private fun setupRecyclerView() {
//        val query: Query = firestore.collection("users")
//            .document(currentUser.uid)
//            .collection("cart")
//
//        val options = FirestoreRecyclerOptions.Builder<CartItem>()
//            .setQuery(query, CartItem::class.java)
//            .build()
//
//        adapter = CartItemAdapter(options, viewModel)
//
//        binding.orderDetail.adapter = adapter

    }

    private fun checkOut() {
        val orderType = binding.orderType.selectedItem.toString()
        Log.d("CartFragment", orderType)
        viewModel.checkout(orderType, "")
    }

    private fun navigateToPaymentFragment() {
        findNavController().navigateUp()
    }

    private fun showCartItemDetail(dish: Dish) {
        val action = CartFragmentDirections.actionCartFragmentToDishFragment(dish)
        findNavController().navigate(action)
    }
}