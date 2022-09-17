package com.simon.secondtest.utils.network


    import android.content.Context
    import android.net.ConnectivityManager
    import android.net.NetworkCapabilities
    import android.os.Build
    import com.simon.secondtest.utils.exceptions.NoConnectivityException
    import okhttp3.Interceptor
    import okhttp3.Response

    @Suppress("DEPRECATION")
    class ConnectivityInterceptor (context: Context) : Interceptor {

        val appContext = context.applicationContext

        override fun intercept(chain: Interceptor.Chain): Response {
            if(!isOnline())
                throw NoConnectivityException()
            return chain.proceed(chain.request())
        }

        private fun isOnline():Boolean{
            val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo:Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork=  connectivityManager.activeNetwork?:return false
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
                if(
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                )
                    return true

                return false

            } else {
                connectivityManager.activeNetworkInfo?.isConnected?:false
            }
            return networkInfo
        }
    }
