package com.example.bkmerchant.accountActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bkmerchant.R
import com.example.bkmerchant.databinding.PersonalInfoFragmentBinding
import com.example.bkmerchant.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class PersonalInfoFragment : Fragment() {
    private lateinit var binding: PersonalInfoFragmentBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: User
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.personal_information)

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        val args = PersonalInfoFragmentArgs.fromBundle(requireArguments())
        user = args.user

        binding = PersonalInfoFragmentBinding.inflate(inflater, container, false)
        binding.user = user

        binding.saveButton.setOnClickListener { saveInformation() }

        return binding.root
    }

    private fun saveInformation() {
        val name = binding.userName.text.toString()
        val phoneNumber = binding.userPhoneNumber.text.toString()
        val email = binding.userEmail.text.toString()

        if (name.trim().isEmpty()) {
            binding.userName.error = resources.getString(R.string.empty_field)
        } else if (phoneNumber.trim().isEmpty()) {
            binding.userPhoneNumber.error = resources.getString(R.string.empty_field)
        } else if (email.trim().isEmpty()) {
            binding.userEmail.error = resources.getString(R.string.empty_field)
        } else {
            val updateMap = HashMap<String, Any>()
            val currentEmail = currentUser.email

            updateMap["name"] = name
            updateMap["phoneNumber"] = phoneNumber

            if (email != currentEmail) {
                currentUser.updateEmail(email)
                    .addOnSuccessListener {
                        currentUser.sendEmailVerification()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Please check your email",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                updateMap["email"] = email
                                updateUser(updateMap)
                            }
                        firestore.collection("userTypes")
                            .whereEqualTo("email", currentEmail)
                            .limit(1)
                            .get()
                            .addOnSuccessListener {querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    for (document in querySnapshot) {
                                        document.reference.update("email", email)
                                    }
                                }
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                    }
            } else {
                updateUser(updateMap)
            }
        }
    }

    private fun updateUser(map: HashMap<String, Any>) {
        firestore.collection("users")
            .document(user.id)
            .update(map)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Update successfully", Toast.LENGTH_LONG)
                    .show()
                navigateToAccountFragment()
            }
    }

    private fun navigateToAccountFragment() {
        findNavController().navigate(R.id.accountFragment)
//        findNavController().navigateUp()
    }
}