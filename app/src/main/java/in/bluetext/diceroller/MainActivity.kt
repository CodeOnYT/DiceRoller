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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.bluetext.diceroller.ui.theme.DiceRollerTheme
import android.media.SoundPool
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
        MobileAds.initialize(this) {}
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

class Dice {
    private var currentSide: Int = 1

    fun roll(): Int {
        currentSide = (1..6).random()
        return currentSide
    }
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableIntStateOf(1) }
    val rotation = remember { Animatable(0f) }
    var isDiceRolling by remember { mutableStateOf(false) }
    val dice = remember { Dice() }
    val context = LocalContext.current

    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .build()
    }
    val soundId = remember { soundPool.load(context, R.raw.dice_roll, 1) }

    val diceImages = arrayOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )

    LaunchedEffect(isDiceRolling) {
        if (isDiceRolling) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            rotation.animateTo(360f, animationSpec = tween(500))
            result = dice.roll()
            rotation.snapTo(0f)
            isDiceRolling = false
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Click on the button to roll the dice",
            fontSize = 20.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your dice value is:")
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$result",
            fontSize = 50.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(diceImages[result - 1]),
            contentDescription = result.toString(),
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotation.value
                    transformOrigin = TransformOrigin.Center
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { isDiceRolling = true }) {
            Text(stringResource(R.string.roll))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.weight(1f))

        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-2549187779179978/3708722513" // Test Ad Unit ID
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "©BlueTEXT.in 2024, a Tulsiram Methre Company",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.padding(8.dp)
        )
    }
}