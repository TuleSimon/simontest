package com.simon.secondtest.models

import com.google.gson.annotations.SerializedName

data class ProductColorsItem(@SerializedName("colour_name")
                             val colourName: String = "",
                             @SerializedName("hex_value")
                             val hexValue: String = "")