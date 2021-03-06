package com.fanyichen.focusbalance.data_manager.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class CountDownBean : RealmObject {
    @PrimaryKey
    var date: String = ""
    var finishTimes: Int = 0
    var latestRecordCountdownTime: String = ""
    var latestRecordRestTime: String = ""
}