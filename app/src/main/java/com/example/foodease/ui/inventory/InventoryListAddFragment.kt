package com.example.foodease.ui.inventory

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodease.databinding.FragmentInventoryListAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InventoryListAddFragment : Fragment() {
    private lateinit var binding: FragmentInventoryListAddBinding
    private lateinit var dbRef: DatabaseReference
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInventoryListAddBinding.inflate(inflater, container, false)

        dbRef = FirebaseDatabase.getInstance().getReference("inventory")

        binding.buttonSubmitInventory.setOnClickListener {
            insertInventoryToDatabase()
        }

        binding.buttonBackInventory.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun insertInventoryToDatabase(){
        val inventoryName = binding.editTextFoodName.text.toString()
        val inventoryDesc = binding.editTextFoodDesc.text.toString()
        val inventoryDate = binding.editTextExpiryDate.text.toString()
        val inventoryQuantity = binding.editTextQuantity.text.toString()

        Log.d(TAG, "Inventory Name: $inventoryName")
        Log.d(TAG, "Inventory Description: $inventoryDesc")
        Log.d(TAG, "Inventory Date: $inventoryDate")
        Log.d(TAG, "Inventory Quantity: $inventoryQuantity")



        if(inputValidate(inventoryName, inventoryDesc, inventoryDate, inventoryQuantity)){
            val id = dbRef.push().key!!
            val inventory = Inventory(id,inventoryName, inventoryDesc, inventoryDate, inventoryQuantity)



            dbRef.child(id).setValue(inventory).addOnCompleteListener {
                Log.d(TAG, "Inventory added successfully")
                Toast.makeText(requireContext(), "New inventory added", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Inventory added successfully to Room Database")
                findNavController().navigateUp()
            }.addOnFailureListener{ err ->
                Log.e(TAG, "Error adding inventory: ${err.message}", err)
                Toast.makeText(requireContext(), "Error ${err.message}", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }

            val inventoryItem = Inventory(
                name = inventoryName,
                description = inventoryDesc,
                date = inventoryDate,
                quantity = inventoryQuantity)

            if(inventoryItem != null){
                inventoryViewModel.insert(inventoryItem)
                Log.d(TAG, "Inventory added successfully to Room Database")
            }else{
                Log.d(TAG, "Inventory failed to added to Room Database")
            }


        }
        else{
            Toast.makeText(requireContext(), "Fill up the required field", Toast.LENGTH_LONG).show()
        }




    }

    private fun inputValidate(inventoryName: String, inventoryDesc: String, inventoryDate: String, inventoryQuantity: String): Boolean{
        return !TextUtils.isEmpty(inventoryName) && !TextUtils.isEmpty(inventoryDesc) && !TextUtils.isEmpty(inventoryDate) && !TextUtils.isEmpty(inventoryQuantity)
    }



}