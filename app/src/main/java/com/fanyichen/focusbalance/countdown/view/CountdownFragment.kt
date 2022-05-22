package com.fanyichen.focusbalance.countdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fanyichen.focusbalance.R
import com.fanyichen.focusbalance.countdown.view.widget.TimeUnitView
import com.google.android.material.button.MaterialButton

class CountdownFragment : Fragment() {
    var state: State = State.PREPARING

    private var minuteTimeUnitView: TimeUnitView? = null
    private var secondTimeUnitView: TimeUnitView? = null
    private var actionButton: MaterialButton? = null

    var onStart: (() -> Unit)? = null
    var onStop: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_count_down, container, false)
        minuteTimeUnitView = view.findViewById(R.id.minute_timeunitview)
        secondTimeUnitView = view.findViewById(R.id.second_timeunitview)
        actionButton = view.findViewById(R.id.countdown_action_button)
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        setState()
        actionButton?.setOnClickListener { _ ->
            if (onStart == null || onStop == null) {
                return@setOnClickListener
            }
            when (state) {
                State.PREPARING -> {
                    onStart()
                }
                State.COUNTING_DOWN -> {
                    onStop()
                }
            }
        }
    }

    private fun setState() {
        when (state) {
            State.PREPARING -> {
                minuteTimeUnitView?.setState(TimeUnitView.State.PREPARING)
                secondTimeUnitView?.setState(TimeUnitView.State.PREPARING)
            }
            State.COUNTING_DOWN -> {
                minuteTimeUnitView?.setState(TimeUnitView.State.COUNTING_DOWN)
                secondTimeUnitView?.setState(TimeUnitView.State.COUNTING_DOWN)
            }
        }
    }

    fun setTime(minute: String, second: String) {
        minuteTimeUnitView?.setTimeText(minute)
        secondTimeUnitView?.setTimeText(second)
    }

    enum class State(val value: Int) {
        PREPARING(0),
        COUNTING_DOWN(1)
    }


    companion object {
        const val STATE = "extra_state"
        const val MINUTE = "extra_minute"
        const val SECOND = "extra_second"
    }
}