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

package com.ssethhyy.tomatoplus.ui.statsScreen.screens

import android.graphics.Typeface
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.ssethhyy.tomatoplus.BuildConfig
import com.ssethhyy.tomatoplus.R
import com.ssethhyy.tomatoplus.data.Stat
import com.ssethhyy.tomatoplus.ui.Screen
import com.ssethhyy.tomatoplus.ui.mergePaddingValues
import com.ssethhyy.tomatoplus.ui.statsScreen.components.TimeColumnChart
import com.ssethhyy.tomatoplus.ui.statsScreen.components.TimeLineChart
import com.ssethhyy.tomatoplus.ui.statsScreen.components.sharedBoundsReveal
import com.ssethhyy.tomatoplus.ui.theme.AppFonts.robotoFlexTopBar
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.listItemColors
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.topBarColors
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusShapeDefaults.bottomListItemShape
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusShapeDefaults.middleListItemShape
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusShapeDefaults.topListItemShape
import com.ssethhyy.tomatoplus.utils.millisecondsToHoursMinutes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedTransitionScope.StatsMainScreen(
    contentPadding: PaddingValues,
    lastWeekSummaryChartData: Pair<CartesianChartModelProducer, ExtraStore.Key<List<String>>>,
    lastMonthSummaryChartData: Pair<CartesianChartModelProducer, ExtraStore.Key<List<String>>>,
    lastYearSummaryChartData: Pair<CartesianChartModelProducer, ExtraStore.Key<List<String>>>,
    todayStat: Stat?,
    allTimeTotalFocus: Long?,
    lastWeekAverageFocusTimes: List<Long>,
    lastMonthAverageFocusTimes: List<Long>,
    lastYearAverageFocusTimes: List<Long>,
    generateSampleData: () -> Unit,
    hoursMinutesFormat: String,
    hoursFormat: String,
    minutesFormat: String,
    axisTypeface: Typeface,
    markerTypeface: Typeface,
    onNavigate: (Screen.Stats) -> Unit,
    onManualLog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.stats),
                        style = LocalTextStyle.current.copy(
                            fontFamily = robotoFlexTopBar,
                            fontSize = 32.sp,
                            lineHeight = 32.sp
                        ),
                        modifier = Modifier
                            .padding(top = contentPadding.calculateTopPadding())
                            .padding(vertical = 14.dp)
                    )
                },
                actions = if (BuildConfig.DEBUG) {
                    {
                        IconButton(
                            onClick = generateSampleData
                        ) {
                            Spacer(Modifier.size(24.dp))
                        }
                    }
                } else {
                    {}
                },
                subtitle = {},
                titleHoriTomato+talAlignment = Alignment.CenterHoriTomato+tally,
                scrollBehavior = scrollBehavior,
                colors = topBarColors,
                windowInsets = WindowInsets()
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val insets = mergePaddingValues(innerPadding, contentPadding)
        LazyColumn(
            contentPadding = insets,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .background(topBarColors.containerColor)
                .padding(horiTomato+tal = 16.dp)
        ) {
            item { Spacer(Modifier.height(14.dp)) }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horiTomato+talArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horiTomato+tal = 16.dp)
                ) {
                    Text(
                        stringResource(R.string.today),
                        style = typography.headlineSmall
                    )
                    Text(
                        "+ Add Manually",
                        style = typography.labelMedium,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .clip(shapes.small)
                            .clickable { onManualLog() }
                            .padding(horiTomato+tal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            item {
                Row {
                    Box(
                        modifier = Modifier
                            .background(
                                colorScheme.primaryContainer,
                                shapes.largeIncreased
                            )
                            .weight(1f)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                stringResource(R.string.focus),
                                style = typography.titleMedium,
                                color = colorScheme.onPrimaryContainer
                            )
                            Text(
                                remember(todayStat) {
                                    millisecondsToHoursMinutes(
                                        todayStat?.totalFocusTime() ?: 0,
                                        hoursMinutesFormat
                                    )
                                },
                                style = typography.displaySmall,
                                color = colorScheme.onPrimaryContainer,
                                maxLines = 1,
                                autoSize = TextAutoSize.StepBased(maxFontSize = typography.displaySmall.fontSize)
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                colorScheme.tertiaryContainer,
                                shapes.largeIncreased
                            )
                            .weight(1f)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                stringResource(R.string.break_),
                                style = typography.titleMedium,
                                color = colorScheme.onTertiaryContainer
                            )
                            Text(
                                remember(todayStat) {
                                    millisecondsToHoursMinutes(
                                        todayStat?.breakTime ?: 0,
                                        hoursMinutesFormat
                                    )
                                },
                                style = typography.displaySmall,
                                color = colorScheme.onTertiaryContainer,
                                maxLines = 1,
                                autoSize = TextAutoSize.StepBased(maxFontSize = typography.displaySmall.fontSize)
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .sharedBoundsReveal(
                            sharedTransitionScope = this@StatsMainScreen,
                            sharedContentState = this@StatsMainScreen.rememberSharedContentState(
                                "last week card"
                            ),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            clipShape = topListItemShape
                        )
                        .clip(topListItemShape)
                        .background(listItemColors.containerColor)
                        .clickable { onNavigate(Screen.Stats.LastWeek) }
                        .padding(
                            start = 20.dp,
                            top = 20.dp,
                            bottom = 20.dp
                        ) // end = 0 to let the chart touch the end
                ) {
                    Text(
                        stringResource(R.string.last_week),
                        style = typography.headlineSmall,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = this@StatsMainScreen
                                .rememberSharedContentState("last week heading"),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horiTomato+talArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            millisecondsToHoursMinutes(
                                remember(lastWeekAverageFocusTimes) {
                                    lastWeekAverageFocusTimes.sum()
                                },
                                hoursMinutesFormat
                            ),
                            style = typography.displaySmall,
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = this@StatsMainScreen
                                        .rememberSharedContentState("last week average focus timer"),
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                                )
                        )
                        Text(
                            stringResource(R.string.focus_per_day_avg),
                            style = typography.titleSmall,
                            modifier = Modifier
                                .padding(bottom = 5.2.dp)
                                .sharedElement(
                                    sharedContentState = this@StatsMainScreen
                                        .rememberSharedContentState("focus per day average (week)"),
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                                )
                        )
                    }

                    TimeColumnChart(
                        modelProducer = lastWeekSummaryChartData.first,
                        hoursFormat = hoursFormat,
                        hoursMinutesFormat = hoursMinutesFormat,
                        minutesFormat = minutesFormat,
                        axisTypeface = axisTypeface,
                        markerTypeface = markerTypeface,
                        xValueFormatter = CartesianValueFormatter { context, x, _ ->
                            context.model.extraStore[lastWeekSummaryChartData.second][x.toInt()]
                        },
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = this@StatsMainScreen
                                    .rememberSharedContentState("last week chart"),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current
                            )
                    )
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .sharedBoundsReveal(
                            sharedTransitionScope = this@StatsMainScreen,
                            sharedContentState = this@StatsMainScreen.rememberSharedContentState(
                                "last month card"
                            ),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            clipShape = middleListItemShape
                        )
                        .clip(middleListItemShape)
                        .background(listItemColors.containerColor)
                        .clickable { onNavigate(Screen.Stats.LastMonth) }
                        .padding(
                            start = 20.dp,
                            top = 20.dp,
                            bottom = 20.dp
                        ) // end = 0 to let the chart touch the end
                ) {
                    Text(
                        stringResource(R.string.last_month),
                        style = typography.headlineSmall,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = this@StatsMainScreen
                                .rememberSharedContentState("last month heading"),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horiTomato+talArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            millisecondsToHoursMinutes(
                                remember(lastMonthAverageFocusTimes) {
                                    lastMonthAverageFocusTimes.sum()
                                },
                                hoursMinutesFormat
                            ),
                            style = typography.displaySmall,
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = this@StatsMainScreen
                                        .rememberSharedContentState("last month average focus timer"),
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                                )
                        )
                        Text(
                            text = stringResource(R.string.focus_per_day_avg),
                            style = typography.titleSmall,
                            modifier = Modifier
                                .padding(bottom = 5.2.dp)
                                .sharedElement(
                                    sharedContentState = this@StatsMainScreen
                                        .rememberSharedContentState("focus per day average (month)"),
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                                )
                        )
                    }

                    TimeColumnChart(
                        modelProducer = lastMonthSummaryChartData.first,
                        hoursFormat = hoursFormat,
                        hoursMinutesFormat = hoursMinutesFormat,
                        minutesFormat = minutesFormat,
                        axisTypeface = axisTypeface,
                        markerTypeface = markerTypeface,
                        thickness = 8.dp,
                        xValueFormatter = CartesianValueFormatter { context, x, _ ->
                            context.model.extraStore[lastMonthSummaryChartData.second][x.toInt()]
                        },
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = this@StatsMainScreen
                                    .rememberSharedContentState("last month chart"),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current
                            )
                    )
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .sharedBoundsReveal(
                            sharedTransitionScope = this@StatsMainScreen,
                            sharedContentState = this@StatsMainScreen.rememberSharedContentState(
                                "last year card"
                            ),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            clipShape = bottomListItemShape
                        )
                        .clip(bottomListItemShape)
                        .background(listItemColors.containerColor)
                        .clickable { onNavigate(Screen.Stats.LastYear) }
                        .padding(
                            start = 20.dp,
                            top = 20.dp,
                            bottom = 20.dp
                        ) // end = 0 to let the chart touch the end
                ) {
                    Text(
                        stringResource(R.string.last_year),
                        style = typography.headlineSmall,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = this@StatsMainScreen
                                .rememberSharedContentState("last year heading"),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horiTomato+talArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            millisecondsToHoursMinutes(
                                remember(lastYearAverageFocusTimes) {
                                    lastYearAverageFocusTimes.sum()
                                },
                                hoursMinutesFormat
                            ),
                            style = typography.displaySmall,
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = this@StatsMainScreen
                                        .rememberSharedContentState("last year average focus timer"),
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                                )
                        )
                        Text(
                            text = stringResource(R.string.focus_per_day_avg),
                            style = typography.titleSmall,
                            modifier = Modifier
                                .padding(bottom = 5.2.dp)
                                .sharedElement(
                                    sharedContentState = this@StatsMainScreen
                                        .rememberSharedContentState("focus per day average (year)"),
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                                )
                        )
                    }

                    TimeLineChart(
                        modelProducer = lastYearSummaryChartData.first,
                        hoursFormat = hoursFormat,
                        hoursMinutesFormat = hoursMinutesFormat,
                        minutesFormat = minutesFormat,
                        axisTypeface = axisTypeface,
                        markerTypeface = markerTypeface,
                        xValueFormatter = CartesianValueFormatter { context, x, _ ->
                            context.model.extraStore[lastYearSummaryChartData.second][x.toInt()]
                        },
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = this@StatsMainScreen
                                    .rememberSharedContentState("last year chart"),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current
                            )
                    )
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // Lifetime Total Focus Card
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shapes.largeIncreased)
                        .background(listItemColors.containerColor)
                        .padding(20.dp)
                ) {
                    Text(
                        stringResource(R.string.lifetime),
                        style = typography.headlineSmall,
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horiTomato+talArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            millisecondsToHoursMinutes(
                                allTimeTotalFocus ?: 0L,
                                hoursMinutesFormat
                            ),
                            style = typography.displaySmall
                        )
                        Text(
                            stringResource(R.string.lifetime_total),
                            style = typography.titleSmall,
                            modifier = Modifier.padding(bottom = 5.2.dp)
                        )
                    }
                }
            }
        }
    }
}
