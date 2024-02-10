package com.CUST.MedVerify.blockchainapp.presentation.users

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TransactionData(navController: NavHostController) {
    var messagesList by remember {
        mutableStateOf<QuerySnapshot?>(null)
    }
    val db = FirebaseFirestore.getInstance()
    val messagesCollection = db.collection(
        FirebaseAuth
        .getInstance()
        .currentUser?.displayName.toString().replaceFirstChar {
        it.uppercase()
    }
    ).document(FirebaseAuth.getInstance().currentUser?.email.toString()).collection("Messages")

    messagesCollection.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            Log.w("Firestore", "Error retrieving messages", exception)
            return@addSnapshotListener
        }
        messagesList = snapshot
    }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "${FirebaseAuth.getInstance().currentUser?.displayName}") },
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
                    },
                )
            }, content = {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)){
                    if (messagesList != null){
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(messagesList!!.size()) { message ->
                                MessageItem(messagesList!!.documents[message].get("message").toString())
                            }
                        }
                    }
                }
            }
        )
}
@Composable
fun MessageItem(message: String) {
    Card(
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
