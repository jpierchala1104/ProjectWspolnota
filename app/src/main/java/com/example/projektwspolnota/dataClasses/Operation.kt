package com.example.projektwspolnota.dataClasses

sealed class Operation<T> {
    class InProgress<T> : Operation<T>()
    data class Completed<T>(val value: T) : Operation<T>()
    data class Failed<T>(val error: Throwable) : Operation<T>()
}