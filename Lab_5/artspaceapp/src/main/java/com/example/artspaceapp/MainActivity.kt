package com.example.artspaceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspaceapp.data.DataSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var screenIndex by remember { mutableIntStateOf(0) }
            val artList = DataSource.Artworks

            val currentArt = artList[screenIndex]

            val onPreviousClick = {
                screenIndex = when (screenIndex) {
                    0 -> artList.lastIndex
                    else -> screenIndex - 1
                }
            }
            val onNextClick = {
                screenIndex = when (screenIndex) {
                    artList.lastIndex -> 0
                    else -> screenIndex + 1
                }
            }
            val configuration = LocalConfiguration.current
            val isWideScreen = configuration.screenWidthDp > 600

            if (isWideScreen) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.6f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        ArtworkImage(currentArt.imageResourceId)
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ArtworkInfo(currentArt.titleId, currentArt.artistId, currentArt.yearId)
                        Spacer(modifier = Modifier.height(32.dp))
                        Buttons(onPreviousClick, onNextClick)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ArtworkImage(currentArt.imageResourceId)
                    }
                    ArtworkInfo(currentArt.titleId, currentArt.artistId, currentArt.yearId)
                    Spacer(modifier = Modifier.height(24.dp))
                    Buttons(onPreviousClick, onNextClick)
                }
            }
        }
    }
}

@Composable
fun ArtworkImage(image: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(image),
        contentDescription = "Photo",
    )
}

@Composable
fun ArtworkInfo(title: Int, artist: Int, year: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, Color.Black)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(artist) + " (" +stringResource(year) + ")",
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Buttons(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val buttonModifier = Modifier
            .weight(1f)
            .height(48.dp)
            .border(1.5.dp, Color.Black)

        val buttonColors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEBEBEB),
            contentColor = Color.Black
        )
        CastomeButton(R.string.previous_button, onPreviousClick, buttonColors, buttonModifier)
        CastomeButton(R.string.next_button, onNextClick, buttonColors, buttonModifier)
    }
}

@Composable
fun CastomeButton(
    name: Int,
    onClick: () -> Unit,
    colors: ButtonColors,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        shape = RoundedCornerShape(2.dp),
    ) {
        Text(text = stringResource(name), fontSize = 16.sp)
    }
}