package ru.ifmo.se.lab4.service

import arrow.core.Either
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ifmo.se.lab4.CryptoUtils
import ru.ifmo.se.lab4.model.CustomError
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.User
import ru.ifmo.se.lab4.model.UserId
import ru.ifmo.se.lab4.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val cryptoUtil: CryptoUtils
) {
    @Transactional
    fun findByEmail(username: String): Either<CustomError, User> = userRepository.findByUsername(username)

    @Transactional
    fun findByEmailAndPassword(username: String, password: String): Either<CustomError, User> =
        userRepository.findByUsernameAndPassword(username, cryptoUtil.digestPasswordSha(password))

    @Transactional
    fun register(username: String, password: String): Boolean {
        val user = User(UserId.generate(), username, cryptoUtil.digestPasswordSha(password))
        return userRepository.insert(user)
            .fold({ false }, { true })
    }
}
