package com.example.myapplication.blockchainapp.presentation.navigationcomponent

sealed class Screen(val route : String){
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
    object CustomerSignUpScreen : Screen("customer_signup_screen")
    object ManufacturerSignUpScreen : Screen("manufacturer_signup_screen")
    object DistributorSignUpScreen : Screen("distributor_signup_screen")
    object RetailerSignUpScreen : Screen("retailer_signup_screen")
    object PrimaryScreen : Screen("main_screen")
    object GetAllScreen : Screen("GetAll")
    object GetScreen : Screen("Get")
    object PostScreen : Screen("Post")
    object UpdateScreen : Screen("Update")
    object HistoryScreen : Screen("History")
    object ResetCredScreen : Screen("ResetCred")

    object AllUsersScreen : Screen("AllUsers")

    object ChainUsersScreen : Screen("ChainUsers")

    object Transaction : Screen("Transaction")


}
