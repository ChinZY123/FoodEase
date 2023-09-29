package com.example.foodease.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.foodease.R
import com.example.foodease.databinding.FragmentEventCreateBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class EventCreateFragment : Fragment() {

    private var _binding : FragmentEventCreateBinding? = null
    private val binding get() = _binding!!

    private lateinit var storageRef : StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the back button
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Hide the bottom navigation view
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavAdmin)
        bottomNavigationView?.visibility = View.GONE

        _binding = FragmentEventCreateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreate = binding.buttonCreateEvent

        buttonCreate.setOnClickListener{
            val eventName = binding.editTextEventName.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            val startDate = binding.editTextStartingDate.text.toString().trim()
            val endDate = binding.editTextStartingDate.text.toString().trim()
            val venue = binding.editTextVenueAddress.text.toString().trim()
            val editTextVolunteerRequired = binding.editTextVolunteerRequired.text.toString().trim()
            val volunteerRequired = 0

            //Validate Event Name
            if (eventName.isEmpty()){
                Snackbar.make(view, "Title cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Validate Description
            if(description.isEmpty()){
                Snackbar.make(view, "Description cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(startDate.isEmpty()){
                Snackbar.make(view, "Starting Date cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(endDate.isEmpty()){
                Snackbar.make(view, "Ending Date cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(venue.isEmpty()){
                Snackbar.make(view, "Venue address cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(editTextVolunteerRequired.isEmpty()){
                Snackbar.make(view, "Volunteer Required cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Database
            val database = Firebase.database
            val ref = database.getReference("events") //Database Name
            val newChildRef = ref.push()
            val id = newChildRef.key?: ""

            val event = Event(id,eventName,description, venue, startDate, endDate, volunteerRequired)

            newChildRef.setValue(event).addOnSuccessListener {
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.eventFragment)
            }.addOnFailureListener{
                Snackbar.make(view, "Failed", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //Show the bottom navigation view
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavAdmin)
        bottomNavigationView?.visibility = View.VISIBLE
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}