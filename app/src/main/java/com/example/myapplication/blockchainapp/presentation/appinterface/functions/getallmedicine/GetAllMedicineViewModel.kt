package com.example.myapplication.blockchainapp.presentation.appinterface.functions.getallmedicine

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.blockchainapp.data.blockchianapi.MedicineApi
import com.example.myapplication.blockchainapp.data.blockchianapi.url
import com.example.myapplication.blockchainapp.data.dto.Medicine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class GetAllMedicineViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private fun updateLoadingState(newValue: Boolean) {
        _loading.value = newValue
    }

    var allMedicineData = mutableStateOf(emptyList<Medicine>())
        private set



    fun events(getAllScreenEvents: GetAllScreenEvents) {
        when (getAllScreenEvents) {
            is GetAllScreenEvents.GetAll -> {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                getAll(retrofit, allMedicineData)
            }
        }
    }


    private fun getAll(retrofit: Retrofit, allMedicineData: MutableState<List<Medicine>>) {
        updateLoadingState(true)
        viewModelScope.launch {
            try {
                val medicineApiService = retrofit.create(MedicineApi::class.java)
                val medicineList = medicineApiService.medicineList()
                allMedicineData.value = medicineList
                Log.e("Retrofit : ", medicineList.toString())
            } catch (e: Exception) {
                Log.e("Retrofit Fail :", e.message.toString())
            }finally {
                updateLoadingState(false)
            }
        }
    }
}


