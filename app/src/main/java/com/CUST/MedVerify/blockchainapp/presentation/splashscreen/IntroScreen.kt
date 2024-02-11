package com.CUST.MedVerify.blockchainapp.presentation.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CUST.MedVerify.blockchainapp.R

@Composable
fun IntroScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0B1446),
                            Color(0xFF040F4B),
                            Color(0xFF23D5F0)
                        )
                    )
                )
        ) {
            // Logo at the top
            Text(text = "MedVerify", fontWeight = FontWeight.Bold, fontSize = 45.sp, color = Color(
                0xFF05AAC2
            )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(painter = painterResource(id = R.drawable.auth), contentDescription = "App Logo", modifier = Modifier
                .size(150.dp)
                .clip(
                    RoundedCornerShape(50.dp)
                )
            )

            // Main title

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(text = "Ensuring medication authenticity\nwith blockchain technology", textAlign = TextAlign.Center, color = Color.White, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // Central Graphic or Image
            Image(painter = painterResource(id = R.drawable.qrcode), contentDescription = "Central Graphic", modifier = Modifier
                .size(150.dp)
                .clip(
                    RoundedCornerShape(50.dp)
                ))

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Description
            Text(text = "Create records, verify authenticity,\nsecure medical information", textAlign = TextAlign.Center, color = Color.White, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // Central Graphic or Image
            Image(painter = painterResource(id = R.drawable.search), contentDescription = "Central Graphic", modifier = Modifier
                .size(150.dp)
                .clip(
                    RoundedCornerShape(50.dp)
                ))

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Description
            Text(text = "Search Records, Scan QR Code,\nverify medical information", textAlign = TextAlign.Center, color = Color.White, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {  }, modifier = Modifier.width(200.dp),
                colors = ButtonColors(
                containerColor = Color(0xFF05AAC2),
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Black
            )
            ) {
                Text(
                    text = "Start",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,


                )
            }
        }
    }
}

@Preview
@Composable
fun Intro(){
    IntroScreen()
}