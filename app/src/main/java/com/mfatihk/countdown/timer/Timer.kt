package com.mfatihk.countdown.timer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mfatihk.countdown.R
import com.mfatihk.countdown.databinding.ViewCountdownTimerBinding
import com.mfatihk.countdown.helper.DateTimeAdapter
import java.util.Date
import java.util.TimeZone

class Timer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding: ViewCountdownTimerBinding =
        ViewCountdownTimerBinding.inflate(LayoutInflater.from(context), this, true)

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

    var elapsedDays: Long = 0
    var elapsedHours: Long = 0
    var elapsedMinutes: Long = 0
    var elapsedSeconds: Long = 0


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

    fun setup(endDate: Long = 0L, screenWidth: Int) {
        if(screenWidth<730){
            updateTimerSize(200)
        } else if(screenWidth<1100){
            updateTimerSize(300)
        }

        val currentDate = Date()
        val oldDate: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
            DateTimeAdapter("yyyy-MM-dd HH:mm:ss").toJson(Date(endDate + TimeZone.getDefault().getOffset(endDate))))
        var diff: Long = oldDate.time - currentDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        if(diff > 0) {
            elapsedDays = diff / daysInMilli
            diff %= daysInMilli
            elapsedHours = diff / hoursInMilli
            diff %= hoursInMilli
            elapsedMinutes = diff / minutesInMilli
            diff %= minutesInMilli
            elapsedSeconds = diff / secondsInMilli

            // First animation will be used to complete remaining seconds of the first minute. After that 60 seconds animation will be started.
            val secondAnimation = ObjectAnimator.ofInt(progressSecond, "progress", (60-elapsedSeconds).toInt(), 60)
            tvProgressSecond.text = (60-elapsedSeconds).toString()
            secondAnimation.duration = 1000*(elapsedSeconds)
            secondAnimation.interpolator = LinearInterpolator()
            secondAnimation.start()
            secondAnimation.repeatCount = ValueAnimator.INFINITE

            // After first animation completed new animation which has one minute total process will start and it will repeat itself until the countdown is over.
            val secondInitialAnimation = ObjectAnimator.ofInt(progressSecond, "progress", 0, 60)
            secondInitialAnimation.repeatCount = ValueAnimator.INFINITE
            secondInitialAnimation.duration = 60000
            secondInitialAnimation.interpolator = LinearInterpolator()

            initializeTimers()

            secondAnimation.addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                if(tvProgressSecond != null)
                    tvProgressSecond.text = (60-progress).toString()
            }
            secondInitialAnimation.addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                if(tvProgressSecond != null)
                    tvProgressSecond.text = (60-progress).toString()
            }
            secondAnimation.addListener(object : AnimatorListenerAdapter() {
                // If first animation is the last minute of the countdown then it will stop the timer
                // Otherwise new animation which has one minute duration will start
                // However, in both cases first animation (secondAnimation) should be stopped
                override fun onAnimationRepeat(animation: Animator) {
                    if(elapsedDays == 0L && elapsedHours == 0L && elapsedMinutes == 0L) {
                        elapsedSeconds = 0
                        progressMinute.progress = 60
                        tvProgressMinute.text = "0"
                        elapsedMinutes = 0
                        secondAnimation.removeAllListeners()
                    } else {
                        checkRemainingTime()
                        secondInitialAnimation.start()
                    }
                    secondAnimation.end()
                    super.onAnimationRepeat(animation)
                }
            })

            secondInitialAnimation.addListener(object : AnimatorListenerAdapter() {
                // This animation will called after the first one is finished
                override fun onAnimationRepeat(animation: Animator) {
                    if(elapsedDays == 0L && elapsedHours == 0L && elapsedMinutes == 0L) {
                        //elapsedSeconds = 0
                        progressMinute.progress = 60
                        tvProgressMinute.text = "0"
                        elapsedMinutes = 0
                        secondInitialAnimation.end()
                        secondInitialAnimation.removeAllListeners()
                    } else {
                        checkRemainingTime()
                    }
                    secondAnimation.end()
                    super.onAnimationRepeat(animation)
                }
            })
        } else {
            tvProgressSecond.text = "0"
            initializeTimers()
        }
    }

    private fun checkRemainingTime () {
        if(elapsedMinutes <= 0 ) {
            elapsedMinutes = 59
            //elapsedHours-= 1
            if(elapsedHours <= 0) {
                elapsedHours = 23
                elapsedDays-= 1
            } else {
                elapsedHours-=1
            }
        } else {
            elapsedMinutes -= 1
        }
        initializeTimers()
    }

    private fun initializeTimers() {
        progressMinute.progress = (60-elapsedMinutes).toInt()
        tvProgressMinute.text = elapsedMinutes.toInt().toString()
        progressHour.progress = (24-elapsedHours).toInt()
        tvProgressHour.text = elapsedHours.toInt().toString()
        progressDay.progress = if(elapsedDays > 30) 1 else (30-elapsedDays).toInt()
        tvProgressDay.text = elapsedDays.toInt().toString()
    }

    private fun updateTimerSize(size: Int) {
        listOf(progressDay, progressHour, progressMinute, progressSecond).forEach { countDown ->
            countDown.layoutParams.height = size
            countDown.layoutParams.width = size
        }
    }
}