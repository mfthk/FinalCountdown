package com.mfatihk.finalcountdown

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

    init {
        tvProgressDay = binding.tvProgressDay
        tvProgressHour = binding.tvProgressHour
        tvProgressMinute = binding.tvProgressMinute
        tvProgressSecond = binding.tvProgressSecond
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
}