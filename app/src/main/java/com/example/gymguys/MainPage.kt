package com.example.gymguys

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// --- ایمپورت‌های جدید برای آیکون ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
// ----------------------------------------------

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.util.Locale

private val AldrichFontFamily = FontFamily(Font(R.font.aldrich))

@Composable
fun MainPage(
    navController: NavHostController,
    viewModel: StepCounterViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {

    var hasPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            viewModel.startListening()
        }
    }

    DisposableEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permissionStatus = context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)
            if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                hasPermission = true
                viewModel.startListening()
            } else {
                permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        } else {
            hasPermission = true
            viewModel.startListening()
        }

        onDispose {
            viewModel.stopListening()
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.mainpagebackground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = {
                authViewModel.signOut()
                navController.navigate("SignIn") {
                    popUpTo("MainPage") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White,
                containerColor = Color.Black.copy(alpha = 0.5f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Sign Out",
                modifier = Modifier.size(24.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp, end = 10.dp, start = 10.dp)
                .height(150.dp)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .clickable(enabled = !hasPermission) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                    }
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp), 
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val steps = viewModel.steps.value
                    val distance = viewModel.distanceInMeters.value

                    Text(
                        text = if (hasPermission) "$steps" else "--",
                        color = Color.White,
                        fontSize = 42.sp,
                        fontFamily = AldrichFontFamily
                    )
                    Text(
                        text = if (hasPermission) "Steps" else "Tap to grant permission",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = AldrichFontFamily
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (hasPermission) formatDistance(distance) else "---",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        fontFamily = AldrichFontFamily
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Image(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.activity),
                    contentDescription = null,
                )
            }

            if (hasPermission) {
                IconButton(
                    onClick = { viewModel.resetSteps() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restart Steps"
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GlassyButton(
                text = " Workout Logger",
                onClick = {
                    navController.navigate("Workoutlogger")
                },
                iconResId = R.drawable.gymiconworkoutlogger,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(80.dp)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassyButton(
                    text = "BMI Test",
                    onClick = { navController.navigate("BMICalculator") },
                    iconResId = R.drawable.bmi,
                    modifier = Modifier
                        .height(90.dp)
                        .weight(1f)
                )

                GlassyButton(
                    text = "Nutrition",
                    onClick = {
                        navController.navigate("Nutrition")
                    },
                    iconResId = R.drawable.nutrition,
                    modifier = Modifier
                        .height(90.dp)
                        .weight(1f)
                )
            }
            GlassyButton(
                text = ("Al"),
                onClick = { navController.navigate("Ai")},
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .fillMaxWidth()
            )
        }
    }
}


private fun formatDistance(distanceInMeters: Double): String {
    return if (distanceInMeters < 1000) {
        String.format(Locale.US, "%.0f m", distanceInMeters)
    } else {
        val distanceInKm = distanceInMeters / 1000.0
        String.format(Locale.US, "%.2f km", distanceInKm)
    }
}


@Composable
fun GlassyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconResId: Int? = null
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff000000).copy(alpha = 0.4f),
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = text,
                fontSize = 15.sp,
                fontFamily = AldrichFontFamily
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPagePreview() {
    val navController = rememberNavController()
    MainPage(navController = navController)
}