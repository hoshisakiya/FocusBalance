package com.fanyichen.focusbalance.countdown.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fanyichen.focusbalance.R

class TimeUnitView(context: Context, attributes: AttributeSet) :
    FrameLayout(context, attributes) {
    private lateinit var timeText: TextView
    private lateinit var unitText: TextView

    private var state = State.PREPARING
    private var type = Type.MINUTE

    init {
        LayoutInflater.from(context).inflate(R.layout.view_time_unit, this, true)
        initAttrs(attributes)
        initView()
    }

    private fun initView() {
        timeText = findViewById(R.id.time_text)
        unitText = findViewById(R.id.unit_text)
        setUnitText()
        setColor()
    }

    private fun initAttrs(attributes: AttributeSet) {
        val typedArray = resources.obtainAttributes(attributes, R.styleable.TimeUnitView)
        when (typedArray.getInt(R.styleable.TimeUnitView_type, 0)) {
            0 -> {
                type = Type.MINUTE
            }
            1 -> {
                type = Type.SECOND
            }
        }
        when (typedArray.getInt(R.styleable.TimeUnitView_state, 0)) {
            0 -> {
                state = State.PREPARING
            }
            1 -> {
                state = State.COUNTING_DOWN
            }
        }
        typedArray.recycle()
    }

    private fun setUnitText() {
        if (type == Type.MINUTE) {
            unitText.text = context.getString(R.string.minute)
        } else if (type == Type.SECOND) {
            unitText.text = context.getString(R.string.second)
        }
    }

    fun setTimeText(time: String) {
        timeText.text = time
    }

    fun setType(type: Type) {
        this.type = type
        setColor()
        setUnitText()
    }

    fun setState(state: State) {
        this.state = state
        setColor()
        setUnitText()
    }

    private fun setColor() {
        if (type == Type.MINUTE) {
            if (state == State.PREPARING) {
                timeText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_light_tertiary
                    )
                )
                unitText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_light_tertiary
                    )
                )
            } else if (state == State.COUNTING_DOWN) {
                timeText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_light_primary
                    )
                )
                unitText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_light_primary
                    )
                )
            }
        } else if (type == Type.SECOND) {
            if (state == State.PREPARING) {
                timeText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.google.android.material.R.color.material_dynamic_tertiary80
                    )
                )
                unitText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.google.android.material.R.color.material_dynamic_tertiary80
                    )
                )
            } else if (state == State.COUNTING_DOWN) {
                timeText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.google.android.material.R.color.material_dynamic_primary70
                    )
                )
                unitText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.google.android.material.R.color.material_dynamic_primary70
                    )
                )
            }
        }
    }

    enum class Type {
        MINUTE,
        SECOND
    }

    enum class State {
        PREPARING,
        COUNTING_DOWN
    }
}