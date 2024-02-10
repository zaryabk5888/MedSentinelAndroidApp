package com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.getmedicine

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetMedicineScreen(
    navController: NavHostController,
    getMedicineViewModel: GetMedicineViewModel = viewModel()
) {
    val textFieldValue by getMedicineViewModel.id.collectAsState()
    val oneMedicineData by getMedicineViewModel.oneMedicineData.collectAsState()
    val loading by getMedicineViewModel.loading.collectAsState()
    val json by getMedicineViewModel.json.collectAsState()
    val dialogue by getMedicineViewModel.dialogueBoxOpener.collectAsState()
    val loadingQrCode by getMedicineViewModel.loadingQrCode.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackBarHostState = SnackbarHostState()
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) {
                    Snackbar(
                        action = {
                            TextButton(
                                onClick = { it.dismiss() },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    "Dismiss",
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        },
                        modifier = Modifier.padding(
                            bottom = 40.dp
                        ),
                        content = {
                            Text(
                                text = it.visuals.message,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Medicine Data",
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go Back",
                            )
                        }
                    },
                )
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { getMedicineViewModel.updateTextFieldValue(it) },
                        label = {
                            Text(text = "ID")
                        },
                        modifier = Modifier.fillMaxWidth(),

                        )

                    Row(
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    ) {
                        ElevatedButton(
                            onClick = {
                                getMedicineViewModel.events(
                                    getScreenEvents = GetScreenEvents.GetId
                                )
                                keyboardController?.hide()
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(text = "Search")
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }

                        ElevatedButton(
                            onClick = {
                                getMedicineViewModel.updateTextFieldValue(newValue = "")
                                getMedicineViewModel.updateMedicineState(value = null)
                            },
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                        ) {
                            Text(text = "Clear All")
                            Icon(
                                imageVector = Icons.Filled.DeleteOutline,
                                contentDescription = "Clear All"
                            )
                        }
                    }

                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        if (oneMedicineData != null) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFFD5C7C7), Color(0xFF83ADF7))
                                        ),
                                        shape = RoundedCornerShape(2.dp)
                                    ),
                            ) {

                                Text(
                                    text = "Basic Information",
                                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 6.dp),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = Color(0xFF0487FA)
                                    )

                                MedicationInfoCard(
                                    title = "Journey Status",
                                    value = oneMedicineData!!.JourneyCompleted
                                )
                                MedicationInfoCard(
                                    title = "ID",
                                    value = oneMedicineData!!.ID
                                )
                                MedicationInfoCard(
                                        title = "Batch No",
                                value = oneMedicineData!!.Batch_No
                                )
                                MedicationInfoCard(
                                    title = "Name",
                                    value = oneMedicineData!!.Name
                                )
                                Text(
                                    text = "Additional Information",
                                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 6.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                MedicationInfoCard(
                                    title = "Brand Name",
                                    value = oneMedicineData!!.BrandName
                                )
                                MedicationInfoCard(
                                    title = "Drap No",
                                    value = oneMedicineData!!.DrapNo
                                )
                                MedicationInfoCard(
                                    title = "Dosage Form",
                                    value = oneMedicineData!!.DosageForm
                                )
                                MedicationInfoCard(
                                    title = "TimeStamp",
                                    value = oneMedicineData!!.TimeStamp
                                )
                                MedicationInfoCard(
                                    title = "Composition",
                                    value = oneMedicineData!!.Composition
                                )
                                MedicationInfoCard(
                                    title = "Manufacture Date",
                                    value = oneMedicineData!!.ManufactureDate
                                )
                                MedicationInfoCard(
                                    title = "Expiry Date",
                                    value = oneMedicineData!!.ExpiryDate
                                )

                                Text(
                                    text = "Transaction Information",
                                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 6.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )

                                MedicationInfoCard(
                                    title = "Sender ID",
                                    value = oneMedicineData!!.SenderId
                                )

                                MedicationInfoCard(
                                    title = "Receiver ID",
                                    value = oneMedicineData!!.ReceiverId
                                )
                                MedicationInfoCard(
                                    title = "Current Location",
                                    value = oneMedicineData!!.Location
                                )

                            }

                            //"manufacturer@gmail.com" == oneMedicineData!!.SenderId
                            // (FirebaseAuth.getInstance().currentUser!!.email.toString() == oneMedicineData!!.SenderId)
                            if ((FirebaseAuth.getInstance().currentUser!!.email.toString() == oneMedicineData!!.SenderId))
                            {
                                Button(
                                    onClick = {
                                        getMedicineViewModel.events(
                                            getScreenEvents = GetScreenEvents.GenerateQrCode
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(text = "Generate Qr Code")
                                }

                                if (loadingQrCode){
                                    DialogBoxForQrCode(json = json!!, id = oneMedicineData!!.ID)
                                }
                            }
                        }else{
                            LaunchedEffect(Unit){
                                snackBarHostState.showSnackbar(
                                    message = "No Record Found",
                                    actionLabel = "Dismiss",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MedicationInfoCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(bottom = 4.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
    }
}


@Composable
fun DialogBoxForQrCode(json: String, onDismiss: () -> Unit = {}, id: String) {
    var showDialog by remember { mutableStateOf(true) }
    var uploadStatus by remember { mutableStateOf<UploadStatus?>(null) }

    val context = LocalContext.current

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "QR Code",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Barcode(
                        modifier = Modifier
                            .size(250.dp),  // Adjust size as needed
                        resolutionFactor = 10,  // Optional: Increase resolution
                        type = BarcodeType.QR_CODE,
                        value = json
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to upload data to Firestore Storage
                    UploadButton(
                        json = json,
                        onSuccess = { uploadStatus = UploadStatus.Success },
                        onFailure = { uploadStatus = UploadStatus.Failure },
                        id = id
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Show upload status
                    uploadStatus?.let { status ->
                        UploadStatusIcon(status = status)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Dismiss the dialog when the user clicks the "Dismiss" button
                            showDialog = false
                            onDismiss()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Dismiss")
                    }
                }
            }
        }
    }
}

@Composable
private fun UploadButton(json: String, onSuccess: () -> Unit, onFailure: () -> Unit, id: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val uploadButtonState = remember { mutableStateOf(UploadButtonState.Idle) }

    Button(
        onClick = {
            when (uploadButtonState.value) {
                UploadButtonState.Idle -> {
                    uploadButtonState.value = UploadButtonState.Uploading
                    uploadToFirestoreStorage(storageRef, json, onSuccess, onFailure, id)
                }
                UploadButtonState.Uploading -> {
                    // Disable button during upload
                }
                UploadButtonState.Success, UploadButtonState.Failure -> {
                    // Reset button state
                    uploadButtonState.value = UploadButtonState.Idle
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        when (uploadButtonState.value) {
            UploadButtonState.Idle -> {
                Icon(Icons.Default.CloudUpload, contentDescription = null)
                Text(text = "Upload to Firestore")
            }
            UploadButtonState.Uploading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = "Uploading...")
            }
            UploadButtonState.Success -> {
                Icon(Icons.Default.Done, contentDescription = null, tint = Color.Green)
                Text(text = "Uploaded successfully", color = Color.Green)
            }
            UploadButtonState.Failure -> {
                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color.Red)
                Text(text = "Upload failed", color = Color.Red)
            }
        }
    }
}

private fun uploadToFirestoreStorage(
    storageRef: StorageReference,
    json: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    id: String
) {
    try {
        // Upload JSON to Firestore Storage
        // Convert JSON to bytes (you may need to adjust based on your data format)
        val jsonBytes = json.toByteArray(Charsets.UTF_8)

        // Create a reference to the file with a unique name
        val fileName = id//"qr_code_${System.currentTimeMillis()}.json"
        val qrCodeRef = storageRef.child(fileName)

        // Upload the file and metadata
        val uploadTask = qrCodeRef.putBytes(jsonBytes)

        uploadTask.addOnSuccessListener {
            // Upload success
            onSuccess()
            Log.d("Upload", "Upload success: $fileName")
        }.addOnFailureListener {
            // Upload failure
            onFailure()
            Log.e("Upload", "Upload failed", it)
        }
    } catch (e: Exception) {
        Log.e("Upload", "Upload failed", e)
        onFailure()
    }

}

@Composable
private fun UploadStatusIcon(status: UploadStatus) {
    val icon = when (status) {
        UploadStatus.Success -> Icons.Default.Done
        UploadStatus.Failure -> Icons.Default.Warning
    }

    val iconColor = when (status) {
        UploadStatus.Success -> Color.Green
        UploadStatus.Failure -> Color.Red
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconColor)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = when (status) {
            UploadStatus.Success -> "Upload successful"
            UploadStatus.Failure -> "Upload failed"
        })
    }
}

enum class UploadStatus {
    Success,
    Failure
}

enum class UploadButtonState {
    Idle,
    Uploading,
    Success,
    Failure
}