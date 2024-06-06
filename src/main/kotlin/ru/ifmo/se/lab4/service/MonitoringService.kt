package ru.ifmo.se.lab4.service

import org.springframework.stereotype.Service
import ru.ifmo.se.lab4.jmx.AttemptCounter
import ru.ifmo.se.lab4.jmx.TimeCounter
import ru.ifmo.se.lab4.model.Attempt
import java.lang.management.ManagementFactory
import javax.management.MBeanServer
import javax.management.ObjectName


@Service
class MonitoringService {
    private val attemptCounters: MutableMap<String, AttemptCounter> = mutableMapOf()

    private val timeCounters: MutableMap<String, TimeCounter> = mutableMapOf()

    private final val server: MBeanServer = ManagementFactory.getPlatformMBeanServer()

    fun count(username: String, attempt: Attempt) {
        synchronized(username) {
            if (username !in attemptCounters) {
                attemptCounters[username] = AttemptCounter()
                server.registerMBean(attemptCounters[username], ObjectName("attemptCounter", "name", username))
            }
            attemptCounters[username]?.increment(attempt)
        }
    }

    fun countTime(username: String) {
        synchronized(username) {
            if (username !in timeCounters) {
                timeCounters[username] = TimeCounter()
                server.registerMBean(timeCounters[username], ObjectName("timeCounter", "name", username))
            }
            timeCounters[username]?.addTime(System.currentTimeMillis())
        }
    }
}
