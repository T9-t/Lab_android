package com.example.composequadrant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                val colors = arrayOf(Color(0xFFEADDFF), Color(0xFFD0BCFF), Color(0xFFB69DF8), Color(0xFFF6EDFF))
                val textTitles = arrayOf(R.string.title_1, R.string.title_2, R.string.title_3, R.string.title_4)
                val text = arrayOf(R.string.text_1, R.string.text_2, R.string.text_3, R.string.text_4)

                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    ColorColumn(textTitles[0], text[0], colors[0], Modifier.weight(1f))
                    ColorColumn(textTitles[1], text[1], colors[1], Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    ColorColumn(textTitles[2], text[2], colors[2], Modifier.weight(1f))
                    ColorColumn(textTitles[3], text[3], colors[3], Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ColorColumn(title: Int, text: Int, color: Color, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColumnText(title, text)
    }
}

@Composable
fun ColumnText(title: Int, text: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(title),
        fontWeight = FontWeight.Bold,

        modifier = Modifier.padding(bottom = 16.dp)
    )
    Text(
        text = stringResource(text),
        textAlign = TextAlign.Justify
    )
}