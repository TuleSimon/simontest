package com.simon.secondtest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(@SerializedName("website_link")
                        val websiteLink: String? = "",
                        @SerializedName("image_link")
                        val imageLink: String ?= "",

                        @SerializedName("description")
                        val description: String? = "",
                        @SerializedName("created_at")
                        val createdAt: String ?= "",
                        @SerializedName("api_featured_image")
                        val apiFeaturedImage: String? = "",
                        @SerializedName("product_type")
                        val productType: String ?= "",
                        @SerializedName("updated_at")
                        val updatedAt: String ?= "",
                        @SerializedName("price")
                        val price: String ?= "",
                        @SerializedName("tag_list")
                        val tagList: List<String>?,
                        @SerializedName("name")
                        val name: String? = "",
                        @SerializedName("price_sign")
                        val priceSign: String ?= "",
                        @SerializedName("currency")
                        val currency: String? = "",
                        @SerializedName("id")
                        val id: Int ?= 0,
                        @SerializedName("category")
                        val category: String? = "",
                        @SerializedName("product_colors")
                        val productColors: List<ProductColorsItem>?,
                        @SerializedName("brand")
                        val brand: String ?= "",
                        @SerializedName("product_api_url")
                        val productApiUrl: String? = "",
                        @SerializedName("product_link")
                        val productLink: String ?= ""):Parcelable