package com.CUST.MedVerify.blockchainapp.presentation.appinterface.functions.getmedicine

sealed class GetScreenEvents{
    object GetId:GetScreenEvents()
    object GenerateQrCode:GetScreenEvents()
}
