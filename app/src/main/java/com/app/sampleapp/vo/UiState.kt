package com.app.sampleapp.vo

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Error<out T>(val throwable: Throwable): UiState<T>()
    data class Failure(val code: Int, val message: String) : UiState<Nothing>()
    data class Success<out T>(val data: T): UiState<T>()
}