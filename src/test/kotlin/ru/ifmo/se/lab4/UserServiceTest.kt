package ru.ifmo.se.lab4

import arrow.core.left
import arrow.core.right
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.ifmo.se.lab4.authentication.CryptoUtils
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.User
import ru.ifmo.se.lab4.model.UserId
import ru.ifmo.se.lab4.repository.pg.PgUserRepository
import ru.ifmo.se.lab4.service.UserService

class UserServiceTest {
    private lateinit var userRepository: PgUserRepository

    private lateinit var userService: UserService

    private lateinit var cryptoUtils: CryptoUtils

    @BeforeEach
    fun setUp() {
        userRepository = mock()
        cryptoUtils = mock()
        userService = UserService(userRepository, cryptoUtils)
    }

    @Test
    fun `findByEmail, success`() {
        // arrange
        whenever(userRepository.findByUsername(expectedUser.username)) doReturn expectedUser.right()

        // act
        val actualUser = userService.findByEmail(expectedUser.username)

        // assert
        assert(actualUser.isRight())
        actualUser.onRight {
            assert(it == expectedUser)
        }
    }

    @Test
    fun `findByEmail, user not found, NotFound exception`() {
        // arrange
        whenever(userRepository.findByUsername(expectedUser.username)) doReturn RepositoryError.NotFound.left()

        // act
        val actualUser = userService.findByEmail(expectedUser.username)

        // assert
        assert(actualUser.isLeft())
        actualUser.onLeft {
            assert(it is RepositoryError.NotFound)
        }
    }

    @Test
    fun `findByEmail, unexpected error, SqlError exception`() {
        // arrange
        whenever(userRepository.findByUsername(expectedUser.username)) doReturn RepositoryError.SqlError(
            RuntimeException()
        ).left()

        // act
        val actualUser = userService.findByEmail(expectedUser.username)

        // assert
        assert(actualUser.isLeft())
        actualUser.onLeft {
            assert(it is RepositoryError.SqlError)
        }
    }

    @Test
    fun `findByEmailAndPassword, success`() {
        // arrange
        whenever(cryptoUtils.digestPasswordSha(expectedUser.password)) doReturn expectedUser.password
        whenever(
            userRepository.findByUsernameAndPassword(
                expectedUser.username,
                expectedUser.password
            )
        ) doReturn expectedUser.right()

        // act
        val actualUser = userService.findByEmailAndPassword(expectedUser.username, expectedUser.password)

        // assert
        assert(actualUser.isRight())
        actualUser.onRight {
            assert(it == expectedUser)
        }
    }

    @Test
    fun `findByEmailAndPassword, user not found, NotFound exception`() {
        // arrange
        whenever(cryptoUtils.digestPasswordSha(expectedUser.password)) doReturn expectedUser.password
        whenever(
            userRepository.findByUsernameAndPassword(
                expectedUser.username,
                expectedUser.password
            )
        ) doReturn RepositoryError.NotFound.left()

        // act
        val actualUser = userService.findByEmailAndPassword(expectedUser.username, expectedUser.password)

        // assert
        assert(actualUser.isLeft())
        actualUser.onLeft {
            assert(it is RepositoryError.NotFound)
        }
    }

    @Test
    fun `findByEmailAndPassword, unexpected exception, SqlError exception`() {
        // arrange
        whenever(cryptoUtils.digestPasswordSha(expectedUser.password)) doReturn expectedUser.password
        whenever(
            userRepository.findByUsernameAndPassword(
                expectedUser.username,
                expectedUser.password
            )
        ) doReturn RepositoryError.SqlError(RuntimeException()).left()

        // act
        val actualUser = userService.findByEmailAndPassword(expectedUser.username, expectedUser.password)

        // assert
        assert(actualUser.isLeft())
        actualUser.onLeft {
            assert(it is RepositoryError.SqlError)
        }
    }

    @Test
    fun `register, success, true`() {
        // arrange
        whenever(cryptoUtils.digestPasswordSha(expectedUser.password)) doReturn expectedUser.password
        whenever(userRepository.insert(any())) doReturn expectedUser.right()

        // act
        val inserted = userService.register(expectedUser.username, expectedUser.password)

        // assert
        assert(inserted)
    }

    @Test
    fun `register, user already exists, false`() {
        // arrange
        whenever(cryptoUtils.digestPasswordSha(expectedUser.password)) doReturn expectedUser.password
        whenever(userRepository.insert(any())) doReturn RepositoryError.SqlError(RuntimeException("existed")).left()

        // act
        val inserted = userService.register(expectedUser.username, expectedUser.password)

        // assert
        assert(!inserted)
    }

    @Test
    fun `register, not inserted, false`() {
        // arrange
        whenever(cryptoUtils.digestPasswordSha(expectedUser.password)) doReturn expectedUser.password
        whenever(userRepository.insert(any())) doReturn RepositoryError.NoData.left()

        // act
        val inserted = userService.register(expectedUser.username, expectedUser.password)

        // assert
        assert(!inserted)
    }

    companion object {
        val expectedUser = User(
            UserId.generate(),
            "username",
            "password"
        )
    }
}
