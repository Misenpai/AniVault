package com.example.anivault.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator

class RevolvingProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var sweepAngle = 0f
    private val rectF = RectF()

    private val backgroundColor = Color.parseColor("#222222") // Darker gray
    private val circleColor = Color.parseColor("#444444") // Slightly lighter than background
    private val progressColor = Color.MAGENTA
    private val strokeWidth = 25f // Thick stroke
    private val gapAngle = 15f
    private val progressArcLength = 65f // Slightly longer to ensure overlap
    private val animationDuration = 1000L
    private val margin = 40f // Margin from edges

    private val cornerRadius: Float

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.strokeCap = Paint.Cap.ROUND
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)
        startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background with rounded corners
        paint.style = Paint.Style.FILL
        paint.color = backgroundColor
        val backgroundRect = RectF(
            0f,
            0f,
            width.toFloat(),
            height.toFloat()
        )
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, paint)

        // Draw progress indicator
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(width, height) / 2f) - strokeWidth / 2 - margin

        rectF.set(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius
        )

        // Draw main circle with gap
        paint.style = Paint.Style.STROKE
        paint.color = circleColor
        canvas.drawArc(rectF, sweepAngle + gapAngle / 2, 360f - gapAngle, false, paint)

        // Draw progress arc
        paint.color = progressColor
        val progressStartAngle = sweepAngle - gapAngle / 2
        canvas.drawArc(rectF, progressStartAngle, progressArcLength, false, paint)
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = animationDuration
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE

        animator.addUpdateListener { animation ->
            sweepAngle = animation.animatedValue as Float
            invalidate()
        }

        animator.start()
    }
}