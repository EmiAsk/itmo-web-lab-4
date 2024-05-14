package ru.ifmo.se.lab4.repository.pg

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.ifmo.se.lab4.model.Attempt
import ru.ifmo.se.lab4.model.AttemptId
import ru.ifmo.se.lab4.model.RepositoryError
import ru.ifmo.se.lab4.model.UserId
import ru.ifmo.se.lab4.repository.AttemptRepository
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.util.UUID


@Repository
class PgAttemptRepository(private val jdbcTemplate: JdbcTemplate) : AttemptRepository {
    override fun findAllByUserId(userId: UserId): Either<RepositoryError, List<Attempt>> =
        Either.catch {
            jdbcTemplate.query("SELECT * FROM attempt WHERE user_id = ?", ::toAttempt, userId.value)
        }
            .mapLeft { RepositoryError.SqlError(it) }


    override fun findAll(): Either<RepositoryError, List<Attempt>> = Either.catch {
        jdbcTemplate.query("SELECT * FROM attempt", ::toAttempt)
    }
        .mapLeft { RepositoryError.SqlError(it) }


    override fun insert(attempt: Attempt): Either<RepositoryError, Attempt> =
        Either.catch {
            with(attempt) {
                jdbcTemplate.queryForStream(
                    "INSERT INTO attempt VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING *",
                    ::toAttempt,
                    id.value, x, y, r, timestamp, success, userId.value
                ).findAny()
            }
        }
            .mapLeft { RepositoryError.SqlError(it) }
            .flatMap { if (it.isEmpty) RepositoryError.NoData.left() else it.get().right() }


    override fun deleteAllByUserId(userId: UserId): Either<RepositoryError, List<AttemptId>> = Either.catch {
        jdbcTemplate.query(
            "DELETE FROM attempt WHERE user_id = ? RETURNING id",
            { rs, _ -> AttemptId(rs.getObject("id", UUID::class.java)) },
            userId.value
        )
    }
        .mapLeft { RepositoryError.SqlError(it) }

    private fun toAttempt(row: ResultSet, rowNum: Int): Attempt =
        with(row) {
            Attempt(
                AttemptId(getObject("id", UUID::class.java)),
                getDouble("x"),
                getDouble("y"),
                getDouble("r"),
                getObject("timestamp", OffsetDateTime::class.java),
                getBoolean("success"),
                UserId(getObject("user_id", UUID::class.java)),
            )
        }
}
