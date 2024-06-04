package com.example.weatherapp.api

sealed class NetworkResponse<out T> {
    data class success<T>(val data: T) : NetworkResponse<T>()
    data class failed(val message: String) : NetworkResponse<Nothing>()
    object loading : NetworkResponse<Nothing>()

}