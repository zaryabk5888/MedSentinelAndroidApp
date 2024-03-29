package com.CUST.MedVerify.blockchainapp.presentation.navigationcomponent

import AllUsers
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.CUST.MedVerify.blockchainapp.presentation.appinterface.PrimaryScreen
import com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.getallmedicine.GetAllMedicineScreen
import com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.getmedicine.GetMedicineScreen
import com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.historymedicine.GetMedicineHistoryScreen
import com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.postmedicine.PostMedicineScreen
import com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.updatemedicine.UpdateMedicineScreen
import com.CUST.MedVerify.blockchainapp.presentation.login.CheckLoginStatus
import com.CUST.MedVerify.blockchainapp.presentation.signup.AccountSelectionScreen
import com.CUST.MedVerify.blockchainapp.presentation.signup.reset.ResetCred
import com.CUST.MedVerify.blockchainapp.presentation.signup.type.Customer
import com.CUST.MedVerify.blockchainapp.presentation.signup.type.Distributor
import com.CUST.MedVerify.blockchainapp.presentation.signup.type.Manufacturer
import com.CUST.MedVerify.blockchainapp.presentation.signup.type.Retailer

import com.CUST.MedVerify.blockchainapp.presentation.users.ChainUsers
import com.CUST.MedVerify.blockchainapp.presentation.users.FailedAuth
import com.CUST.MedVerify.blockchainapp.presentation.users.MedicineAdded
import com.CUST.MedVerify.blockchainapp.presentation.users.TransactionData


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route,) {
        composable(
            route = Screen.LoginScreen.route
        ) {
            CheckLoginStatus(navController = navController)
        }
        composable(Screen.SignUpScreen.route) {
            AccountSelectionScreen(navController = navController)
        }
        composable(Screen.CustomerSignUpScreen.route) {
            Customer(navController = navController)
        }
        composable(Screen.ManufacturerSignUpScreen.route) {
            Manufacturer(navController = navController)
        }
        composable(Screen.DistributorSignUpScreen.route) {
            Distributor(navController = navController)
        }
        composable(Screen.RetailerSignUpScreen.route) {
            Retailer(navController = navController)
        }
        composable(Screen.PrimaryScreen.route) {
            PrimaryScreen(navController = navController)
        }
        composable(Screen.GetAllScreen.route) {
            GetAllMedicineScreen(navController = navController)
        }
        composable(Screen.GetScreen.route) {
            GetMedicineScreen(navController = navController)
        }
        composable(Screen.PostScreen.route) {
            PostMedicineScreen(navController = navController)
        }
        composable(Screen.UpdateScreen.route) {
            UpdateMedicineScreen(navController = navController)
        }
        composable(Screen.HistoryScreen.route) {
            GetMedicineHistoryScreen(navController = navController)
        }
        composable(Screen.ResetCredScreen.route) {
            ResetCred(navController = navController)
        }
        composable(Screen.AllUsersScreen.route) {
            AllUsers(navController = navController)
        }
        composable(Screen.ChainUsersScreen.route) {
            ChainUsers(navController = navController)
        }
        composable(Screen.Transaction.route) {
            TransactionData(navController = navController)
        }
        composable(Screen.FailedAuth.route) {
            FailedAuth(navController = navController)
        }
        composable(Screen.AddedMedicine.route) {
            MedicineAdded(navController = navController)
        }
    }
}
