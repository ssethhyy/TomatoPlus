/*
 * Copyright (c) 2026 ssethhyy
 * Copyright (c) 2025-2026 Vrushal (modifications)
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 */

package com.ssethhyy.tomatoplus.ui.statsScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Dialog for manually logging focus time.
 * Allows users to add focus time that wasn't tracked by the timer.
 */
@Composable
fun ManualLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (focusMinutes: Int, sessions: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var focusMinutes by remember { mutableIntStateOf(25) }
    var sessions by remember { mutableIntStateOf(1) }
    
    // Validation - max 18 hours (1080 minutes) total
    val totalMinutes = focusMinutes * sessions
    val isValid = remember(focusMinutes, sessions) {
        focusMinutes in 5..240 && sessions in 1..10 && totalMinutes <= 1080
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Log Focus Time",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Add focus time that you completed without using the timer.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Focus Time Slider
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horiTomato+talArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Duration",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${focusMinutes} min",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = focusMinutes.toFloat(),
                        onValueChange = { focusMinutes = it.toInt() },
                        valueRange = 5f..240f, // Max 4 hours per session
                        steps = 46, // Every 5 minutes
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        text = if (totalMinutes > 1080) "Max 18 hours total" else "How long did you focus?",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (totalMinutes > 1080) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Sessions
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horiTomato+talArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Sessions",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "$sessions",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = sessions.toFloat(),
                        onValueChange = { sessions = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        text = "How many focus sessions?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    onConfirm(focusMinutes, sessions)
                    onDismiss()
                },
                enabled = isValid
            ) {
                Text("Log Time")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
}
