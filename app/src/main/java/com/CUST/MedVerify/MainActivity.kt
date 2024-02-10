package com.CUST.MedVerify

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.CUST.MedVerify.blockchainapp.presentation.navigationcomponent.Navigation
import com.CUST.MedVerify.ui.theme.MedVerifyTheme
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseApp.initializeApp(this)
                MedVerifyTheme {
                   //MedicineQRCode()
                       Navigation()
                   // GpsPermissionAndLocation()
                }
            }
    }
}



















