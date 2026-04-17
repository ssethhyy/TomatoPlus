/*
 * Copyright (c) 2025-2026 ssethhyy
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 *
 * Tomato+ is free software: you can redistribute it and/or modify it under the terms of the MIT License.
 * See the LICENSE file in the project root for more information.
 */

package com.ssethhyy.tomatoplus.ui.timerScreen.viewModel

data class TimerState(
    val timerMode: TimerMode = TimerMode.FOCUS,
    val timeStr: String = "25:00",
    val totalTime: Long = 25 * 60,
    val timerRunning: Boolean = false,
    val nextTimerMode: TimerMode = TimerMode.SHORT_BREAK,
    val nextTimeStr: String = "5:00",
    val showBrandTitle: Boolean = true,
    val currentFocusCount: Int = 1,
    val totalFocusCount: Int = 4,
    val alarmRinging: Boolean = false,
    val serviceRunning: Boolean = false,
    val isMusicPlaying: Boolean = false,
    val musicProgress: Float = 0f,
    val isMusicLoading: Boolean = false,
    val musicCurrentTime: String = "00:00",
    val musicDuration: String = "00:00"
)

enum class TimerMode {
    FOCUS, SHORT_BREAK, LONG_BREAK, BRAND
}
