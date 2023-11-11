package com.example.myapplication.blockchainapp.presentation.appinterface.functions.updatemedicine

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.blockchainapp.data.blockchianapi.MedicineApi
import com.example.myapplication.blockchainapp.data.blockchianapi.url
import com.example.myapplication.blockchainapp.data.dto.Medicine
import com.example.myapplication.blockchainapp.data.dto.MedicineId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UpdateMedicineViewModel : ViewModel() {
    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    fun updateTextFieldValue(newValue: String) {
        _id.value = newValue
    }

    private val _sender = MutableStateFlow("")
    val sender = _sender.asStateFlow()

    fun updateSenderTextFieldValue(newValue: String) {
        _sender.value = newValue
    }

    private val _receiver = MutableStateFlow("")
    val receiver = _receiver.asStateFlow()

    fun updateReceiverTextFieldValue(newValue: String) {
        _receiver.value = newValue
    }

    var submitChangeDialogue = mutableStateOf(false)

    var changeConfirmDialogue = mutableStateOf(false)

    var success = mutableStateOf(false)

    var isKeyBoardActive = mutableStateOf(false)


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

    val gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

    // Build Retrofit with the custom Gson instance
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    fun events(updateScreenEvents: UpdateScreenEvents) {
        when (updateScreenEvents) {
            is UpdateScreenEvents.Get -> {
                getForUpdate(
                    retrofit = retrofit
                )
            }

            is UpdateScreenEvents.Post -> {
                update(
                    retrofit = retrofit
                )
            }

        }
    }

    private fun getForUpdate(
        retrofit: Retrofit
    ) {
        updateLoadingState(true)
        viewModelScope.launch {
            try {
                val medicineApiService = retrofit.create(MedicineApi::class.java)
                val medicineData = medicineApiService.getMedicine(
                    MedicineId(
                        ID = _id.value
                    )
                )
                _oneMedicineData.value = medicineData

                Log.e("Retrofit :  ${_loading.value}", medicineData.toString())
            } catch (e: Exception) {
                Log.e("Retrofit Fail : ${_loading.value}", e.message.toString())

            } finally {
                Log.e("Retrofit Finally : ${_loading.value}", "${_oneMedicineData.value}")
                updateLoadingState(false)
            }
        }
    }

    private fun update(retrofit: Retrofit) {
        updateLoadingState(true)
        viewModelScope.launch {
            try {
                val medicineApiService = retrofit.create(MedicineApi::class.java)
                val response = medicineApiService.updateMedicine(
                    data = Medicine(
                        ID = oneMedicineData.value!!.ID,
                        Name = oneMedicineData.value!!.Name,
                        Manufacturer = oneMedicineData.value!!.Manufacturer,
                        ManufactureDate = oneMedicineData.value!!.ManufactureDate,
                        ExpiryDate = oneMedicineData.value!!.ExpiryDate,
                        BrandName = oneMedicineData.value!!.BrandName,
                        Composition = oneMedicineData.value!!.Composition,
                        SenderId = _sender.value,
                        ReceiverId = _receiver.value,
                        DrapNo = oneMedicineData.value!!.DrapNo,
                        DosageForm = oneMedicineData.value!!.DosageForm,
                        TimeStamp = oneMedicineData.value!!.TimeStamp,
                        Batch_No = oneMedicineData.value!!.Batch_No,
                        JourneyCompleted = "false"
                    )
                )
                success.value = true
                changeConfirmDialogue.value = true
            } catch (e: Exception) {
                // Handle the exception
                Log.e("Retrofit: Exception", e.message.toString())
                success.value = false
                changeConfirmDialogue.value = true
            } finally {
                Log.e("Retrofit Finally: ${_loading.value}", "${_oneMedicineData.value}")
                getForUpdate(retrofit = retrofit)
                updateLoadingState(false)
            }
        }
    }
    fun getChainUsers(userEmail: String): MutableList<ChainUser> {

        val db = FirebaseFirestore.getInstance()


        val userCollection = db.collection("Manufacturer")


        val chainUsersCollection = userCollection.document(userEmail).collection("ChainUsers")


        val chainUsers = chainUsersCollection.get()


        val addedChainUsers = mutableListOf<ChainUser>()
        chainUsers.addOnSuccessListener { result ->
            for (document in result.documents) {
                val chainUser = document.toObject(ChainUser::class.java)
                if (chainUser != null) {
                    addedChainUsers.add(chainUser)
                }
            }
        }

// Return the list of chain users
        return addedChainUsers
    }
}
data class ChainUser(val email : String)