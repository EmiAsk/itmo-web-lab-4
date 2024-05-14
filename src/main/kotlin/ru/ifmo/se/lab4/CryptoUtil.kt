package ru.ifmo.se.lab4

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64

@Component
class CryptoUtils {
    fun digestPassword(plainTextPassword: String?): String {
        return try {
            encoder().encode(plainTextPassword)
        } catch (e: Exception) {
            throw RuntimeException("Exception encoding password", e)
        }
    }

    fun digestPasswordSha(plainTextPassword: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(plainTextPassword.toByteArray(StandardCharsets.UTF_8))
            val passwordDigest = md.digest()
            String(Base64.getEncoder().encode(passwordDigest))
        } catch (e: Exception) {
            throw RuntimeException("Exception encoding password", e)
        }
    }

    @Bean
    private fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder(BCRYPT_STRENGTH)
    }

    companion object {
        private const val BCRYPT_STRENGTH = 11
    }
}
