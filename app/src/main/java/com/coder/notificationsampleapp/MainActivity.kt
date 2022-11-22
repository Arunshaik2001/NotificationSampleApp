package com.coder.notificationsampleapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.coder.notificationsampleapp.notifcation_channels.NotificationChannels.createNotificationChannel
import com.coder.notificationsampleapp.notification.Notification
import com.coder.notificationsampleapp.ui.theme.NotificationSampleAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationSampleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
        createNotificationChannel(this)
    }
}

@Composable
fun HomeScreen(){
    val context = LocalContext.current
    val permissionGranted = remember {
        mutableStateOf(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
    }

    LaunchedEffect(key1 = true) {
        delay(5000)
        Notification.createFullScreenIntentNotification(context)
    }

    Log.i("RequestPermission","${permissionGranted.value}")

    if(!permissionGranted.value) {
        RequestNotificationPermission {
            permissionGranted.value = true

        }
    }
    else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                Notification.simpleNotification(context, "Hello title", "Description")
            }) {
                Text(text = "Simple Notification")
            }

            Button(onClick = {
                Notification.createProgressNotification(context)
            }) {
                Text(text = "Progress Bar Notification")
            }


            Button(onClick = {
                Notification.createNotificationWithBackStack(context)
            }) {
                Text(text = "Notification with special activity")
            }

            Button(onClick = {
                Notification.createNotificationWithLargeIcon(context)
            }) {
                Text(text = "Notification with large image")
            }

            Button(onClick = {
                Notification.createNotificationWithLargeText(context)
            }) {
                Text(text = "Notification with large text")
            }

            Button(onClick = {
                Notification.createNotificationWithConversationText(context)
            }) {
                Text(text = "Notification with conversation text")
            }

            Button(onClick = {

                Notification.createNotificationWithMediaControl(context)
            }) {
                Text(text = "Notification with media controls")
            }

            Button(onClick = {

                Notification.createFullScreenIntentNotification(context)
            }) {
                Text(text = "Notification with full screen intent")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotificationSampleAppTheme {
        Greeting("Android")
    }
}