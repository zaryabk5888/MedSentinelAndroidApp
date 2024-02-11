package com.CUST.MedVerify.blockchainapp.presentation.splashscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CUST.MedVerify.blockchainapp.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100) // Simulate loading
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Background color or image
            Box(
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
                Column(
                    modifier = Modifier.fillMaxSize(),

                    ) {

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .offset(y = 200.dp,)
                            .clip(RoundedCornerShape(50.dp))
                            .align(Alignment.CenterHorizontally),
                    )
                    Text(
                        text = "MedVerify",
                        modifier = Modifier
                            .offset(y = 220.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color(
                            0xFF05AAC2
                        ),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,


                    )

                    Text(
                        text = "Ensure Medicine Authenticity",
                        modifier = Modifier
                            .offset(y = 240.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color.White,
                        fontSize = 26.sp,


                        )

                    Button(
                        modifier = Modifier
                            .offset(y = 490.dp)
                            .align(Alignment.CenterHorizontally).width(200.dp),
                        onClick = {
                        },
                        colors = ButtonColors(
                            containerColor = Color(0xFF23D5F0),
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Next",
                            color = Color.White,
                            fontSize = 28.sp
                        )
                    }

                }

            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}


