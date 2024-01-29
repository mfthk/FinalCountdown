package com.mfatihk.countdown.timer

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mfatihk.countdown.R
import com.mfatihk.countdown.databinding.ViewCountdownTimerBinding
import com.mfatihk.countdown.helper.DateTimeAdapter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class Timer(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context, attrs) {
    private var binding: ViewCountdownTimerBinding =
        ViewCountdownTimerBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var countDownTimer: CountDownTimer

    private var timerLayout: LinearLayout = binding.timerLayout
    private var tvProgressDay: TextView = binding.tvProgressDay
    private var tvProgressHour: TextView = binding.tvProgressHour
    private var tvProgressMinute: TextView = binding.tvProgressMinute
    private var tvProgressSecond: TextView = binding.tvProgressSecond
    private var progressDay: ProgressBar = binding.progressDay
    private var progressHour: ProgressBar = binding.progressHour
    private var progressMinute: ProgressBar = binding.progressMinute
    private var progressSecond: ProgressBar = binding.progressSecond
    private var tvDay: TextView = binding.tvDay
    private var tvHour: TextView = binding.tvHour
    private var tvMinute: TextView = binding.tvMinute
    private var tvSecond: TextView = binding.tvSecond

    init {
        timerLayout = binding.timerLayout
        tvProgressDay = binding.tvProgressDay
        tvProgressHour = binding.tvProgressHour
        tvProgressMinute = binding.tvProgressMinute
        tvProgressSecond = binding.tvProgressSecond
        progressDay = binding.progressDay
        progressHour = binding.progressHour
        progressMinute = binding.progressMinute
        progressSecond = binding.progressSecond
        tvDay = binding.tvDay
        tvHour = binding.tvHour
        tvMinute = binding.tvMinute
        tvSecond = binding.tvSecond

        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Timer)
        val countdownBackgroundColor = typedArray.getColor(
            R.styleable.Timer_countdownBackgroundColor,
            ContextCompat.getColor(context, android.R.color.transparent)
        )
        val timerTextColor = typedArray.getColor(
            R.styleable.Timer_timerTextColor,
            ContextCompat.getColor(context, R.color.countdownInsideText)
        )
        val countdownTextColor = typedArray.getColor(
            R.styleable.Timer_countdownTextColor,
            ContextCompat.getColor(context, R.color.countdownHeaderText)
        )
        typedArray.recycle()

        timerLayout.setBackgroundColor(countdownBackgroundColor)

        tvDay.setTextColor(countdownTextColor)
        tvHour.setTextColor(countdownTextColor)
        tvMinute.setTextColor(countdownTextColor)
        tvSecond.setTextColor(countdownTextColor)

        tvProgressDay.setTextColor(timerTextColor)
        tvProgressHour.setTextColor(timerTextColor)
        tvProgressMinute.setTextColor(timerTextColor)
        tvProgressSecond.setTextColor(timerTextColor)
    }

    fun setup(endDate: Long, screenWidth: Int) {
        if(screenWidth<730){
            updateTimerSize(200)
        } else if(screenWidth<1100){
            updateTimerSize(300)
        }

        val currentTime = Date()
        val oldDate: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(
            DateTimeAdapter("yyyy-MM-dd HH:mm:ss").toJson(Date(endDate + TimeZone.getDefault().getOffset(endDate))))

        val difference: Long = oldDate.time - currentTime.time
        countDownTimer = object : CountDownTimer(difference, 1000) {
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

                progressDay.progress = if(elapsedDays > 30) 1 else (30-elapsedDays).toInt()
                progressHour.progress = (24-elapsedHours).toInt()
                progressMinute.progress = (60-elapsedMinutes).toInt()
                progressSecond.progress = (60-elapsedSeconds).toInt()
            }

            override fun onFinish() {
                tvProgressDay.text = "0"
                tvProgressHour.text = "0"
                tvProgressMinute.text = "0"
                tvProgressSecond.text = "0"
            }
        }.start()
    }

    private fun updateTimerSize(size: Int) {
        listOf(progressDay, progressHour, progressMinute, progressSecond).forEach { countDown ->
            countDown.layoutParams.height = size
            countDown.layoutParams.width = size
        }
    }
}