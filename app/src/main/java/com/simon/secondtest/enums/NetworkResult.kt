package com.simon.secondtest.enums



sealed class NetworkResult<T>(
    val data:T ?=null,
    val message:String ?=null
){
    class Success<T>(data: T): NetworkResult<T>(data)
    class Error<T>(message: String?, data: T?=null): NetworkResult<T>(data,message = message)
    class Loading<T>: NetworkResult<T>()
    class NetworkError<T>(message: String?): NetworkResult<T>(message = message)

}
