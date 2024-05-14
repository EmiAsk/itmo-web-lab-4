package ru.ifmo.se.lab4.model.dto

import ru.ifmo.se.lab4.model.CustomError

data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String
)

fun CustomError.toErrorResponse(): ErrorResponse =
    ErrorResponse(
        errorCode = code,
        errorMessage = message
    )
