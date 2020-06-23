package com.example.bkmerchant.menu.dish

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.DishFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import io.grpc.Context

class DishFragment : Fragment() {
    private lateinit var binding: DishFragmentBinding
    private lateinit var viewModelFactory: DishViewModelFactory
    private lateinit var viewModel: DishViewModel

    private lateinit var storageReference: StorageReference
    private var dishImageUri: Uri? = null
    private var uploadTask: StorageTask<UploadTask.TaskSnapshot>? = null
    private lateinit var storeId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.dish)

        storageReference = FirebaseStorage.getInstance().getReference("dish_image")

        val args = DishFragmentArgs.fromBundle(requireArguments())
        storeId = args.dish.storeId
        viewModelFactory = DishViewModelFactory(args.dish)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DishViewModel::class.java)

        binding = DishFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.dishImage.setOnClickListener {
            imageLoader()
        }
        binding.doneFab.setOnClickListener {
            if (uploadTask != null && uploadTask!!.isInProgress) {
                Toast.makeText(context, "Uploading in progress", Toast.LENGTH_SHORT).show()
            } else {
                checkEmptyField()
            }
        }

        viewModel.navigateToMenuFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                navigateToMenuFragment()
            }
        })

        viewModel.catList.observe(viewLifecycleOwner, Observer {categories ->
            val arrayAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, categories) }
            binding.dishCategory.adapter = arrayAdapter
        })

        return binding.root
    }

    private fun imageLoader() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.data!=null) {
            dishImageUri = data.data
            binding.dishImage.setImageURI(dishImageUri)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = activity?.contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        if (contentResolver != null) {
            return mimeType.getExtensionFromMimeType(contentResolver.getType(uri))
        }
        return null
    }

    private fun uploadImage() {
        val itemIndex = binding.dishCategory.selectedItemPosition
        dishImageUri?.let {imageUri ->
            val fileReference = storageReference
                .child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri))
            binding.loadingProgress.visibility = View.VISIBLE
            uploadTask = fileReference.putFile(imageUri)
                .addOnSuccessListener {
                    Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                    fileReference.downloadUrl
                        .addOnSuccessListener {
                            Log.d("DishFragment", it.toString())
                            viewModel.saveDish(it.toString(), itemIndex)
                            binding.loadingProgress.visibility = View.GONE
                        }
                        .addOnFailureListener {
                            Log.d("DishFragment", it.toString())
                        }

                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {task ->
                    val progress = 100.0 * task.bytesTransferred / task.totalByteCount
                    binding.loadingProgress.progress = progress.toInt()
                }
        }
        if (dishImageUri == null) {
            viewModel.saveDish("", itemIndex)
        }
    }

    private fun checkEmptyField() {
        var check = true
        val name = viewModel.name.value ?: ""
        val price = viewModel.price.value ?: ""
        if (name.trim().isEmpty()) {
            check = false
            binding.dishNameText.error = "Please enter this field"
        }
        if (price.isEmpty()) {
            check = false
            binding.dishPriceText.error = "Please enter this field"
        }
        if (check) {
            uploadImage()
        }
    }

    private fun navigateToMenuFragment() {
        val action = DishFragmentDirections.actionDishFragmentToMenuFragment()
        action.storeId = storeId
        findNavController().navigate(action)
    }
}