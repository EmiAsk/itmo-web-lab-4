package ru.ifmo.se.lab4.model

interface RepositoryError : CustomError {
    data class SqlError(override val cause: Throwable) : RepositoryError, ThrowableError

    object NoData : RepositoryError {
        override val code: String = "NO_DATA"
        override val message: String = "Repository responds with empty body"
    }

    object NotFound : RepositoryError {
        override val code: String = "NOT_FOUND"
        override val message: String = "Not found"
    }
}
