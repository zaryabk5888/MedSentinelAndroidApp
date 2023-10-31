package com.example.myapplication.blockchainapp.presentation.appinterface.functions.updatemedicine

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMedicineScreen(
    navController: NavHostController,
    updateMedicineViewModel: UpdateMedicineViewModel = viewModel(),

) {
    val textFieldValue by updateMedicineViewModel.id.collectAsState()

    val loading by updateMedicineViewModel.loading.collectAsState()
    val oneMedicineData by updateMedicineViewModel.oneMedicineData.collectAsState()
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Update Medicine Data") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )
        },
    ) { paddingValue ->
        Box(
            modifier = Modifier.padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(state = scrollState)


            ) {

                OutlinedTextField(
                    label = { Text(text = "ID") },
                    value = textFieldValue,
                    onValueChange = { updateMedicineViewModel.updateTextFieldValue(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    ElevatedButton(
                        onClick = {
                            updateMedicineViewModel.events(
                                updateScreenEvents = UpdateScreenEvents.Get
                            )
                            keyboardController?.hide()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Search")
                    }

                    ElevatedButton(
                        onClick = {
                            updateMedicineViewModel.updateReceiverTextFieldValue(newValue = "")
                            updateMedicineViewModel.updateSenderTextFieldValue(newValue = "")
                            updateMedicineViewModel.updateTextFieldValue(newValue = "")
                            updateMedicineViewModel.updateMedicineState(value = null)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Clear")
                    }
                }

                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .align(CenterHorizontally)
                    )
                } else  {
                    if (oneMedicineData != null) {
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
                            title = "Description",
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


                        if (updateMedicineViewModel.isKeyBoardActive.value) {
                            LaunchedEffect(Unit) {
                                delay(100)
                                scrollState.animateScrollTo(10000)
                            }
                        }
                        ElevatedButton(
                            onClick = {
                               updateMedicineViewModel.submitChangeDialogue.value = !updateMedicineViewModel.submitChangeDialogue.value
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(text = "Change OwnerShip")
                        }
                        //Dialog to show Success or Failure
                        if (updateMedicineViewModel.submitChangeDialogue.value) {
                            LaunchedEffect(Unit) {
                                delay(12000) // Wait for 2 seconds
                            }
                           ShowStatusDialog(
                                onClose = {
                                    updateMedicineViewModel.submitChangeDialogue.value = false
                                },
                                viewModel = updateMedicineViewModel,
                               keyboardController
                            )
                        }
                        //Dialog to show Success or Failure
                        if (updateMedicineViewModel.changeConfirmDialogue.value) {
                            LaunchedEffect(Unit) {
                                delay(12000) // Wait for 2 seconds
                            }
                           AlertDialog(onDismissRequest = {
                               updateMedicineViewModel.changeConfirmDialogue.value = false
                           }) {
                               Icon(
                                   imageVector =
                                       if (updateMedicineViewModel.success.value) {Icons.Filled.CheckCircle }else{ Icons.Filled.Error},
                                       contentDescription = "Success",
                               tint = if (updateMedicineViewModel.success.value) Color.Green else Color.Red,
                               modifier = Modifier.size(48.dp)
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
            .padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(text = value)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowStatusDialog(

    onClose: () -> Unit,
    viewModel: UpdateMedicineViewModel,
    keyboardController: SoftwareKeyboardController?,
) {
    val textSenderFieldValue by viewModel.sender.collectAsState()
    val textReceiverFieldValue by viewModel.receiver.collectAsState()

    AlertDialog(
            onDismissRequest = onClose,
            modifier = Modifier
                .size(300.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                OutlinedTextField(
                    label = { Text(text = "Sender") },
                    value = textSenderFieldValue,
                    onValueChange = { viewModel.updateSenderTextFieldValue(it) },

                    )
                val context = LocalContext.current
                OutlinedTextField(
                    label = { Text(text = "Receiver") },
                    value = textReceiverFieldValue,
                    onValueChange = {
                        viewModel.updateReceiverTextFieldValue(
                            it
                        )
                    },
                    modifier = Modifier
                        .onFocusChanged {
                            viewModel.isKeyBoardActive.value = it.hasFocus == true
                        }
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    ElevatedButton(onClick = {
                        viewModel.submitChangeDialogue.value = false
                        keyboardController?.hide()
                        viewModel.isKeyBoardActive.value = false
                    }) {
                        Text(text = "Close")
                    }

                    ElevatedButton(
                        onClick = {
                        viewModel.events(
                            updateScreenEvents = UpdateScreenEvents.Post
                        )
                            viewModel.submitChangeDialogue.value = false
                        keyboardController?.hide()
                    }) {
                        Text(text = "Submit")
                    }

                }
            }
        }
    }

