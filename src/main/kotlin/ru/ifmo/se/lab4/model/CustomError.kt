package ru.ifmo.se.lab4.model

interface CustomError {
    val code: String
    val message: String
}

interface ThrowableError : CustomError {
    val cause: Throwable
    override val code: String
        get() = cause::class.simpleName?.uppercase() ?: "UNEXPECTED_ERROR"
    override val message: String
        get() = cause.message ?: "Unexpected error"
}
