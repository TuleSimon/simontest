package com.simon.secondtest.data.datasource

import com.simon.secondtest.data.api.ProductApi
import com.simon.secondtest.interfaces.ProductDataSourceInterface
import com.simon.secondtest.models.ProductModel
import retrofit2.Response
import javax.inject.Inject

class FakeProductDataSource @Inject constructor(
) :ProductDataSourceInterface{
     val fakeApiProductList = mutableListOf(
        ProductModel(name = "testname1", productType = "testtype1", brand = "testbrand", price = "3", currency = "$",
            productColors = null, tagList = null),
        ProductModel(name = "testname2", productType = "testtype1", brand = "testbrand", price = "7", currency = "$",
            productColors = null, tagList = null),
        ProductModel(name = "testname3", productType = "testtype2", brand = "testbrand2", price = "4", currency = "$",
            productColors = null, tagList = null),
        ProductModel(name = "testname4", productType = "testtype2", brand = "testbrand2", price = "3", currency = "$",
            productColors = null, tagList = null),
        ProductModel(name = "testname5", productType = "testtype3", brand = "testbrand3", price = "6", currency = "$",
            productColors = null, tagList = null),
    )
    override
    suspend fun getProducts():Response<List<ProductModel>>{
        return Response.success(fakeApiProductList)
    }
}