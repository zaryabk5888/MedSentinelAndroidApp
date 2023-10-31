package com.example.myapplication.blockchainapp.presentation.appinterface.functions.postmedicine

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                OutlinedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                    ) {
                        Text(
                            text = "Add Medicine Data",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
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
                                label = { Text(text = "ID") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Batch_No field
                            OutlinedTextField(
                                value = postMedicineViewModel.batch_no.value,
                                onValueChange = { postMedicineViewModel.batch_no.value = it },
                                label = { Text(text = "Batch No") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Medicine name field
                            OutlinedTextField(
                                value = postMedicineViewModel.name.value,
                                onValueChange = { postMedicineViewModel.name.value = it },
                                label = { Text(text = "Medicine Name") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Brand field
                            OutlinedTextField(
                                value = postMedicineViewModel.brand.value,
                                onValueChange = { postMedicineViewModel.brand.value = it },
                                label = { Text(text = "Brand") }
                            )

                            // Manufacturer field
                            OutlinedTextField(
                                value = postMedicineViewModel.manufacturer.value,
                                onValueChange = { postMedicineViewModel.manufacturer.value = it },
                                label = { Text(text = "Manufacturer") }
                            )

                            // Composition field
                            OutlinedTextField(
                                value = postMedicineViewModel.composition.value,
                                onValueChange = { postMedicineViewModel.composition.value = it },
                                label = { Text(text = "Composition") }
                            )

                            // Dosage field
                            OutlinedTextField(
                                value = postMedicineViewModel.dosageForm.value,
                                onValueChange = { postMedicineViewModel.dosageForm.value = it },
                                label = { Text(text = "DosageForm") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // DrapNo field
                            OutlinedTextField(
                                value = postMedicineViewModel.drapNo.value,
                                onValueChange = { postMedicineViewModel.drapNo.value = it },
                                label = { Text(text = "DrapNo") }
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
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Expiry Date field
                            OutlinedTextField(
                                value = postMedicineViewModel.expiryDate.value,
                                onValueChange = { },
                                label = { Text(text = "Expiry Date") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        postMedicineViewModel.datePickerDialogForExpiry.value = !postMedicineViewModel.datePickerDialogForExpiry.value
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.EditCalendar,
                                            contentDescription = "Expiry Date"
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Sender Name field
                            OutlinedTextField(
                                value = postMedicineViewModel.senderId.value,
                                onValueChange = { postMedicineViewModel.senderId.value = it },
                                label = { Text(text = "Sender Name") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Receiver Name field
                            OutlinedTextField(
                                value = postMedicineViewModel.receiverId.value,
                                onValueChange = { postMedicineViewModel.receiverId.value = it },
                                label = { Text(text = "Receiver Name") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Journey Status
                            OutlinedTextField(
                                value = "false",
                                onValueChange = {  },
                                label = { Text(text = "Journey Status") }
                            )
                        }

                        Spacer(modifier = Modifier.height(26.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    postMedicineViewModel.submit.value = true
                                    postMedicineViewModel.events(
                                        PostScreenEvents =PostScreenEvents.Add
                                    )
                                },
                                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
                            ) {
                                Text(text = "Submit")
                            }
                        }
                    }
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
    AlertDialog(
        onDismissRequest = onClose,
        modifier = Modifier.size(300.dp).background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
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
                text = if (loading){"Loading..."} else{if (success) viewModel.retrofitMessage.value else viewModel.retrofitMessage.value },
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
