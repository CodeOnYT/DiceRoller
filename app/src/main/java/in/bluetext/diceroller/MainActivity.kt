package `in`.bluetext.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.bluetext.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableIntStateOf(1) }
    val rotation = remember { Animatable(0f) }
    var isRolling by remember { mutableStateOf(false) }

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    // LaunchedEffect to handle dice roll animation
    LaunchedEffect(isRolling) {
        if (isRolling) {
            // Animate rotation
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = tween(durationMillis = 500)
            )
            result = (1..6).random() // Update dice result after animation
            rotation.snapTo(0f) // Reset rotation for the next roll
            isRolling = false
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Click on the button to roll the dice",
            fontSize = 20.sp,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your dice value is:")
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "$result" ,
            fontSize = 50.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(imageResource),
            contentDescription = result.toString(),
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotation.value
                    transformOrigin = TransformOrigin.Center
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            isRolling = true
        }) {
            Text(stringResource(R.string.roll))
        }

    }
    Column( modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally){
    Spacer(modifier = Modifier.weight(1f)) // Push copyright to the bottom
    Text(
        text = "©BlueTEXT.in 2024, a Tulsiram Methre Company",
        fontSize = 10.sp, // Adjust font size as needed
        color = Color.Gray, // Adjust color as needed
        modifier = Modifier.padding(8.dp) // Add padding if desired
    )}
}
