package com.fanyichen.focusbalance.countdown.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanyichen.focusbalance.countdown.model.CountDownLogic

class CountDownViewModel(val context: Context): ViewModel() {
    val viewState = MutableLiveData<ViewState>()
    private val countDownLogic: CountDownLogic = CountDownLogic(context)

    sealed class ViewState() {
        object Loading: ViewState()
        data class PrepareCounting(val countdown: CountDownLogic.CountdownData): ViewState()
        data class CountingDown(val countdown: CountDownLogic.CountdownData): ViewState()
        data class CountingComplete(val countdown: CountDownLogic.CountdownData): ViewState()
        object TodayCountingComplete: ViewState()
    }
}