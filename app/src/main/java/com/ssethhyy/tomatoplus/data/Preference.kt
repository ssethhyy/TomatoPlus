/*
 * Copyright (c) 2025-2026 ssethhyy
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 *
 * Tomato+ is free software: you can redistribute it and/or modify it under the terms of the MIT License.
 * See the LICENSE file in the project root for more information.
 */

package com.ssethhyy.tomatoplus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

import kotlinx.serialization.Serializable

/**
 * Class for storing boolean preferences in the app's database
 */
@Entity(tableName = "boolean_preference")
@Serializable
data class BooleanPreference(
    @PrimaryKey
    val key: String,
    val value: Boolean
)

/**
 * Class for storing integer preferences in the app's database
 */
@Entity(tableName = "int_preference")
@Serializable
data class IntPreference(
    @PrimaryKey
    val key: String,
    val value: Int
)

/**
 * Class for storing string preferences in the app's database
 */
@Entity(tableName = "string_preference")
@Serializable
data class StringPreference(
    @PrimaryKey
    val key: String,
    val value: String
)
