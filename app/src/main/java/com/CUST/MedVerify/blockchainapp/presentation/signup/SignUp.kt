package com.CUST.MedVerify.blockchainapp.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.CUST.MedVerify.blockchainapp.R
import com.CUST.MedVerify.blockchainapp.presentation.navigationcomponent.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelectionScreen(navController: NavHostController) {
 Scaffold(
  topBar = {
   TopAppBar(
    title = { Text(text = "") },
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
    },
    colors = TopAppBarColors(
     containerColor = Color(0xFF0B1446),
     scrolledContainerColor = Color.Transparent,
     navigationIconContentColor = Color.White,
     titleContentColor = Color.White,
     actionIconContentColor = Color.White
    )
   )
  }
 ) { paddingValues ->
  Box(
   modifier = Modifier
    .padding(paddingValues)
    .fillMaxSize()
    .background(
     Brush.verticalGradient(
      colors = listOf(
       Color(0xFF0B1446),
       Color(0xFF040F4B),
       Color(0xFF23D5F0)
      )
     )
    ),
  ) {
   Column {

    Image(
     painter = painterResource(id = R.drawable.logo),
     contentDescription = "Logo",
     modifier = Modifier
      .clip(RoundedCornerShape(50.dp))
      .align(Alignment.CenterHorizontally),
    )
    Spacer(modifier =Modifier.height(15.dp))
    Text(
     text = "User Role Selection",
     fontSize = 24.sp,
     color = Color.White,
     modifier = Modifier.align(Alignment.CenterHorizontally),
     fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(30.dp))

    val cardDataList = listOf(
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
     CardDataForSignUp(
      "Customer",
     ) {
      navController.navigate(Screen.CustomerSignUpScreen.route)
     }
    )
    GridOfCardsForSignUp(cardDataList)
   }
  }
 }
}

data class CardDataForSignUp(val label: String,val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridOfCardsForSignUp(cardDataList: List<CardDataForSignUp>) {
 LazyVerticalGrid(
  columns = GridCells.Fixed(1), // Display 2 cards per row
  modifier = Modifier
   .padding(16.dp).fillMaxSize()
 ) {
  items(cardDataList.size) { index ->
   val card = cardDataList[index]
   ElevatedCard(
    modifier = Modifier
     .padding(8.dp)
     .height(80.dp)
     .padding(start = 25.dp, end = 25.dp, )
     .clip(RoundedCornerShape(20.dp)),
    colors = CardColors(
     containerColor = Color(0xFF23D5F0),
     contentColor = Color.White,
     disabledContainerColor = Color.Gray,
     disabledContentColor = Color.Black
    ),
    onClick = card.onClick,
   ) {
    Column(
     modifier = Modifier.fillMaxSize(),
     verticalArrangement = Arrangement.Center,
     horizontalAlignment = Alignment.CenterHorizontally
    ) {
     Text(
      text = card.label,
      style = TextStyle(fontSize = 28.sp),
      color = Color.White,
     )

    }
   }
  }
 }
}
