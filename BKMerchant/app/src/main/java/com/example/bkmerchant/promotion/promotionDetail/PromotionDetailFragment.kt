package com.example.bkmerchant.promotion.promotionDetail

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.PromotionDetailFragmentBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.promotion.Promotion
import java.util.*

class PromotionDetailFragment : Fragment() {
    private lateinit var binding: PromotionDetailFragmentBinding
    private lateinit var viewModelFactory: PromotionDetailFactory
    private lateinit var viewModel: PromotionDetailViewModel
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.promotion_detail_fragment, container, false)

        calendar = Calendar.getInstance()

        val args = PromotionDetailFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = PromotionDetailFactory(args.promotion)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(PromotionDetailViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupDropdownList()

        binding.activateDay.setOnClickListener { openDatePicker() }
        binding.expireDay.setOnClickListener { closeDatePicker() }
        binding.activateDayTime.setOnClickListener { openTimePicker() }
        binding.expireDayTime.setOnClickListener { closeTimePicker() }

        binding.doneFab.setOnClickListener { checkValidate() }
        binding.listItem.setOnClickListener { openMultiChoiceDialog() }

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.navigateToPromotionEvent.observe(viewLifecycleOwner, Observer { event ->
            if (event == true) {
                findNavController().navigateUp()
            }
        })

        return binding.root
    }

    private fun checkValidate() {
        if (binding.discountLabel.text.toString().isEmpty()) {
            binding.discountLabel.error = resources.getString(R.string.empty_field)
        } else if (binding.discountValue.text.toString().isEmpty()) {
            binding.discountValue.error = resources.getString(R.string.empty_field)
        } else if (binding.numAllow.text.toString().isEmpty()) {
            binding.numAllow.error = resources.getString(R.string.empty_field)
        } else if (viewModel.discountScope == 0 && binding.discountCode.text.toString().isEmpty()) {
            binding.discountCode.error = resources.getString(R.string.empty_field)
        } else if (viewModel.discountScope == 0 && binding.fromOrder.text.toString().isEmpty()) {
            binding.fromOrder.error = resources.getString(R.string.empty_field)
        } else if (viewModel.discountScope == 0 && binding.toOrder.text.toString().isEmpty()) {
            binding.toOrder.error = resources.getString(R.string.empty_field)
        } else if (viewModel.discountScope > 1 && viewModel.discountList.isEmpty() && binding.listItem.text.toString().isEmpty()) {
            binding.listItem.error = resources.getString(R.string.empty_field)
        } else if (viewModel.discountType == 0 && binding.discountValue.text.toString().toDouble() >= 100) {
            binding.discountValue.error = "Discount percent must be less than 100"
        } else if (binding.numAllow.text.toString().toInt() == 0) {
            binding.numAllow.error = resources.getString(R.string.empty_field)
        } else {
            viewModel.savePromotion()
        }
    }

    private fun setupDropdownList() {
        val typeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.discount_type,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.discountType.setAdapter(typeAdapter)
        binding.discountType.setText(typeAdapter.getItem(viewModel.discountType), false)

        val scopeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.discount_scope,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.discountScope.setAdapter(scopeAdapter)
        binding.discountScope.setText(scopeAdapter.getItem(viewModel.discountScope), false)

        if (viewModel.promotion.id.isNotEmpty()) {
            binding.discountTypeLayout.isEnabled = false
            binding.discountScopeLayout.isEnabled = false

            var orderDiscountVisibility = View.VISIBLE
            var itemDiscountVisibility = View.GONE
            when (viewModel.discountScope) {
                0 -> {}
                1 -> {
                    orderDiscountVisibility = View.GONE
                }
                else -> {
                    var text = ""
                    for (key in viewModel.discountList.keys) {
                        text += viewModel.discountList[key] + "\n"
                    }
                    binding.listItem.setText(text.trim())

                    orderDiscountVisibility = View.GONE
                    itemDiscountVisibility = View.VISIBLE
                }
            }
            binding.discountCodeLayout.visibility = orderDiscountVisibility
            binding.orderFromLayout.visibility = orderDiscountVisibility
            binding.orderToLayout.visibility = orderDiscountVisibility
            binding.listItemLayout.visibility = itemDiscountVisibility
        } else {
            binding.discountType.setOnItemClickListener { parent, view, position, id ->
                viewModel.discountType = position
            }

            binding.discountScope.setOnItemClickListener { parent, view, position, id ->
                viewModel.discountScope = position
                var orderDiscountVisibility = View.VISIBLE
                var itemDiscountVisibility = View.GONE
                when (position) {
                    0 -> {}
                    1 -> {
                        orderDiscountVisibility = View.GONE
                    }
                    else -> {
                        orderDiscountVisibility = View.GONE
                        itemDiscountVisibility = View.VISIBLE
                        binding.listItem.setText("")
                        if (position == 2) {
                            viewModel.checkedCategories.fill(false)
                        } else {
                            viewModel.checkedItems.fill(false)
                        }
                    }
                }
                binding.discountCodeLayout.visibility = orderDiscountVisibility
                binding.orderFromLayout.visibility = orderDiscountVisibility
                binding.orderToLayout.visibility = orderDiscountVisibility
                binding.listItemLayout.visibility = itemDiscountVisibility
            }
        }
    }

    private fun openMultiChoiceDialog() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Select items")
            .setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }

        if (viewModel.discountScope == 2) {
            builder.setMultiChoiceItems(
                viewModel.categories,
                viewModel.checkedCategories
            ) { dialog: DialogInterface, i: Int, b: Boolean ->
                viewModel.checkedCategories[i] = b
            }
                .setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
                    var text = ""
                    for (i in 0 until viewModel.categories.size) {
                        if (viewModel.checkedCategories[i]) {
                            text += viewModel.categories[i] + "\n"
                        }
                    }
                    binding.listItem.setText(text.trim())
                }
                .show()
        } else {
            builder.setMultiChoiceItems(
                viewModel.items,
                viewModel.checkedItems
            ) { dialog: DialogInterface, i: Int, b: Boolean ->
                viewModel.checkedItems[i] = b
            }
                .setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
                    var text = ""
                    for (i in 0 until viewModel.items.size) {
                        if (viewModel.checkedItems[i]) {
                            text += viewModel.items[i] + "\n"
                        }
                    }
                    binding.listItem.setText(text.trim())
                }
                .show()
        }
    }

    private fun openDatePicker() {
        val curYear = calendar.get(Calendar.YEAR)
        val curMonth = calendar.get(Calendar.MONTH)
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val string = "${month + 1}/${dayOfMonth}/$year"
                Log.d("PromotionDetail", string)
                val date = SimpleDateFormat("MM/dd/yyyy").parse(string)
                Log.d("PromotionDetail", date?.time.toString())
                viewModel.activateDay.value = Timestamp(date ?: Date())
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
            datePickerListener,
            curYear, curMonth, curDay
        )
        datePickerDialog.show()
    }

    private fun closeDatePicker() {
        val curYear = calendar.get(Calendar.YEAR)
        val curMonth = calendar.get(Calendar.MONTH)
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val string = "${month + 1}/${dayOfMonth}/$year"
                Log.d("PromotionDetail", string)
                val date = SimpleDateFormat("MM/dd/yyyy").parse(string)
                Log.d("PromotionDetail", date?.time.toString())
                viewModel.expireDay.value = Timestamp(date ?: Date())
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
            datePickerListener,
            curYear, curMonth, curDay
        )
        datePickerDialog.show()
    }

    private fun openTimePicker() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            viewModel.activateDayTime.value = hourOfDay * 60 + minute
        }
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun closeTimePicker() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            viewModel.expireDayTime.value = hourOfDay * 60 + minute
        }
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }
}