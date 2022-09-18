package com.simon.secondtest.data.repository

import com.simon.secondtest.data.datasource.ProductDataSource
import com.simon.secondtest.interfaces.ProductDataSourceInterface
import com.simon.secondtest.models.ProductModel
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDataSource: ProductDataSourceInterface
) {

    //fetching products from our product data source
    suspend fun getProducts(): Response<List<ProductModel>> {
        return productDataSource.getProducts()
    }

}