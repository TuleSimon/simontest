package com.simon.secondtest.data.api

import com.simon.secondtest.models.ProductModel
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {

    @GET("products.json")
    suspend fun getProducts():Response<List<ProductModel>>
}