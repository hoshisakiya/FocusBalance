package com.fanyichen.focusbalance.countdown.model

import android.content.Context
import com.fanyichen.focusbalance.LocalDataManager.UserSettings
import com.fanyichen.focusbalance.LocalDataManager.UserSettingsUtil
import com.fanyichen.focusbalance.LocalDataManager.dao.CountdownDao
import com.fanyichen.focusbalance.util.*
import java.util.*

class CountDownLogic(context: Context) {
    companion object {
        private const val DEFAULT_MINUTE: Int = 25
        private const val DEFAULT_FOCUS_TIMES = 5
    }

    private lateinit var countdownData: CountdownData
    private val userSettingsUtil = UserSettingsUtil(context)
    private val countdownDao = CountdownDao()

    init {
        readFromData()
    }

    /**
     * 从数据库读取今天倒计时数据
     */
    private fun readFromData() {
        val totalMinute = userSettingsUtil
            .readUserSettings(UserSettings.FOCUS_LENGTH, DEFAULT_MINUTE)
        val totalTime = userSettingsUtil
            .readUserSettings(UserSettings.DAILY_FOCUS_TIMES, DEFAULT_FOCUS_TIMES)
        var lastRestTime = ""
        var restMinute = 10
        var currentTimes = 0
        var minutes = 0
        var seconds = 0
        val countDownBean = countdownDao.queryCountDown(getTodayString())
        if (countDownBean != null) {
            val latestRecordRestTime = countDownBean.latestRecordRestTime
            val latestRecordDate = getDate(latestRecordRestTime)
            if ((latestRecordDate != null) && isToday(latestRecordDate)) {
                val deltaMinute = getTimeDeltaMinute(latestRecordDate, Date())
                if (deltaMinute < 10) {
                    restMinute = deltaMinute.toInt()
                }
                lastRestTime = latestRecordRestTime
            }
            currentTimes = countDownBean.finishTimes
            val countDownTime = countDownBean.latestRecordCountdownTime
            val countDownDate = getDate(countDownTime)
            if (countDownDate != null) {
                minutes = getMin(countDownDate)
                seconds = getSecond(countDownDate) - minutes * 60
            }
        }

        countdownData = CountdownData(
            lastRestTime = lastRestTime,
            restMinute = restMinute,
            currentTimes = currentTimes,
            totalMinute = totalMinute,
            totalTime = totalTime,
            minute = minutes,
            second = seconds
        )
    }

    /**
     * 倒计时
     */
    fun countDown() {
        val currentMinute = countdownData.minute
        val currentSecond = countdownData.second
        val nextSecond = currentSecond - 1
        if (nextSecond < 0) {
            countdownData.second = 59
            val nextMinute = currentMinute - 1
            if (nextMinute < 0) {
                timeUp()
                return
            } else {
                countdownData.minute -= 1
            }
        } else {
            countdownData.second -= 1
        }
        recordTime()
    }

    /**
     * 时间到
     */
    private fun timeUp() {
        countdownData.currentTimes -= 1
        countdownData.lastRestTime = getNow()
        countdownDao.updateCountDown(
            getTodayString(),
            countdownData.currentTimes,
            getNow(),
            getNow()
        )
    }

    /**
     * 倒计时记录时间
     */
    private fun recordTime() {
        countdownDao.updateCountDown(
            getTodayString(),
            null,
            getNow(),
            null
        )
    }

    /**
     * 休息倒计时
     */
    private fun countDownRest() {
        val lastRestDate = getDate(countdownData.lastRestTime) ?: return
        countdownData.restMinute = getTimeDeltaMinute(lastRestDate, Date()).toInt()
    }

    data class CountdownData(
        var restMinute: Int, // 休息分钟
        var lastRestTime: String, // 最后一次休息时间
        var currentTimes: Int, // 目前次数
        var totalMinute: Int, // 总分钟数
        var totalTime: Int, // 总次数
        var minute: Int, // 分钟
        var second: Int // 时间
    )
}