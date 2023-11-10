package com.dicoding.storyapp.view.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.storyapp.R


class CustomPasswordTv : AppCompatEditText {
    private lateinit var icError: Drawable
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun init(){
        icError = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) < 8) {
                    setError("Password must be more than 8 characters", icError)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

}