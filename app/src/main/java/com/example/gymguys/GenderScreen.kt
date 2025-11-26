package com.example.gymguys

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderScreen(navController: NavController) {
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    var age by remember { mutableStateOf(32) }
    var weight by remember { mutableStateOf(70) }
    var height by remember { mutableStateOf(170) }
    
    // Dialog states for pickers
    var showAgePicker by remember { mutableStateOf(false) }
    var showHeightPicker by remember { mutableStateOf(false) }
    var showWeightPicker by remember { mutableStateOf(false) }

    val myFont = remember { FontFamily(Font(R.font.anton)) }
    
    val isButtonEnabled by remember { derivedStateOf { selectedGender != null } }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splashwallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Color(0xAA000000)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Let’s get to know you better",
                    fontSize = 28.sp,
                    fontFamily = myFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Select your gender to personalize your workouts",
                    fontSize = 14.sp,
                    fontFamily = myFont,
                    color = Color(0xFFCCCCCC),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GenderOption(
                        gender = Gender.MALE,
                        imageRes = R.drawable.genderboy,
                        title = "Male",
                        isSelected = selectedGender == Gender.MALE,
                        onSelect = { selectedGender = Gender.MALE },
                        fontFamily = myFont
                    )
                    GenderOption(
                        gender = Gender.FEMALE,
                        imageRes = R.drawable.gendergirl,
                        title = "Female",
                        isSelected = selectedGender == Gender.FEMALE,
                        onSelect = { selectedGender = Gender.FEMALE },
                        fontFamily = myFont
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PickerButton(
                    label = "Age",
                    value = age,
                    unit = "yr",
                    onClick = { showAgePicker = true },
                    fontFamily = myFont
                )

                Spacer(modifier = Modifier.height(12.dp))

                PickerButton(
                    label = "Height",
                    value = height,
                    unit = "cm",
                    onClick = { showHeightPicker = true },
                    fontFamily = myFont
                )

                Spacer(modifier = Modifier.height(12.dp))

                PickerButton(
                    label = "Weight",
                    value = weight,
                    unit = "kg",
                    onClick = { showWeightPicker = true },
                    fontFamily = myFont
                )
            }

            Button(
                onClick = {
                    println("Gender: $selectedGender, Age: $age, Height: $height, Weight: $weight")
                    navController.navigate("SignIn") {
                        popUpTo("SignIn") { inclusive = true }
                    }
                },
                enabled = isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isButtonEnabled) Color(0xffec6426) else Color(0xff632713)
                ),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontFamily = myFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    
    if (showAgePicker) {
        NumberPickerDialog(
            title = "Select Age",
            value = age,
            range = 10..100,
            unit = "yr",
            onValueChange = { age = it },
            onDismiss = { showAgePicker = false },
            fontFamily = myFont
        )
    }
    
    if (showHeightPicker) {
        NumberPickerDialog(
            title = "Select Height",
            value = height,
            range = 100..220,
            unit = "cm",
            onValueChange = { height = it },
            onDismiss = { showHeightPicker = false },
            fontFamily = myFont
        )
    }
    
    if (showWeightPicker) {
        NumberPickerDialog(
            title = "Select Weight",
            value = weight,
            range = 30..200,
            unit = "kg",
            onValueChange = { weight = it },
            onDismiss = { showWeightPicker = false },
            fontFamily = myFont
        )
    }
}

@Composable
fun GenderOption(
    gender: Gender,
    @DrawableRes imageRes: Int,
    title: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    fontFamily: FontFamily
) {
    val backgroundColor = remember(isSelected) {
        if (isSelected) Color(0xffec6426) else Color(0xff632713)
    }
    val borderWidth = remember(isSelected) {
        if (isSelected) 3.dp else 1.dp
    }
    val textColor = remember(isSelected) {
        if (isSelected) Color(0xffec6426) else Color(0xff632713)
    }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onSelect() }) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(
                    width = borderWidth,
                    color = backgroundColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PickerButton(
    label: String,
    value: Int,
    unit: String,
    onClick: () -> Unit,
    fontFamily: FontFamily
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xffec6426).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label ($unit)",
                fontSize = 18.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "$value",
                fontSize = 24.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xffec6426)
            )
        }
    }
}

@Composable
fun NumberPickerDialog(
    title: String,
    value: Int,
    range: IntRange,
    unit: String,
    onValueChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    fontFamily: FontFamily
) {
    var currentValue by remember(value) { mutableStateOf(value) }
    val coroutineScope = rememberCoroutineScope()
    val initialIndex = remember(value, range) { 
        (value - range.first).coerceIn(0, range.count() - 1) 
    }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    
    LaunchedEffect(Unit) {
        delay(50) // Reduced delay
        listState.animateScrollToItem(initialIndex)
    }
    
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .debounce(100)
            .distinctUntilChanged()
            .collect { index ->
                val newValue = (range.first + index.coerceIn(0, range.count() - 1))
                if (newValue in range && newValue != currentValue) {
                    currentValue = newValue
                }
            }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Current: $currentValue $unit",
                    fontFamily = fontFamily,
                    fontSize = 16.sp,
                    color = Color(0xffec6426),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xffec6426).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "$currentValue $unit",
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xffec6426)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        state = listState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(vertical = 80.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            count = range.count(),
                            key = { index -> range.first + index }
                        ) { index ->
                            val current = range.first + index
                            val isSelected = current == currentValue
                            val fontSize = remember(isSelected) { 
                                if (isSelected) 28.sp else 18.sp 
                            }
                            val textColor = remember(isSelected) {
                                if (isSelected) Color(0xffec6426) else Color.Gray.copy(alpha = 0.6f)
                            }

                            Text(
                                text = "$current",
                                fontSize = fontSize,
                                fontFamily = fontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = textColor,
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        currentValue = current
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(index)
                                        }
                                    },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onValueChange(currentValue)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffec6426)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "Done",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontFamily = fontFamily,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 8.dp
    )
}

@Composable
fun VerticalNumberPicker(
    label: String,
    value: Int,
    range: IntRange,
    unit: String,
    onValueChange: (Int) -> Unit,
    fontFamily: FontFamily
) {
    val coroutineScope = rememberCoroutineScope()
    val initialIndex = (value - range.first).coerceIn(0, range.count() - 1)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    var programmaticScroll by remember { mutableStateOf(false) }
    
    LaunchedEffect(value) {
        if (!programmaticScroll) {
            val targetIndex = (value - range.first).coerceIn(0, range.count() - 1)
            val currentIndex = listState.firstVisibleItemIndex
            if (currentIndex != targetIndex) {
                programmaticScroll = true
                listState.animateScrollToItem(targetIndex)
                delay(300)
                programmaticScroll = false
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label ($unit)",
            fontSize = 18.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 30.dp),
                modifier = Modifier.fillMaxWidth(),
                userScrollEnabled = true
            ) {
                items(
                    count = range.count(),
                    key = { index -> range.first + index }
                ) { index ->
                    val current = range.first + index
                    val isSelected = remember(current, value) { current == value }
                    val alpha = remember(isSelected) { if (isSelected) 1f else 0.4f }
                    val fontSize = remember(isSelected) { if (isSelected) 36.sp else 28.sp }
                    val textColor = remember(isSelected) {
                        if (isSelected) Color(0xffec6426) else Color.White.copy(alpha = alpha)
                    }

                    Text(
                        text = current.toString(),
                        fontSize = fontSize,
                        fontFamily = fontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = textColor,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .clickable {
                                if (!programmaticScroll) {
                                    coroutineScope.launch {
                                        programmaticScroll = true
                                        listState.animateScrollToItem(index)
                                        onValueChange(current)
                                        delay(300)
                                        programmaticScroll = false
                                    }
                                }
                            },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun HorizontalNumberPicker(
    label: String,
    value: Int,
    range: IntRange,
    unit: String,
    onValueChange: (Int) -> Unit,
    fontFamily: FontFamily
) {
    val coroutineScope = rememberCoroutineScope()
    val initialIndex = (value - range.first).coerceIn(0, range.count() - 1)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    var programmaticScroll by remember { mutableStateOf(false) }
    
    LaunchedEffect(value) {
        if (!programmaticScroll) {
            val targetIndex = (value - range.first).coerceIn(0, range.count() - 1)
            val currentIndex = listState.firstVisibleItemIndex
            if (currentIndex != targetIndex) {
                programmaticScroll = true
                listState.animateScrollToItem(targetIndex)
                delay(300)
                programmaticScroll = false
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label ($unit)",
            fontSize = 18.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(horizontal = 40.dp),
                modifier = Modifier.fillMaxWidth(),
                userScrollEnabled = true
            ) {
                items(
                    count = range.count(),
                    key = { index -> range.first + index }
                ) { index ->
                    val current = range.first + index
                    val isSelected = remember(current, value) { current == value }
                    val alpha = remember(isSelected) { if (isSelected) 1f else 0.4f }
                    val fontSize = remember(isSelected) { if (isSelected) 36.sp else 28.sp }
                    val textColor = remember(isSelected) {
                        if (isSelected) Color(0xffec6426) else Color.White.copy(alpha = alpha)
                    }

                    Text(
                        text = current.toString(),
                        fontSize = fontSize,
                        fontFamily = fontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = textColor,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clickable {
                                if (!programmaticScroll) {
                                    coroutineScope.launch {
                                        programmaticScroll = true
                                        listState.animateScrollToItem(index)
                                        onValueChange(current)
                                        delay(300)
                                        programmaticScroll = false
                                    }
                                }
                            },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

enum class Gender {
    MALE, FEMALE
}

