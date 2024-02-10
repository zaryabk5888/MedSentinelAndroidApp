package com.CUST.MedVerify.blockchainapp.presentation.users.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MedicineAddedViewModel : ViewModel() {
    private val _loadingOne = MutableStateFlow(false)
    val loadingOne = _loadingOne.asStateFlow()

}