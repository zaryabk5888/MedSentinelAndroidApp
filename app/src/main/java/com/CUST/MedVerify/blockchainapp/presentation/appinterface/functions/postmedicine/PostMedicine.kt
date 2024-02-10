package com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.postmedicine

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostMedicineScreen(
    navController: NavHostController,
    postMedicineViewModel: PostMedicineViewModel = viewModel()
) {
    // Date picker state
    val datePicker = rememberDatePickerState()

    val loading by postMedicineViewModel.loading.collectAsState()

    val context = LocalContext.current

    val generateQrCode = remember {
        mutableStateOf(false)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Medicine Data") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardColors(
                        containerColor = Color.White.copy(alpha = 1f),
                        contentColor = Color.White.copy(alpha = 1f),
                        disabledContainerColor = Color.Blue.copy(alpha = 1f),
                        disabledContentColor = Color.Blue.copy(alpha = 1f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "New Medicine Data",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(30.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // ID field
                            OutlinedTextField(
                                value = postMedicineViewModel.id.value,
                                onValueChange = { postMedicineViewModel.id.value = it },
                                label = { Text(text = "ID") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Batch_No field
                            OutlinedTextField(
                                value = postMedicineViewModel.batch_no.value,
                                onValueChange = { postMedicineViewModel.batch_no.value = it },
                                label = { Text(text = "Batch No") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Medicine name field
                            OutlinedTextField(
                                value = postMedicineViewModel.name.value,
                                onValueChange = { postMedicineViewModel.name.value = it },
                                label = { Text(text = "Medicine Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Brand field
                            OutlinedTextField(
                                value = postMedicineViewModel.brand.value,
                                onValueChange = { postMedicineViewModel.brand.value = it },
                                label = { Text(text = "Brand") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Manufacturer field
                            OutlinedTextField(
                                value = postMedicineViewModel.manufacturer.value,
                                onValueChange = { postMedicineViewModel.manufacturer.value = it },
                                label = { Text(text = "Manufacturer") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Composition field
                            OutlinedTextField(
                                value = postMedicineViewModel.composition.value,
                                onValueChange = { postMedicineViewModel.composition.value = it },
                                label = { Text(text = "Composition") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Dosage field
                            OutlinedTextField(
                                value = postMedicineViewModel.dosageForm.value,
                                onValueChange = { postMedicineViewModel.dosageForm.value = it },
                                label = { Text(text = "DosageForm") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // DrapNo field
                            OutlinedTextField(
                                value = postMedicineViewModel.drapNo.value,
                                onValueChange = { postMedicineViewModel.drapNo.value = it },
                                label = { Text(text = "DrapNo") },
                                modifier = Modifier.fillMaxWidth()
                            )



                            Spacer(modifier = Modifier.height(10.dp))

                            // Manufactured Date field
                            OutlinedTextField(
                                value = postMedicineViewModel.manufacturedDate.value,
                                onValueChange = { },
                                label = { Text(text = "Manufactured Date") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        postMedicineViewModel.datePickerDialogForManufacturer.value =
                                            !postMedicineViewModel.datePickerDialogForManufacturer.value
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.EditCalendar,
                                            contentDescription = "Manufactured Date"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Expiry Date field
                            OutlinedTextField(
                                value = postMedicineViewModel.expiryDate.value,
                                onValueChange = { },
                                label = { Text(text = "Expiry Date") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        postMedicineViewModel.datePickerDialogForExpiry.value =
                                            !postMedicineViewModel.datePickerDialogForExpiry.value
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.EditCalendar,
                                            contentDescription = "Expiry Date"
                                        )
                                    }

                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Sender Name field
                            OutlinedTextField(
                                value = postMedicineViewModel.senderId.value,
                                onValueChange = { postMedicineViewModel.senderId.value = it },
                                label = { Text(text = "Sender Name") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Receiver Name field
                            OutlinedTextField(
                                value = postMedicineViewModel.receiverId.value,
                                onValueChange = { postMedicineViewModel.receiverId.value = it },
                                label = { Text(text = "Receiver Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))


                                //Location request
                            val settingResultRequest = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult()
                            ) { activityResult ->
                                if (activityResult.resultCode == Activity.RESULT_OK)
                                    Log.d("appDebug", "Accepted")
                                else {
                                    Log.d("appDebug", "Denied")
                                }
                            }

                            // location Status
                            OutlinedTextField(
                                value = "${postMedicineViewModel.currentLocationLatitude.value}, ${postMedicineViewModel.currentLocationLongitude.value}",
                                onValueChange = {

                                },
                                label = { Text(text = "Location") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(
                                        onClick = {

                                            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                postMedicineViewModel.getGpsPermissionStatus.value = true
                                                Toast.makeText(context,"Location Permission Not Granted",
                                                    Toast.LENGTH_SHORT).show()
                                            } else {

                                                postMedicineViewModel.checkLocationSetting(context,
                                                    onDisabled = { intentSenderRequest ->
                                                        settingResultRequest.launch(intentSenderRequest)
                                                    },
                                                    onEnabled = { /* This will call when setting is already enabled */
                                                        postMedicineViewModel.getLocation(context)
                                                    }
                                                )
                                            }

                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.GpsFixed,
                                            contentDescription = "Location"
                                        )
                                    }

                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Journey Status
                            OutlinedTextField(
                                value = "false",
                                onValueChange = { },
                                label = { Text(text = "Journey Status") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(26.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    if (postMedicineViewModel.id.value.isEmpty() || postMedicineViewModel.name.value.isEmpty() || postMedicineViewModel.batch_no.value.isEmpty() ||
                                        postMedicineViewModel.brand.value.isEmpty() || postMedicineViewModel.manufacturedDate.value.isEmpty() || postMedicineViewModel.expiryDate.value.isEmpty() ||
                                        postMedicineViewModel.senderId.value.isEmpty() || postMedicineViewModel.receiverId.value.isEmpty() ||
                                        postMedicineViewModel.dosageForm.value.isEmpty() || postMedicineViewModel.currentLocationLatitude.value.isEmpty()
                                        || postMedicineViewModel.currentLocationLongitude.value.isEmpty()
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Please fill all the fields",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        postMedicineViewModel.submit.value = true
                                        postMedicineViewModel.events(
                                            PostScreenEvents = PostScreenEvents.Add
                                        )
                                    }

                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = "Submit")
                            }
                        }
                    }
                }
                if (postMedicineViewModel.getGpsPermissionStatus.value){
                    Gps(postMedicineViewModel = postMedicineViewModel)
                }


                //Dialog to show Success or Failure
                if (postMedicineViewModel.submit.value) {
                    LaunchedEffect(Unit) {
                        delay(12000) // Wait for 2 seconds
                        postMedicineViewModel.submit.value = false // Automatically close the dialog
                    }
                    ShowStatusDialog(
                        success = postMedicineViewModel.success.value,
                        loading = loading,
                        onClose = {
                            postMedicineViewModel.submit.value = false
                                  },
                        viewModel = postMedicineViewModel
                    )
                }


                // Date picker dialog
                if (postMedicineViewModel.datePickerDialogForManufacturer.value || postMedicineViewModel.datePickerDialogForExpiry.value) {
                    DatePickerDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (postMedicineViewModel.datePickerDialogForManufacturer.value) {
                                        postMedicineViewModel.manufacturedDate.value =
                                            convertMillisToDate(datePicker.selectedDateMillis!!.toLong())
                                        postMedicineViewModel.datePickerDialogForManufacturer.value = false
                                    } else {
                                        postMedicineViewModel.expiryDate.value =
                                            convertMillisToDate(datePicker.selectedDateMillis!!.toLong())
                                        postMedicineViewModel.datePickerDialogForExpiry.value = false
                                    }
                                }
                            ) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    postMedicineViewModel.datePickerDialogForManufacturer.value = false
                                    postMedicineViewModel.datePickerDialogForExpiry.value = false
                                }
                            ) {
                                Text(text = "Close")
                            }
                        }
                    ) {
                        DatePicker(state = datePicker, showModeToggle = true)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowStatusDialog(
    success: Boolean,
    loading: Boolean,
    onClose: () -> Unit,
    viewModel: PostMedicineViewModel
) {
    BasicAlertDialog(
        onDismissRequest = onClose,
        modifier = Modifier
            .height(200.dp)
            .width(300.dp)
            .background(Color.White)
            .clip(
                RoundedCornerShape(4.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (success) Icons.Default.CheckCircle else Icons.Default.Error,
                contentDescription = "Status Icon",
                tint = if (success) Color.Green else Color.Red,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (loading) {
                    "Loading..."
                } else {
                    if (success) viewModel.retrofitMessage.value else viewModel.retrofitMessage.value
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (success) Color.Black else Color.Red
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (loading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = onClose) {
                    Text(text = "Close")
                }
            }
        }
    }
}


fun clearAll(postMedicineViewModel: PostMedicineViewModel) {
    postMedicineViewModel.id.value = ""
    postMedicineViewModel.name.value = ""
    postMedicineViewModel.brand.value = ""
    postMedicineViewModel.description.value = ""
    postMedicineViewModel.manufacturer.value = ""
    postMedicineViewModel.manufacturedDate.value = ""
    postMedicineViewModel.expiryDate.value = ""
    postMedicineViewModel.senderId.value = ""
    postMedicineViewModel.receiverId.value = ""
    postMedicineViewModel.retrofitMessage.value = ""
    postMedicineViewModel.composition.value = ""
    postMedicineViewModel.drapNo.value = ""
    postMedicineViewModel.dosageForm.value = ""
    postMedicineViewModel.batch_no.value = ""
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToDate(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDateTime.format(formatter)
}


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun Gps(postMedicineViewModel: PostMedicineViewModel) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission is granted. Continue with your task.
            postMedicineViewModel.getLocation(context)
        } else {
            postMedicineViewModel.currentGpsLocationStatus.value = "Permission Denied"
            Toast.makeText(context,"Grant Location Permission Manually",
                Toast.LENGTH_SHORT).show()
            // Permission is denied. Show a message to the user.
        }
    }

    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{

        }
    }

}

