package com.CUST.MedVerify.blockchainapp.presentation.signup.type

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.CUST.MedVerify.blockchainapp.R

import com.CUST.MedVerify.blockchainapp.presentation.navigationcomponent.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Distributor(navController: NavHostController) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    var clickedSignUp by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                            navController.navigate(Screen.SignUpScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color(0xFF0B1446),
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .verticalScroll(rememberScrollState())
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
            Box(
                modifier = Modifier
                    .fillMaxSize()


            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.distributor),
                        contentDescription = "Rectangle 23",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Text(
                        text = "Distributor Sign Up",
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    OutlinedTextField(
                        label = { Text("Username") },
                        value = username.value,
                        onValueChange = { username.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        label = { Text("Email") },
                        value = email.value,
                        onValueChange = { email.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        label = { Text("Password") },
                        value = password.value,
                        onValueChange = { password.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        label = { Text("Confirm Password") },
                        value = confirmPassword.value,
                        onValueChange = { confirmPassword.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Button(
                        onClick = {
                            clickedSignUp = true
                            signUpDistributor(
                                username.value,
                                email.value,
                                password.value,
                                confirmPassword.value,
                                context,
                                navController
                            ) { signUpSuccess ->
                                clickedSignUp = signUpSuccess
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                color = Color(0xFF4785FF),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        enabled = !clickedSignUp
                    ) {
                        Text(
                            text = "Register",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}
fun checkIfValidForDistributor(vararg strings: String): Boolean {
    val noneEmpty = strings.none { it.isEmpty() }
    val password = strings[2]
    val confirmPassword = strings[3]
    val passwordsMatch = password == confirmPassword
    return noneEmpty && passwordsMatch
}
fun signUpDistributor(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    context: Context,
    navController: NavHostController,
    onSignUpResult: (Boolean) -> Unit
) {
    val fireBaseFireStore = FirebaseFirestore.getInstance()
    val isValid = checkIfValidForDistributor(username, email, password, confirmPassword)
    val data = hashMapOf(
        "email" to email,
        "role" to "distributor" // Change the role to "distributor"
        // Add any distributor-specific data here
    )
    if (isValid) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {authResult ->
                val user = authResult.user

                val profileUpdates = userProfileChangeRequest {
                    displayName = "distributor"
                }

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateProfileTask ->
                    if (updateProfileTask.isSuccessful) {
                        Log.e(ContentValues.TAG, "signUpDistributor: Display name set to distributor")
                    } else {
                        Log.e(ContentValues.TAG, "signUpDistributor: Failed to set display name")
                    }
                }}.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fireBaseFireStore.collection("Distributor").document(email).set(data)
                        .addOnCompleteListener {
                            Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show()
                            onSignUpResult(true)
                            navController.navigate(Screen.LoginScreen.route)
                        }.addOnFailureListener {
                            Toast.makeText(context, "Sign up successful but role not saved", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    onSignUpResult(false)
                    Log.e("firebase", task.exception?.message.toString())
                    Toast.makeText(context, "Sign up failed $email$password", Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        onSignUpResult(false)
        Toast.makeText(context, "Some fields are empty or the passwords do not match", Toast.LENGTH_SHORT).show()
    }
}
