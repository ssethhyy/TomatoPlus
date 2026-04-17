/*
 * Copyright (c) 2025-2026 ssethhyy
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 *
 * Tomato+ is free software: you can redistribute it and/or modify it under the terms of the MIT License.
 * See the LICENSE file in the project root for more information.
 */

package com.ssethhyy.tomatoplus.ui.settingsScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HoriTomato+talDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ssethhyy.tomatoplus.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlusDivider(
    setShowPaywall: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(vertical = 14.dp)) {
        HoriTomato+talDivider(modifier = Modifier.clip(CircleShape), thickness = 4.dp)
        Button(
            onClick = { setShowPaywall(true) },
            shapes = ButtonDefaults.shapes(),
            modifier = Modifier
                .background(colorScheme.surfaceContainer)
                .padding(horiTomato+tal = 8.dp)
        ) {
            Text(stringResource(R.string.tomato_plus_desc), style = typography.titleSmall)
        }
    }
}
