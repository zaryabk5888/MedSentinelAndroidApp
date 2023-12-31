package com.example.myapplication.blockchainapp.presentation.signup.type



import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
import com.example.blockchainapp.R
import com.example.myapplication.blockchainapp.presentation.navigationcomponent.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Customer(navController: NavHostController) {
    val username = remember { mutableStateOf("customer@gmail.com") }
    val email = remember { mutableStateOf("customer@gmail.com") }
    val password = remember { mutableStateOf("123456") }
    val confirmPassword = remember { mutableStateOf("123456") }
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
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFF6F6F6),
                                Color(0xFFF6F6F6)
                            )
                        )
                    )
                    .padding(16.dp)
                    .clip(RoundedCornerShape(40.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.customer),
                        contentDescription = "Rectangle 23",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Text(
                        text = "Looks Like You Are New Here!",
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Text(
                        text = "Register Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFF42C3FE)
                    )

                    OutlinedTextField(
                        label = { Text("Username") },
                        value = username.value,
                        onValueChange = { username.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp
                    )

                    OutlinedTextField(
                        label = { Text("Email") },
                        value = email.value,
                        onValueChange = { email.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp
                    )

                    OutlinedTextField(
                        label = { Text("Password") },
                        value = password.value,
                        onValueChange = { password.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp
                    )

                    OutlinedTextField(
                        label = { Text("Confirm Password") },
                        value = confirmPassword.value, // Use confirmPassword here
                        onValueChange = { confirmPassword.value = it }, // Use confirmPassword here
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        enabled = !clickedSignUp
                    )

                    Button(
                        onClick = {
                            clickedSignUp = true
                            signUpCustomer(
                                username.value,
                                email.value,
                                password.value,
                                confirmPassword.value,
                                context,
                                navController
                            ){ loginSuccess ->
                                clickedSignUp = loginSuccess
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



fun checkIfValid(vararg strings: String): Boolean {
    val noneEmpty = strings.none { it.isEmpty() }
    val password = strings[2]
    val confirmPassword = strings[3]
    val passwordsMatch = password == confirmPassword
    return noneEmpty && passwordsMatch
}

fun signUpCustomer(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    context: Context,
    navController: NavHostController,
    onSignUpResult: (Boolean) -> Unit
) {
    val fireBaseFireStore = FirebaseFirestore.getInstance()
    val isValid = checkIfValid(username, email, password, confirmPassword)
    val data = hashMapOf(
        "email" to "$email",
        "role" to "customer"
    )
    if (isValid) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {authResult ->
                val user = authResult.user

                val profileUpdates = userProfileChangeRequest {
                    displayName = "customer"
                }

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateProfileTask ->
                    if (updateProfileTask.isSuccessful) {
                        Log.e(TAG, "signUpCustomer: Display name set to customer")
                    } else {
                        Log.e(TAG, "signUpCustomer: Failed to set display name")
                    }
                }
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fireBaseFireStore.collection("Customer").document(email).set(data)
                        .addOnCompleteListener {
                            Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show()
                            onSignUpResult(true)
                            navController.navigate(Screen.LoginScreen.route)
                        }.addOnFailureListener {
                            Toast.makeText(context, "Sign up successful but role not saved", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    onSignUpResult(false)
                    Log.e("firebase",task.exception!!.message.toString())
                    Toast.makeText(context, "Sign up failed $email$password", Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        onSignUpResult(false)
        Toast.makeText(context, "Some fields are empty or the passwords do not match", Toast.LENGTH_SHORT).show()
    }
}












