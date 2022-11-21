package com.coder.notificationsampleapp

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermission(isGranted: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val notificationPermissionState =
                rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

            if (notificationPermissionState.status.isGranted) {
                isGranted()
                Text("Notification permission Granted")
            } else {
                Column {
                    val textToShow = if (notificationPermissionState.status.shouldShowRationale) {
                        "The notification permission is important for this app. Please grant the permission."
                    } else {
                        "notifications not available"
                    }

                    Text(textToShow)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { notificationPermissionState.launchPermissionRequest() }) {
                        Text("Request permission")
                    }
                }
            }
        }
    }

}

