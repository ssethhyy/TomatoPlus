/*
 * Copyright (c) 2025-2026 ssethhyy
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 *
 * Tomato+ is free software: you can redistribute it and/or modify it under the terms of the MIT License.
 * See the LICENSE file in the project root for more information.
 */

package com.ssethhyy.tomatoplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.ssethhyy.tomatoplus.TomatoPlusApplication
import com.ssethhyy.tomatoplus.ui.AppScreen
import com.ssethhyy.tomatoplus.ui.Screen
import com.ssethhyy.tomatoplus.ui.settingsScreen.viewModel.SettingsViewModel
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusTheme
import com.ssethhyy.tomatoplus.utils.toColor

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels(factoryProducer = { SettingsViewModel.Factory })

    private val appContainer by lazy {
        (application as TomatoPlusApplication).container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appContainer.activityTurnScreenOn = {
            setShowWhenLocked(it)
            setTurnScreenOn(it)
        }

        setContent {
            val settingsState by settingsViewModel.settingsState.collectAsStateWithLifecycle()

            val darkTheme = when (settingsState.theme) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            val seed = settingsState.colorScheme.toColor()

            val isPlus by settingsViewModel.isPlus.collectAsStateWithLifecycle()

            TomatoPlusTheme(
                darkTheme = darkTheme,
                seedColor = seed,
                blackTheme = settingsState.blackTheme
            ) {
                val colorScheme = colorScheme
                LaunchedEffect(colorScheme) {
                    settingsViewModel.updateThemeColors(colorScheme)
                }

                var initialScreen by remember { mutableStateOf<Screen?>(null) }

                LaunchedEffect(Unit) {
                    val completed = appContainer.appPreferenceRepository.getBooleanPreference("is_onboarding_completed") ?: false
                    
                    // Check if opened from widget with navigation target
                    val navigateTo = intent?.getStringExtra("navigate_to")
                    initialScreen = when {
                        !completed -> Screen.Onboarding
                        navigateTo == "stats" -> Screen.Stats.Main
                        else -> Screen.Timer
                    }
                }

                if (initialScreen != null) {
                    val timerFrequency = if (settingsState.highRefreshRate) 120f else 60f
                    AppScreen(
                        initialScreen = initialScreen!!,
                        isPlus = isPlus,
                        isAODEnabled = settingsState.aodEnabled,
                        setTimerFrequency = {
                            appContainer.stateRepository.timerFrequency = it
                        }
                    )
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        // Reduce the timer loop frequency when not visible to save battery
        appContainer.stateRepository.timerFrequency = 1f
    }

    override fun onStart() {
        super.onStart()
        // Increase the timer loop frequency again when visible to make the progress smoother
        val settingsState = appContainer.stateRepository.settingsState.value
        val timerFrequency = if (settingsState.highRefreshRate) 120f else 60f
        appContainer.stateRepository.timerFrequency = timerFrequency
    }
}
