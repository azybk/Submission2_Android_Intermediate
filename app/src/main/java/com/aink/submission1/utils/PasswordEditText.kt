package com.aink.submission1.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText: AppCompatEditText {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val errorMessage = util.checkPasswordLength(s.toString())
                if (!errorMessage.isEmpty()) {
                    this@PasswordEditText.error = errorMessage
                }

            }

            override fun afterTextChanged(s: Editable) { }

        })
    }

}