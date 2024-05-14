package ru.ifmo.se.lab4.model

import java.util.UUID


@JvmInline
value class UserId(val value: UUID) {
    companion object {
        @JvmStatic
        fun generate(): UserId = UserId(UUID.randomUUID())
    }
}


data class User(
    val id: UserId,
    val username: String,
    val password: String
)
