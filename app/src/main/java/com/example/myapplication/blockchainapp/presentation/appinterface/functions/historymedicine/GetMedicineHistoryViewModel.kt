package com.example.myapplication.blockchainapp.presentation.appinterface.functions.historymedicine

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.blockchainapp.data.blockchianapi.MedicineApi
import com.example.myapplication.blockchainapp.data.blockchianapi.address
import com.example.myapplication.blockchainapp.data.dto.Medicine
import com.example.myapplication.blockchainapp.data.dto.MedicineId
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetMedicineHistoryViewModel :ViewModel() {

    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    fun updateTextFieldValue(newValue: String) {
        _id.value = newValue
    }

    var previousId = mutableStateOf("")

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private fun updateLoadingState(newValue: Boolean) {
        _loading.value = newValue
    }

    private val _authenticityScore = MutableStateFlow(0)
    val authenticityScore = _authenticityScore.asStateFlow()

    fun updateAuthenticityScoreState(newValue: Int) {
        _authenticityScore.value += newValue
    }
    fun clearAuthenticityScoreState() {
        _authenticityScore.value = 0
    }

    var submitChangeDialogue = mutableStateOf(false)

    private val _bar = MutableStateFlow("")
    val barcodeInfo = _bar.asStateFlow()

    fun updateBarState(newValue: String) {
        _bar.value = newValue
    }

    var authenticityPercentage by mutableStateOf(0)

    var button by mutableStateOf(false)

    private val _camerOn = MutableStateFlow(false)
    val cameraOn = _camerOn.asStateFlow()

    fun updateCameraState(newValue: Boolean) {
        _camerOn.value = newValue
    }

    var allMedicineData = mutableStateOf(emptyList<Medicine>())
        private set




    fun events(getScreenEvents: GetHistoryScreenEvents) {
        when (getScreenEvents) {
            is GetHistoryScreenEvents.GetHistory -> {
                val retrofit = Retrofit.Builder()
                    .baseUrl(address)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                getHistory(retrofit)
            }
        }
    }

    fun getHistory(retrofit: Retrofit) {
        updateLoadingState(true)
        viewModelScope.launch {
            try {
                val medicineApiService = retrofit.create(MedicineApi::class.java)
                val medicineData = medicineApiService.medicineHistoryList(MedicineId(
                    ID = _id.value
                )
                )
                previousId.value = _id.value

                allMedicineData.value = medicineData
                Log.e("Retrofit : ", medicineData.size.toString())
            } catch (e: Exception) {
                Log.e("Retrofit Fail :", e.message.toString())
            }finally {
                if (allMedicineData.value.isEmpty()){
                    button = true
                }
                updateLoadingState(false)
                if (authenticityScore.value==0 || previousId.value!=_id.value){
                    _authenticityScore.value = 0
                    val medicine = allMedicineData.value.lastOrNull()
                    if (medicine != null) {
                        authenticateMedicine(medicine = medicine)
                    }
                }


            }
        }
    }

    private fun authenticateMedicine(
        medicine: Medicine,
    ): Int {
        val db = Firebase.firestore
        var res = 0
        db.collection("DRAP_Api")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.id == medicine.Name){
                        if (document.data["Drap_No"] == medicine.DrapNo){
                            updateAuthenticityScoreState(20)
                            Log.e("doom", "${authenticityScore.value}")
                        }
                    }else{
                        continue
                    }
                }
            }.addOnFailureListener { exception ->
                Log.w("", "Error getting documents.", exception)
            }.continueWith {
                if (medicine.Batch_No.isNotEmpty()){
                    updateAuthenticityScoreState(20)
                }
                if (medicine.Manufacturer.isNotEmpty()){
                    updateAuthenticityScoreState(10)
                }
                if (medicine.ID.isNotEmpty()){
                   updateAuthenticityScoreState(15)
                }
                res =  finalCheck(medicine)
            }

        return res

    }

    fun finalCheck(medicine: Medicine): Int {
        if (allMedicineData.value.size<8){
            updateAuthenticityScoreState(15)
        }
        var isFakeData = false

        allMedicineData.value.forEach {
            if (it.ID!=medicine.ID ||
                it.Manufacturer!=medicine.Manufacturer ||
                it.ManufactureDate!=medicine.ManufactureDate ||
                it.Batch_No!=medicine.Batch_No ||
                it.Name!=medicine.Name ||
                it.DrapNo!=medicine.DrapNo ||
                it.BrandName!=medicine.BrandName
            ){//fake one exist
                isFakeData = true
                return@forEach
            }
        }
        if (isFakeData) {
            // Handle the case where fake data exists
        } else {
            // Add 20 points to a new variable (e.g., "points")
           updateAuthenticityScoreState(20)
        }
        Log.e("", "finalCheck: ${authenticityScore.value}", )
        return authenticityScore.value
    }

}


