package ru.ifmo.se.lab4.repository

import arrow.core.Either
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.User

interface UserRepository {
    fun findByUsername(username: String): Either<RepositoryError, User>

    fun findByUsernameAndPassword(username: String, password: String): Either<RepositoryError, User>

    fun insert(user: User): Either<RepositoryError, User>
}
