package ru.ifmo.se.lab4.jmx

import mu.KotlinLogging
import ru.ifmo.se.lab4.model.Attempt
import java.util.concurrent.locks.ReentrantLock

val log = KotlinLogging.logger { }


interface AttemptCounterMBean {
    fun getHit(): Int

    fun getMiss(): Int
}

class AttemptCounter : AttemptCounterMBean {
    private var hits = 0

    private var miss = 0

    private val lock = ReentrantLock()
    override fun getHit(): Int = hits

    override fun getMiss(): Int = miss

    fun increment(attempt: Attempt) {
        try {
            lock.lock()
            if (attempt.success) {
                hits++
            } else {
                miss++
            }
        } catch (e: Exception) {
            log.error { e }
        } finally {
            lock.unlock()
        }
    }
}
