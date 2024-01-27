package com.example.myapplication.blockchainapp.data.dto


data class Medicine(
    val ID: String, //15%
    val Name: String,//5%
    val Manufacturer: String,//5%
    val ManufactureDate: String,//5%
    val ExpiryDate: String,//5%
    val BrandName: String,//5%
    val Composition: String,//5%
    val SenderId: String,//5%
    val ReceiverId: String,//5%
    val DrapNo: String, //20%
    val DosageForm: String,
    val TimeStamp: String,
    val Batch_No : String,//batch no 15%
    val JourneyCompleted : String,
    val Location : String,
)

