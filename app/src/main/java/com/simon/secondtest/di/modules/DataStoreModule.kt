package com.simon.secondtest.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.simon.secondtest.utils.extensionsFunctions.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {



    @Provides
    @Singleton
    fun dataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
         appContext.dataStore
}