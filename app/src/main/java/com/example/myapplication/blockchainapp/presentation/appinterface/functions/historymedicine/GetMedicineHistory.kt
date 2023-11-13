package com.example.myapplication.blockchainapp.presentation.appinterface.functions.historymedicine
//noinspection UsingMaterialAndMaterial3Libraries

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.blockchainapp.data.dto.Medicine
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import java.util.concurrent.Executors


@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun GetMedicineHistoryScreen(
    navController: NavHostController,
    getMedicineHistoryViewModel: GetMedicineHistoryViewModel = viewModel()
) {



    val textFieldValue by getMedicineHistoryViewModel.id.collectAsState()

    val loading by getMedicineHistoryViewModel.loading.collectAsState()

    val cameraOn by getMedicineHistoryViewModel.cameraOn.collectAsState()

    val authenticityScore by getMedicineHistoryViewModel.authenticityScore.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current



    val snackBarHostState = remember {
        SnackbarHostState()
    }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) {
                    Snackbar(
                        action = {
                            TextButton(
                                onClick = { it.dismiss() },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.Cyan
                                )
                            ) {
                                Text("Dismiss")
                            }
                        },
                    ) {
                        Text(
                            text = it.visuals.message,
                            color = Color.Cyan
                        )
                    }
                }
            },
            topBar = {
                TopAppBar(title = { Text(text = "Medicine History") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
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
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            label = { Text(text = "ID") },
                            value = textFieldValue,
                            onValueChange = { getMedicineHistoryViewModel.updateTextFieldValue(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        IconButton(
                            onClick = {
                                getMedicineHistoryViewModel.updateCameraState(!cameraOn)
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Camera, contentDescription = "Qr Code Scan")
                        }

                    }
                    if (cameraOn){
                        PreviewViewComposable(getMedicineHistoryViewModel)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ElevatedButton(
                            onClick = {
                                // Authenticate the medicine
                                getMedicineHistoryViewModel.events(
                                    getScreenEvents = GetHistoryScreenEvents.GetHistory
                                )
                                keyboardController?.hide()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Check Authenticity",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        ElevatedButton(
                            onClick = {
                                getMedicineHistoryViewModel.updateTextFieldValue(newValue = "")
                                getMedicineHistoryViewModel.allMedicineData.value = emptyList()
                                getMedicineHistoryViewModel.button = false
                                getMedicineHistoryViewModel.clearAuthenticityScoreState()
                            },
                            modifier = Modifier.weight(1f),

                            ) {
                            Text(
                                text = "Clear ",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    //Dialog to show Success or Failure
                    if (getMedicineHistoryViewModel.submitChangeDialogue.value) {
                        LaunchedEffect(Unit) {
                            delay(12000) // Wait for 2 seconds
                        }
                        ShowScoreDialog(
                            onClose = {
                                getMedicineHistoryViewModel.submitChangeDialogue.value = false
                            },
                            getMedicineHistoryViewModel,
                            authenticityScore
                        )
                    }
                    // Display authenticity percentage

                    Spacer(modifier = Modifier.height(10.dp))

                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    } else {
                        if (getMedicineHistoryViewModel.allMedicineData.value.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Column {
                                Text(
                                    text = getMedicineHistoryViewModel.allMedicineData.value[0].ID,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 30.sp,
                                    fontFamily = FontFamily.Cursive,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.align(
                                        alignment = Alignment.CenterHorizontally
                                    ),

                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                ElevatedButton(
                                    onClick = {
                                              getMedicineHistoryViewModel.submitChangeDialogue.value = true
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "Show Score")
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                getMedicineHistoryViewModel.allMedicineData.value.forEach { medicine ->
                                    EachHistoryRecord(medicine = medicine)
                                }
                            }
                        } else {
                            if (getMedicineHistoryViewModel.button){
                                LaunchedEffect(Unit) {
                                    snackBarHostState.showSnackbar(
                                        message = "No Medicine By That Id",
                                        actionLabel = "Dismiss",
                                        duration = SnackbarDuration.Short
                                    )
                                    getMedicineHistoryViewModel.button = false
                                }
                            }
                        }
                    }

                  
                }
            }
        }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowScoreDialog(
    onClose: () -> Unit,
    getMedicineHistoryViewModel: GetMedicineHistoryViewModel,
    authenticityScore: Int,
    ) {

    AlertDialog(
        onDismissRequest = onClose,
        modifier = Modifier
            .height(200.dp)
            .width(300.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Display authenticity percentage
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Authenticity Percentage: ${authenticityScore}%",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if(authenticityScore <= 60){
                Text(
                    text = "Authenticity Failed and Message sent to the Manufacturer",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                ElevatedButton(
                    onClick = {
                    getMedicineHistoryViewModel.submitChangeDialogue.value = false

                }) {
                    Text(text = "Close")
                }

            }
        }
    }
}




@Composable
fun EachHistoryRecord(medicine: Medicine) {
    val isExpanded = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { isExpanded.value = !isExpanded.value }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)

        ) {
          
            Text(
                text = "Name : ${medicine.Name}",
            )  
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                    text = "Journey Status : ${medicine.JourneyCompleted}",
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Batch No : ${medicine.Batch_No}",
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Brand Name : ${medicine.BrandName}",
                )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Drap No : ${medicine.DrapNo}",
                )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "TimeStamp Of transaction : ${medicine.TimeStamp}",
                )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Dosage Form : ${medicine.DosageForm}",
                )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Composition : ${medicine.Composition}",
                )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Manufacture Date : ${medicine.ManufactureDate}",
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Expiry Date : ${medicine.ExpiryDate}",
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sender ID : ${medicine.SenderId}",
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Receiver ID : ${medicine.ReceiverId}",
            )

        }
    }
}

class BarcodeAnalyser(
    val callback: () -> Unit
) : ImageAnalysis.Analyzer {
    // Store the barcode information in a mutable state variable
    private val barcodeInfo = mutableStateListOf<Barcode>()
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun  analyze(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_CODABAR,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_CODE_93,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_ITF,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E,
            )
            .build()

        val scanner = BarcodeScanning.getClient(options)

        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        logBarcodeInfo(barcode)

                    }
                    barcodeInfo.clear()
                    barcodeInfo.addAll(barcodes)
                    callback()
                }
                .addOnFailureListener { exception ->
                    Log.e("BarcodeAnalyser", "Error processing barcode: ${exception.message}", exception)
                }
        }
        imageProxy.close()
    }

    private fun logBarcodeInfo(barcode: Barcode) {
        Log.e("BarcodeAnalyser", "value: ${barcode.rawValue}")
        Log.d("BarcodeAnalyser", "Format: ${barcode.format}")
        Log.d("BarcodeAnalyser", "Bounding Box: ${barcode.boundingBox}")
        Log.d("BarcodeAnalyser", "URL: ${barcode.url}")
        // Log additional properties as needed
    }

    // Get the stored barcode information
    fun getBarcodeInfo(): List<Barcode> = barcodeInfo.toList()
}

@Composable
fun PreviewViewComposable(getMedicineHistoryViewModel: GetMedicineHistoryViewModel) {
    val barcodeInfo = getMedicineHistoryViewModel.barcodeInfo.collectAsState()

    // Dialog state
    val showDialog = remember { mutableStateOf(false) }

    AndroidView({ context ->
        val cameraExecutor = Executors.newSingleThreadExecutor()
        val previewView = PreviewView(context).also {
            it.scaleType = PreviewView.ScaleType.FILL_CENTER
        }
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageCapture = ImageCapture.Builder().build()

            val barcodeAnalyser = BarcodeAnalyser {
                showDialog.value = true // Show the dialog when a barcode is found
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    context as ComponentActivity, cameraSelector, preview, imageCapture, imageAnalyzer)

            } catch (exc: Exception) {
                Log.e("DEBUG", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
        previewView
    },
        modifier = Modifier
            .size(width = 250.dp, height = 250.dp)
    )
}
