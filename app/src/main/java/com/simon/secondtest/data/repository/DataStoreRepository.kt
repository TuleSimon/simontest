package com.simon.secondtest.data.repository

import com.simon.secondtest.data.datastore.DataStoreSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStoreSource: DataStoreSource
)
{
    suspend fun getSelectedBrands(): Flow<String> {
        return dataStoreSource.getSelectedBrand()
    }

    suspend fun setSelectedBrand(brand:String){
        dataStoreSource.setSelectedBrand(brand)
    }
}