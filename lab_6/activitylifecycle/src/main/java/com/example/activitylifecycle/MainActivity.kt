package com.example.activitylifecycle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.activitylifecycle.ui.theme.Module_projectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("override_method", "onCreate()")
        enableEdgeToEdge()
        setContent {
            Module_projectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Log.v("override_method", "onStart()")
    }
    override fun onResume() {
        super.onResume()
        Log.v("override_method", "onResume()")
    }
    override fun onPause() {
        super.onPause()
        Log.v("override_method", "onPause()")
    }
    override fun onStop() {
        super.onStop()
        Log.v("override_method", "onStop()")
    }
    override fun onRestart() {
        super.onRestart()
        Log.v("override_method", "onRestart()")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v("override_method", "onDestroy()")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Module_projectTheme {
        Greeting("Android")
    }
}