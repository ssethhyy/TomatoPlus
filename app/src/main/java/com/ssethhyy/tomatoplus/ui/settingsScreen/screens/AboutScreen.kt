/*
 * Copyright (c) 2026 ssethhyy
 * Copyright (c) 2025-2026 Vrushal (modifications)
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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.ssethhyy.tomatoplus.BuildConfig
import com.ssethhyy.tomatoplus.R
import com.ssethhyy.tomatoplus.ui.mergePaddingValues
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.ClickableListItem
import com.ssethhyy.tomatoplus.ui.settingsScreen.components.LicenseBottomSheet
import com.ssethhyy.tomatoplus.ui.theme.AppFonts.googleFlex600
import com.ssethhyy.tomatoplus.ui.theme.AppFonts.robotoFlexTopBar
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.listItemColors
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.topBarColors
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusShapeDefaults.bottomListItemShape
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusShapeDefaults.topListItemShape
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutScreen(
    contentPadding: PaddingValues,
    isPlus: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uriHandler = LocalUriHandler.current

    val socialLinks = remember {
        listOf(
            SocialLink(R.drawable.github, "https://github.com/ssethhyy")
        )
    }

    var showLicense by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(stringResource(R.string.about), fontFamily = robotoFlexTopBar)
                },
                subtitle = {
                    Text(stringResource(R.string.app_name))
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
                Box(Modifier.background(listItemColors.containerColor, topListItemShape)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.timer_filled),
                            tint = colorScheme.onPrimaryContainer,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    colorScheme.primaryContainer,
                                    MaterialShapes.Cookie12Sided.toShape()
                                )
                                .padding(12.dp) // Added padding for better look inside the shape
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                if (!isPlus) stringResource(R.string.app_name)
                                else stringResource(R.string.app_name_plus),
                                color = colorScheme.onSurface,
                                style = typography.titleLarge,
                                fontFamily = googleFlex600
                            )
                            Text(
                                text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                                style = typography.labelLarge,
                                color = colorScheme.primary
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        Row(horiTomato+talArrangement = Arrangement.spacedBy(2.dp)) {
                            FilledTonalIconButton(
                                onClick = { uriHandler.openUri("https://github.com/ssethhyy") },
                                shapes = IconButtonDefaults.shapes()
                            ) {
                                Icon(
                                    painterResource(R.drawable.github),
                                    contentDescription = "GitHub",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
            item {
                Box(Modifier.background(listItemColors.containerColor, bottomListItemShape)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = "https://github.com/ssethhyy.png",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(MaterialShapes.Square.toShape())
                            )

                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    "ssethhyy", // Updated Name
                                    style = typography.titleLarge,
                                    color = colorScheme.onSurface,
                                    fontFamily = googleFlex600
                                )
                                Text(
                                    "Developer",
                                    style = typography.labelLarge,
                                    color = colorScheme.secondary
                                )
                            }
                            Spacer(Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(8.dp))
                        Row {
                            Spacer(Modifier.width((64 + 16).dp))
                            FlowRow(horiTomato+talArrangement = Arrangement.spacedBy(8.dp)) {
                                socialLinks.fastForEach {
                                    FilledTonalIconButton(
                                        onClick = { uriHandler.openUri(it.url) },
                                        shapes = IconButtonDefaults.shapes(),
                                        modifier = Modifier.width(52.dp)
                                    ) {
                                        Icon(
                                            painterResource(it.icon),
                                            null,
                                            modifier = Modifier.size(ButtonDefaults.SmallIconSize)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(12.dp)) }

            // Removed TopButton and BottomButton (BMC and Translate)

            item { Spacer(Modifier.height(12.dp)) }

            item {
                ClickableListItem(
                    leadingContent = { Icon(painterResource(R.drawable.gavel), null) },
                    headlineContent = { Text(stringResource(R.string.license)) },
                    supportingContent = { Text("MIT License") },
                    items = 1,
                    index = 0
                ) { showLicense = true }
            }
        }
    }

    if (showLicense) {
        LicenseBottomSheet({ showLicense = false })
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    TomatoPlusTheme(dynamicColor = false) {
        AboutScreen(
            contentPadding = PaddingValues(),
            isPlus = true,
            onBack = {}
        )
    }
}

data class SocialLink(
    @param:DrawableRes val icon: Int,
    val url: String
)
