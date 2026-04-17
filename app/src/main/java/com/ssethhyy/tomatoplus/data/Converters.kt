/*
 * Copyright (c) 2025-2026 ssethhyy
 *
 * This file is part of Tomato+ - a minimalist pomodoro timer for Android.
 *
 * Tomato+ is free software: you can redistribute it and/or modify it under the terms of the MIT License.
 * See the LICENSE file in the project root for more information.
 */

package com.ssethhyy.tomatoplus.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun localDateToString(localDate: LocalDate?): String? {
        return localDate?.toString()
    }

    @TypeConverter
    fun stringToLocalDate(date: String?): LocalDate? {
        return if (date != null) LocalDate.parse(date) else null
    }
}
