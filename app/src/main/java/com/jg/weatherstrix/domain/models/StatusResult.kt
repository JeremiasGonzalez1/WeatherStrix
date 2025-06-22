package com.jg.weatherstrix.domain.models

sealed class StatusResult <out T> {

    data class Success<out T>(val value: T) : StatusResult<T>()

    data class Error(val message: String) : StatusResult<Nothing>()

}