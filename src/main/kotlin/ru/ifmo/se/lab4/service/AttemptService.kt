package ru.ifmo.se.lab4.service

import arrow.core.Either
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ifmo.se.lab4.jmx.AttemptCounter
import ru.ifmo.se.lab4.model.AttemptId
import ru.ifmo.se.lab4.model.CustomError
import ru.ifmo.se.lab4.model.UserId
import ru.ifmo.se.lab4.model.dto.AttemptRequest
import ru.ifmo.se.lab4.model.dto.AttemptResponse
import ru.ifmo.se.lab4.model.dto.toAttempt
import ru.ifmo.se.lab4.model.dto.toResponse
import ru.ifmo.se.lab4.repository.AttemptRepository
import java.lang.management.ManagementFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.management.MBeanServer
import javax.management.ObjectName
import kotlin.math.pow


@Service
class AttemptService(
    val attemptRepository: AttemptRepository,
    private val monitoringService: MonitoringService
) {
    private fun checkAttempt(attempt: AttemptRequest): Boolean =
        attempt.run {
            (x < 0 && y > 0 && (x.pow(2) + y.pow(2)).pow(0.5) <= r) ||
                    (x < 0 && x > (-r / 2) && y < 0 && y > -r) ||
                    (x > 0 && y > 0 && y < -x + r / 2)
        }

    @Transactional
    fun addAttempt(attemptRequest: AttemptRequest, userId: UserId, username: String): Either<CustomError, AttemptResponse> {
        val attempt = attemptRequest.toAttempt(
            AttemptId.generate(),
            OffsetDateTime.now(ZoneOffset.UTC),
            checkAttempt(attemptRequest),
            userId
        )
        monitoringService.count(username, attempt)
        monitoringService.countTime(username)
        return attemptRepository.insert(attempt).map { it.toResponse() }
    }

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
