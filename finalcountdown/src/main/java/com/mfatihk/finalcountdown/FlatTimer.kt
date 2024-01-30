package com.mfatihk.finalcountdown

import android.content.Context
import android.content.res.ColorStateList
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.mfatihk.finalcountdown.databinding.ViewFlatCountdownTimerBinding
import java.util.Calendar

class FlatTimer(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context, attrs) {
    private var binding: ViewFlatCountdownTimerBinding =
        ViewFlatCountdownTimerBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var countDownTimer: CountDownTimer

    private var tvProgressDay: TextView = binding.tvProgressDay
    private var tvProgressHour: TextView = binding.tvProgressHour
    private var tvProgressMinute: TextView = binding.tvProgressMinute
    private var tvProgressSecond: TextView = binding.tvProgressSecond

    private var tvDay: TextView = binding.tvDay
    private var tvHour: TextView = binding.tvHour
    private var tvMinute: TextView = binding.tvMinute
    private var tvSecond: TextView = binding.tvSecond

    private var progressDay: MaterialCardView = binding.progressDay
    private var progressHour: MaterialCardView = binding.progressHour
    private var progressMinute: MaterialCardView = binding.progressMinute
    private var progressSecond: MaterialCardView = binding.progressSecond

    init {
        tvProgressDay = binding.tvProgressDay
        tvProgressHour = binding.tvProgressHour
        tvProgressMinute = binding.tvProgressMinute
        tvProgressSecond = binding.tvProgressSecond
        tvDay = binding.tvDay
        tvHour = binding.tvHour
        tvMinute = binding.tvMinute
        tvSecond = binding.tvSecond
        progressDay = binding.progressDay
        progressHour = binding.progressHour
        progressMinute = binding.progressMinute
        progressSecond = binding.progressSecond

        setupAttributes(attrs)
    }

    fun setup(endDate: Long) {
        val currentTime = Calendar.getInstance().time

        val different = endDate - currentTime.time
        countDownTimer = object : CountDownTimer(different, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                tvProgressDay.text = "$elapsedDays"
                tvProgressHour.text = "$elapsedHours"
                tvProgressMinute.text = "$elapsedMinutes"
                tvProgressSecond.text = "$elapsedSeconds"
            }

            override fun onFinish() {
                tvProgressDay.text = "0"
                tvProgressHour.text = "0"
                tvProgressMinute.text = "0"
                tvProgressSecond.text = "0"
            }
        }.start()
    }

    fun stopTimer() {
        countDownTimer.cancel()
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlatTimer)
        val timerTextColor = typedArray.getColor(
            R.styleable.Timer_timerTextColor,
            ContextCompat.getColor(context, R.color.countdown_inside_text)
        )
        val countdownTextColor = typedArray.getColor(
            R.styleable.Timer_countdownTextColor,
            ContextCompat.getColor(context, R.color.countdown_header_text)
        )
        val fieldBackgroundColor = typedArray.getColor(
            R.styleable.FlatTimer_countdownFieldBgColor,
            ContextCompat.getColor(context, R.color.countdown_header_text)
        )
        val fieldStrokeColor = typedArray.getColor(
            R.styleable.FlatTimer_countdownFieldStrokeColor,
            ContextCompat.getColor(context, R.color.countdown_header_text)
        )

        typedArray.recycle()

        progressDay.strokeColor = fieldStrokeColor
        progressDay.backgroundTintList = ColorStateList.valueOf(fieldBackgroundColor)
        progressHour.strokeColor = fieldStrokeColor
        progressHour.backgroundTintList = ColorStateList.valueOf(fieldBackgroundColor)
        progressMinute.strokeColor = fieldStrokeColor
        progressMinute.backgroundTintList = ColorStateList.valueOf(fieldBackgroundColor)
        progressSecond.strokeColor = fieldStrokeColor
        progressSecond.backgroundTintList = ColorStateList.valueOf(fieldBackgroundColor)

        tvDay.setTextColor(countdownTextColor)
        tvHour.setTextColor(countdownTextColor)
        tvMinute.setTextColor(countdownTextColor)
        tvSecond.setTextColor(countdownTextColor)

        tvProgressDay.setTextColor(timerTextColor)
        tvProgressHour.setTextColor(timerTextColor)
        tvProgressMinute.setTextColor(timerTextColor)
        tvProgressSecond.setTextColor(timerTextColor)
    }
}