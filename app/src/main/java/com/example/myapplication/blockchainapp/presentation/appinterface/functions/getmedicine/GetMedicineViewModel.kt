package com.example.myapplication.blockchainapp.presentation.appinterface.functions.getmedicine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.blockchainapp.data.blockchianapi.MedicineApi
import com.example.myapplication.blockchainapp.data.blockchianapi.address
import com.example.myapplication.blockchainapp.data.dto.Medicine
import com.example.myapplication.blockchainapp.data.dto.MedicineId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetMedicineViewModel :ViewModel() {

    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    fun updateTextFieldValue(newValue: String) {
        _id.value = newValue
    }

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private fun updateLoadingState(newValue: Boolean) {
        _loading.value = newValue
    }

    private val _oneMedicineData = MutableStateFlow<Medicine?>(null)
    val oneMedicineData = _oneMedicineData.asStateFlow()

    fun updateMedicineState(value: Medicine?) {
        _oneMedicineData.value = value
    }


    // other functions...


    fun events(getScreenEvents: GetScreenEvents) {
        when (getScreenEvents) {
            is GetScreenEvents.GetId -> {
                val retrofit = Retrofit.Builder()
                    .baseUrl(address)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                get(retrofit)
            }
        }
    }

    fun get(retrofit: Retrofit) {
        updateLoadingState(true)
        viewModelScope.launch {
            try {
                val medicineApiService = retrofit.create(MedicineApi::class.java)
                val medicineData = medicineApiService.getMedicine(MedicineId(
                    ID = _id.value
                ))
                _oneMedicineData.value = medicineData
                Log.e("Retrofit : ", medicineData.toString())
            } catch (e: Exception) {
                Log.e("Retrofit Fail :", e.message.toString())
            }finally {
                updateLoadingState(false)
            }
        }
    }
}


