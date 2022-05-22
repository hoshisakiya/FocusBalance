package com.fanyichen.focusbalance.countdown.model

import android.content.Context
import com.fanyichen.focusbalance.data_manager.UserSettings
import com.fanyichen.focusbalance.data_manager.UserSettingsUtil
import com.fanyichen.focusbalance.data_manager.dao.CountdownDao
import com.fanyichen.focusbalance.data_manager.bean.CountDownBean
import com.fanyichen.focusbalance.util.*
import java.util.*

class CountDownLogic(context: Context) {
    companion object {
        private const val DEFAULT_MINUTE: Int = 25
        private const val DEFAULT_FOCUS_TIMES = 5
    }

    private lateinit var countdownData: CountdownDisplay
    val data get() = countdownData
    private val userSettingsUtil = UserSettingsUtil(context)
    private val countdownDao = CountdownDao()

    init {
        initData()
    }

    /**
     * 从数据库读取今天倒计时数据
     */
    private fun initData() {
        var totalMinute = userSettingsUtil
            .readUserSettings(UserSettings.FOCUS_LENGTH, -1)
        var totalTime = userSettingsUtil
            .readUserSettings(UserSettings.DAILY_FOCUS_TIMES, -1)
        if (totalMinute == null || totalMinute == -1) {
            userSettingsUtil.writeUserSettings(UserSettings.FOCUS_LENGTH, DEFAULT_MINUTE)
            totalMinute = DEFAULT_MINUTE
        }
        if (totalTime == null || totalTime == -1) {
            userSettingsUtil.writeUserSettings(UserSettings.DAILY_FOCUS_TIMES, DEFAULT_FOCUS_TIMES)
            totalTime = DEFAULT_FOCUS_TIMES
        }
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

        countdownData = CountdownDisplay(
            lastRestTime = lastRestTime,
            restMinute = restMinute,
            currentTimes = currentTimes,
            totalMinute = totalMinute,
            totalTime = totalTime,
            minute = minutes,
            second = seconds
        )

        if (countDownBean == null) {
            val newCountDownBean = CountDownBean()
            newCountDownBean.date = getNow()
            countdownDao.insertCountDown(newCountDownBean)
        }
    }

    /**
     * 倒计时
     */
    fun countDown(onTimeUp: () -> Unit) {
        val currentMinute = countdownData.minute
        val currentSecond = countdownData.second
        val nextSecond = currentSecond - 1
        if (nextSecond < 0) {
            countdownData.second = 59
            val nextMinute = currentMinute - 1
            if (nextMinute < 0) {
                timeUp(onTimeUp)
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
    private fun timeUp(onTimeUp: () -> Unit) {
        countdownData.currentTimes += 1
        countdownData.lastRestTime = getNow()
        countdownDao.updateCountDown(
            getTodayString(),
            countdownData.currentTimes,
            getNow(),
            getNow()
        )
        onTimeUp()
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

    data class CountdownDisplay(
        var restMinute: Int, // 休息分钟
        var lastRestTime: String, // 最后一次休息时间
        var currentTimes: Int, // 目前次数
        var totalMinute: Int, // 总分钟数
        var totalTime: Int, // 总次数
        var minute: Int, // 分钟
        var second: Int // 时间
    )
}