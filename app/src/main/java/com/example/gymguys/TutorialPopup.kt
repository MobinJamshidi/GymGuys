package com.example.gymguys

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.graphicsLayer
import com.example.gymguys.PreferencesManager
import androidx.compose.ui.platform.LocalContext

data class TutorialStep(
    val title: String,
    val description: String,
    val icon: String = "💪"
)

@Composable
fun TutorialPopup(
    screenKey: String,
    steps: List<TutorialStep>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val tutorialKey = remember(screenKey) { "tutorial_$screenKey" }
    
    var wasShown by remember {
        mutableStateOf(preferencesManager.getBoolean(tutorialKey, false))
    }
    
    var currentStep by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(!wasShown) }
    
    val scaleAnim by animateFloatAsState(
        targetValue = if (showDialog) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ScaleAnimation"
    )
    
    val alphaAnim by animateFloatAsState(
        targetValue = if (showDialog) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "AlphaAnimation"
    )
    
    val slideOffset by animateDpAsState(
        targetValue = if (showDialog) 0.dp else 50.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "SlideAnimation"
    )
    
    if (showDialog && steps.isNotEmpty()) {
        Dialog(
            onDismissRequest = {
                preferencesManager.setBoolean(tutorialKey, true)
                showDialog = false
                onDismiss()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                shadowElevation = 16.dp
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1a1a1a),
                                    Color(0xFF0a0a0a)
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = steps[currentStep].icon,
                            fontSize = 64.sp,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .graphicsLayer {
                                    scaleX = scaleAnim
                                    scaleY = scaleAnim
                                    alpha = alphaAnim
                                }
                        )
                        
                        Text(
                            text = steps[currentStep].title,
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.anton)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xffec6426),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .graphicsLayer {
                                    translationY = slideOffset.toPx()
                                    alpha = alphaAnim
                                }
                        )
                        
                        Text(
                            text = steps[currentStep].description,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.aldrich)),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .graphicsLayer {
                                    translationY = slideOffset.toPx()
                                    alpha = alphaAnim
                                }
                        )
                        
                        Row(
                            modifier = Modifier.padding(bottom = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            steps.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = if (index == currentStep) 
                                                Color(0xffec6426) 
                                            else 
                                                Color(0xff632713).copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (currentStep > 0) {
                                OutlinedButton(
                                    onClick = { 
                                        currentStep--
                                        // Reset animation
                                        showDialog = false
                                        showDialog = true
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.White
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xff632713),
                                                Color(0xff632713)
                                            )
                                        )
                                    )
                                ) {
                                    Text(
                                        text = "Previous",
                                        fontFamily = FontFamily(Font(R.font.aldrich)),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            
                            Button(
                                onClick = {
                                    if (currentStep < steps.size - 1) {
                                        currentStep++
                                        // Reset animation
                                        showDialog = false
                                        showDialog = true
                                    } else {
                                        preferencesManager.setBoolean(tutorialKey, true)
                                        showDialog = false
                                        onDismiss()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xffec6426),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (currentStep < steps.size - 1) "Next" else "Got it!",
                                    fontFamily = FontFamily(Font(R.font.aldrich)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


