package com.CUST.MedVerify.blockchainapp.presentation.login


import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.CUST.MedVerify.blockchainapp.R
import com.CUST.MedVerify.blockchainapp.presentation.navigationcomponent.Screen
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


@Composable
fun CheckLoginStatus(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .align(Alignment.CenterHorizontally),
                )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //.........................Text: title
                Text(
                    text = "MedVerify",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    color = Color(
                        0xFF05AAC2
                    ),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))


                Spacer(modifier = Modifier.padding(3.dp))


                val cornerRadius = 16.dp


                Spacer(modifier = Modifier.padding(10.dp))

                SimpleOutlinedTextFieldSample(
                    cornerRadius = cornerRadius,
                    nameButton = "Login",
                    navController = navController
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleOutlinedTextFieldSample(
    cornerRadius: Dp,
    nameButton: String,
    navController: NavController
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var email by rememberSaveable { mutableStateOf("manufacturer@gmail.com") }
    var password by rememberSaveable { mutableStateOf("123456") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var clickedLogin by rememberSaveable { mutableStateOf(false) }
    var loginError by rememberSaveable { mutableStateOf(false) }
    var attempt by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Name or Email Address",
            )
        },
        placeholder = { Text(text = "Name or Email Address") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                // do something here
            }
        ),
        enabled = !clickedLogin,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedLabelColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "Enter Password",
            )
        },
        visualTransformation =
        if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                if (passwordHidden) VisualTransformation.None else PasswordVisualTransformation()
                val description = if (passwordHidden) "Show password" else "Hide password"
                Icon(
                    imageVector = if (passwordHidden) Icons.Default.Visibility
                    else Icons.Default.VisibilityOff, contentDescription = description,tint = Color.White
                )
            }
        },
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                // do something here
            }
        ),
        enabled = !clickedLogin,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedLabelColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )

    Spacer(modifier = Modifier.padding(10.dp))
    val loadingModifier = Modifier.padding(start = 32.dp, end = 32.dp)
    val buttonModifier = if (clickedLogin) loadingModifier else Modifier
    //Login Button
    Button(
        modifier = buttonModifier.width(200.dp),
        onClick = {
            if (email.isNotEmpty()&&password.isNotEmpty()){
                clickedLogin = true
                loginError = false
            }else{
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }

            // You can add any loading indicator here (e.g., CircularProgressIndicator)
            // or show a loading message while attempting login.
            // For simplicity, we use a CircularProgressIndicator.
        },
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        if (clickedLogin) {
            // Show CircularProgressIndicator while logging in
            CircularProgressIndicator(
                modifier = loadingModifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                color = Color.Gray
            )
        } else {
            // Show login button label
            Text(
                text = nameButton,
                fontSize = 28.sp,
                color = Color.White
            )
        }
    }

    if (clickedLogin) {
        // Attempt login and handle success or failure
        loginClient(email, password, navController) { loginSuccess ->
            if (loginSuccess) {
                // Handle successful login
                clickedLogin = false // Reset clickedLogin
            } else {
                Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show()
                clickedLogin = false // Reset clickedLogin
                loginError = true
            }
        }
    }

    // Show a login error message if login failed
    if (loginError) {
        Text(
            text = "Login failed. Please check your credentials.",
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
    Spacer(modifier = Modifier.padding(10.dp))
    androidx.compose.material3.TextButton(
        onClick = {
        navController.navigate(Screen.SignUpScreen.route){
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
    },
        enabled = !clickedLogin
    ) {
        Text(
            text = "Create An Account",
            letterSpacing = 1.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }


    Spacer(modifier = Modifier.padding(5.dp))
    androidx.compose.material3.TextButton(onClick = {

        navController.navigate(Screen.ResetCredScreen.route){
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
    },
        enabled = !clickedLogin
    ) {
        Text(
            text = "Forgot Password?",
            letterSpacing = 1.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
    Spacer(modifier = Modifier.padding(20.dp))

}


fun checkIfValid(vararg strings: String): Boolean {
    return strings.none { it.isEmpty() }
}

fun loginClient(
    email: String,
    password: String,
    navController: NavController,
    onLoginResult: (Boolean) -> Unit
) {
    val isValid = checkIfValid(email, password)
    if (isValid) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
               //two factor authentication

                if (task.isSuccessful) {
                    navController.navigate(Screen.PrimaryScreen.route)
                    onLoginResult(true)
                } else {
                    Log.e("firebase", task.exception!!.message.toString())
                    onLoginResult(false) // Notify failure
                }
            }
    } else {
        Log.e("firebase", "Some fields are empty or the passwords do not match")
        onLoginResult(false) // Notify failure
    }
}





