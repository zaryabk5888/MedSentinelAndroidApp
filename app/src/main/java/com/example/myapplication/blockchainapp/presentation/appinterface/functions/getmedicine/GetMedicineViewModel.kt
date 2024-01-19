package com.example.myapplication.blockchainapp.presentation.appinterface.functions.getmedicine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.blockchainapp.data.blockchianapi.MedicineApi
import com.example.myapplication.blockchainapp.data.blockchianapi.address
import com.example.myapplication.blockchainapp.data.dto.Medicine
import com.example.myapplication.blockchainapp.data.dto.MedicineId
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetMedicineViewModel :ViewModel() {
    private val _loadingQrCode = MutableStateFlow(false)
    val loadingQrCode = _loadingQrCode.asStateFlow()

    fun updateLoadingQrState(newValue: Boolean) {
        _loadingQrCode.value = newValue
    }

    private val _id = MutableStateFlow("6")
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
    private val _json = MutableStateFlow<String?>(null)
    val json = _json.asStateFlow()

    private fun updateJsonState(newValue: String) {
        _json.value = newValue
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

            GetScreenEvents.GenerateQrCode -> {
                try {
                    val medicineData = Medicine(
                        ID = oneMedicineData.value!!.ID,
                        Name =oneMedicineData.value!!.Name,
                        Manufacturer = oneMedicineData.value!!.Manufacturer,
                        ManufactureDate = oneMedicineData.value!!.ManufactureDate,
                        ExpiryDate = oneMedicineData.value!!.ExpiryDate,
                        BrandName = oneMedicineData.value!!.BrandName,
                        Composition = oneMedicineData.value!!.Composition,
                        SenderId = oneMedicineData.value!!.SenderId,
                        ReceiverId = oneMedicineData.value!!.ReceiverId,
                        DrapNo = oneMedicineData.value!!.DrapNo,
                        DosageForm = oneMedicineData.value!!.DosageForm,
                        TimeStamp = oneMedicineData.value!!.TimeStamp,
                        Batch_No = oneMedicineData.value!!.Batch_No,
                        JourneyCompleted = "false"
                    )

                    val json = Gson().toJson(medicineData)
                    updateJsonState(json)
                }  catch (e:Exception){
                    Log.e("Error : ", e.message.toString())
                }finally {
                    updateLoadingQrState(true)
                }

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


