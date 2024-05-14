package ru.ifmo.se.lab4.service

import arrow.core.Either
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ifmo.se.lab4.model.AttemptId
import ru.ifmo.se.lab4.model.CustomError
import ru.ifmo.se.lab4.model.UserId
import ru.ifmo.se.lab4.model.dto.AttemptRequest
import ru.ifmo.se.lab4.model.dto.AttemptResponse
import ru.ifmo.se.lab4.model.dto.toAttempt
import ru.ifmo.se.lab4.model.dto.toResponse
import ru.ifmo.se.lab4.repository.AttemptRepository
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.math.pow


@Service
class AttemptService(
    val attemptRepository: AttemptRepository
) {
    private fun checkAttempt(attempt: AttemptRequest): Boolean =
        attempt.run {
            (x < 0 && y > 0 && (x.pow(2) + y.pow(2)).pow(0.5) <= r) ||
                    (x < 0 && x > (-r / 2) && y < 0 && y > -r) ||
                    (x > 0 && y > 0 && y < -x + r / 2)
        }

    @Transactional
    fun addAttempt(attempt: AttemptRequest, userId: UserId): Either<CustomError, AttemptResponse> =
        attemptRepository.insert(
            attempt.toAttempt(
                AttemptId.generate(),
                OffsetDateTime.now(ZoneOffset.UTC),
                checkAttempt(attempt),
                userId
            )
        )
            .map { it.toResponse() }

    @Transactional
    fun getAttemptsByUserId(userId: UserId): Either<CustomError, List<AttemptResponse>> =
        attemptRepository.findAllByUserId(userId)
            .map {
                it.map { attempt -> attempt.toResponse() }
            }

    @Transactional
    fun deleteAllAttemptsByUser(userId: UserId): Either<CustomError, List<AttemptId>> =
        attemptRepository.deleteAllByUserId(userId)
}
