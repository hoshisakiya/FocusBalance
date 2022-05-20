package com.fanyichen.focusbalance.LocalDataManager

import android.content.Context
import android.content.SharedPreferences
import com.fanyichen.focusbalance.R

enum class UserSettings(val setting: String) {
    DAILY_FOCUS_TIMES("key_daily_focus_times"),
    FOCUS_LENGTH("key_focus_length")
}

class UserSettingsUtil(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.user_settings), Context.MODE_PRIVATE
    )

    fun readUserSettings(userSettings: UserSettings, defaultValue: Int): Int {
        return sharedPref.getInt(userSettings.setting, defaultValue)
    }
}