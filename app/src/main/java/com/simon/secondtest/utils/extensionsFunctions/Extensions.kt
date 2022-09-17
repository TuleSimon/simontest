package com.simon.secondtest.utils.extensionsFunctions

import android.view.View

const val SELECTED_BRAND_KEY = "selected_brand"

fun View.hide(){
    if(this.visibility==View.VISIBLE)
        this.visibility = View.GONE
}
fun View.show(){
    if(this.visibility==View.INVISIBLE || this.visibility==View.GONE)
        this.visibility = View.VISIBLE
}

fun View.hide2(){
    if(this.visibility==View.VISIBLE)
        this.visibility = View.GONE
}
fun View.show2(){
    if(this.visibility==View.INVISIBLE || this.visibility==View.GONE)
        this.visibility = View.VISIBLE
}