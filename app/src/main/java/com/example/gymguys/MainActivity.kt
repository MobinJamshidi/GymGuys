package com.example.gymguys

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymguys.ui.screens.SplashScreen
import com.example.gymguys.ui.theme.GYMGuysTheme


class MainActivity : ComponentActivity() {
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        window.decorView.post {
            hideSystemBars()
        }
        
        setContent {
            GYMGuysTheme {
                AppNavigation()
            }
        }
    }
    
    private fun hideSystemBars() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.post {
                hideSystemBars()
            }
        }
    }
}


@Composable 
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ){
        composable("splash"){SplashScreen(navController = navController)}
        composable("onboarding"){OnboardingScreen(navController = navController)}
        composable("gender"){GenderScreen(navController = navController)}
        composable("SignIn"){SignIn(navController = navController)}
        composable("SignUP"){SignUp(navController = navController)}
        composable("MainPage"){MainPage(navController = navController)}
        composable("Nutrition"){WorkOut(navController = navController)}
        composable("Workoutlogger"){WorkoutLogger(navController = navController)}
        composable("Ai"){Ai(navController = navController)}
        composable("BMICalculator"){BMICalculator(navController = navController)}
    }
}



