package ru.ifmo.se.lab4.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import ru.ifmo.se.lab4.UserPrincipal

class UserDetailsServiceImpl(private val service: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        service.findByEmail(username)
            .fold(
                ifLeft = { throw UsernameNotFoundException("User not found") },
                ifRight = { UserPrincipal(it) }
            )
}
