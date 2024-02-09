package com.medchain.medverify.blockchainapp.presentation.appinterface.functions.updatemedicine

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medchain.medverify.blockchainapp.data.blockchianapi.MedicineApi
import com.medchain.medverify.blockchainapp.data.blockchianapi.address
import com.medchain.medverify.blockchainapp.data.dto.Medicine
import com.medchain.medverify.blockchainapp.data.dto.MedicineId
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date

class UpdateMedicineViewModel : ViewModel() {
    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    fun updateTextFieldValue(newValue: String) {
        _id.value = newValue
    }

    private val _sender = MutableStateFlow("${FirebaseAuth.getInstance().currentUser?.email}")
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

    var currentLocationLatitude = mutableStateOf("")

    var currentLocationLongitude = mutableStateOf("")

    var currentGpsLocationStatus = mutableStateOf("")

    var getGpsPermissionStatus = mutableStateOf(false)

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private fun updateLoadingState(newValue: Boolean) {
        _loading.value = newValue
    }

    private val _camerOn = MutableStateFlow(false)
    val cameraOn = _camerOn.asStateFlow()

    fun updateCameraState(newValue: Boolean) {
        _camerOn.value = newValue
    }

    private val _oneMedicineData = MutableStateFlow<Medicine?>(null)
    val oneMedicineData = _oneMedicineData.asStateFlow()

    fun updateMedicineState(value: Medicine?) {
        _oneMedicineData.value = value
    }

    var startQrCodeScanner by mutableStateOf(true)
    var processingQrCodedata by mutableStateOf(true)
    var qrCodeData by mutableStateOf("")

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

    // Build Retrofit with the custom Gson instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(address)
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
                if (FirebaseAuth.getInstance().currentUser?.displayName == "retailer"){
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
                            JourneyCompleted = "true",
                            Location = "${currentLocationLatitude.value},${currentLocationLongitude.value}"
                        )
                    )
                }else{
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
                            JourneyCompleted = "false",
                            Location = "${currentLocationLatitude.value},${currentLocationLongitude.value}"
                        )
                    )
                }

                success.value = true
                changeConfirmDialogue.value = true
            } catch (e: Exception) {
                // Handle the exception
                Log.e("Retrofit: Exception", e.message.toString())
                success.value = false
                changeConfirmDialogue.value = true
            } finally {
                Log.e("Retrofit Finally: ${_loading.value}", "${_oneMedicineData.value}")
                try {
                    val currentTime = Date().time
                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val formattedDate = formatter.format(currentTime)


                    val message = "Transferred ownership of ${oneMedicineData.value!!.ID} from ${sender.value} to ${receiver.value} on $formattedDate"

                    val data = hashMapOf(
                        "message" to message,
                    )
                    FirebaseFirestore.getInstance()
                        .collection(
                            FirebaseAuth.getInstance().currentUser?.displayName.toString().replaceFirstChar {
                            it.uppercase()
                        }
                        )
                        .document("${FirebaseAuth.getInstance().currentUser?.email}")
                        .collection("Messages")
                        .add(data).addOnSuccessListener { result ->
                            Log.e(ContentValues.TAG, "listToShow: $result")
                        }.addOnCompleteListener {
                            
                        }
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error saving message: $e")
                }finally {
                    getForUpdate(retrofit = retrofit)
                    updateLoadingState(false)
                }

            }
        }
    }

    fun getLocation(context: Context) {
        val fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        )
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Process location data
                    currentLocationLatitude.value = location.latitude.toString()
                    currentLocationLongitude.value = location.longitude.toString()
                    Log.d("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                    currentGpsLocationStatus.value = "Success"
                } else {
                    // Handle location not found
                    currentGpsLocationStatus.value = "Fail"
                    Log.d("Location", "Location not found")
                }
            }
            .addOnFailureListener { exception ->
                // Handle location retrieval errors
                currentGpsLocationStatus.value = "Fail"
                Log.e("Location", "Error getting location: ${exception.message}")
            }
    }

    // call this function on button click
    fun checkLocationSetting(
        context: Context,
        onDisabled: (IntentSenderRequest) -> Unit,
        onEnabled: () -> Unit
    ) {

        val locationRequest = LocationRequest.create().apply {

        }

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)

        val gpsSettingTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())

        gpsSettingTask.addOnSuccessListener { onEnabled() }
        gpsSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()
                    onDisabled(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // ignore here
                }
            }
        }

    }

}
