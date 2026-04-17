/*
 * Copyright (c) 2026 ssethhyy
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 *
 * Tomato+ is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tomato+ is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the MIT License along with Tomato+.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.ssethhyy.tomatoplus.ui.settingsScreen

import android.annotation.SuppressLint
import android.widget.Toast
import android.app.LocaleManager
import android.os.Build
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHoriTomato+tally
import androidx.compose.animation.slideOutHoriTomato+tally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ssethhyy.tomatoplus.BuildConfig
import com.ssethhyy.tomatoplus.R
import com.ssethhyy.tomatoplus.ui.Screen
import com.ssethhyy.tomatoplus.ui.mergePaddingValues
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.ClickableListItem
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.LocaleBottomSheet
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.NowBarBottomSheet
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.WhatsNewBottomSheet
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.PlusPromo
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.SwitchListItem
import com.ssethhyy.tomatoplus.ui.settingsScreen.screens.AboutScreen
import com.ssethhyy.tomatoplus.ui.settingsScreen.screens.AlarmSettings
import com.ssethhyy.tomatoplus.ui.settingsScreen.screens.AppearanceSettings
import com.ssethhyy.tomatoplus.ui.settingsScreen.screens.MusicSettings
import com.ssethhyy.tomatoplus.ui.settingsScreen.screens.TimerSettings
import com.ssethhyy.tomatoplus.ui.settingsScreen.viewModel.SettingsAction
import com.ssethhyy.tomatoplus.ui.settingsScreen.viewModel.SettingsState
import com.ssethhyy.tomatoplus.ui.settingsScreen.viewModel.SettingsViewModel
import com.ssethhyy.tomatoplus.ui.settingsScreens
import com.ssethhyy.tomatoplus.ui.theme.AppFonts.robotoFlexTopBar
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.listItemColors
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.topBarColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenRoot(
    setShowPaywall: (Boolean) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
) {
    val context = LocalContext.current
    val backStack = viewModel.backStack

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(Unit) {
        viewModel.runTextFieldFlowCollection()
        onDispose { viewModel.cancelTextFieldFlowCollection() }
    }

    val focusTimeInputFieldState = viewModel.focusTimeTextFieldState
    val shortBreakTimeInputFieldState = viewModel.shortBreakTimeTextFieldState
    val longBreakTimeInputFieldState = viewModel.longBreakTimeTextFieldState

    val isPlus by viewModel.isPlus.collectAsStateWithLifecycle()
    val serviceRunning by viewModel.serviceRunning.collectAsStateWithLifecycle()

    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

    val sessionsSliderState = rememberSaveable(
        saver = SliderState.Saver(
            viewModel.sessionsSliderState.onValueChangeFinished,
            viewModel.sessionsSliderState.valueRange
        )
    ) {
        viewModel.sessionsSliderState
    }

    SettingsScreen(
        isPlus = isPlus,
        serviceRunning = serviceRunning,
        settingsState = settingsState,
        backStack = backStack,
        contentPadding = contentPadding,
        focusTimeInputFieldState = focusTimeInputFieldState,
        shortBreakTimeInputFieldState = shortBreakTimeInputFieldState,
        longBreakTimeInputFieldState = longBreakTimeInputFieldState,
        sessionsSliderState = sessionsSliderState,
        onAction = viewModel::onAction,
        setShowPaywall = setShowPaywall,
        modifier = modifier
    )
}

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SettingsScreen(
    isPlus: Boolean,
    serviceRunning: Boolean,
    settingsState: SettingsState,
    backStack: SnapshotStateList<Screen.Settings>,
    contentPadding: PaddingValues,
    focusTimeInputFieldState: TextFieldState,
    shortBreakTimeInputFieldState: TextFieldState,
    longBreakTimeInputFieldState: TextFieldState,
    sessionsSliderState: SliderState,
    onAction: (SettingsAction) -> Unit,
    setShowPaywall: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val currentLocales =
        if (Build.VERSION.SDK_INT >= 33) {
            context
                .getSystemService(LocaleManager::class.java)
                .applicationLocales
        } else null
    val currentLocalesSize = currentLocales?.size() ?: 0

    var showLocaleSheet by remember { mutableStateOf(false) }
    var showNowBarSheet by remember { mutableStateOf(false) }
    var showWhatsNewSheet by remember { mutableStateOf(false) }

    if (showLocaleSheet && currentLocales != null)
        LocaleBottomSheet(
            currentLocales = currentLocales,
            setShowSheet = { showLocaleSheet = it }
        )
    
    if (showNowBarSheet)
        NowBarBottomSheet(onDismiss = { showNowBarSheet = false })
    
    if (showWhatsNewSheet)
        WhatsNewBottomSheet(onDismiss = { showWhatsNewSheet = false })

    if (settingsState.isShowingEraseDataDialog) {
        ResetDataDialog(
            resetData = { onAction(SettingsAction.EraseData) },
            onDismiss = { onAction(SettingsAction.CancelEraseData) }
        )
    }

    NavDisplay(
        backStack = backStack,
        onBack = backStack::removeLastOrNull,
        transitionSpec = {
            (slideInHoriTomato+tally(initialOffsetX = { it }))
                .togetherWith(slideOutHoriTomato+tally(targetOffsetX = { -it / 4 }) + fadeOut())
        },
        popTransitionSpec = {
            (slideInHoriTomato+tally(initialOffsetX = { -it / 4 }) + fadeIn())
                .togetherWith(slideOutHoriTomato+tally(targetOffsetX = { it }))
        },
        predictivePopTransitionSpec = {
            (slideInHoriTomato+tally(initialOffsetX = { -it / 4 }) + fadeIn())
                .togetherWith(slideOutHoriTomato+tally(targetOffsetX = { it }))
        },
        entryProvider = entryProvider {
            entry<Screen.Settings.Main> {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    stringResource(R.string.settings),
                                    style = LocalTextStyle.current.copy(
                                        fontFamily = robotoFlexTopBar,
                                        fontSize = 32.sp,
                                        lineHeight = 32.sp
                                    )
                                )
                            },
                            subtitle = {},
                            colors = topBarColors,
                            titleHoriTomato+talAlignment = Alignment.CenterHoriTomato+tally,
                            scrollBehavior = scrollBehavior
                        )
                    },
                    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                ) { innerPadding ->
                    val insets = mergePaddingValues(innerPadding, contentPadding)
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        contentPadding = insets,
                        modifier = Modifier
                            .background(topBarColors.containerColor)
                            .fillMaxSize()
                            .padding(horiTomato+tal = 16.dp)
                    ) {
                        item { Spacer(Modifier.height(14.dp)) }

                        item {
                            PlusPromo()
                            
                            // Focus Sounds - links to dedicated Music Settings screen
                            ClickableListItem(
                                headlineContent = { Text(stringResource(R.string.focus_sounds)) },
                                leadingContent = {
                                    Icon(
                                        painterResource(R.drawable.music_note),
                                        null
                                    )
                                },
                                supportingContent = { 
                                    Text(if (settingsState.isMusicEnabled) "Enabled" else "Disabled") 
                                },
                                trailingContent = {
                                    Icon(painterResource(R.drawable.arrow_forward_big), null)
                                },
                                items = 3,
                                index = 0
                            ) { backStack.add(Screen.Settings.Music) }
                        }

                        item {
                            // About item
                            ClickableListItem(
                                leadingContent = {
                                    Icon(painterResource(R.drawable.info), null)
                                },
                                headlineContent = {
                                    Text(stringResource(R.string.about))
                                },
                                supportingContent = {
                                    Text(stringResource(R.string.app_name) + " ${BuildConfig.VERSION_NAME}")
                                },
                                trailingContent = {
                                    Icon(painterResource(R.drawable.arrow_forward_big), null)
                                },
                                items = 3,
                                index = 1
                            ) { backStack.add(Screen.Settings.About) }
                        }

                        item {
                            // What's New item
                            ClickableListItem(
                                leadingContent = {
                                    Icon(painterResource(R.drawable.new_releases), null)
                                },
                                headlineContent = {
                                    Text(stringResource(R.string.whats_new))
                                },
                                supportingContent = {
                                    Text("Version ${BuildConfig.VERSION_NAME}")
                                },
                                items = 3,
                                index = 2
                            ) { showWhatsNewSheet = true }
                        }

                        item { Spacer(Modifier.height(12.dp)) }

                        itemsIndexed(settingsScreens) { index, item ->
                            ClickableListItem(
                                leadingContent = {
                                    Icon(painterResource(item.icon), null)
                                },
                                headlineContent = { Text(stringResource(item.label)) },
                                supportingContent = {
                                    Text(
                                        remember {
                                            item.innerSettings.joinToString(", ") {
                                                context.getString(it)
                                            }
                                        },
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                trailingContent = {
                                    Icon(painterResource(R.drawable.arrow_forward_big), null)
                                },
                                items = settingsScreens.size,
                                index = index
                            ) { backStack.add(item.route) }
                        }

                        item { Spacer(Modifier.height(12.dp)) }

                        if (currentLocales != null)
                            item {
                                ClickableListItem(
                                    leadingContent = {
                                        Icon(
                                            painterResource(R.drawable.language),
                                            contentDescription = null
                                        )
                                    },
                                    headlineContent = { Text(stringResource(R.string.language)) },
                                    supportingContent = {
                                        Text(
                                            if (currentLocalesSize > 0) currentLocales.get(0).displayName
                                            else stringResource(R.string.system_default)
                                        )
                                    },
                                    colors = listItemColors,
                                    items = 1,
                                    index = 0
                                ) { showLocaleSheet = true }
                            }

                        if (Build.VERSION.SDK_INT >= 36 && Build.MANUFACTURER == "samsung") {
                            item {
                                Spacer(Modifier.height(14.dp))
                                ClickableListItem(
                                    leadingContent = {
                                        Icon(
                                            painterResource(R.drawable.mobile_text),
                                            null
                                        )
                                    },
                                    headlineContent = { Text(stringResource(R.string.now_bar)) },
                                    trailingContent = {
                                        Icon(
                                            painterResource(R.drawable.info),
                                            null
                                        )
                                    },
                                    items = 1,
                                    index = 0
                                ) { showNowBarSheet = true }
                            }
                        }

                        item { Spacer(Modifier.height(12.dp)) }

                        item {
                            val exportLauncher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.CreateDocument("application/json")
                            ) { uri ->
                                uri?.let {
                                    onAction(SettingsAction.ExportData(it))
                                }
                            }

                            ClickableListItem(
                                leadingContent = {
                                    Icon(painterResource(R.drawable.ic_backup), null)
                                },
                                headlineContent = { Text("Backup Data") },
                                supportingContent = { Text("Export your data to a JSON file") },
                                items = 2,
                                index = 0
                            ) {
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
                                val fileName = "tomato_plus_backup_${LocalDateTime.now().format(formatter)}.json"
                                exportLauncher.launch(fileName)
                            }
                        }

                        item {
                            val importLauncher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.OpenDocument()
                            ) { uri ->
                                uri?.let {
                                    onAction(SettingsAction.ImportData(it))
                                }
                            }

                            ClickableListItem(
                                leadingContent = {
                                    Icon(painterResource(R.drawable.ic_restore), null)
                                },
                                headlineContent = { Text("Restore Data") },
                                supportingContent = { Text("Import data from a JSON file") },
                                items = 2,
                                index = 1
                            ) {
                                importLauncher.launch(arrayOf("application/json"))
                            }
                        }

                        item { Spacer(Modifier.height(12.dp)) }

                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(
                                    onClick = { onAction(SettingsAction.AskEraseData) },
                                ) {
                                    Text(stringResource(R.string.reset_data))
                                }
                            }
                        }
                    }
                }
            }

            entry<Screen.Settings.About> {
                AboutScreen(
                    contentPadding = contentPadding,
                    isPlus = isPlus,
                    onBack = backStack::removeLastOrNull
                )
            }

            entry<Screen.Settings.Alarm> {
                AlarmSettings(
                    settingsState = settingsState,
                    contentPadding = contentPadding,
                    onAction = onAction,
                    onBack = backStack::removeLastOrNull,
                    modifier = modifier,
                )
            }
            entry<Screen.Settings.Appearance> {
                AppearanceSettings(
                    settingsState = settingsState,
                    contentPadding = contentPadding,
                    isPlus = isPlus,
                    onAction = onAction,
                    setShowPaywall = setShowPaywall,
                    onBack = backStack::removeLastOrNull,
                    modifier = modifier,
                )
            }
            entry<Screen.Settings.Timer> {
                TimerSettings(
                    isPlus = isPlus,
                    serviceRunning = serviceRunning,
                    settingsState = settingsState,
                    contentPadding = contentPadding,
                    focusTimeInputFieldState = focusTimeInputFieldState,
                    shortBreakTimeInputFieldState = shortBreakTimeInputFieldState,
                    longBreakTimeInputFieldState = longBreakTimeInputFieldState,
                    sessionsSliderState = sessionsSliderState,
                    onAction = onAction,
                    setShowPaywall = setShowPaywall,
                    onBack = backStack::removeLastOrNull,
                    modifier = modifier,
                )
            }
            entry<Screen.Settings.Music> {
                MusicSettings(
                    settingsState = settingsState,
                    contentPadding = contentPadding,
                    onAction = onAction,
                    onBack = backStack::removeLastOrNull,
                    modifier = modifier,
                )
            }
        }
    )
}
