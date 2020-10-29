package com.arttseng.mvvmsample.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast

fun View.rgbBackground(rgbHex:String) {
    var hex = "#" + rgbHex.padStart(6, '0')
    setBackgroundColor( Color.parseColor(hex) )
}

fun Context.toast(message: String){
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}