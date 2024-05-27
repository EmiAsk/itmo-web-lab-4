package ru.ifmo.se.lab4.controller

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FAILED_DEPENDENCY
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.lab4.authentication.CryptoUtils
import ru.ifmo.se.lab4.authentication.JwtUtils
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.UserData
import ru.ifmo.se.lab4.model.dto.ErrorResponse
import ru.ifmo.se.lab4.model.dto.toErrorResponse
import ru.ifmo.se.lab4.service.UserService

@RestController
@CrossOrigin
class UserController(
    private val jwt: JwtUtils,
    private val crypto: CryptoUtils,
    private val service: UserService,
) {
    private val authManager = AuthenticationManager { _: Authentication -> null }

    @PostMapping("/login")
    private fun login(@RequestBody userData: UserData): ResponseEntity<out Any> {
        val username = userData.username
        val password = crypto.digestPasswordSha(userData.password)
        return service.findByEmailAndPassword(username, password)
            .fold(
                ifLeft = {
                    when (it) {
                        is RepositoryError.NotFound -> ResponseEntity(it.toErrorResponse(), NOT_FOUND)
                        else -> ResponseEntity(it.toErrorResponse(), FAILED_DEPENDENCY)
                    }
                },
                ifRight = {
                    authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
                    val token = jwt.generateToken(username, listOf("USER"))
                    ResponseEntity.ok(token)
                }
            )
    }

    @PostMapping("/register")
    private fun register(@RequestBody userData: UserData): ResponseEntity<out Any> {
        val username = userData.username
        val password = crypto.digestPasswordSha(userData.password)
        return service.findByEmail(username)
            .fold(
                ifLeft = {
                    if (service.register(username, password)) {
                        ResponseEntity("User has been created", CREATED)
                    } else {
                        ResponseEntity(it.toErrorResponse(), UNPROCESSABLE_ENTITY)
                    }
                },
                ifRight = {
                    ResponseEntity(
                        ErrorResponse(
                            errorCode = "ALREADY_EXISTS",
                            errorMessage = "User with the same email exists"
                        ), BAD_REQUEST
                    )
                }
            )
    }
}
