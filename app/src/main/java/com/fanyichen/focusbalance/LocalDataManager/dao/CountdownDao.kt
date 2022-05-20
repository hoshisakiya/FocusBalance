package com.fanyichen.focusbalance.LocalDataManager.dao

import android.text.TextUtils
import com.fanyichen.focusbalance.LocalDataManager.dao.bean.CountDownBean
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.query
import io.realm.query.RealmQuery

class CountdownDao {
    private val config = RealmConfiguration.Builder(schema = setOf(CountDownBean::class))
        .build()
    private val realm = Realm.open(config)

    fun queryCountDown(date: String): CountDownBean? {
        val countDownQuery: RealmQuery<CountDownBean> = realm.query("date = $0", date)
        return countDownQuery.first().find()
    }

    fun insertCountDown(data: CountDownBean) {
        realm.writeBlocking {
            this.copyToRealm(CountDownBean().apply {
                date = data.date
                finishTimes = data.finishTimes
                latestRecordCountdownTime = data.latestRecordCountdownTime
                latestRecordRestTime = data.latestRecordRestTime
            })
        }
    }

    fun updateCountDown(date: String,
                        finishTimesParam: Int?,
                        countDownTime: String?,
                        restTime: String?) {
        realm.writeBlocking {
            val countDown: CountDownBean? = queryCountDown(date)
            countDown?.apply {
                if (finishTimesParam != null) {
                    finishTimes = finishTimesParam
                }
                if (!TextUtils.isEmpty(countDownTime)) {
                    latestRecordCountdownTime = countDownTime!!
                }
                if (!TextUtils.isEmpty(restTime)) {
                    latestRecordRestTime = restTime!!
                }
            }
        }
    }
}