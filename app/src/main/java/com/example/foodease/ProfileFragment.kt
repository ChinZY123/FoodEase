package com.example.foodease

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodease.databinding.FragmentProfileBinding
import com.example.foodease.ui.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the back button
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.profileFrame.visibility = View.GONE
        Log.i("startProfile", "Auth Result ${Firebase.auth.currentUser}")
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.show()

        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            val userId = user.uid
            val db = Firebase.database
            val userRef = db.getReference("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Parse and display user data
                        val userProfile = dataSnapshot.getValue(User::class.java)
                        if (userProfile != null) {
                            // Update the UI with user data
                            binding.textViewName.text = userProfile.name
                            binding.textViewEmail.text = userProfile.email

                            progressDialog.dismiss()
                            binding.profileFrame.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error

                }
            })

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.containerProfileInformation.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_profileInformationFragment)
        }

        binding.containerHistory.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_eventHistoryFragment)
        }

        binding.containerLogOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileFragment_to_selectLogin)
        }

        binding.containerEdit.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_userUpdateFragment)
        }
    }
}