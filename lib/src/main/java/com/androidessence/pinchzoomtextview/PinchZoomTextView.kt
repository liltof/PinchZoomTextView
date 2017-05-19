package com.androidessence.pinchzoomtextview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView

/**
 * TextView that increases/decreases font size as it is pinched/zoomed.
 *
 * Created by adam.mcneilly on 5/19/17.
 */
class PinchZoomTextView(context: Context, attrs: AttributeSet? = null): TextView(context, attrs) {

    /**
     * The ratio of the text size compared to its original.
     */
    private var ratio: Float = 1.0F

    /**
     * The distance between two pointers when they are first placed on the screen
     */
    private var baseDistance: Int = 0

    /**
     * The ratio of the text size when the gesture is started.
     */
    private var baseRatio: Float = 0.0F

    /**
     * Boolean flag for whether or not zoom feature is enabled.
     */
    var zoomEnabled: Boolean = true

    /**
     * Handles the touch event by the user and determines whether font size should grow,
     * and by how much.
     *
     * If the action is simply `POINTER_DOWN` it means the user is just setting their fingers down,
     * so collect base values.
     *
     * Otherwise, the user is pinching, so get the distance between the pointers, find the ratio
     * we need, and set the text size. Note: based on an initial size of 13, and can't exceed a ratio
     * of 1024.
     *
     * Inspiration taken from: http://stackoverflow.com/a/20303367/3131147
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> paintFlags = paintFlags.or(Paint.LINEAR_TEXT_FLAG.or(Paint.SUBPIXEL_TEXT_FLAG))
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> paintFlags = paintFlags.and(Paint.LINEAR_TEXT_FLAG.or(Paint.SUBPIXEL_TEXT_FLAG).inv())
        }

        // Must have two gestures
        if (zoomEnabled && event?.pointerCount == 2) {
            val action = event.action
            val distance = getDistance(event)
            val pureAction = action.and(MotionEvent.ACTION_MASK)

            if (pureAction == MotionEvent.ACTION_POINTER_DOWN) {
                baseDistance = distance
                baseRatio = ratio
            } else {
                val delta = (distance - baseDistance) / STEP
                val multi = Math.pow(2.0, delta.toDouble()).toFloat()
                ratio = Math.min(1024.0F, Math.max(0.1F, baseRatio * multi))
                textSize = ratio + 13
            }
        }

        return true
    }

    /**
     * Returns the distance between to pointers on the screen.
     */
    private fun getDistance(event: MotionEvent): Int {
        val dx = event.getX(0) - event.getX(1)
        val dy = event.getY(0) - event.getY(1)
        val magnitude = (dx * dx + dy * dy).toDouble()

        return Math.sqrt(magnitude).toInt()
    }

    companion object {
        /**
         * Consider each "step" between the two pointers as 200px. In other words, the TV size will grow every 200 pixels.
         */
        private val STEP: Float = 200.0F
    }
}