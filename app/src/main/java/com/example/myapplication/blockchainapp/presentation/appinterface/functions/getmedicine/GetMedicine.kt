package com.example.myapplication.blockchainapp.presentation.appinterface.functions.getmedicine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetMedicineScreen(
    navController: NavHostController,
    getMedicineViewModel: GetMedicineViewModel = viewModel()
) {
    val textFieldValue by getMedicineViewModel.id.collectAsState()
    val oneMedicineData by getMedicineViewModel.oneMedicineData.collectAsState()
    val loading by getMedicineViewModel.loading.collectAsState()
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
                        modifier = Modifier.align(Alignment.CenterHorizontally),

                        )

                    Row(
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    ) {
                        OutlinedIconButton(
                            onClick = {
                                getMedicineViewModel.events(
                                    getScreenEvents = GetScreenEvents.GetId
                                )
                                keyboardController?.hide()
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                        }

                        OutlinedIconButton(
                            onClick = {
                                getMedicineViewModel.updateTextFieldValue(newValue = "")
                                getMedicineViewModel.updateMedicineState(value = null)
                            },
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                        ) {
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
                            OutlinedCard(
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 4.dp
                                ),
                                modifier = Modifier.padding(16.dp)
                            ) {

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
                                MedicationInfoCard(
                                    title = "Sender ID",
                                    value = oneMedicineData!!.SenderId
                                )
                                MedicationInfoCard(
                                    title = "Receiver ID",
                                    value = oneMedicineData!!.ReceiverId
                                )

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(text = value)
        }
    }
}
