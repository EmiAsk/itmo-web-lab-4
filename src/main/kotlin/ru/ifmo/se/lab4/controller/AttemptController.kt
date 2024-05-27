package ru.ifmo.se.lab4.controller

import arrow.core.getOrElse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.lab4.authentication.JwtUtils
import ru.ifmo.se.lab4.authentication.UserPrincipal
import ru.ifmo.se.lab4.model.dto.AttemptRequest
import ru.ifmo.se.lab4.model.dto.toErrorResponse
import ru.ifmo.se.lab4.service.AttemptService
import ru.ifmo.se.lab4.service.UserService


@RestController
@RequestMapping("attempt")
@CrossOrigin
class AttemptController(
    private val attemptService: AttemptService,
    private val userService: UserService,
    private val jwt: JwtUtils
) {
    @PostMapping
    fun addAttempt(@AuthenticationPrincipal user: UserPrincipal, @RequestBody attempt: AttemptRequest): Any {
        return attemptService.addAttempt(attempt, user.userId)
            .getOrElse { it.toErrorResponse() }
    }

    @GetMapping
    fun getAttempts(@AuthenticationPrincipal user: UserPrincipal): Any {
        return attemptService.getAttemptsByUserId(user.userId)
            .getOrElse { it.toErrorResponse() }
    }

    @DeleteMapping
    fun deleteAttempts(@AuthenticationPrincipal user: UserPrincipal): Any {
        return attemptService.deleteAllAttemptsByUser(user.userId)
            .getOrElse { it.toErrorResponse() }
    }

//    private fun getCurrentUser(request: HttpServletRequest): User? =
//        userService.findByEmail(jwt.getEmail(jwt.resolveToken(request)))
}
