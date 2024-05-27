package ru.ifmo.se.lab4.authentication

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64

@Component
class CryptoUtils {
    fun digestPasswordSha(plainTextPassword: String): String =
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(plainTextPassword.toByteArray(StandardCharsets.UTF_8))
            val passwordDigest = md.digest()
            String(Base64.getEncoder().encode(passwordDigest))
        } catch (e: Exception) {
            throw RuntimeException("Exception encoding password", e)
        }

    @Bean
    private fun encoder(): PasswordEncoder = BCryptPasswordEncoder(BCRYPT_STRENGTH)

    companion object {
        private const val BCRYPT_STRENGTH = 11
    }
}
