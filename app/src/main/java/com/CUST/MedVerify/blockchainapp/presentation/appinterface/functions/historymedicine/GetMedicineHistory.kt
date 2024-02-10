package com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.historymedicine
//noinspection UsingMaterialAndMaterial3Libraries

import android.Manifest
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.CUST.MedVerify.blockchainapp.data.dto.Medicine
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import java.util.concurrent.Executors
import kotlin.math.cos
import kotlin.math.sin


@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun GetMedicineHistoryScreen(
    navController: NavHostController,
    getMedicineHistoryViewModel: GetMedicineHistoryViewModel = viewModel()
) {
    val context = LocalContext.current


    val textFieldValue by getMedicineHistoryViewModel.id.collectAsState()

    val loading by getMedicineHistoryViewModel.loading.collectAsState()

    val cameraOn by getMedicineHistoryViewModel.cameraOn.collectAsState()

    val authenticityScore by getMedicineHistoryViewModel.authenticityScore.collectAsState()

    val gpt3Response = getMedicineHistoryViewModel.gpt3Response.collectAsState()

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
                ) {Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(0.75f)
                            .padding(end = 10.dp),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 4.dp
                        )

                    ) {
                        OutlinedTextField(
                            label = { Text(text = "ID") },
                            value = textFieldValue,
                            onValueChange = { getMedicineHistoryViewModel.updateTextFieldValue(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black), // Set text color
                            shape = RoundedCornerShape(8.dp) // Add rounded corners
                        )
                    }

                    ElevatedCard(
                        modifier = Modifier
                            .weight(0.25f),
                        shape = CircleShape
                    ) {
                        IconButton(
                            onClick = {
                                getMedicineHistoryViewModel.updateCameraState(!cameraOn)
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .align(alignment = Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Camera,
                                contentDescription = "Qr Code Scan"

                            )
                        }
                    }
                }

                    if (cameraOn){
                        if (getMedicineHistoryViewModel.startQrCodeScanner){
                            getMedicineHistoryViewModel.qrCodeData = CameraPermissionComposable()

                            if (getMedicineHistoryViewModel.qrCodeData.isNotEmpty()){
                                getMedicineHistoryViewModel.startQrCodeScanner = false
                            }

                            if (!(getMedicineHistoryViewModel.processingQrCodedata and getMedicineHistoryViewModel.startQrCodeScanner)){
                                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                                if (getMedicineHistoryViewModel.qrCodeData.isNotEmpty()){
                                    val jsonQrCodeData = Gson().fromJson(getMedicineHistoryViewModel.qrCodeData, Medicine::class.java)
                                    getMedicineHistoryViewModel.updateTextFieldValue(jsonQrCodeData.ID)
                                    getMedicineHistoryViewModel.processingQrCodedata = !getMedicineHistoryViewModel.processingQrCodedata

                                }
                            }
                        }




                        Log.e(TAG, "qr: ${getMedicineHistoryViewModel.qrCodeData}", )
                        //PreviewViewComposable()
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clickable {
                                    // Handle Show History button click
                                    if (getMedicineHistoryViewModel.id.value.isNotEmpty()) {
                                        getMedicineHistoryViewModel.allMedicineData.value =
                                            emptyList()
                                        getMedicineHistoryViewModel.events(
                                            getScreenEvents = GetHistoryScreenEvents.GetHistory
                                        )
                                        getMedicineHistoryViewModel.updateTextFieldValue("")

                                        keyboardController?.hide()
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                "Scan or Enter ID",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                    //
                                },
                            color = Color(0xFF677EFA), // Green color
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Show History",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp)) // Add some spacing between buttons

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clickable {
                                    // Handle Clear button click
                                    getMedicineHistoryViewModel.updateTextFieldValue(newValue = "")
                                    getMedicineHistoryViewModel.allMedicineData.value = emptyList()
                                    getMedicineHistoryViewModel.snackBarButton = false
                                    getMedicineHistoryViewModel.clearAuthenticityScoreState()
                                },
                            color = Color(0xFF9C6DF1), // Orange color
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Clear",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        thickness = 1.dp
                    )


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
                                Row (modifier = Modifier.fillMaxWidth()){
                                    Button(
                                        onClick = {
                                            getMedicineHistoryViewModel.submitChangeDialogue.value = true
                                        },
                                        modifier = Modifier.weight(0.5f),
                                        colors = ButtonColors(
                                            containerColor = Color(0xFF3981FF),
                                            contentColor = Color.White,
                                            disabledContentColor = Color.White,
                                            disabledContainerColor = Color(0xFF3981FF),
                                        )
                                    ) {
                                        Text(text = "Show Score")
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Button(onClick = {
                                        getMedicineHistoryViewModel.getGeminiResponse()
                                        getMedicineHistoryViewModel.submitAiDialogue.value = true
                                    },
                                        modifier = Modifier.weight(0.5f)
                                    ) {
                                        Text(text = "Generate AI Response")
                                    }
                                }
                                if (getMedicineHistoryViewModel.submitAiDialogue.value){
                                    ShowDialog(
                                        onClose = {
                                            getMedicineHistoryViewModel.submitAiDialogue.value = false
                                        },
                                        value = gpt3Response.value
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                getMedicineHistoryViewModel.allMedicineData.value.forEach { medicine ->
                                    EachHistoryRecord(medicine = medicine)
                                }
                            }
                        } else {
                            if (getMedicineHistoryViewModel.snackBarButton){
                                LaunchedEffect(Unit) {
                                    snackBarHostState.showSnackbar(
                                        message = "No Medicine By That Id",
                                        actionLabel = "Dismiss",
                                        duration = SnackbarDuration.Short
                                    )
                                    getMedicineHistoryViewModel.snackBarButton = false
                                }
                            }
                        }
                    }

                  
                }
            }
        }

}
@Composable
fun ShowDialog(
    onClose: () -> Unit,
    value: String
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = {
            onClose()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF333333), Color(0xFF3981FF))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .height(400.dp) // Occupy full height
                .width(300.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = value,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onClose() },
                modifier = Modifier
                    .align(Alignment.End) // Align the button to the left
            ) {
                Text(text = "Close")
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
    val animatedPercentage = remember { Animatable(0f) }

    LaunchedEffect(authenticityScore) {
        animatedPercentage.animateTo(
            targetValue = authenticityScore.toFloat() / 100,
            animationSpec = tween(durationMillis = 1500)
        )
    }

    BasicAlertDialog(
        onDismissRequest = onClose,
        modifier = Modifier
            .width(300.dp)
            .height(400.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF333333), Color(0xFF3981FF))
                ),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display authenticity percentage
            Spacer(modifier = Modifier.height(10.dp))

            // Custom circular gauge composable with animated rotating needle and styling
            CircularGauge(
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 8.dp),
                percentage = animatedPercentage.value
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Score : ${((animatedPercentage.value) * 100).toInt()}%",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if ((animatedPercentage.value * 100).toInt() <= 60) {
                Text(
                    text = "Authenticity Failed and Message sent to the Manufacturer",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }
            Button(onClick = {
                getMedicineHistoryViewModel.submitChangeDialogue.value = false
            }) {
                Text(text = "Close")
            }
        }
    }
}


@Composable
fun CircularGauge(modifier: Modifier = Modifier, percentage: Float) {
    Canvas(
        modifier = modifier
    ) {
        val gaugeBackgroundColor = Color(0xFF4B4B4B)
        val gaugeColor = Color(0xFFE60B55)
        val needleColor = Color(0xFFFF6347)
        val strokeWidth = 20f
        val startAngle = 135f
        val sweepAngle = 270f * percentage
        val needleLength = 170f

        // Draw background gauge
        drawArc(
            color = gaugeBackgroundColor,
            startAngle = startAngle,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(strokeWidth)
        )

        // Draw actual gauge
        drawArc(
            color = gaugeColor,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(strokeWidth)
        )

        // Draw animated needle with glowing effect
        val needleAngle = startAngle + sweepAngle
        val needleEndX = center.x + needleLength * cos(needleAngle.toRadians())
        val needleEndY = center.y + needleLength * sin(needleAngle.toRadians())

        drawLine(
            color = needleColor,
            start = center,
            end = Offset(needleEndX.toFloat(), needleEndY.toFloat()),
            strokeWidth = strokeWidth / 2,
            cap = StrokeCap.Round
        )

        drawCircle(
            color = needleColor.copy(alpha = 0.5f),
            center = center,
            radius = strokeWidth * 1.5f
        )
    }
}

private fun Float.toRadians(): Float = this * Math.PI.toFloat() / 180f




@Composable
fun EachHistoryRecord(medicine: Medicine) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { isExpanded = !isExpanded }
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF64B5F6),
                        Color(0xFF2196F3)
                    )

                ),
                shape = RoundedCornerShape(8.dp),
            ),


    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Section 1: Basic Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Medicine Name",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = medicine.Name,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
                Icon(
                    imageVector = Icons.Default.MedicalInformation,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            // Section 2: Additional Details
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    HorizontalDivider(thickness = 1.dp, color = Color.Black)
                    LabeledText(label = "Journey Status", value = medicine.JourneyCompleted)
                    LabeledText(label = "Batch Number", value = medicine.Batch_No)
                    LabeledText(label = "Brand Name", value = medicine.BrandName)
                    LabeledText(label = "Drap Number", value = medicine.DrapNo)
                    LabeledText(label = "Timestamp", value = medicine.TimeStamp)
                    LabeledText(label = "Dosage Form", value = medicine.DosageForm)
                    LabeledText(label = "Composition", value = medicine.Composition)
                    LabeledText(label = "Manufacture Date", value = medicine.ManufactureDate)
                    LabeledText(label = "Expiry Date", value = medicine.ExpiryDate)
                    HorizontalDivider(thickness = 1.dp, color = Color.Black)
                }
            }

            // Section 3: Transaction IDs
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Transaction IDs",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            LabeledText(label = "Sender ID", value = medicine.SenderId)
            LabeledText(label = "Receiver ID", value = medicine.ReceiverId)
            LabeledText(label = "Location", value = medicine.Location)
        }
    }
}

@Composable
fun LabeledText(label: String, value: String) {
    Row {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            color = Color.White
        )
    }
}



@androidx.annotation.OptIn(ExperimentalGetImage::class) @OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionComposable(): String {

    val v = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val launchCamera = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, proceed with camera actions
            true
        } else {
            // Handle permission denial
            false
        }
    }


Column {
    Button(onClick = {
        if (cameraPermissionState.hasPermission) {
            launchCamera.value = !launchCamera.value
        } else {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }, modifier = Modifier.fillMaxWidth()
    ) {
        Text("Open Camera")
    }
    Spacer(modifier = Modifier.height(30.dp))
    if (launchCamera.value) {

        v.value = PreviewViewComposable()
        Log.e(TAG, "yes: $v", )
        return@Column
    }
}
    return v.value
}




@ExperimentalGetImage
@Composable
fun PreviewViewComposable(): String {
    val qrCodeData = remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                { context ->
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

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, BarcodeAnalyser { qrcodeData ->
                                    Toast.makeText(
                                        context,
                                        "Barcode found $qrcodeData",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    cameraExecutor.shutdown()
                                    cameraProvider.unbindAll()
                                //    getMedicineHistoryViewModel.qrCodeData = qrcodeData.toString()
                                  //  getMedicineHistoryViewModel.startQrCodeScanner = false
                                    qrCodeData.value = qrcodeData.toString()
                                    return@BarcodeAnalyser

                                }
                                )
                            }
                        

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            // Unbind use cases before rebinding
                            cameraProvider.unbindAll()

                            // Bind use cases to camera
                            cameraProvider.bindToLifecycle(
                                context as ComponentActivity,
                                cameraSelector,
                                preview,
                                imageCapture,
                                imageAnalyzer
                            )

                        } catch (exc: Exception) {
                            Log.e("DEBUG", "Use case binding failed", exc)
                        }
                    }, ContextCompat.getMainExecutor(context))
                    previewView
                },

                )
            Spacer(modifier = Modifier.height(30.dp))
       // else{
          //  if (getMedicineHistoryViewModel.processingQrCodedata){
               // CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
               // if (getMedicineHistoryViewModel.qrCodeData.isNotEmpty()){
                  //  val jsonQrCodeData = Gson().fromJson(getMedicineHistoryViewModel.qrCodeData, Medicine::class.java)
                //    getMedicineHistoryViewModel.updateTextFieldValue(jsonQrCodeData.ID)
              //      getMedicineHistoryViewModel.processingQrCodedata = !getMedicineHistoryViewModel.processingQrCodedata
            //        Log.e(TAG, "PreviewViewComposable: yes", )
          //      }
        //    }
      //  }
    }
    return qrCodeData.value
}

class BarcodeAnalyser(
    val callback: (Any?) -> Unit
) : ImageAnalysis.Analyzer {
    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val qrCodeData = barcodes[0].displayValue // Assuming there's only one QR code
                        callback(qrCodeData)
                    }
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
        }
        imageProxy.close()
    }
}