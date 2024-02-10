package com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.postmedicine

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CUST.MedVerify.blockchainapp.data.blockchianapi.MedicineApi
import com.CUST.MedVerify.blockchainapp.data.blockchianapi.address
import com.CUST.MedVerify.blockchainapp.data.dto.Medicine
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    var currentLocationLatitude = mutableStateOf("")

    var currentLocationLongitude = mutableStateOf("")

    var currentGpsLocationStatus = mutableStateOf("")

    var getGpsPermissionStatus = mutableStateOf(false)


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
                        JourneyCompleted = "false",
                        Location = "${currentLocationLatitude.value}, ${currentLocationLongitude.value}"
                    )
                )

                if (response!!.isSuccessful) {
                    Log.e("Retrofit ok :", response!!.body().toString())
                    // Handle successful response here
                    medicineData.value = response!!.body().toString()
                    retrofitMessage.value = "Successfully Added Medicine ${id.value}"
                    success.value = true
                    try {
                        val medicineID = "${id.value}"

                        val medicine = hashMapOf(
                            "medicineID" to medicineID,
                        )
                        FirebaseFirestore.getInstance()
                            .collection("Manufacturer")
                            .document(senderId.value)
                            .collection("Medicine-Added")
                            .document()
                            .set(medicine)
                            .addOnSuccessListener { result ->
                                Log.e(ContentValues.TAG, "Success in adding: $result")
                            }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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



