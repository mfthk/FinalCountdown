package com.mfatihk.finalcountdown

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.appcompat.app.AppCompatActivity
import com.mfatihk.finalcountdown.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d("activity", "FINAL COUNTDOWN ACTIVITY")
        val display: Display = this.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val widthPixels: Int = size.x
        //binding.timer.setup(screenWidth = widthPixels, endDate = 1705402960000, startDate = 1705402740000)
        binding.timer.setup(screenWidth = widthPixels, endDate = 1705406436000)
    }
}