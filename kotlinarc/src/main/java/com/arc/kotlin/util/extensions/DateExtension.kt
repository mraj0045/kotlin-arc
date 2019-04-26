package com.arc.kotlin.util.extensions

import com.arc.kotlin.util.formatter.DateFormatter
import java.util.*

/**Formats the date with the given pattern and returns string
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun Date.format(pattern: String): String = DateFormatter().format(this, pattern)

/**Formats the String to Date Object
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.format(pattern: String): Date? = DateFormatter().format(this, pattern)

/**Formats the String to specified pattern
 * @param fromPattern Pattern to format(e.g, yyyy-MM-dd)
 * @param toPattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.format(fromPattern: String, toPattern: String): String =
    DateFormatter().format(this, fromPattern, toPattern)

/**Formats the date with the given pattern and returns string. With TimeZone Id
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun Date.formatTz(timeZoneId: String, pattern: String): String =
    DateFormatter(timeZoneId).format(this, pattern)

/**Formats the String to Date Object. With TimeZone Id
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.formatTz(timeZoneId: String, pattern: String): Date? =
    DateFormatter(timeZoneId).format(this, pattern)

/**Formats the String to specified pattern. With TimeZone Id
 * @param fromPattern Pattern to format(e.g, yyyy-MM-dd)
 * @param toPattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.formatTz(timeZoneId: String, fromPattern: String, toPattern: String): String =
    DateFormatter(timeZoneId).format(this, fromPattern, toPattern)
