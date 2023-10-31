package com.example.myapplication.sampleapp


import android.os.Bundle
import android.util.Log
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                PinVM()
            }
        }
    }
}
@Composable
fun PinVM() {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://34.239.118.250:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var allMedicineData by remember {
        mutableStateOf(emptyList<Medicine>())
    }
    var oneMedicineData by remember {
        mutableStateOf<Medicine?>(null)
    }
    var getAll by remember {
        mutableStateOf(false)
    }
    var getOne by remember {
        mutableStateOf(false)
    }
    var createMed by remember {
        mutableStateOf(false)
    }
    var deleteMed by remember {
        mutableStateOf(false)
    }
    var updateMed by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                getAll = !getAll
            }
            ){
                Text(text = "Get All")
            }
            Button(onClick = {
                getOne = !getOne
            }
            ){
                Text(text = "Get One")
            }
            Button(onClick = {
                createMed = !createMed
            }
            ){
                Text(text = "Create Medicine")
            }
            Button(onClick = {
                deleteMed = !deleteMed
            }
            ){
                Text(text = "Delete Medicine")
            }

        }
        Column {
            Button(onClick = {
                updateMed = !updateMed
            }
            ){
                Text(text = "Update Medicine")
            }
            LazyColumn{
                items(allMedicineData){ data->
                    Text(text = data.toString())
                }
            }
            Text(text = "$oneMedicineData")
        }
    }



    if (getAll){
        LaunchedEffect(Unit) {
            try {
                val myApiService = retrofit.create(ApiService::class.java)
                allMedicineData = myApiService.medicineList()
                Log.e("Retrofit : ", allMedicineData.toString())
            }catch (e :java.lang.Exception){
                Log.e("Retrofit Fail :",e.message.toString())
            }
        }

    }
    if (getOne){
        LaunchedEffect(Unit) {
            try {
                val myApiService = retrofit.create(ApiService::class.java)
                oneMedicineData = myApiService.getMedicine(getmed(id = "medicine1"))
                Log.e("Retrofit : ", oneMedicineData.toString())
            }catch (e :java.lang.Exception){
                Log.e("Retrofit Fail :",e.message.toString())
            }
        }
    }
    if (createMed){
        LaunchedEffect(Unit) {
            try {
                val myApiService = retrofit.create(ApiService::class.java)
                oneMedicineData = myApiService.createMedicine(
                    data = Medicine(
                        ID = "2",
                        Name = "Panadol",
                        Manufacturer = "ABC Pharmaceuticals",
                        ManufactureDate = "2022-01-01",
                        ExpiryDate = "2024-01-01",
                        BrandName = "Brand1",
                        Composition = "Composition1",
                        SenderId = "Sender1",
                        ReceiverId = "Receiver1",
                        DrapNo = "DrapNo1",
                        DosageForm = "DosageForm1",
                        Description = "Description1",
                        History = listOf("London")
                    )
                )

                Log.e("Retrofit : ", oneMedicineData.toString())
            }catch (e :java.lang.Exception){
                Log.e("Retrofit Fail :",e.message.toString())
            }
        }
    }
    if (deleteMed){
        LaunchedEffect(Unit) {
            try {
                val myApiService = retrofit.create(ApiService::class.java)
                oneMedicineData = myApiService.deleteMedicine(data = getmed("2"))
                Log.e("Retrofit : ", oneMedicineData.toString())
            }catch (e :Exception){
                Log.e("Retrofit Fail :",e.message.toString())
            }
        }
    }
    if (updateMed){
        LaunchedEffect(Unit) {
            try {
                val myApiService = retrofit.create(ApiService::class.java)
                oneMedicineData = myApiService.updateMedicine(
                    data = Medicine(
                        ID = "2",
                        Name = "Panadol",
                        Manufacturer = "ABC Pharmaceuticals",
                        ManufactureDate = "2022-01-01",
                        ExpiryDate = "2024-01-01",
                        BrandName = "Brand1",
                        Composition = "Composition1",
                        SenderId = "Sender1",
                        ReceiverId = "Receiver2",
                        DrapNo = "DrapNo2",
                        DosageForm = "DosageForm2",
                        Description = "Description2",
                        History = listOf("London")
                    )
                )
                Log.e("Retrofit : ", oneMedicineData.toString())
            }catch (e :java.lang.Exception){
                Log.e("Retrofit Fail :",e.message.toString())
            }
        }
    }

}

data class Medicine(
    val ID: String,
    val Name: String,
    val Manufacturer: String,
    val ManufactureDate: String,
    val ExpiryDate: String,
    val BrandName: String,
    val Composition: String,
    val SenderId: String,
    val ReceiverId: String,
    val DrapNo: String,
    val DosageForm: String,
    val Description: String,
    val History: List<String>
)

data class getmed(
    val id : String
)


interface ApiService {

    @GET("medicines")
    suspend fun medicineList() : List<Medicine>

    @POST("get")
    suspend fun getMedicine(@Body data:getmed): Medicine

    @POST("create")
    suspend fun createMedicine(@Body data:Medicine) : Medicine

    @POST("delete")
    suspend fun deleteMedicine(@Body data:getmed) : Medicine

    @POST("update")
    suspend fun updateMedicine(@Body data:Medicine) : Medicine
}























