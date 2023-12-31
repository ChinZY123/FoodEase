package com.example.foodease.ui.inventory

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodease.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class InventoryListUpdateFragment : Fragment() {
    private lateinit var updatedId: TextView
    private lateinit var updatedName: EditText
    private lateinit var updatedDesc: EditText
    private lateinit var updatedQuantity: EditText
    private lateinit var updatedDate: EditText
    private lateinit var btnSaveUpdate: Button
    private lateinit var btnUpdateBack: Button
    private lateinit var dbRef: DatabaseReference
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_inventory_list_update, container, false)
        updatedId = view.findViewById(R.id.textViewUpdateId)
        updatedName = view.findViewById(R.id.foodnameEditText)
        updatedDesc = view.findViewById(R.id.descEditText)
        updatedQuantity = view.findViewById(R.id.quantityEditText)
        updatedDate = view.findViewById(R.id.dateEditText)
        btnSaveUpdate = view.findViewById(R.id.buttonSaveUpdate)
        btnUpdateBack = view.findViewById(R.id.buttonUpdateBack)

        dbRef = FirebaseDatabase.getInstance().getReference("inventory")

        val args = arguments
        if (args != null) {
            val inventoryId = args.getString("InventoryId")
            val inventoryName = args.getString("InventoryName")
            val inventoryDesc = args.getString("InventoryDesc")
            val inventoryQuantity = args.getString("InventoryQuantity")
            val inventoryDate = args.getString("InventoryDate")

            Log.d(ContentValues.TAG, "Inventory ID: $inventoryId")
            Log.d(ContentValues.TAG, "Inventory ID: $inventoryName")
            Log.d(ContentValues.TAG, "Inventory ID: $inventoryDesc")
            Log.d(ContentValues.TAG, "Inventory ID: $inventoryQuantity")
            Log.d(ContentValues.TAG, "Inventory ID: $inventoryDate")

            // Find TextViews in  fragment layout and set the data

            updatedId.text = "$inventoryId"
            updatedName.setText(inventoryName)
            updatedDesc.setText(inventoryDesc)
            updatedQuantity.setText(inventoryQuantity)
            updatedDate.setText(inventoryDate)

        }

        btnSaveUpdate.setOnClickListener {
            val name = updatedName.text.toString()
            val desc = updatedDesc.text.toString()
            val quantity = updatedQuantity.text.toString()
            val date = updatedDate.text.toString()

            if(inputValidate(name, desc, date, quantity)) {

                val inventory = mapOf(
                    "name" to name,
                    "description" to desc,
                    "quantity" to quantity,
                    "date" to date

                )


                val inventoryId = args?.getString("InventoryId")
                if (inventoryId != null) {
                    dbRef.child(inventoryId).updateChildren(inventory).addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Inventory updated successful",
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_inventoryListUpdateFragment_to_inventoryListFragment)

                    }.addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Fail to update inventory",
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_inventoryListUpdateFragment_to_inventoryListFragment)

                    }

                    val inventoryItem = Inventory(
                        name = name,
                        description = desc,
                        date = date,
                        quantity = quantity)

                    if(inventoryItem != null){
                        inventoryViewModel.update(inventoryItem)
                        Log.d(ContentValues.TAG, "Inventory updated successfully to Room Database")
                    }else{
                        Log.d(ContentValues.TAG, "Inventory failed to update to Room Database")
                    }
                }

            }else{
                Toast.makeText(requireContext(), "Fill up the required field", Toast.LENGTH_LONG).show()
            }


        }
        btnUpdateBack.setOnClickListener {
            findNavController().navigate(R.id.action_inventoryListUpdateFragment_to_inventoryListFragment)
        }
        return view
    }

    private fun inputValidate(name: String, desc: String, date: String, quantity: String): Boolean{
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(quantity)
    }



}