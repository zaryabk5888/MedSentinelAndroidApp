package com.CUST.MedVerify.blockchainapp.presentation.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.CUST.MedVerify.blockchainapp.presentation.navigationcomponent.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelectionScreen(navController: NavHostController) {
 Scaffold(
  topBar = {
   TopAppBar(
    title = { Text(text = "Select Account Type") },
    navigationIcon = {
     IconButton(onClick = {
      navController.popBackStack()
      navController.navigate(Screen.LoginScreen.route)
     }) {
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

   val cardDataList = listOf(
    CardDataForSignUp(
     "Customer",
    ) {
     navController.navigate(Screen.CustomerSignUpScreen.route)
    },
    CardDataForSignUp(
     "Manufacturer",

    ) {
     navController.navigate(Screen.ManufacturerSignUpScreen.route)
    },
    CardDataForSignUp(
     "Distributor",

    ) {
     navController.navigate(Screen.DistributorSignUpScreen.route)
    },
    CardDataForSignUp(
     "Retailer",

    ) {
     navController.navigate(Screen.RetailerSignUpScreen.route)
    },
   )
   GridOfCardsForSignUp(cardDataList)
  }
 }
}

data class CardDataForSignUp(val label: String,val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridOfCardsForSignUp(cardDataList: List<CardDataForSignUp>) {
 LazyVerticalGrid(
  columns = GridCells.Fixed(2), // Display 2 cards per row
  modifier = Modifier
   .fillMaxWidth()
   .padding(16.dp)
 ) {
  items(cardDataList.size) { index ->
   val card = cardDataList[index]
   ElevatedCard(
    modifier = Modifier
     .padding(8.dp)
     .fillMaxWidth()
     .height(250.dp),
    onClick = card.onClick,
   ) {
    Column(
     modifier = Modifier.fillMaxSize(),
     verticalArrangement = Arrangement.Center,
     horizontalAlignment = Alignment.CenterHorizontally
    ) {
     Text(
      text = card.label,
      style = TextStyle(fontSize = 18.sp),
      color = MaterialTheme.colorScheme.tertiary,
     )

    }
   }
  }
 }
}
