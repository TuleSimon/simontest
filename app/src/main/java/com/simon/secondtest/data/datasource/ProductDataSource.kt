package com.simon.secondtest.data.datasource

import com.simon.secondtest.data.api.ProductApi
import com.simon.secondtest.interfaces.ProductDataSourceInterface
import com.simon.secondtest.models.ProductModel
import retrofit2.Response
import javax.inject.Inject

class ProductDataSource @Inject constructor(
    private val productApi: ProductApi
):ProductDataSourceInterface {

    override
    suspend fun getProducts():Response<List<ProductModel>>{
        return productApi.getProducts()
    }
}