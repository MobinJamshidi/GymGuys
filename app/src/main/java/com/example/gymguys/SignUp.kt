package com.example.gymguys


import android.webkit.WebSettings
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


@Composable
fun SignUp(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    var isBlurred by remember { mutableStateOf(false) }
    val myFont = FontFamily(Font(R.font.anton))
    val myfontt = FontFamily(Font(R.font.aldrich))
    
    var errorMessage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        delay(2000)
        isBlurred = true
    }

    val blurValue by animateFloatAsState(
        targetValue = if (isBlurred) 16f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logindark),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(blurValue.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.padding(top = 150.dp, start = 15.dp),
                text = "Sign Up",
                fontSize = 60.sp,
                fontFamily = myfontt,
                color = Color(0xffffffff),
                fontWeight = FontWeight.Bold
            )

            val NameState = remember { mutableStateOf("") }
            TextField(
                value = NameState.value,
                onValueChange = { NameState.value = it },
                label = {
                    Text(
                        text = "Full Name",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },
                placeholder = {
                    Text(
                        text = "Mark Zuckerberg",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },

                textStyle = TextStyle(
                    fontFamily = myfontt,
                    color = Color(0xffffffff),
                    fontSize = 16.sp
                ),

                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xffffffff),
                    unfocusedIndicatorColor = Color(0xffffffff),
                    focusedLabelColor = Color(0xffffffff),
                    unfocusedLabelColor = Color(0xffffffff),
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    ),
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            val emailState = remember { mutableStateOf("") }
            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = {
                    Text(
                        text = "Email",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },
                placeholder = {
                    Text(
                        text = "example@gmail.com",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },

                textStyle = TextStyle(
                    fontFamily = myfontt,
                    color = Color(0xffffffff),
                    fontSize = 16.sp
                ),

                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xffffffff),
                    unfocusedIndicatorColor = Color(0xffffffff),
                    focusedLabelColor = Color(0xffffffff),
                    unfocusedLabelColor = Color(0xffffffff),
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    ),
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )

            val passState = remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }
            TextField(
                value = passState.value,
                onValueChange = { passState.value = it },
                label = {
                    Text(
                        text = "Pass",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },
                placeholder = {
                    Text(
                        text = "Mark123Jam",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },
                textStyle = TextStyle(
                    fontFamily = myfontt,
                    color = Color(0xffffffff),
                    fontSize = 16.sp
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color(0xffffffff)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xffffffff),
                    unfocusedIndicatorColor = Color(0xffffffff),
                    focusedLabelColor = Color(0xffffffff),
                    unfocusedLabelColor = Color(0xffffffff),
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            val ConfirmpassState = remember { mutableStateOf("") }
            var confirmPasswordVisible by remember { mutableStateOf(false) }
            TextField(
                value = ConfirmpassState.value,
                onValueChange = { ConfirmpassState.value = it },
                label = {
                    Text(
                        text = "Confirm Pass",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },
                placeholder = {
                    Text(
                        text = "Mark123Jam",
                        color = Color(0xffffffff),
                        fontFamily = myfontt

                    )
                },
                textStyle = TextStyle(
                    fontFamily = myfontt,
                    color = Color(0xffffffff),
                    fontSize = 16.sp
                ),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                            tint = Color(0xffffffff)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xffffffff),
                    unfocusedIndicatorColor = Color(0xffffffff),
                    focusedLabelColor = Color(0xffffffff),
                    unfocusedLabelColor = Color(0xffffffff),
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            
            // Error Message
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color(0xffff0000),
                    fontSize = 14.sp,
                    fontFamily = myfontt,
                    modifier = Modifier.padding(start = 10.dp, top = 8.dp)
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        errorMessage = null
                        if (passState.value != ConfirmpassState.value) {
                            errorMessage = "Passwords do not match"
                            return@Button
                        }
                        viewModel.signUp(
                            email = emailState.value.trim(),
                            password = passState.value,
                            fullName = NameState.value.trim(),
                            onSuccess = {
                                navController.navigate("MainPage") {
                                    popUpTo("SignUp") { inclusive = true }
                                }
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .padding(top = 20.dp, start = 30.dp, end = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isLoading) Color(0xff632713) else Color(0xffec6426),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    if (uiState.isLoading) {
                        Text(
                            text = "Creating Account...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            fontFamily = myfontt
                        )
                    } else {
                        Text(
                            text = "Create Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            fontFamily = myfontt
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "I Have An Account ",
                        color = Color(0xffffffff),
                        fontSize = 14.sp,
                        fontFamily = myfontt,
                    )

                    TextButton(
                        onClick = {navController.navigate("SignIn")},
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        Text(
                            text = "Sign In",
                            color = Color(0xffec6426),
                            fontSize = 14.sp,
                            fontFamily = myfontt
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpPreview() {
    val navController = rememberNavController()

    SignUp(navController = navController)
}