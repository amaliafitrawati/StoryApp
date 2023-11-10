package com.dicoding.storyapp.view.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.storyapp.R

class CustomEmailTv : AppCompatEditText{
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


    private fun init(){
        icError = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) < 1) {
                    setError("Email can not empty!", icError)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, icError, null)
                }else if(!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    setError("Please input valid email!", icError)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

}