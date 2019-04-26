package com.arc.kotlin.util.extensions

import android.util.Patterns
import com.arc.kotlin.util.Security

/** Checks whether the email is valid or not*/
fun String?.isLegalEmail(): Boolean = this != null && Patterns.EMAIL_ADDRESS.matcher(this).matches()

/** Checks whether the password is legal password or not.
 *
 * Password should include at-least one Uppercase letter and number, max length 16 and min length 8*/
fun String?.isLegalPassword(): Boolean = this != null
        && this.trim().isNotEmpty()
        && matches(Regex(".*[A-Z].*"))
        && matches(Regex(".*[a-z].*"))
        && matches(Regex(".*[0-9].*"))
        && length >= 8
        && length < 17

/**
 * Creates SHA1 for passed value
 */
fun String.sha1(): String = Security.SHA1(this)