package com.androidessence.pinchzoomtextview.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.androidessence.pinchzoomtextview.PinchZoomTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById(R.id.text_view) as PinchZoomTextView

        val enableToggle = findViewById(R.id.zoom_toggle) as Button
        enableToggle.setOnClickListener {
            val newEnabledState = !textView.zoomEnabled
            textView.zoomEnabled = newEnabledState
            enableToggle.text = if (newEnabledState) getString(R.string.zoom_enabled) else getString(R.string.zoom_disabled)
        }
    }
}
