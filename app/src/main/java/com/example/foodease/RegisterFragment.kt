package com.example.foodease

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodease.databinding.FragmentRegisterBinding
import com.example.foodease.ui.user.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) { //initialized auth object by calling the Firebase Authentication
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        // Show the toolbar
        (activity as AppCompatActivity?)?.supportActionBar?.show()

        //Hide the bottom navigation view
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavAdmin)
        bottomNavigationView?.visibility = View.GONE

        binding.textViewHyperLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        val registerButton = binding.buttonRegister
        Log.i("startRegister", "Auth Result ${auth.currentUser}")
        registerButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.length < 8) {
                Toast.makeText(
                    context,
                    "Password cannot less than 8 characters",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                Toast.makeText(context, "Confirm Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (confirmPassword != password) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Please Wait")
            progressDialog.setMessage("Loading ...")
            progressDialog.show()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid.toString()
                        val userProfile = User(userId, name, email)

                        user?.uid.let{userId->
                            val db = Firebase.database
                            val ref = db.getReference("users")
                            ref.child(userId.toString()).setValue(userProfile)
                        }

                        progressDialog.dismiss()

                        Toast.makeText(
                            requireContext(),
                            "Created successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("firebase", "Auth Result $user")
                        updateUI(user)
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }

        return view
    }

    private fun updateUI(user: FirebaseUser?) {
        // Implement UI updates here based on user registration status
        if (user != null) {
            findNavController().navigate(R.id.selectLogin)
        } else {
            // Registration failed, handle the error or show an appropriate message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
