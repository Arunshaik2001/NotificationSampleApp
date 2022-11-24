package com.coder.notificationsampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.coder.notificationsampleapp.ui.theme.NotificationSampleAppTheme

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = intent.data?.lastPathSegment ?: return
        
        setContent{
            NotificationSampleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = message, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}