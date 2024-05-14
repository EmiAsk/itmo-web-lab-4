package ru.ifmo.se.lab4.repository.pg

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.Use
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.User
import ru.ifmo.se.lab4.model.UserId
import ru.ifmo.se.lab4.repository.UserRepository
import java.sql.ResultSet
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Repository
class PgUserRepository(private val jdbcTemplate: JdbcTemplate) : UserRepository {
    override fun findByUsername(username: String): Either<RepositoryError, User> =
        Either.catch {
            jdbcTemplate.queryForStream("SELECT * FROM client WHERE username = ?", ::toUser, username).findAny()
        }
            .mapLeft { RepositoryError.SqlError(it) }
            .flatMap { if (it.isEmpty) RepositoryError.NotFound.left() else it.get().right() }

    override fun findByUsernameAndPassword(username: String, password: String): Either<RepositoryError, User> =
        Either.catch {
            jdbcTemplate.queryForStream(
                "SELECT * FROM client WHERE username = ? AND password = ?", ::toUser, username, password
            ).findAny()
        }
            .mapLeft { RepositoryError.SqlError(it) }
            .flatMap { if (it.isEmpty) RepositoryError.NotFound.left() else it.get().right() }

    override fun insert(user: User): Either<RepositoryError, User> =
        Either.catch {
            with(user) {
                jdbcTemplate.queryForStream(
                    "INSERT INTO client VALUES (?, ?, ?) RETURNING *",
                    ::toUser,
                    id.value, username, password
                ).findAny()
            }
        }
            .mapLeft { RepositoryError.SqlError(it) }
            .flatMap { if (it.isEmpty) RepositoryError.NoData.left() else it.get().right() }

    private fun toUser(row: ResultSet, rowNum: Int): User =
        with(row) {
            User(
                UserId(getObject("id", UUID::class.java)),
                getString("username"),
                getString("password")
            )
        }
}
