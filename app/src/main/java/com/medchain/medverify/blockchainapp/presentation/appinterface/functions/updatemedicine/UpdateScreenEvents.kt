package com.medchain.medverify.blockchainapp.presentation.appinterface.functions.updatemedicine

sealed class UpdateScreenEvents{
    object Get:UpdateScreenEvents()
    object Post:UpdateScreenEvents()
}
