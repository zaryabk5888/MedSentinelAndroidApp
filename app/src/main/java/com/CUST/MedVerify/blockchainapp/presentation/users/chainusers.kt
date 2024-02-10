package com.CUST.MedVerify.blockchainapp.presentation.users

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChainUsers(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Chain Users") },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
            )
        },

        ){paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){

            ChainUsersList()

        }
    }
}

@Composable
fun ChainUsersList() {
    val userEmail = FirebaseAuth.getInstance().currentUser?.email

    var refresh by remember {
        mutableStateOf(true)
    }

    var loading by remember {
        mutableStateOf(true)
    }

    val userRole = FirebaseAuth.getInstance().currentUser?.displayName

    var addedChainUsers by remember {
        mutableStateOf<QuerySnapshot?>(null)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)) {


            TextButton(
                onClick = {
                    refresh = true

                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Refresh")
            }

        }

        if(refresh){
            if (addedChainUsers == null){
                if (loading){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                    when (userRole) {
                        "manufacturer" -> {
                            try {
                                if (userEmail != null) {
                                    FirebaseFirestore.getInstance()
                                        .collection("Manufacturer")
                                        .document(userEmail)
                                        .collection("ChainUsers")
                                        .get().addOnSuccessListener { result ->
                                            loading = false
                                            addedChainUsers= result
                                            Log.e(ContentValues.TAG, "listToShow: $addedChainUsers")
                                        }
                                }
                            } catch (e: Exception) {
                                Log.e(ContentValues.TAG, "Error fetching data for chainusers: $e")
                                loading = false
                            }
                        }
                        "distributor" -> {
                            try {
                                if (userEmail != null) {
                                    FirebaseFirestore.getInstance()
                                        .collection("Distributor")
                                        .document(userEmail)
                                        .collection("ChainUsers")
                                        .get().addOnSuccessListener { result ->
                                            loading = false
                                            addedChainUsers= result
                                            Log.e(ContentValues.TAG, "listToShow: $addedChainUsers")
                                        }
                                }
                            } catch (e: Exception) {
                                Log.e(ContentValues.TAG, "Error fetching data for chainusers: $e")
                                loading = false
                            }
                        }
                        else -> {
                            try {
                                if (userEmail != null) {
                                    FirebaseFirestore.getInstance()
                                        .collection("Retailer")
                                        .document(userEmail)
                                        .collection("ChainUsers")
                                        .get().addOnSuccessListener { result ->
                                            loading = false
                                            addedChainUsers= result
                                            Log.e(ContentValues.TAG, "listToShow: $addedChainUsers")
                                        }
                                }
                            } catch (e: Exception) {
                                Log.e(ContentValues.TAG, "Error fetching data for chainusers: $e")
                                loading = false
                            }
                        }
                    }

                }
            }else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (addedChainUsers!= null) {
                        items(addedChainUsers!!.size()) { index ->
                            val record = addedChainUsers!!.documents[index]
                            if (record.get("email")!="${FirebaseAuth.getInstance().currentUser?.email}") {
                                ListDesignForChainUsers(record = record)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ListDesignForChainUsers(record: DocumentSnapshot) {
    LocalContext.current
    val statusChainUser by remember {
        mutableStateOf(false)
    }
    CustomCard {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${record.get("email")}",
                modifier = Modifier.weight(1f)
            )

            // Add your IconButton for removing user here
            IconButton(
                onClick = {
                    // Handle friend request action
                 //   removeUser(record,context)
                }
            ) {
                // Add your icon for sending friend request here
                Icon(
                    imageVector = if (statusChainUser){
                        Icons.Default.Check
                    }else{
                        Icons.Default.Remove
                    },
                    contentDescription = "Send Friend Request"
                )
            }

        }
    }
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 8.dp, end = 8.dp)
            .clip(RoundedCornerShape(4.dp)),
        elevation = CardDefaults.cardElevation(),
    ) {
        content(Modifier.fillMaxSize())
    }
}
