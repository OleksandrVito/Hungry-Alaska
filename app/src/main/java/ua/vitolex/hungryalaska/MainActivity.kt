package ua.vitolex.hungryalaska

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.vitolex.hungryalaska.ui.theme.HungryAlaskaTheme

class MainActivity : ComponentActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        loadAd()
        setContent {
            val mContext = LocalContext.current
            val mMediaPlayer = MediaPlayer.create(mContext, R.raw.sound)
            mMediaPlayer.start()

            val music = remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(5000)
                MainScope().launch {
//                    mInterstitialAd?.show(this@MainActivity)
                }
            }
            HungryAlaskaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    RandomRedSquareApp()
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .wrapContentSize(align = Alignment.BottomEnd)
                    ) {
                        // IconButton for Music
                        IconButton(onClick = {
                            if (music.value == true) {
                                mMediaPlayer.pause()
                                music.value = false
                            } else {
                                mMediaPlayer.start()
                                music.value = true
                            }


                        }) {
                            if (music.value == true) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_music_note_24),
                                    contentDescription = "",
                                    Modifier
                                        .size(60.dp)
                                        .scale(0.6f)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_music_off_24),
                                    contentDescription = "",
                                    Modifier
                                        .size(60.dp)
                                        .scale(0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-1869740172940843/8013271327",
            adRequest,
            object : InterstitialAdLoadCallback() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(TAG, it) }
                    mInterstitialAd = null
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback = adListener()
                }
            })
    }

    private fun adListener() = object : FullScreenContentCallback() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
//            count.value = 1

            Log.d(TAG, "Ad dismissed fullscreen content.")
            mInterstitialAd = null
            loadAd()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            // Called when ad fails to show.
            Log.e(TAG, "Ad failed to show fullscreen content.")
            mInterstitialAd = null
            loadAd()
        }
    }
}







