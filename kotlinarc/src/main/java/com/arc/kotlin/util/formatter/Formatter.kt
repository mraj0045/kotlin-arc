package com.arc.kotlin.util.formatter

import androidx.annotation.NonNull
import java.util.*

interface Formatter {
    /**
     * Formats the given date object to the specified pattern
     *
     * @param date {@link Date} object
     * @param pattern Pattern to format(e.g, yyyy-MM-dd)
     */
    fun format(@NonNull date: Date?, @NonNull pattern: String): String

    /**
     * Formats the given date object to the specified pattern
     *
     * @param date string date value
     * @param pattern Pattern to which the date follows(e.g, yyyy-MM-dd)
     */
    fun format(@NonNull date: String, @NonNull pattern: String): Date?

    /**
     * Formats the given date object to the specified pattern
     *
     * @param date string date value
     * @param fromPattern Pattern to which the date follows(e.g, yyyy-MM-dd)
     * @param toPattern Pattern to format(e.g, yyyy-MM-dd)
     */
    fun format(@NonNull date: String, @NonNull fromPattern: String, @NonNull toPattern: String): String

}