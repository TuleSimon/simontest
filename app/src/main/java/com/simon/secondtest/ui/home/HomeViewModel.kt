package com.simon.secondtest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simon.secondtest.data.repository.ProductRepository
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.enums.NetworkResult
import com.simon.secondtest.utils.exceptions.NoConnectivityException
import com.simon.secondtest.utils.productHelper.ProductHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val nointerneterror = "No internet connection"

     val products = MutableStateFlow<NetworkResult<List<ProductModel>>>(NetworkResult.Loading())

    val brands = MutableStateFlow<NetworkResult<List<String>>>(NetworkResult.Loading())

    init {
        updateBrandsAutomatically()
    }


    //fetching products from our repository
    fun getProducts() = viewModelScope.launch {

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
                }
                is NetworkResult.Error -> {
                    brands.emit(NetworkResult.Error("Error loading products"))
                }
                is NetworkResult.Success -> {
                    it.data?.apply {
                        val brandss = ProductHelper.extractBrands(this)
                        if(brandss.isNotEmpty()) {
                            brands.emit(NetworkResult.Success(brandss))
                            Timber.d(brandss.toString())
                        }
                    }
                }
                else -> {
                    //TO DO
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

}