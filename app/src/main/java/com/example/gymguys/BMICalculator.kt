package com.example.gymguys

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavController
import kotlin.math.pow

@Composable
fun BMICalculator(navController: NavController) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Double?>(null) }
    var bmiCategory by remember { mutableStateOf<String?>(null) }
    
    val myFont = remember { FontFamily(Font(R.font.anton)) }
    val myfontt = remember { FontFamily(Font(R.font.aldrich)) }
    
    val tutorialSteps = listOf(
        TutorialStep(
            icon = "📊",
            title = "BMI Calculator",
            description = "Calculate your Body Mass Index (BMI) to understand your body composition and health status."
        ),
        TutorialStep(
            icon = "⚖️",
            title = "Enter Your Weight",
            description = "Enter your weight in kilograms (kg). For example: 70 for 70 kg."
        ),
        TutorialStep(
            icon = "📏",
            title = "Enter Your Height",
            description = "Enter your height in centimeters (cm). For example: 175 for 175 cm."
        ),
        TutorialStep(
            icon = "📈",
            title = "View Your Results",
            description = "Your BMI will be calculated automatically. Check the scale below to see your category: Underweight, Normal, Overweight, or Obese."
        )
    )
    
    fun calculateBMI() {
        val weightValue = weight.toDoubleOrNull()
        val heightValue = height.toDoubleOrNull()
        
        if (weightValue != null && heightValue != null && weightValue > 0 && heightValue > 0) {
            val heightInMeters = heightValue / 100.0
            val bmi = weightValue / (heightInMeters.pow(2))
            bmiResult = bmi
            
            bmiCategory = when {
                bmi < 18.5 -> "Underweight"
                bmi < 25 -> "Normal"
                bmi < 30 -> "Overweight"
                else -> "Obese"
            }
        } else {
            bmiResult = null
            bmiCategory = null
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        TutorialPopup(
            screenKey = "bmi_calculator",
            steps = tutorialSteps,
            onDismiss = { }
        )
        
        Image(
            painter = painterResource(id = R.drawable.mainpagebackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000))
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.1f),
                        contentColor = Color(0xffff4800)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = "BMI Calculator",
                    fontSize = 36.sp,
                    fontFamily = myFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Calculate your Body Mass Index",
                    fontSize = 16.sp,
                    fontFamily = myfontt,
                    color = Color(0xFFCCCCCC),
                    textAlign = TextAlign.Center
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { 
                        weight = it
                        calculateBMI()
                    },
                    label = {
                        Text(
                            text = "Weight (kg)",
                            fontFamily = myfontt,
                            color = Color.White
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Enter your weight",
                            fontFamily = myfontt,
                            color = Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color(0xFFCCCCCC),
                        focusedIndicatorColor = Color(0xffec6426),
                        unfocusedIndicatorColor = Color(0xffec6426).copy(alpha = 0.5f),
                        focusedContainerColor = Color.White.copy(alpha = 0.15f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )
                )
                
                OutlinedTextField(
                    value = height,
                    onValueChange = { 
                        height = it
                        calculateBMI()
                    },
                    label = {
                        Text(
                            text = "Height (cm)",
                            fontFamily = myfontt,
                            color = Color.White
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Enter your height",
                            fontFamily = myfontt,
                            color = Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color(0xFFCCCCCC),
                        focusedIndicatorColor = Color(0xffec6426),
                        unfocusedIndicatorColor = Color(0xffec6426).copy(alpha = 0.5f),
                        focusedContainerColor = Color.White.copy(alpha = 0.15f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (bmiResult != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            Color(0xffec6426)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Your BMI",
                                fontSize = 18.sp,
                                fontFamily = myfontt,
                                color = Color(0xFFCCCCCC)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = String.format("%.1f", bmiResult),
                                fontSize = 48.sp,
                                fontFamily = myFont,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xffec6426)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = bmiCategory ?: "",
                                fontSize = 20.sp,
                                fontFamily = myFont,
                                fontWeight = FontWeight.Bold,
                                color = when (bmiCategory) {
                                    "Underweight" -> Color(0xFF4FC3F7)
                                    "Normal" -> Color(0xFF66BB6A)
                                    "Overweight" -> Color(0xFFFFA726)
                                    "Obese" -> Color(0xFFEF5350)
                                    else -> Color.White
                                }
                            )
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.3f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Enter your weight and height",
                                fontSize = 16.sp,
                                fontFamily = myfontt,
                                color = Color(0xFFCCCCCC),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BMI Scale",
                        fontSize = 18.sp,
                        fontFamily = myFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BMIScaleItem("Underweight", "< 18.5", Color(0xFF4FC3F7), myfontt)
                        BMIScaleItem("Normal", "18.5-24.9", Color(0xFF66BB6A), myfontt)
                        BMIScaleItem("Overweight", "25-29.9", Color(0xFFFFA726), myfontt)
                        BMIScaleItem("Obese", "≥ 30", Color(0xFFEF5350), myfontt)
                    }
                }
            }
        }
    }
}

@Composable
fun BMIScaleItem(
    label: String,
    range: String,
    color: Color,
    fontFamily: FontFamily
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontFamily = fontFamily,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = range,
            fontSize = 9.sp,
            fontFamily = fontFamily,
            color = Color(0xFFCCCCCC),
            textAlign = TextAlign.Center
        )
    }
}

