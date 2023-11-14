package com.example.myapplication.blockchainapp.presentation.appinterface.functions.postmedicine

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.blockchainapp.data.blockchianapi.MedicineApi
import com.example.myapplication.blockchainapp.data.blockchianapi.address
import com.example.myapplication.blockchainapp.data.dto.Medicine
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostMedicineViewModel  : ViewModel() {

    private var medicineData = mutableStateOf<Any?>(null)

    var retrofitMessage = mutableStateOf("")

    var datePickerDialogForManufacturer = mutableStateOf(false)
    var datePickerDialogForExpiry = mutableStateOf(false)

    var success = mutableStateOf(false)

    var submit = mutableStateOf(false)

    var id = mutableStateOf("")
    var brand = mutableStateOf("")
    var name = mutableStateOf("")
    var manufacturer = mutableStateOf("")
    var composition = mutableStateOf("")
    var description = mutableStateOf("")
    var dosageForm = mutableStateOf("")
    var drapNo = mutableStateOf("")
    var expiryDate = mutableStateOf("")
    var manufacturedDate = mutableStateOf("")
    var senderId = mutableStateOf("${FirebaseAuth.getInstance().currentUser?.email}")
    var receiverId = mutableStateOf("")
    var batch_no = mutableStateOf("")


    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private fun updateLoadingState(newValue: Boolean) {
        _loading.value = newValue
    }



    fun events(PostScreenEvents: PostScreenEvents) {
        when (PostScreenEvents) {
            is PostScreenEvents.Add -> {
                val retrofit = Retrofit.Builder()
                    .baseUrl(address)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                post(retrofit)
            }
        }
    }

    private fun post(retrofit: Retrofit) {
        var response: Response<Any>?
        updateLoadingState(true)
        viewModelScope.launch {
            try {
                val medicineApiService = retrofit.create(MedicineApi::class.java)
                response = medicineApiService.createMedicine(
                    data = Medicine(
                        ID = id.value,
                        Name = name.value,
                        Manufacturer = manufacturer.value,
                        ManufactureDate = manufacturedDate.value,
                        ExpiryDate = expiryDate.value,
                        BrandName = brand.value,
                        Composition = composition.value,
                        SenderId = senderId.value,
                        ReceiverId = receiverId.value,
                        DrapNo = drapNo.value,
                        DosageForm = dosageForm.value,
                        TimeStamp = description.value,
                        Batch_No = batch_no.value,
                        JourneyCompleted = "false"
                    )
                )

                if (response!!.isSuccessful) {
                    Log.e("Retrofit ok :", response!!.body().toString())
                    // Handle successful response here
                    medicineData.value = response!!.body().toString()
                    retrofitMessage.value = "Successfully Added Medicine ${id.value}"
                    success.value = true

                } else {
                    Log.e("Retrofit not ok :", response.toString())
                    // Handle error response here
                    retrofitMessage.value = "Medicine against Id : ${id.value} already exists"
                    success.value = false
                }
            } catch (e: Exception) {
                Log.e("Retrofit: Exception", e.message.toString())
                // Handle exception here
            } finally {
                updateLoadingState(false)
            }
        }
    }

}



