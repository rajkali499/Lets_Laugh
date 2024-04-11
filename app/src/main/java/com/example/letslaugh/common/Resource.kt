package com.example.letslaugh.common

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val dataList: List<T>? = null
) {
    class Success<T>(data: T? = null, dataList: List<T>? = null) :
        Resource<T>(data, null, dataList)

    class Error<T>(data: T? = null, message: String?) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
