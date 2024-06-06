package ru.ifmo.se.lab4.jmx

interface TimeCounterMBean {
    fun getAverageTime(): Long
}

class TimeCounter : TimeCounterMBean {
    private var times: MutableList<Long> = mutableListOf()

    override fun getAverageTime(): Long {
        var diffSum = 0L
        for (i in 1..<times.size) {
            diffSum += times[i] - times[i - 1]
        }
        return diffSum / (times.size - 1)
    }

    fun addTime(time: Long) {
        times.add(time)
    }
}
