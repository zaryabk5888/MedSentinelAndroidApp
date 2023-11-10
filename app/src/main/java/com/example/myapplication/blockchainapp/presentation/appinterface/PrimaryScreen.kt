package com.example.myapplication.blockchainapp.presentation.appinterface


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.blockchainapp.presentation.navigationcomponent.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutines = rememberCoroutineScope()
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "${FirebaseAuth.getInstance().currentUser?.displayName}") },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.enableOnBackPressed(true)
                                navController.popBackStack()
                                navController.navigate(Screen.LoginScreen.route)
                                FirebaseAuth.getInstance().signOut()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Go Back"
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (drawerState.isClosed) {
                                    coroutines.launch {
                                        drawerState.open()
                                    }
                                }else{
                                    coroutines.launch {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.MenuOpen,
                                contentDescription = "Menu"
                            )
                        }
                    },
                )
            },
            content = {
                ModalNavigationDrawer(drawerContent = {
                                                      Column(
                                                          modifier = Modifier
                                                              .width(200.dp)
                                                              .fillMaxHeight()
                                                              .background(
                                                                  Color.White
                                                              )) {

                                                      }
                                                      }, drawerState = drawerState) {
                    Box(
                        modifier = Modifier.padding(
                            it
                        )
                    ) {
                        var loading by remember {
                            mutableStateOf(true)
                        }
                        val owner by remember {
                            mutableStateOf(FirebaseAuth.getInstance().currentUser?.email)
                        }
                        Log.e("owner:",owner.toString())
                        var ownerRole by remember {
                            mutableStateOf("")
                        }
                        if (loading){
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                CircularProgressIndicator()
                            }

                            val firebaseFireStore = FirebaseFirestore.getInstance()
                            ownerRole = FirebaseAuth.getInstance().currentUser?.displayName.toString()

                            loading = false
                        }else{
                            Log.e(TAG, "PrimaryScreen: $ownerRole", )
                            if (ownerRole == "manufacturer"){
                                val cardDataList = listOf(
                                    CardData(
                                        "Get All Medicine",
                                        Icons.AutoMirrored.Filled.List
                                    ) {
                                        navController.navigate(Screen.GetAllScreen.route)
                                    },
                                    CardData(
                                        "Search Medicine",
                                        Icons.Filled.Search
                                    ) {
                                        navController.navigate(Screen.GetScreen.route)
                                    },
                                    CardData(
                                        "Add Medicine",
                                        Icons.Filled.Add
                                    ) {
                                        navController.navigate(Screen.PostScreen.route)
                                    },
                                    CardData(
                                        "Update Medicine",
                                        Icons.Filled.Edit
                                    ) {
                                        navController.navigate(Screen.UpdateScreen.route)
                                    },
                                    CardData(
                                        "Show Medicine History",
                                        Icons.Filled.History
                                    ) {
                                        navController.navigate(Screen.HistoryScreen.route)
                                    }
                                )
                                GridOfCards(cardDataList)
                            }else if (
                                ownerRole == "customer"
                            ){
                                val customerDataList = listOf(
                                    CardData(
                                        "Search Medicine",
                                        Icons.Filled.Search
                                    ) {
                                        navController.navigate(Screen.GetScreen.route)
                                    },
                                    CardData(
                                        "Show Medicine History",
                                        Icons.Filled.History
                                    ) {
                                        navController.navigate(Screen.HistoryScreen.route)
                                    }
                                )
                                GridOfCards(cardDataList = customerDataList)
                            }
                            else if (ownerRole == "distributor") {
                                val cardDataList = listOf(
                                    CardData(
                                        "Get All Medicine",
                                        Icons.AutoMirrored.Filled.List
                                    ) {
                                        navController.navigate(Screen.GetAllScreen.route)
                                    },
                                    CardData(
                                        "Search Medicine",
                                        Icons.Filled.Search
                                    ) {
                                        navController.navigate(Screen.GetScreen.route)
                                    },
                                    CardData(
                                        "Update Medicine",
                                        Icons.Filled.Edit
                                    ) {
                                        navController.navigate(Screen.UpdateScreen.route)
                                    },
                                    CardData(
                                        "Show Medicine History",
                                        Icons.Filled.History
                                    ) {
                                        navController.navigate(Screen.HistoryScreen.route)
                                    }
                                )
                                GridOfCards(cardDataList)
                            }  else if (ownerRole == "retailer") {
                                val cardDataList = listOf(
                                    CardData(
                                        "Search Medicine",
                                        Icons.Filled.Search
                                    ) {
                                        navController.navigate(Screen.GetScreen.route)
                                    },
                                    CardData(
                                        "Update Medicine",
                                        Icons.Filled.Edit
                                    ) {
                                        navController.navigate(Screen.UpdateScreen.route)
                                    },
                                    CardData(
                                        "Show Medicine History",
                                        Icons.Filled.History
                                    ) {
                                        navController.navigate(Screen.HistoryScreen.route)
                                    }
                                )
                                GridOfCards(cardDataList)
                            }
                        }
                    }
                }
            }
        )
    }
}

data class CardData(val label: String, val icon: ImageVector, val onClick: () -> Unit)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridOfCards(cardDataList: List<CardData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Display 2 cards per row
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        items(cardDataList.size) {index ->
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
                    Icon(
                        imageVector = card.icon,
                        contentDescription = "",
                        modifier = Modifier.size(50.dp)
                    )

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


