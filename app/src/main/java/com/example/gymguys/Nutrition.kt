package com.example.gymguys

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image // <--- NEW import
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box // <--- NEW import
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // <--- NEW import
import androidx.compose.ui.res.painterResource // <--- NEW import
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

private val DarkAppColorScheme = darkColorScheme(
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    primary = Color.White,
    onPrimary = Color.Black,
    surfaceVariant = Color(0x66252525)
)

data class WorkoutInfo(
    val label: String,
    val title: String,
    val description: String
)

data class Exercise(
    val name: String,
    val description: String,
    val sets: String,
    val reps: String
)

private val bicepsExercises = listOf(
    Exercise("Dumbbell Curl", "The classic move for building mass.", "3-4", "8-12"),
    Exercise("Hammer Curl", "Targets the brachialis (side of the arm).", "3", "10-12"),
    Exercise("Barbell Curl", "Great for overall bicep strength.", "3", "8-10"),
    Exercise("Preacher Curl", "Excellent for isolating the bicep peak.", "3", "10-12")
)

private val tricepsExercises = listOf(
    Exercise("Tricep Pushdown", "Great cable move for tricep definition.", "3-4", "10-15"),
    Exercise("Overhead Extension", "Stretches and builds the long head.", "3", "10-12"),
    Exercise("Dips", "Bodyweight move engaging chest and triceps.", "3", "To Failure"),
    Exercise("Skullcrushers", "Lying barbell move for mass.", "3-4", "8-12")
)

private val shouldersExercises = listOf(
    Exercise("Overhead Press", "Main move for shoulder strength and size.", "4", "6-10"),
    Exercise("Lateral Raise", "Targets the medial (side) delt for width.", "3", "12-15"),
    Exercise("Front Raise", "Isolates the anterior (front) delt.", "3", "10-12"),
    Exercise("Reverse Pec Deck", "Targets the posterior (rear) delts.", "3", "12-15")
)

private val chestExercises = listOf(
    Exercise("Bench Press", "The fundamental compound lift for chest.", "4", "5-8"),
    Exercise("Dumbbell Flyes", "Great stretch and isolation move.", "3", "10-12"),
    Exercise("Push-ups", "Excellent bodyweight chest builder.", "3", "To Failure"),
    Exercise("Incline Dumbbell Press", "Focuses on the upper (clavicular) pecs.", "3-4", "8-12")
)

private val latsExercises = listOf(
    Exercise("Pull-ups", "The best bodyweight move for back width.", "4", "To Failure"),
    Exercise("Lat Pulldown", "Simulates pull-ups, good for all levels.", "3-4", "10-12"),
    Exercise("Bent-over Row", "Builds overall back thickness.", "4", "8-10")
)

private val trapsExercises = listOf(
    Exercise("Barbell Shrugs", "The primary move for upper trap mass.", "4", "10-15"),
    Exercise("Dumbbell Shrugs", "Allows for a slightly better range of motion.", "3", "12-15")
)

private val lowerBackExercises = listOf(
    Exercise("Deadlift", "The king of all lifts, heavily engages lower back.", "3-5", "3-5"),
    Exercise("Hyperextension", "Isolation move to strengthen the erector spinae.", "3", "12-15")
)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkOut(navController: NavHostController) {
    val myfontt = FontFamily(Font(R.font.aldrich))
    
    val tutorialSteps = listOf(
        TutorialStep(
            icon = "🍎",
            title = "Nutrition Guide",
            description = "Explore comprehensive nutrition information and meal plans tailored for your fitness goals."
        ),
        TutorialStep(
            icon = "🏋️",
            title = "Exercise Categories",
            description = "Browse different muscle groups: Biceps, Triceps, Shoulders, Chest, Back, and more. Each category has detailed exercises."
        ),
        TutorialStep(
            icon = "📋",
            title = "Exercise Details",
            description = "Each exercise shows recommended sets and reps. Follow the guidelines for optimal results."
        ),
        TutorialStep(
            icon = "📊",
            title = "Track Progress",
            description = "Use the progress bar at the top to see which muscle groups you've explored. Complete all sections for a full-body guide!"
        )
    )

    val workoutOptions = listOf(
        WorkoutInfo("Biceps", "Biceps Focus", "Choose exercises for your biceps."),
        WorkoutInfo("Triceps", "Triceps Focus", "Exercises to build your triceps."),
        WorkoutInfo("Shoulders", "Shoulder Day", "Build strong, defined shoulders."),
        WorkoutInfo("Chest", "Chest Workout", "Develop your pectoral muscles."),
        WorkoutInfo("Flamenco", "Flamenco Style", "Learn the basics of Flamenco."),
        WorkoutInfo("Latissimus dorsi", "Back (Lats)", "Focus on your Latissimus Dorsi."),
        WorkoutInfo("Trapezius", "Back (Traps)", "Exercises for your trapezius."),
        WorkoutInfo("Lower back", "Lower Back", "Strengthen your lower back.")
    )

    var selectedIndex by remember { mutableStateOf(0) }

    MaterialTheme(colorScheme = DarkAppColorScheme) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                TutorialPopup(
                    screenKey = "nutrition",
                    steps = tutorialSteps,
                    onDismiss = { }
                )
                Image(
                    painter = painterResource(id = R.drawable.workout2), // <--- REPLACE with your image name
                    contentDescription = null, // Decorative image, no description needed
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.9f
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    TextButton(onClick = { navController.navigate("MainPage") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xffff4800))
                        Spacer(Modifier.width(4.dp))
                        Text("", color = Color(0xffff4800), fontSize = 16.sp, fontFamily = myfontt)
                    }
                    Spacer(Modifier.height(24.dp))

                    val totalSteps = workoutOptions.size
                    val currentStep = selectedIndex + 1
                    ProgressBar(step = currentStep, totalSteps = totalSteps)
                    Spacer(Modifier.height(48.dp))

                    val currentInfo = workoutOptions[selectedIndex]
                    Text(currentInfo.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = myfontt)
                    Spacer(Modifier.height(8.dp))
                    Text(currentInfo.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(Modifier.height(32.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        workoutOptions.forEachIndexed { index, info ->
                            DanceStyleChip(
                                text = info.label,
                                isSelected = selectedIndex == index,
                                onClick = { selectedIndex = index }
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    when (selectedIndex) {
                        0 -> BicepsContent()
                        1 -> TricepsContent()
                        2 -> ShouldersContent()
                        3 -> ChestContent()
                        4 -> FlamencoContent()
                        5 -> LatsContent()
                        6 -> TrapsContent()
                        7 -> LowerBackContent()
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun DanceStyleChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val myfontt = FontFamily(Font(R.font.aldrich))
    val backgroundColor = if (isSelected) Color(0xffff4800) else Color.Transparent
    val textColor = if (isSelected) Color.Black else Color.White
    val border = if (isSelected) null else BorderStroke(1.dp, Color.Gray)
    Surface(
        color = backgroundColor,
        shape = CircleShape,
        border = border,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(text, fontFamily = myfontt,color = textColor,fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
    }
}

@Composable
fun ProgressBar(step: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 1..totalSteps) {
            val color = if (i <= step) Color(0xffff4800) else Color.DarkGray
            Divider(color = color, thickness = 4.dp, modifier = Modifier.weight(1f).clip(RoundedCornerShape(2.dp)))
        }
    }
}


@Composable
fun ExerciseCard(exercise: Exercise) {
    val myfontt = FontFamily(Font(R.font.aldrich))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = exercise.name,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = myfontt
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = exercise.description,
                color = Color.LightGray,
                fontSize = 14.sp,
                fontFamily = myfontt
            )
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(end = 24.dp)) {
                    Text(

                        text = "Sets",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = myfontt

                    )
                    Text(
                        text = exercise.sets,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = myfontt

                    )
                }
                Column {
                    Text(
                        text = "Reps",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = myfontt

                    )
                    Text(
                        text = exercise.reps,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = myfontt

                    )
                }
            }
        }
    }
}

@Composable
fun BicepsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        bicepsExercises.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun TricepsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        tricepsExercises.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun ShouldersContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        shouldersExercises.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun ChestContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        chestExercises.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun FlamencoContent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Flamenco Details", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Information about the art of Flamenco...", color = Color.Gray)
    }
}

@Composable
fun LatsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        latsExercises.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun TrapsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        trapsExercises.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun LowerBackContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        lowerBackExercises.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}