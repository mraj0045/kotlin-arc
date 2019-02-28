package com.arc.kotlin.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

abstract class BasePreferenceManager(private val context: Context, private val preferenceName: String) {

    /**
     * Creates Shared Preference object from the Context name
     */
    protected val preference: SharedPreferences
        get() = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    /**
     * Creates Shared Preference Editor object for editing preference values
     */
    protected val editor: SharedPreferences.Editor
        @SuppressLint("CommitPrefEdits") get() = preference.edit()

    protected abstract fun clear()
}