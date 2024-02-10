package com.CUST.MedVerify.blockchainapp.presentation.signup.reset

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.CUST.MedVerify.blockchainapp.presentation.navigationcomponent.Screen
import com.google.firebase.auth.FirebaseAuth
import com.CUST.MedVerify.blockchainapp.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetCred(navController: NavController) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    action = {
                        TextButton(
                            onClick = {
                                data.dismiss()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Cyan
                            )
                        ) {
                            Text("Dismiss")
                        }
                    },
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "Recovery Link Send Successfully",
                        color = Color.Cyan
                    )
                }
            }
        }
    ) { paddingValue ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(
                    color = Color.Transparent,
                )
        ) {
            Box(
                modifier = Modifier
                    /*.background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(25.dp, 5.dp, 25.dp, 5.dp)
                            )*/
                    .align(Alignment.BottomCenter),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_forgot),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),

                    )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //.........................Spacer
                    Spacer(modifier = Modifier.height(50.dp))

                    //.........................Text: title
                    Text(
                        text = "Reset Password",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 130.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))


                    val gradientColor = listOf(Color(0xFF484BF1), Color(0xFF673AB7))
                    val cornerRadius = 16.dp


                    Spacer(modifier = Modifier.padding(10.dp))
                    /* Button(
                                 onClick = {},
                                 modifier = Modifier
                                     .fillMaxWidth(0.8f)
                                     .height(50.dp)
                             ) {
                                 Text(text = "Login", fontSize = 20.sp)
                             }*/
                    ResetEmailID(
                        nameButton = "Submit",
                        snackBarHostState = snackBarHostState,
                        gradientColor,
                        cornerRadius
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    TextButton(onClick = {
                        navController.navigate(Screen.SignUpScreen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }

                    }) {
                        Text(
                            text = "Sign Up?",
                            letterSpacing = 1.sp,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }
    }

    }


@Composable
fun ResetEmailID(
    nameButton: String,
    snackBarHostState: SnackbarHostState,
    gradientColor: List<Color>,
    cornerRadius: Dp
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    val clickedSubmit = remember { mutableStateOf(false) }
    val emailSent = remember { mutableStateOf(false) }
    val context = LocalContext.current
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Enter Registered Email",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        placeholder = { Text(text = "Enter Registered Email") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                // Do something here if needed
            }
        ),
        enabled = !clickedSubmit.value
    )

    Spacer(modifier = Modifier.height(18.dp))

    if (emailSent.value) {
        LaunchedEffect(Unit) {
            snackBarHostState.showSnackbar(
                message = "Email Sent For Password Recovery",
                actionLabel = "Dismiss",
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
        Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Email Sent")
    } else {
        androidx.compose.material3.Button(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Blue,
                    shape = RoundedCornerShape(bottomStart = 18.dp, topEnd = 18.dp)
                )
                .padding(start = 32.dp, end = 32.dp),

            onClick = {
                if (text.isNotEmpty() && !clickedSubmit.value) {
                    clickedSubmit.value = true
                    FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(text)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                emailSent.value = true
                            } else {
                                Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show()
                            }

                            clickedSubmit.value = false
                        }
                }
            },
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),


            ) {

            if (clickedSubmit.value) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = nameButton,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

