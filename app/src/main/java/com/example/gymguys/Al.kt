package com.example.gymguys

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

private val AldrichFontFamily = FontFamily(Font(R.font.aldrich))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ai(navController: NavHostController) {
    val viewModel: GeminiViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val tutorialSteps = listOf(
        TutorialStep(
            icon = "🤖",
            title = "AI Fitness Assistant",
            description = "Your personal AI trainer is here! Ask anything about workouts, nutrition, supplements, and fitness tips."
        ),
        TutorialStep(
            icon = "💬",
            title = "Ask Questions",
            description = "Type your question in the input field at the bottom. You can ask in English or Persian!"
        ),
        TutorialStep(
            icon = "💪",
            title = "Get Expert Advice",
            description = "Get personalized workout plans, nutrition tips, supplement recommendations, and exercise techniques."
        ),
        TutorialStep(
            icon = "🗑️",
            title = "Clear Chat",
            description = "Use the X button in the top right to start a new conversation anytime."
        )
    )

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        TutorialPopup(
            screenKey = "ai_assistant",
            steps = tutorialSteps,
            onDismiss = { }
        )

        Image(
            painter = painterResource(id = R.drawable.mainpagebackground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.9f),
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { navController.popBackStack() }
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
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Fitness AI Assistant",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontFamily = AldrichFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Ask me anything about gym & sports",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    fontFamily = AldrichFontFamily
                                )
                            }
                        }

                        if (uiState.messages.isNotEmpty()) {
                            IconButton(
                                onClick = { viewModel.clearChat() },
                                modifier = Modifier.size(40.dp),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color(0xffec6426).copy(alpha = 0.8f),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Chat",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (uiState.messages.isEmpty()) {
                    item {
                        WelcomeMessage()
                    }
                }

                items(uiState.messages) { message ->
                    if (message.isLoading) {
                        LoadingMessage()
                    } else {
                        ChatMessageBubble(message = message)
                    }
                }
            }

            uiState.error?.let { error ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    color = Color.Red.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = AldrichFontFamily,
                            modifier = Modifier.weight(1f),
                            lineHeight = 20.sp
                        )
                        IconButton(
                            onClick = { viewModel.clearError() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent,
                shadowElevation = 12.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.85f),
                                    Color.Black.copy(alpha = 0.95f)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier
                                .weight(1f)
                                .shadow(2.dp, RoundedCornerShape(28.dp)),
                            placeholder = {
                                Text(
                                    text = "Ask about workouts, nutrition, supplements...",
                                    color = Color.Gray,
                                    fontFamily = AldrichFontFamily,
                                    fontSize = 14.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xffec6426),
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                                focusedContainerColor = Color.White.copy(alpha = 0.15f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                                cursorColor = Color(0xffec6426)
                            ),
                            shape = RoundedCornerShape(28.dp),
                            singleLine = false,
                            maxLines = 4,
                            enabled = !uiState.isLoading,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = AldrichFontFamily,
                                fontSize = 14.sp
                            )
                        )

                        FloatingActionButton(
                            onClick = {
                                if (messageText.isNotBlank() && !uiState.isLoading) {
                                    viewModel.sendMessage(messageText)
                                    messageText = ""
                                }
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(4.dp, RoundedCornerShape(28.dp)),
                            containerColor = Color(0xffec6426),
                            contentColor = Color.White,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeMessage() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        color = Color.Black.copy(alpha = 0.75f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "👋 Hi!",
                color = Color(0xffec6426),
                fontSize = 36.sp,
                fontFamily = AldrichFontFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "I'm your Fitness & Sports AI Assistant",
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = AldrichFontFamily,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Ask me about workouts, nutrition, supplements, exercises, and anything related to fitness and sports. I can help in both English and Persian!",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontFamily = AldrichFontFamily,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xffec6426).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "💪 Workouts",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color(0xffec6426),
                        fontSize = 12.sp,
                        fontFamily = AldrichFontFamily
                    )
                }
                Surface(
                    color = Color(0xffec6426).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🍎 Nutrition",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color(0xffec6426),
                        fontSize = 12.sp,
                        fontFamily = AldrichFontFamily
                    )
                }
                Surface(
                    color = Color(0xffec6426).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "💊 Supplements",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color(0xffec6426),
                        fontSize = 12.sp,
                        fontFamily = AldrichFontFamily
                    )
                }
            }
        }
    }
}

/**
 * کامپوزبل تغییر یافته برای افزایش عرض حباب پاسخ‌های AI
 */
@Composable
fun ChatMessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .then(
                    if (message.isUser) {
                        // پیام کاربر: حداکثر 300.dp (عرض اصلی)
                        Modifier.widthIn(max = 300.dp)
                    } else {
                        // پیام AI: پر کردن 85% از عرض والد (افزایش عرض)
                        Modifier.fillMaxWidth(0.85f)
                    }
                )
                .padding(horizontal = 4.dp)
                .shadow(4.dp, RoundedCornerShape(18.dp)),
            color = if (message.isUser) {
                Color(0xffec6426)
            } else {
                Color.White.copy(alpha = 0.95f)
            },
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(16.dp),
                color = if (message.isUser) Color.White else Color.Black,
                fontSize = 15.sp,
                fontFamily = AldrichFontFamily,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun LoadingMessage() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 120.dp)
                .padding(horizontal = 4.dp)
                .shadow(4.dp, RoundedCornerShape(18.dp)),
            color = Color.White.copy(alpha = 0.95f),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color(0xffec6426),
                    strokeWidth = 2.5.dp
                )
                Text(
                    text = "Thinking...",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = AldrichFontFamily
                )
            }
        }
    }
}