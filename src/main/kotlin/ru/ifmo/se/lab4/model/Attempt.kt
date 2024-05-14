package ru.ifmo.se.lab4.model

import java.time.OffsetDateTime
import java.util.UUID


@JvmInline
value class AttemptId(val value: UUID) {
    companion object {
        @JvmStatic
        fun generate(): AttemptId = AttemptId(UUID.randomUUID())
    }
}


data class Attempt(
    val id: AttemptId,
    val x: Double,
    val y: Double,
    val r: Double,
    val timestamp: OffsetDateTime,
    val success: Boolean,
    val userId: UserId
)
