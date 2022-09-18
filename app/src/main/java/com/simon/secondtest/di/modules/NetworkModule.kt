package com.simon.secondtest.di.modules

import android.content.Context
import com.google.gson.GsonBuilder
import com.simon.secondtest.data.api.ProductApi
import com.simon.secondtest.data.datasource.ProductDataSource
import com.simon.secondtest.interfaces.ProductDataSourceInterface
import com.simon.secondtest.utils.CONS.BASEURL
import com.simon.secondtest.utils.network.ConnectivityInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGsonConverterFactory():GsonConverterFactory{
        return GsonConverterFactory.create(GsonBuilder().setLenient().create())
    }

    @Provides
    @Singleton
    fun provideProductDataSource(productApi: ProductApi):ProductDataSourceInterface{
        return ProductDataSource(productApi)
    }

    @Provides
    @Singleton
    fun provideInterceptor(@ApplicationContext context: Context): ConnectivityInterceptor {
        return  ConnectivityInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: ConnectivityInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ):Retrofit{
        return Retrofit.Builder().baseUrl(BASEURL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }


}