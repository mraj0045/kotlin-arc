package com.arc.kotlin.callback

import android.text.Editable
import android.text.TextWatcher

class SimpleTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        // Called when text changed.
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Called before text changed.
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Called while text changed.
    }
}