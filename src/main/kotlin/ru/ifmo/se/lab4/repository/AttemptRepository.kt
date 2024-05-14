package ru.ifmo.se.lab4.repository

import arrow.core.Either
import ru.ifmo.se.lab4.model.Attempt
import ru.ifmo.se.lab4.model.AttemptId
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.UserId

interface AttemptRepository {
    fun findAllByUserId(userId: UserId): Either<RepositoryError, List<Attempt>>

    fun findAll(): Either<RepositoryError, List<Attempt>>

    fun insert(attempt: Attempt): Either<RepositoryError, Attempt>

    fun deleteAllByUserId(userId: UserId): Either<RepositoryError, List<AttemptId>>
}
