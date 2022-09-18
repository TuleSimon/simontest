package com.simon.secondtest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simon.secondtest.data.repository.DataStoreRepository
import com.simon.secondtest.data.repository.ProductRepository
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.enums.NetworkResult
import com.simon.secondtest.utils.exceptions.NoConnectivityException
import com.simon.secondtest.utils.productHelper.ProductHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val nointerneterror = "No internet connection"

    lateinit var selectedBrand: Flow<String>
   private val products = MutableStateFlow<NetworkResult<List<ProductModel>>>(NetworkResult.Loading())
     val brandProducts = MutableStateFlow<NetworkResult<List<ProductModel>>>(NetworkResult.Loading())

    val brands = MutableStateFlow<NetworkResult<List<String>>>(NetworkResult.Loading())

    init {
        updateBrandsAutomatically()
        viewModelScope.launch {
            selectedBrand = dataStoreRepository.getSelectedBrands()
        }

    }


    //fetching products from our repository
    fun getProducts() = viewModelScope.launch {
        brandProducts.emit(NetworkResult.Loading())
        try {
            val response = productRepository.getProducts()

            val products = handleNetworkResponse(response)
            emitProducts(products)
        }
        catch (e: Exception) {
            when (e) {
                is NoConnectivityException -> {
                    emitProducts(NetworkResult.Error(nointerneterror))
                }
                else -> {
                    emitProducts(NetworkResult.Error(e.message))
                }
            }
        }
    }

    //updating the brands automically whenever our product list changes
    private fun updateBrandsAutomatically() = viewModelScope.launch{
        products.collect{
            when(it){
                is NetworkResult.Loading ->{
                    brands.emit(NetworkResult.Loading())
                    brandProducts.emit(NetworkResult.Loading())
                }
                is NetworkResult.Error -> {
                    brands.emit(NetworkResult.Error(it.message))
                    brandProducts.emit(NetworkResult.Error(it.message))
                }
                is NetworkResult.Success -> {
                    it.data?.apply {
                        val brandss = ProductHelper.extractBrands(this)
                        if(brandss.isNotEmpty()) {
                            brands.emit(NetworkResult.Success(brandss))
                            getProductsByBrand(this,brandss)
                            Timber.d(brandss.toString())
                        }
                    }
                }
                else -> {
                    brands.emit(NetworkResult.Error(it.message))
                    brandProducts.emit(NetworkResult.Error(it.message))
                }
            }
        }
    }




    //checking our network response from retrofit if we have the actual data or some sort of error occured
    private fun handleNetworkResponse(response: Response<List<ProductModel>>): NetworkResult<List<ProductModel>> {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.body().isNullOrEmpty() -> {
                return NetworkResult.Error("No data found")
            }

            response.isSuccessful && response.body()!=null -> {
                val products = response.body()!!
                return NetworkResult.Success(products)
            }
            else -> {
                return NetworkResult.NetworkError(nointerneterror)
            }
        }
    }


    //setting our retrofit response to our flow object
    private suspend fun emitProducts(product:NetworkResult<List<ProductModel>>){
        products.emit(product)
    }


    suspend fun getSelectedBrand():Flow<String>{
        return dataStoreRepository.getSelectedBrands()
    }

    suspend fun setSelectedBrand(brand:String){
        dataStoreRepository.setSelectedBrand(brand)
        getSelectedBrand()
        products.value.data?.let { brands.value.data?.let { it1 -> getProductsByBrand(it, it1) } }
    }

    fun getProductsByBrand(list: List<ProductModel>,brandList: List<String>) = viewModelScope.launch{
        if(selectedBrand.first().isNotEmpty()) {
            val lists = ProductHelper.getBrandProducts(selectedBrand.first(), list).sortedBy { it.productType }
            brandProducts.emit(NetworkResult.Success(lists))
        }
        else{
            val lists= ProductHelper.getBrandProducts(brandList[0], list).sortedBy { it.productType }
            brandProducts.emit(NetworkResult.Success(lists))
        }
    }

    fun getProductsValue():MutableStateFlow<NetworkResult<List<ProductModel>>>{
        return products
    }

}