package ru.ifmo.se.lab4

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyVararg
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.User
import ru.ifmo.se.lab4.model.UserId
import ru.ifmo.se.lab4.repository.pg.PgUserRepository
import java.util.stream.Stream

class PgUserRepositoryTest {
    private lateinit var userRepository: PgUserRepository

    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate = mock()
        userRepository = PgUserRepository(jdbcTemplate)
    }

    @Test
    fun `findByUsername, success`() {
        // arrange
        val sampleUser = User(
            UserId.generate(),
            "username",
            "password"
        )
        whenever(jdbcTemplate.queryForStream(any<String>(), any<RowMapper<*>>(), anyVararg())) doReturn Stream.of(
            sampleUser
        )

        // act
        val user = userRepository.findByUsername(sampleUser.username)

        // assert
        assert(user.isRight())
        user.onRight {
            assert(it == sampleUser)
        }
    }

    @Test
    fun `findByUsername, unexpected error, SqlError thrown`() {
        // arrange
        whenever(
            jdbcTemplate.queryForStream(
                any<String>(),
                any<RowMapper<*>>(),
                anyVararg()
            )
        ) doThrow RuntimeException()

        // act
        val user = userRepository.findByUsername("username")

        // assert
        assert(user.isLeft())
        user.onLeft {
            assert(it is RepositoryError.SqlError)
        }
    }

    @Test
    fun `findByUsername, user not found, NotFound thrown`() {
        // arrange
        whenever(jdbcTemplate.queryForStream(any<String>(), any<RowMapper<*>>(), anyVararg())) doReturn Stream.of()

        // act
        val user = userRepository.findByUsername("username")

        // assert
        assert(user.isLeft())
        user.onLeft {
            assert(it is RepositoryError.NotFound)
        }
    }
}
