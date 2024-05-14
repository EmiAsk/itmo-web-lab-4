package ru.ifmo.se.lab4.model.dto

import ru.ifmo.se.lab4.model.Attempt
import java.time.OffsetDateTime

data class AttemptResponse(
    val x: Double,
    val y: Double,
    val r: Double,
    val timestamp: OffsetDateTime,
    val success: Boolean
)

fun Attempt.toResponse() = AttemptResponse(x, y, r, timestamp, success)
