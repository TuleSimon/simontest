package com.simon.secondtest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductColorsItem(@SerializedName("colour_name")
                             val colourName: String ?= "",
                             @SerializedName("hex_value")
                             val hexValue: String ?= ""):Parcelable