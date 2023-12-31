package com.example.foodease.ui.donation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodease.database.donation.DonationDatabase
import com.example.foodease.database.donation.DonationRepository
import com.example.foodease.ui.event.Event
import kotlinx.coroutines.launch

class DonationViewModel(application: Application): AndroidViewModel(application) {
    private val selectedDonation = MutableLiveData<Donation>()

    private var donationList: LiveData<List<Donation>>
    private val repository: DonationRepository

    init {
        //val list = ArrayList<Donation>()
        //donationList.value = list
        val donationDao = DonationDatabase.getDatabase(application).donationDao()
        repository = DonationRepository(donationDao)
        donationList = repository.allDonations
    }

    fun insert(donation: Donation) = viewModelScope.launch {
        repository.insert(donation)
    }

    fun delete(donation: Donation) = viewModelScope.launch {
        repository.delete(donation)
    }

    fun update(donation: Donation) = viewModelScope.launch {
        repository.update(donation)
    }

    fun setSelectedDonation(donation: Donation) {
        selectedDonation.value = donation
    }

    val donations: LiveData<Donation> get() = selectedDonation
}