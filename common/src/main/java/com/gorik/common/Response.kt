package com.gorik.common

sealed class Response<out T> {

    object Loading:Response<Nothing>()
    data class  Error(val exception:Exception):Response<Nothing>()
    data class  Success<out R>(val data  : R):Response<R>()

    override fun toString(): String {

        return when (this){
            is Success-> "Success ::: [$data]"
            is Error-> "Error ::: [$exception]"
            is Loading-> "Loading ::: ...."
        }
    }
}



fun <T> Response<T>.successOr(fallback:T):T{
    return (this as? Response.Success)?.data ?: fallback
}
val <T> Response<T>.data:T?
    get() =(this as? Response.Success)?.data

