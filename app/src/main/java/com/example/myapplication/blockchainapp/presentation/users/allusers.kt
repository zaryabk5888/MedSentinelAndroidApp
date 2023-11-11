import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

// ...


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllUsers(navController: NavHostController) {

    val fireStore by remember {
        mutableStateOf(FirebaseFirestore.getInstance())
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "All Users") },
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

                ListToShow(fireStore)

        }
    }
}

@Composable
fun ListToShow(fireStore: FirebaseFirestore) {
    val userEmail = FirebaseAuth.getInstance().currentUser?.email
    var showManufacturer by remember {
        mutableStateOf(true)
    }
    var showDistributor by remember {
        mutableStateOf(false)
    }
    var showRetailer by remember {
        mutableStateOf(false)
    }

    var loadingForManufacturer by remember {
        mutableStateOf(true)
    }
    var loadingForDistributor by remember {
        mutableStateOf(true)
    }
    var loadingForRetailer by remember {
        mutableStateOf(true)
    }

    var listOfManufacturer by remember {
        mutableStateOf<QuerySnapshot?>(null)
    }
    var listOfDistributor by remember {
        mutableStateOf<QuerySnapshot?>(null)
    }
    var listOfRetailer by remember {
        mutableStateOf<QuerySnapshot?>(null)
    }

    var addedChainUsersList by remember {
        mutableStateOf<QuerySnapshot?>(null)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)) {


            TextButton(
                onClick = {
                    showManufacturer = true
                    showDistributor = false
                    showRetailer = false
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Manufacturer")
            }
            TextButton(
                onClick = {
                    showDistributor = true
                    showManufacturer = false
                    showRetailer = false
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Distributor")
            }
            TextButton(
                onClick = {
                    showDistributor = false
                    showRetailer = true
                    showManufacturer = false
                          },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Retailer")

            }
        }
     

        if(showManufacturer){
            if (listOfManufacturer == null){
                if (loadingForManufacturer){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                        ){
                        CircularProgressIndicator()
                    }
                    try {
                        if (userEmail != null) {
                            FirebaseFirestore.getInstance()
                                .collection("Manufacturer")
                                .document(userEmail)
                                .collection("ChainUsers")
                                .get().addOnSuccessListener { result ->
                                    loadingForManufacturer = false
                                    addedChainUsersList= result
                                    Log.e(ContentValues.TAG, "listToShow: $addedChainUsersList")
                                }.addOnCompleteListener {
                                    fireStore.collection("Manufacturer")
                                        .get()
                                        .addOnSuccessListener { result ->
                                            listOfManufacturer = result
                                            loadingForManufacturer = false
                                            Log.e(TAG, "listToShow: $listOfManufacturer")
                                        }
                                }
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching data for manufacturers: $e")
                        loadingForManufacturer = false
                    }

                }
            }else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (listOfManufacturer != null) {
                        items(listOfManufacturer!!.size()) { index ->
                            val record = listOfManufacturer!!.documents[index]
                            if (record.get("email")!="${FirebaseAuth.getInstance().currentUser?.email}") {
                                ListDesign(record = record, addedChainUsersList)
                            }
                        }
                    }
                }
            }



        }

        if(showDistributor){
            if (listOfDistributor == null) {
                if (loadingForDistributor){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                        try {
                        fireStore.collection("Distributor")
                            .get()
                            .addOnSuccessListener { result ->
                                listOfDistributor = result
                                loadingForDistributor = false
                                Log.e(TAG, "listToShow: $listOfDistributor")
                            }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching data for manufacturers: $e")
                        loadingForDistributor = false
                    }
                        if (userEmail != null) {
                            FirebaseFirestore.getInstance()
                                .collection(FirebaseAuth.getInstance().currentUser?.displayName.toString().replaceFirstChar {
                                    it.uppercase()
                                }
                                )
                                .document(userEmail)
                                .collection("ChainUsers")
                                .get().addOnSuccessListener { result ->
                                    addedChainUsersList = result
                                }
                        }
                    }
                }
            }else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (listOfDistributor != null) {
                        items(listOfDistributor!!.size()) { index ->
                            val record = listOfDistributor!!.documents[index]
                            if (record.get("email")!="${FirebaseAuth.getInstance().currentUser?.email}") {
                                ListDesign(
                                    record = record,
                                    addedChainUsersList
                                )
                            }
                        }
                    }
                }
            }

        }
        if(showRetailer){
            if (listOfRetailer == null){
                if (loadingForRetailer){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                        try {
                        fireStore.collection("Retailer")
                            .get()
                            .addOnSuccessListener { result ->
                                listOfRetailer = result
                                loadingForRetailer = false
                                Log.e(TAG, "listToShow: $listOfRetailer")
                            }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching data for manufacturers: $e")
                        loadingForRetailer = false
                    }
                        if (userEmail != null) {
                            FirebaseFirestore.getInstance()
                                .collection(FirebaseAuth.getInstance().currentUser?.displayName.toString().replaceFirstChar {
                                    it.uppercase()
                                })
                                .document(userEmail)
                                .collection("ChainUsers")
                                .get().addOnSuccessListener { result ->
                                    addedChainUsersList = result
                                }
                        }
                    }
                }
            }else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (listOfRetailer != null) {
                        items(listOfRetailer!!.size()) { index ->
                            val record = listOfRetailer!!.documents[index]
                            if (record.get("email")!="${FirebaseAuth.getInstance().currentUser?.email}"){
                                ListDesign(
                                    record = record,
                                    addedChainUsersList = addedChainUsersList
                                )
                            }
                        }
                    }
                }
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

@Composable
fun ListDesign(record: DocumentSnapshot, addedChainUsersList: QuerySnapshot?) {
    val context = LocalContext.current
    val other = record.get("email")
    var statusChainUser by remember {
        mutableStateOf(false)
    }
    statusChainUser =  if(addedChainUsersList?.find {
            it.get("email") == other
        } !=null){
        true
    }else{
        false
    }

    Log.e(TAG, "ListDesign: $statusChainUser,$other,${addedChainUsersList?.documents?.size}", )
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

            // Add your IconButton for sending friend request here
            IconButton(
                onClick = {
                    // Handle friend request action
                    addUser(record,context)
                }
            ) {
                // Add your icon for sending friend request here
                Icon(
                    imageVector = if (statusChainUser){
                                                      Icons.Default.Check
                                                      }else{
                                                           Icons.Default.Add
                                                           },
                    contentDescription = "Send Friend Request"
                )
            }

        }
    }
}

fun addUser(record: DocumentSnapshot, context: Context){
    val fireStore = FirebaseFirestore.getInstance()
    val userEmail = FirebaseAuth.getInstance().currentUser?.email
    val userRole = FirebaseAuth.getInstance().currentUser?.displayName
    val friend = record.get("email")
    val data = hashMapOf(
        "email" to friend,
    )
    if (userRole == "manufacturer"){
        if (userEmail != null) {
            fireStore
                .collection("Manufacturer")
                .document(userEmail)
                .collection("ChainUsers")
                .document(friend.toString())
                .set(data).addOnCompleteListener {
                    Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to add",Toast.LENGTH_SHORT).show()
                }
        }
    }
    if (userRole == "distributor"){
        if (userEmail != null) {
            fireStore
                .collection("Distributor")
                .document(userEmail)
                .collection("ChainUsers")
                .document(friend.toString())
                .set(data).addOnCompleteListener {
                    Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to add",Toast.LENGTH_SHORT).show()
                }
        }
    }
    if (userRole == "retailer"){
        if (userEmail != null) {
            fireStore
                .collection("Retailer")
                .document(userEmail)
                .collection("ChainUsers")
                .document(friend.toString())
                .set(data).addOnCompleteListener {
                    Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to add",Toast.LENGTH_SHORT).show()
                }
        }
    }
}


