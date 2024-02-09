package com.medchain.medverify.blockchainapp.presentation.users

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.medchain.medverify.blockchainapp.presentation.appinterface.functions.getmedicine.GetMedicineViewModel
import com.medchain.medverify.blockchainapp.presentation.users.viewmodels.MedicineAddedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MedicineAdded(
    navController: NavHostController,
    getMedicineAddedViewModel: MedicineAddedViewModel = viewModel()
                  ) {
    val loading by getMedicineAddedViewModel.loadingOne.collectAsState()

    var messagesList by remember {
        mutableStateOf<QuerySnapshot?>(null)
    }
    val db = FirebaseFirestore.getInstance()
    val messagesCollection = db
        .collection("Manufacturer")
        .document(FirebaseAuth.getInstance().currentUser?.email.toString())
        .collection("Medicine-Added")

    messagesCollection.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            Log.w("FireStore", "Error retrieving medicines", exception)
            return@addSnapshotListener
        }
        messagesList = snapshot
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "All Medicines Added") },
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
                            MedicineAddedMessageItem(messagesList!!.documents[message].get("medicineID").toString(),loading)
                        }
                    }
                }
            }
        }
    )
}
@Composable
fun MedicineAddedMessageItem(message: String, loading: Boolean) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .weight(0.25f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 4.dp)
            ) {
                Text(
                    text = "ID: ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun GenerateQRCode(id: String,getMedicineViewModel: GetMedicineViewModel = viewModel()){

}



