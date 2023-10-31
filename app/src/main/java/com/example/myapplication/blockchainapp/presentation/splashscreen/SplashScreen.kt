package com.example.myapplication.blockchainapp.presentation.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.blockchainapp.R


@Composable
    fun SplashScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Background color or image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue) // Replace with your desired background color or image
            )

            // App logo or image

            Image(
                painter = painterResource(R.drawable.ic_launcher_background), // Replace with your app logo or image resource
                contentDescription = "App Logo"
            )
        }
    }

    @Composable
    fun MainScreen() {

    }

    @Preview
    @Composable
    fun SplashScreenPreview() {
        SplashScreen()
    }

