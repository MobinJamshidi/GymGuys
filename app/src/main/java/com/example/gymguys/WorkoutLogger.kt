package com.example.gymguys

// --- IMPORT های مورد نیاز (شامل فونت و تایپوگرافی) ---

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import java.util.UUID

// --- 0. تعریف فونت و تایپوگرافی سفارشی ---

val myFontFamily = FontFamily(
    Font(R.font.aldrich, FontWeight.Normal)
)

val defaultTypography = Typography()

val appTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = myFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = myFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = myFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = myFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = myFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = myFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = myFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = myFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = myFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = myFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = myFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = myFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = myFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = myFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = myFontFamily)
)



data class ExerciseItem(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var sets: String,
    var reps: String,
    var weightKg: String
)

data class MenuItemCategory(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var items: List<ExerciseItem>
) {
    val count: Int
        get() = items.size
}

// --- 2. داده‌های نمونه ---
val sampleGymData = listOf(
    MenuItemCategory(
        title = "Chest Day",
        items = listOf(
            ExerciseItem(name = "Bench Press", sets = "4", reps = "10", weightKg = "80"),
            ExerciseItem(name = "Dumbbell Flys", sets = "3", reps = "12", weightKg = "15"),
            ExerciseItem(name = "Incline Press", sets = "3", reps = "10", weightKg = "60"),
            ExerciseItem(name = "Push-ups", sets = "3", reps = "15", weightKg = "0")
        )
    ),
    MenuItemCategory(
        title = "Leg Day",
        items = listOf(
            ExerciseItem(name = "Squats", sets = "5", reps = "5", weightKg = "100"),
            ExerciseItem(name = "Leg Press", sets = "4", reps = "12", weightKg = "150"),
            ExerciseItem(name = "Calf Raises", sets = "3", reps = "20", weightKg = "50"),
            ExerciseItem(name = "Leg Curls", sets = "3", reps = "12", weightKg = "40"),
            ExerciseItem(name = "Lunges", sets = "3", reps = "12", weightKg = "20")
        )
    ),
    MenuItemCategory(
        title = "Back Day",
        items = listOf(
            ExerciseItem(name = "Pull-ups", sets = "4", reps = "10", weightKg = "0"),
            ExerciseItem(name = "Barbell Row", sets = "4", reps = "8", weightKg = "70"),
            ExerciseItem(name = "Lat Pulldown", sets = "3", reps = "12", weightKg = "60"),
            ExerciseItem(name = "Deadlift", sets = "3", reps = "5", weightKg = "120")
        )
    ),
    MenuItemCategory(
        title = "Shoulder Day",
        items = listOf(
            ExerciseItem(name = "Overhead Press", sets = "4", reps = "8", weightKg = "50"),
            ExerciseItem(name = "Lateral Raise", sets = "3", reps = "15", weightKg = "10"),
            ExerciseItem(name = "Front Raise", sets = "3", reps = "12", weightKg = "8"),
            ExerciseItem(name = "Rear Delt Fly", sets = "3", reps = "12", weightKg = "12")
        )
    ),
    MenuItemCategory(
        title = "Arm Day",
        items = listOf(
            ExerciseItem(name = "Bicep Curl", sets = "4", reps = "12", weightKg = "15"),
            ExerciseItem(name = "Tricep Pushdown", sets = "4", reps = "12", weightKg = "30"),
            ExerciseItem(name = "Hammer Curl", sets = "3", reps = "10", weightKg = "12"),
            ExerciseItem(name = "Overhead Extension", sets = "3", reps = "12", weightKg = "20")
        )
    ),
    MenuItemCategory(
        title = "Full Body",
        items = listOf(
            ExerciseItem(name = "Squats", sets = "3", reps = "10", weightKg = "80"),
            ExerciseItem(name = "Bench Press", sets = "3", reps = "8", weightKg = "70"),
            ExerciseItem(name = "Pull-ups", sets = "3", reps = "8", weightKg = "0"),
            ExerciseItem(name = "Overhead Press", sets = "3", reps = "8", weightKg = "40")
        )
    )
)

@Composable
fun WorkoutLogger(
    navController: NavHostController
) {
    val innerNavController = rememberNavController()
    val viewModel: WorkoutLoggerViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val mainList = remember { mutableStateListOf<MenuItemCategory>() }

    LaunchedEffect(uiState.categories) {
        mainList.clear()
        mainList.addAll(uiState.categories)
    }

    var isBlurred by remember { mutableStateOf(false) }
    val blurRadius by animateDpAsState(
        targetValue = if (isBlurred) 10.dp else 0.dp,
        label = "BlurAnimation"
    )

    LaunchedEffect(key1 = true) {
        delay(2000L)
        isBlurred = true
    }

    // Tutorial steps
    val tutorialSteps = listOf(
        TutorialStep(
            icon = "💪",
            title = "Welcome to Workout Logger!",
            description = "Track and manage your workouts. Create custom workout plans and log your exercises with sets, reps, and weights."
        ),
        TutorialStep(
            icon = "➕",
            title = "Add New Workout",
            description = "Tap the '+' button to create a new workout category. Give it a name like 'Chest Day' or 'Leg Day'."
        ),
        TutorialStep(
            icon = "📝",
            title = "Add Exercises",
            description = "Click on any workout card to add exercises. Enter the exercise name, sets, reps, and weight for each exercise."
        ),
        TutorialStep(
            icon = "💾",
            title = "Auto Save",
            description = "All your workouts are automatically saved to your account. You can access them anytime!"
        )
    )

    MaterialTheme(
        typography = appTypography
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Tutorial Popup
            TutorialPopup(
                screenKey = "workout_logger",
                steps = tutorialSteps,
                onDismiss = { }
            )
            Image(
                painter = painterResource(id = R.drawable.workoutloggerbackground),
                contentDescription = "Background",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = blurRadius),
                contentScale = ContentScale.Crop
            )

            // 3. این NavHost از کنترلر داخلی استفاده می‌کند
            NavHost(navController = innerNavController, startDestination = "main_grid") {

                composable("main_grid") {
                    MainGridScreen(
                        categories = mainList,
                        isLoading = uiState.isLoading,
                        isSaving = uiState.isSaving,
                        onAddCategory = { title ->
                            viewModel.addCategory(title)
                        },
                        onCardClick = { categoryId ->
                            innerNavController.navigate("detail/$categoryId")
                        },
                        onBackClicked = {
                            navController.navigate("MainPage")
                        }
                    )
                }

                composable("detail/{categoryId}") { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getString("categoryId")
                    val category = mainList.find { it.id == categoryId }

                    if (category != null) {
                        CategoryDetailScreen(
                            category = category,
                            onSaveChanges = { updatedCategory ->
                                viewModel.updateCategory(updatedCategory)
                                innerNavController.popBackStack()
                            },
                            onBack = {
                                innerNavController.popBackStack()
                            }
                        )
                    } else {
                        innerNavController.popBackStack()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGridScreen(
    categories: List<MenuItemCategory>,
    isLoading: Boolean = false,
    isSaving: Boolean = false,
    onAddCategory: (title: String) -> Unit,
    onCardClick: (categoryId: String) -> Unit,
    onBackClicked: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }


    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Menus", style = MaterialTheme.typography.bodySmall, color = Color.White)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Exercise", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                            if (isSaving) {
                                Spacer(modifier = Modifier.width(8.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color(0xffff4800),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked,
                            modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.1f),
                            contentColor = Color(0xffff4800)
                        )) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.List, "List", tint = Color(0xffff4800))
                    }
                    IconButton(onClick = { showDialog.value = true }) {
                        Icon(Icons.Default.Add, "Add", tint = Color(0xffff4800))
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories, key = { it.id }) { category ->
                MenuItemCard(
                    category = category,
                    onClick = { onCardClick(category.id) }
                )
            }
        }
    }

    if (showDialog.value) {
        AddCategoryTitleDialog(
            onDismiss = { showDialog.value = false },
            onSave = { title ->
                onAddCategory(title)
                showDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemCard(
    category: MenuItemCategory,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(category.title, style = MaterialTheme.typography.titleMedium, color = Color(0xffff4800))
                Spacer(Modifier.width(8.dp))
                Text(category.count.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White)
            }
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                category.items.take(3).forEach { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("•", modifier = Modifier.padding(end = 8.dp), color = Color.DarkGray, fontSize = 14.sp)
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (category.items.size > 3) {
                    Text("...", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryTitleDialog(
    onDismiss: () -> Unit,
    onSave: (title: String) -> Unit
) {
    val title = remember { mutableStateOf("") }
    val orangeColor = Color(0xffff4800)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Workout", color = Color.White) },
        text = {
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Workout Title") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = orangeColor,
                    focusedBorderColor = orangeColor,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { onSave(title.value) },
                enabled = title.value.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = orangeColor)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel")
            }
        },
        containerColor = Color.Black.copy(alpha = 0.8f)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    category: MenuItemCategory,
    onSaveChanges: (MenuItemCategory) -> Unit,
    onBack: () -> Unit
) {
    val title = remember { mutableStateOf(category.title) }
    val items = remember { mutableStateListOf(*category.items.toTypedArray()) }
    val orangeColor = Color(0xffff4800)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Edit Workout", color = Color(0xffffffff)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, "Back",
                            tint = orangeColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val updatedCategory = category.copy(
                            title = title.value,
                            items = items.toList()
                        )
                        onSaveChanges(updatedCategory)
                    }) {
                        Icon(
                            Icons.Default.Save, "Save Changes",
                            tint = orangeColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Workout Title") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.headlineSmall,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = orangeColor,
                        focusedBorderColor = orangeColor,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
                    )
                )
            }

            itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                ExerciseItemEditor(
                    item = item,
                    onNameChange = { newName -> items[index] = items[index].copy(name = newName) },
                    onSetsChange = { newSets -> items[index] = items[index].copy(sets = newSets) },
                    onRepsChange = { newReps -> items[index] = items[index].copy(reps = newReps) },
                    onWeightChange = { newWeight -> items[index] = items[index].copy(weightKg = newWeight) },
                    onDelete = { items.removeAt(index) }
                )
            }

            item {
                Button(
                    onClick = {
                        items.add(ExerciseItem(name = "", sets = "", reps = "", weightKg = ""))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = orangeColor)
                ) {
                    Icon(Icons.Default.Add, "Add")
                    Spacer(Modifier.width(8.dp))
                    Text("Add Exercise")
                }
            }
        }
    }
}

@Composable
fun ExerciseItemEditor(
    item: ExerciseItem,
    onNameChange: (String) -> Unit,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    val orangeColor = Color(0xffff4800)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = orangeColor,
        focusedBorderColor = orangeColor,
        unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = item.name,
                    onValueChange = onNameChange,
                    label = { Text("Exercise Name") },
                    modifier = Modifier.weight(1f),
                    colors = textFieldColors
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete", tint = orangeColor)
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = item.sets,
                    onValueChange = onSetsChange,
                    label = { Text("Sets") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = item.reps,
                    onValueChange = onRepsChange,
                    label = { Text("Reps") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = item.weightKg,
                    onValueChange = onWeightChange,
                    label = { Text("kg") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutLoggerPreview() {
    val navController = rememberNavController()
    WorkoutLogger(navController = navController)
}