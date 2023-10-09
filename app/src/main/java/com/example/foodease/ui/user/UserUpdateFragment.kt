package com.example.foodease.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.foodease.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [UserUpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserUpdateFragment : Fragment() {
    private lateinit var nameEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var updateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_update, container, false)

        nameEditText = view.findViewById(R.id.name)
        idEditText = view.findViewById(R.id.idUser)
        updateButton = view.findViewById(R.id.buttonUpdate2)

        // Handle the update button click
        updateButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val id = idEditText.text.toString()

            if (validateUserData(newName, id)) {
                // Update the user's data in Firebase using the provided ID
                updateUserData(id, newName)
                findNavController().navigate(R.id.action_userUpdateFragment_to_profileFragment)
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT)

                // Show a success message or navigate to another screen
            } else {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT)
                // Show an error message or handle validation errors
            }
        }

        return view
    }

    private fun validateUserData(name: String, id: String): Boolean {
        // Implement your validation logic here
        // Return true if data is valid, false otherwise
        return !name.isEmpty() && !id.isEmpty()
    }

    private fun updateUserData(id: String, name: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")

        databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Update the user's data in the database
                    dataSnapshot.ref.child("name").setValue(name)

                    // Show a success message or navigate to another screen
                } else {
                    // Handle the case where the provided ID does not exist in the database
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}
