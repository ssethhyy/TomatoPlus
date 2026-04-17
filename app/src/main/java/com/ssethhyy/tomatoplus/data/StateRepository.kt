/*
 * Copyright (c) 2025-2026 ssethhyy
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 *
 * Tomato+ is free software: you can redistribute it and/or modify it under the terms of the MIT License.
 * See the LICENSE file in the project root for more information.
 */

package com.ssethhyy.tomatoplus.data

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import kotlinx.coroutines.flow.MutableStateFlow
import com.ssethhyy.tomatoplus.service.TimerStateSnapshot
import com.ssethhyy.tomatoplus.ui.settingsScreen.viewModel.SettingsState
import com.ssethhyy.tomatoplus.ui.timerScreen.viewModel.TimerState

class StateRepository {
    val timerState = MutableStateFlow(TimerState())
    val settingsState = MutableStateFlow(SettingsState())
    var timerFrequency: Float = 60f
    var colorScheme: ColorScheme = lightColorScheme()
    var timerStateSnapshot: TimerStateSnapshot =
        TimerStateSnapshot(time = 0, timerState = TimerState())
}
