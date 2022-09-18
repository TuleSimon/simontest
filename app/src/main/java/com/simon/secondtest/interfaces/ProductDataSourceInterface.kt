package com.simon.secondtest.interfaces

import com.simon.secondtest.models.ProductModel
import dagger.Provides
import retrofit2.Response


interface ProductDataSourceInterface {
    suspend fun getProducts(): Response<List<ProductModel>>
}