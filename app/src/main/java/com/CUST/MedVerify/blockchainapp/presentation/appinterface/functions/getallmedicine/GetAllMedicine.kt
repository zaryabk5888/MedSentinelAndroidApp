package com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.getallmedicine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.CUST.MedVerify.blockchainapp.data.dto.Medicine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetAllMedicineScreen(
    navController: NavHostController,
    getAllMedicineViewModel: GetAllMedicineViewModel = viewModel()
) {
    val snackBarHostState = SnackbarHostState()
    val loading by getAllMedicineViewModel.loading.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "All Medicines Data") },
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
            },
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
            }
        ) {
            Box(
                modifier = Modifier.padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    ElevatedButton(
                        onClick = {
                            getAllMedicineViewModel.events(
                                getAllScreenEvents = GetAllScreenEvents.GetAll
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Get All Medicines",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(
                                alignment = Alignment.CenterHorizontally
                            )
                        )
                    } else if (getAllMedicineViewModel.allMedicineData.value.isNotEmpty()) {
                        getAllMedicineViewModel.allMedicineData.value =
                            getAllMedicineViewModel.allMedicineData.value.sortedByDescending {
                                it.ID
                            }
                        getAllMedicineViewModel.allMedicineData.value.forEach { medicine ->
                            EachRecord(medicine)
                        }
                    } else {
                        LaunchedEffect(Unit) {
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
@Composable
fun EachRecord(medicine: Medicine) {
    val isExpanded = remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { isExpanded.value = !isExpanded.value },
        colors = CardDefaults.outlinedCardColors(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Medicine ID",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
            )

            Text(
                text = medicine.ID,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Name: ${medicine.Name}",
                color = MaterialTheme.colorScheme.tertiary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Brand Name: ${medicine.BrandName}",
                color = MaterialTheme.colorScheme.tertiary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Drap No: ${medicine.DrapNo}",
                color = MaterialTheme.colorScheme.tertiary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Manufacture Date: ${medicine.ManufactureDate}",
                color = MaterialTheme.colorScheme.tertiary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Expiry Date: ${medicine.ExpiryDate}",
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}
