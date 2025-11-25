package com.example.gymguys.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator // این را اضافه کنید
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp // این را اضافه کنید
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymguys.PreferencesManager
import com.example.gymguys.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val myFont = FontFamily(Font(R.font.contrail_one))
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(2000)
        
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate("MainPage") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            val isOnboardingCompleted = preferencesManager.isOnboardingCompleted()
            if (isOnboardingCompleted) {
                navController.navigate("SignIn") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "Workout illustration",
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GYM Guys",
                fontSize = 80.sp,
                color = Color(0xffec6426),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = myFont
            )

            Spacer(modifier = Modifier.height(24.dp))

            // حلقه چرخان (Loading Indicator)
            CircularProgressIndicator(
                color = Color(0xffec6426),
                strokeWidth = 5.dp
            )
        }
    }
}