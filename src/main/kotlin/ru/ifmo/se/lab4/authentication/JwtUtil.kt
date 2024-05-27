package ru.ifmo.se.lab4.authentication

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ru.ifmo.se.lab4.service.UserService
import ru.ifmo.se.lab4.service.UserDetailsServiceImpl
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils(
    userService: UserService
) {
    private val userDetails: UserDetailsServiceImpl = UserDetailsServiceImpl(userService)

    @Value("\${app.jwt.secret}")
    private lateinit var secret: String

    fun generateToken(email: String?, roles: List<String?>?): String {
        val claims: Claims = Jwts.claims().subject(email).add("roles", roles).build()
        val now = Date()
        return Jwts
            .builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(Date(now.time + TOKEN_VALIDITY))
            .signWith(key).compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            !claims.payload.expiration.before(Date())
        } catch (e: JwtException) {
            false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return getExpirationDate(token).before(Date())
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun getAuthentication(token: String): Authentication {
        val ud = userDetails.loadUserByUsername(getEmail(token))
        return UsernamePasswordAuthenticationToken(ud, "", ud.authorities)
    }

    fun getEmail(token: String): String {
        return getClaim<String>(token, Claims::getSubject)
    }

    fun getExpirationDate(token: String): Date {
        return getClaim<Date>(token, Claims::getExpiration)
    }

    fun <T> getClaim(token: String, getClaim: Claims.() -> T): T {
        val claims: Claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        return claims.getClaim()
    }

    private val key: SecretKey
        get() = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret))

    companion object {
        private const val TOKEN_VALIDITY: Long = 360000000 // 100 hour
    }
}
