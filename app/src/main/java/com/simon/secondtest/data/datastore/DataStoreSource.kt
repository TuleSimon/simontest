package com.simon.secondtest.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.simon.secondtest.utils.extensionsFunctions.SELECTED_BRAND_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreSource @Inject constructor(
    private val dataStorePrefernece:DataStore<Preferences>
) {



    private val selectedBrand = stringPreferencesKey(SELECTED_BRAND_KEY)

    //getting selected brand from datastore
    private val _getSelectedBrand: Flow<String> = dataStorePrefernece.data
        .map { preferences ->
            // No type safety.
            preferences[selectedBrand] ?: ""
        }

    //getting selected brand from datastore
     fun getSelectedBrand():Flow<String>{
        return _getSelectedBrand
    }

    suspend fun setSelectedBrand(brand:String){
        dataStorePrefernece.edit { brands ->
            brands[selectedBrand] = brand
        }
    }
}