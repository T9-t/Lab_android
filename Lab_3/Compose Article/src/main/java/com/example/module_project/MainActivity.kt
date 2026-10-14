package com.example.module_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier.statusBarsPadding()
            ) {
                val image = painterResource(R.drawable.bg_compose_background)
                Image(
                    painter = image,
                    contentDescription = null
                )
                Text(stringResource(R.string.tutorial_title), 24.sp)
                Text(stringResource(R.string.tutorial_paragraph_1), 14.sp)
                Text(stringResource(R.string.tutorial_paragraph_2), 14.sp)
            }
        }
    }
}

@Composable
fun Text(text: String, size: TextUnit ,modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = size,
        modifier = modifier.padding(10.dp)
    )
}