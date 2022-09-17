package com.simon.secondtest.bindingAdapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.bumptech.glide.Glide
import com.simon.secondtest.R
import java.util.*

class BindingAdapters {

    companion object {

        //function to create a binding adapter for our data binding layout to load image url using coil
        @BindingAdapter("loadImage",)
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, url: String?, ) {
            url?.let {
                Glide.with(imageView.context).load("http://"+it.substring(2 until it.length))
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imageView)

            }
        }


        //function to create a binding adapter for our data binding layout to set product price with currency
        @BindingAdapter("setPrice","setCurrency", requireAll = true)
        @JvmStatic
        fun setProductPrice(textView: TextView, price: String?, currency: String?) {
            textView.text = "$currency $price"
        }


    }
}