package com.example.gymguys

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.gymguys.PreferencesManager
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val pagerState = rememberPagerState(pageCount = { 3 })

    val coroutineScope = rememberCoroutineScope()

    val myFont = FontFamily(Font(R.font.anton))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfffde3cf))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> FirstOnboardingPage(fontFamily = myFont)
                    1 -> SecondOnboardingPage(fontFamily = myFont)
                    2 -> ThirdOnboardingPage(fontFamily = myFont)
                }
            }

            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (index == pagerState.currentPage) Color(0xffec6426) else Color(0xff632713).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        // Mark onboarding as completed
                        preferencesManager.setOnboardingCompleted(true)
                        navController.navigate("gender") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff632713),
                )
            ) {
                Text(
                    text = if (pagerState.currentPage < 2) "Next" else "Get Started",
                    fontFamily = myFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
@Composable
fun FirstOnboardingPage(fontFamily: FontFamily) {
    val rainbowColors = listOf(
        Color.Red,
        Color.Magenta,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color(0xFFFF9800) // Orange
    )
    val brush = remember { Brush.linearGradient(colors = rainbowColors) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfffde3cf)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.girlworkout),
                contentDescription = "Workout illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xfffde3cf))
                        )
                    )
            )
        }

        Text(
            text = "Every journey starts with one move",
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 48.dp)

        )
    }
}

@Composable
fun SecondOnboardingPage(fontFamily: FontFamily) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfffde3cf)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.boyworkout),
                contentDescription = "Workout illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xfffde3cf))
                        )
                    )
            )
        }

        Text(
            text = "Stay consistent, even when it’s hard",
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 48.dp)
        )
    }
}

@Composable
fun ThirdOnboardingPage(fontFamily: FontFamily) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfffde3cf)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagetwo),
                contentDescription = "Workout illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xfffde3cf))
                        )
                    )
            )
        }

        Text(
            text = "Stronger today, unstoppable tomorrow",
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 48.dp)
        )
    }
}