package ru.ifmo.se.lab4.model.dto

import ru.ifmo.se.lab4.model.Attempt
import ru.ifmo.se.lab4.model.AttemptId
import ru.ifmo.se.lab4.model.UserId
import java.time.OffsetDateTime

data class AttemptRequest(
    val x: Double,
    val y: Double,
    val r: Double
)


fun AttemptRequest.toAttempt(
    id: AttemptId,
    timestamp: OffsetDateTime,
    success: Boolean,
    userId: UserId
) = Attempt(id, x, y, r, timestamp, success, userId)

