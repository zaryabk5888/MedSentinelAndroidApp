package com.medchain.medverify.blockchainapp.presentation.appinterface.functions.getmedicine

sealed class GetScreenEvents{
    object GetId:GetScreenEvents()
    object GenerateQrCode:GetScreenEvents()
}
