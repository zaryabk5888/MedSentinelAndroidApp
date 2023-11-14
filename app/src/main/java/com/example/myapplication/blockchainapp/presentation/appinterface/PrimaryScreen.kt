package com.example.myapplication.blockchainapp.presentation.appinterface


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.blockchainapp.presentation.navigationcomponent.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutines = rememberCoroutineScope()
    Surface(

    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                    Text(
                        text = "${FirebaseAuth.getInstance().currentUser?.displayName}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black

                    )
                            },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                                navController.navigate(Screen.LoginScreen.route)
                                FirebaseAuth.getInstance().signOut()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Log out"
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
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                    },
                )
            },
            content = {
                ModalNavigationDrawer(drawerContent = {
                                                      Column(
                                                          modifier = Modifier
                                                              .padding(top = 60.dp, start = 5.dp)
                                                              .fillMaxHeight()
                                                              .width(200.dp)
                                                              .background(
                                                                  Color.White
                                                              )) {
                                                         AllUsersScreen(navController)
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
                        val ownerRole by remember {
                            mutableStateOf(FirebaseAuth.getInstance().currentUser?.displayName.toString())
                        }

                        if (loading){
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                CircularProgressIndicator()
                            }
                            loading = false
                        }else{
                            Log.e(TAG, "PrimaryScreen: $ownerRole")
                            when (ownerRole) {
                                "manufacturer" -> {
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
                                }
                                "customer" -> {
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
                                "distributor" -> {
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
                                }
                                "retailer" -> {
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
            }
        )
    }
}

@Composable
fun AllUsersScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()
    ) {
        if (FirebaseAuth.getInstance().currentUser?.displayName !="customer"){
            TextButton(
                onClick = { navController.navigate(Screen.AllUsersScreen.route) },
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "Add User",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { navController.navigate(Screen.ChainUsersScreen.route) },
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "Chain User",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { navController.navigate(Screen.Transaction.route) },
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "Transaction Record",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (FirebaseAuth.getInstance().currentUser?.displayName == "manufacturer"){
                TextButton(
                    onClick = { navController.navigate(Screen.FailedAuth.route) },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = "Failed Authentication Record",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black
                    )
                }
            }

        }


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


