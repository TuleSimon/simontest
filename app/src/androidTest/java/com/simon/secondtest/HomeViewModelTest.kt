package com.simon.secondtest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.simon.secondtest.data.datasource.FakeProductDataSource
import com.simon.secondtest.data.datastore.DataStoreSource
import com.simon.secondtest.data.repository.DataStoreRepository
import com.simon.secondtest.data.repository.ProductRepository
import com.simon.secondtest.enums.NetworkResult
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.ui.home.HomeViewModel
import com.simon.secondtest.utils.extensionsFunctions.BRAND_DATASTORE_NAME
import com.simon.secondtest.utils.extensionsFunctions.TEST_BRAND_DATASTORE_NAME
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel



    private val testContext: Context =
        InstrumentationRegistry.getInstrumentation().targetContext

    private val testCoroutineDispatcher =  StandardTestDispatcher()
    private val testCoroutineScope = CoroutineScope(testCoroutineDispatcher + Job())

    lateinit var testDataStore: DataStore<Preferences>

    @Before
    fun setUps() {

        testDataStore = getDataStore(testCoroutineScope,testContext)
        repository= DataStoreSource(testDataStore)

            homeViewModel = HomeViewModel(ProductRepository(FakeProductDataSource()),
                DataStoreRepository(repository)
            )

    }


    companion object {
        var INSTANCE: DataStore<Preferences>? = null

        @Synchronized
        fun getDataStore(
            testCoroutineScope: CoroutineScope,
            testContext: Context
        ): DataStore<Preferences> {
            if (INSTANCE == null) {
                INSTANCE = PreferenceDataStoreFactory.create(
                    scope = testCoroutineScope,
                    produceFile =
                    { testContext.preferencesDataStoreFile(TEST_BRAND_DATASTORE_NAME) })
            }
            return INSTANCE as DataStore<Preferences>
        }
    }

    lateinit var  repository: DataStoreSource


    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }




    @After
    fun tearDown() {
    }



    @Test
    fun getSelectedBrand() =runTest{
        val value =  homeViewModel.getSelectedBrand().first()
        Truth.assertThat(value).isEqualTo("testbrand")
    }


    @Test
    fun setSelectedBrand() = runTest{
        homeViewModel.setSelectedBrand("testbrand")
        val value = homeViewModel.getSelectedBrand().first()
        Truth.assertThat(value).isEqualTo("testbrand")
    }

    @Test
    fun getBrandProducts() = runTest{
        homeViewModel.getProductsByBrand(fakeApiProductList, fakeBrands)
        delay(2000)
        val value=homeViewModel.brandProducts.first()
        Truth.assertThat(value.data).isEqualTo(fakeBrandProductList)
    }

    @Test
    fun getBrands() = runTest {
        homeViewModel.getProducts()
        delay(2000)
        val value= homeViewModel.brands.first()
        Truth.assertThat(value.data).isEqualTo(fakeBrands)
        }


    @Test
    fun getProducts() = runTest {
        homeViewModel.getProducts()
        delay(2000)
        val value= homeViewModel.getProductsValue().first()
        Truth.assertThat(value.data).isEqualTo(fakeApiProductList)
    }


}