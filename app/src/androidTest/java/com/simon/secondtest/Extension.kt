package com.simon.secondtest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.utils.extensionsFunctions.BRAND_DATASTORE_NAME
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}



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

val fakeBrandProductList = mutableListOf(
    ProductModel(name = "testname1", productType = "testtype1", brand = "testbrand", price = "3", currency = "$",
        productColors = null, tagList = null),
    ProductModel(name = "testname2", productType = "testtype1", brand = "testbrand", price = "7", currency = "$",
        productColors = null, tagList = null),
)

val fakeBrands = mutableListOf("testbrand","testbrand2","testbrand3")

