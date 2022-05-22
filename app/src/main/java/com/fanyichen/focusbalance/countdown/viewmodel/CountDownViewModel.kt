package com.fanyichen.focusbalance.countdown.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanyichen.focusbalance.countdown.model.CountDownLogic

class CountDownViewModel(context: Context) : ViewModel() {
    private val viewState = MutableLiveData<ViewState>()
    val state: LiveData<ViewState> get() = viewState
    private val logic: CountDownLogic = CountDownLogic(context)

    init {
        viewState.value = ViewState.PrepareCounting(countdown = logic.data)
    }

    fun countDown() {

        logic.countDown {
            viewState.value = ViewState.CountingComplete(countdown = logic.data)
        }
        viewState.value = ViewState.CountingDown(countdown = logic.data)
    }

    fun stopCountDown() {

    }

    sealed class ViewState {
        data class PrepareCounting(val countdown: CountDownLogic.CountdownDisplay) : ViewState()
        data class CountingDown(val countdown: CountDownLogic.CountdownDisplay) : ViewState()
        data class CountingComplete(val countdown: CountDownLogic.CountdownDisplay) : ViewState()
        object TodayCountingComplete : ViewState()
    }
}