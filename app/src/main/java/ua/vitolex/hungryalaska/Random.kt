package ua.vitolex.hungryalaska

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RandomRedSquareApp() {

    // Стан для зберігання координат квадрата та зміни фону середньої частини
    val boxPosition = remember { mutableStateOf(Pair(0f, 0f)) }  // координати квадрата (x, y)

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val steak = remember { mutableStateOf(true) }
    var isLeftSide = remember { mutableStateOf(false) }

    var score = remember { mutableStateOf(0) }
    var visibleBubble = remember { mutableStateOf(true) }

    var loveVisible = remember { mutableStateOf(false) }

    //    для звуку кнопки:
    //    val view = LocalView.current

    // Генерація випадкової позиції для квадрата (лівої або правої частини)
    fun generateRandomPosition() {

        // Випадково вибираємо ліву або праву частину
        isLeftSide.value = Random.nextBoolean()
        steak.value = Random.nextBoolean()

        // Випадкова позиція квадрата в межах лівої чи правої частини

        val x = if (isLeftSide.value) Random.nextInt(
            0,
            (screenWidth / 2 - 125)
        ) else Random.nextInt((screenWidth / 2 + 125).toInt(), (screenWidth - 50f).toInt())
        val y = Random.nextInt(0, (screenHeight - 50f).toInt()) // Випадкове положення по вертикалі

        // Оновлюємо позицію квадрата та змінюємо фон середньої частини
        boxPosition.value = Pair(x.toFloat(), y.toFloat())
    }


    LaunchedEffect(Unit) {
        delay(3000)
        generateRandomPosition()
        visibleBubble.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    // Replace with your image id
                    painterResource(id = R.drawable.snow),
                    contentScale = ContentScale.Crop
                ), contentAlignment = Alignment.TopCenter
        ) {


            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .alpha(if (visibleBubble.value) 1f else 0f)
                        .width(200.dp)
                        .height(80.dp)
                        .offset(155.dp, -55.dp)
                        .clip(SpeechBubbleShape())
                        .background(Color(android.graphics.Color.parseColor("#ADDFFF")))
                        .padding(6.dp)

                ) {
                    Text(
                        text = "Hi! My name is Alaska and I am very hungry.",
                        modifier = Modifier
                            .offset(x = 15.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .alpha(if (loveVisible.value == true && score.value == 10) 1f else 0f)
                        .width(150.dp)
                        .height(150.dp)
//                .offset(x = (screenWidth / 2).dp, y = (screenHeight / 2).dp)
                        .offset(150.dp, -65.dp)
                        .padding(6.dp)
                        .paint(
                            painterResource(id = R.drawable.love),
                            contentScale = ContentScale.Crop
                        )

                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                    .rotate(360f)
                    .paint(
                        painterResource(id = if (isLeftSide.value) R.drawable.santa_husky_right
                        else R.drawable.santa_husky_left),
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (score.value == 10) loveVisible.value = true
                            }
                        )
                    }
            )
        }
    }

    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .alpha(if (boxPosition.value.first == 0f && boxPosition.value.second == 0f) 0f else 1f)
    ) {
        Image(
            painter = painterResource(if (steak.value) R.drawable.steak else R.drawable.sausage),
            contentDescription = "meat",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .offset(x = boxPosition.value.first.dp, y = boxPosition.value.second.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            Log.d(
                                "MyLog",
                                "${boxPosition.value.first.dp} ${boxPosition.value.second.dp}"
                            )
                            // Переміщаємо квадрат при натисканні
                            generateRandomPosition()
                            score.value++

                            //    для звуку кнопки:
//                            view.playSoundEffect(SoundEffectConstants.CLICK)
                        }
                    )
                }
        )
    }
    Text(
        text = "Score: ${score.value}",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .wrapContentSize(align = Alignment.TopStart).padding(10.dp)
    )
}
