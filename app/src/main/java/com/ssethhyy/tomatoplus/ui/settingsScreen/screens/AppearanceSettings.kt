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

package com.ssethhyy.tomatoplus.ui.settingsScreen.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssethhyy.tomatoplus.R
import com.ssethhyy.tomatoplus.ui.mergePaddingValues
import com.ssethhyy.tomatoplus.ui.settingsScreen.SettingsSwitchItem
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.ColorSchemePickerListItem
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.PlusDivider
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.ThemePickerListItem
import com.ssethhyy.tomatoplus.ui.settingsScreen.viewModel.SettingsAction
import com.ssethhyy.tomatoplus.ui.settingsScreen.viewModel.SettingsState
import com.ssethhyy.tomatoplus.ui.theme.AppFonts.robotoFlexTopBar
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.listItemColors
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.switchColors
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.topBarColors
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusShapeDefaults.bottomListItemShape
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusTheme
import com.ssethhyy.tomatoplus.utils.toColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppearanceSettings(
    settingsState: SettingsState,
    contentPadding: PaddingValues,
    isPlus: Boolean,
    onAction: (SettingsAction) -> Unit,
    setShowPaywall: (Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(stringResource(R.string.appearance), fontFamily = robotoFlexTopBar)
                },
                subtitle = {
                    Text(stringResource(R.string.settings))
                },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = onBack,
                        shapes = IconButtonDefaults.shapes(),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = listItemColors.containerColor)
                    ) {
                        Icon(
                            painterResource(R.drawable.arrow_back),
                            null
                        )
                    }
                },
                colors = topBarColors,
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
            item {
                Spacer(Modifier.height(14.dp))
            }
            item {
                ThemePickerListItem(
                    theme = settingsState.theme,
                    onThemeChange = { onAction(SettingsAction.SaveTheme(it)) },
                    items = if (isPlus) 3 else 1,
                    index = 0
                )
            }

            if (!isPlus) {
                item { PlusDivider(setShowPaywall) }
            }

            item {
                ColorSchemePickerListItem(
                    color = settingsState.colorScheme.toColor(),
                    items = 3,
                    index = if (isPlus) 1 else 0,
                    isPlus = isPlus,
                    onColorChange = { onAction(SettingsAction.SaveColorScheme(it)) },
                )
            }
            item {
                val item = SettingsSwitchItem(
                    checked = settingsState.blackTheme,
                    icon = R.drawable.contrast,
                    label = R.string.black_theme,
                    description = R.string.black_theme_desc,
                    onClick = { onAction(SettingsAction.SaveBlackTheme(it)) }
                )
                ListItem(
                    leadingContent = {
                        Icon(painterResource(item.icon), contentDescription = null)
                    },
                    headlineContent = { Text(stringResource(item.label)) },
                    supportingContent = { Text(stringResource(item.description)) },
                    trailingContent = {
                        Switch(
                            checked = item.checked,
                            onCheckedChange = { item.onClick(it) },
                            enabled = isPlus,
                            thumbContent = {
                                if (item.checked) {
                                    Icon(
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.clear),
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            },
                            colors = switchColors
                        )
                    },
                    colors = listItemColors
                )
            }
            item {
                val item = SettingsSwitchItem(
                    checked = settingsState.highRefreshRate,
                    icon = R.drawable.autoplay,
                    label = R.string.high_refresh_rate,
                    description = R.string.high_refresh_rate_desc,
                    onClick = { onAction(SettingsAction.SaveHighRefreshRate(it)) }
                )
                ListItem(
                    leadingContent = {
                        Icon(painterResource(item.icon), contentDescription = null)
                    },
                    headlineContent = { Text(stringResource(item.label)) },
                    supportingContent = { Text(stringResource(item.description)) },
                    trailingContent = {
                        Switch(
                            checked = item.checked,
                            onCheckedChange = { item.onClick(it) },
                            enabled = isPlus,
                            thumbContent = {
                                if (item.checked) {
                                    Icon(
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.clear),
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            },
                            colors = switchColors
                        )
                    },
                    colors = listItemColors,
                    modifier = Modifier.clip(bottomListItemShape)
                )
            }

            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

@Preview
@Composable
fun AppearanceSettingsPreview() {
    val settingsState = SettingsState()
    TomatoPlusTheme(dynamicColor = false) {
        AppearanceSettings(
            settingsState = settingsState,
            contentPadding = PaddingValues(),
            isPlus = false,
            onAction = {},
            setShowPaywall = {},
            onBack = {}
        )
    }
}
