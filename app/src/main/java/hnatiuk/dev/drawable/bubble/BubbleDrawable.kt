package hnatiuk.dev.drawable.bubble

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF

/**
 * side - left, top, bottom, end
 * percentage - position in percent
 * margin - margin in dp
 * arrangement - left, end
 *
 */
class BubbleDrawable(
  private val context: Context,
  private val resId: Int,
  private val side: BubbleTailSide,
  private val position: BubbleTailPosition,
  private val sideMargin: Float = 0f,
  private val cornerRadius: Float = 0f,
  private val color: Int = Color.DKGRAY
) : Drawable() {

  private val paint = Paint().apply {
    color = this@BubbleDrawable.color
  }

  private fun getTailRect(rect: Rect, drawable: Drawable): Rect {
    val startPosition = (if (side.isVerticalSide) rect.left else rect.top) + sideMargin
    val endPosition = (if (side.isVerticalSide) rect.right else rect.bottom) - drawable.intrinsicWidth - sideMargin
    val start = (startPosition + (endPosition - startPosition) * position.percentage).toInt()

    return when (side) {
      BubbleTailSide.LEFT,
      BubbleTailSide.RIGHT
        -> {
        Rect(
          if (side == BubbleTailSide.RIGHT) rect.right - drawable.intrinsicWidth else rect.left,
          start,
          if (side == BubbleTailSide.RIGHT) rect.right else rect.left + drawable.intrinsicWidth,
          start + drawable.intrinsicHeight
        )
      }
      BubbleTailSide.TOP,
      BubbleTailSide.BOTTOM
        -> {
        Rect(
          start,
          if (side == BubbleTailSide.TOP) rect.top else rect.bottom - drawable.intrinsicHeight,
          start + drawable.intrinsicWidth,
          if (side == BubbleTailSide.TOP) rect.top + drawable.intrinsicHeight else rect.bottom
        )
      }
    }
  }

  class RotationInfo(val degree: Float, val px: Float, val py: Float)

  private fun getTailRotation(drawableRect: Rect, drawable: Drawable): RotationInfo? {
    return when (side) {
      BubbleTailSide.LEFT -> {
        RotationInfo(
          degree = 90f,
          px = drawableRect.left + drawable.intrinsicHeight / 2f,
          py = drawableRect.top + drawable.intrinsicHeight / 2f
        )
      }
      BubbleTailSide.TOP -> {
        RotationInfo(
          degree = 180f,
          px = drawableRect.left + drawable.intrinsicWidth / 2f,
          py = drawableRect.top + drawable.intrinsicHeight / 2f
        )
      }
      BubbleTailSide.RIGHT -> {
        RotationInfo(
          degree = 270f,
          px = drawableRect.right - drawable.intrinsicHeight / 2f,
          py = drawableRect.top + drawable.intrinsicHeight / 2f
        )
      }
      BubbleTailSide.BOTTOM -> null
    }
  }

  private fun getBodyRect(rect: Rect, drawableIntrinsicHeight: Int): RectF {
    return Rect(
      rect.left + if (side == BubbleTailSide.LEFT) drawableIntrinsicHeight else 0,
      rect.top + if (side == BubbleTailSide.TOP) drawableIntrinsicHeight else 0,
      rect.right - if (side == BubbleTailSide.RIGHT) drawableIntrinsicHeight else 0,
      rect.bottom - if (side == BubbleTailSide.BOTTOM) drawableIntrinsicHeight else 0
    ).toRectF()
  }

  override fun draw(canvas: Canvas) {
    val drawable = ContextCompat.getDrawable(context, resId) ?: return

    val rect = Rect(bounds)
    val bodyRect = getBodyRect(rect, drawable.intrinsicHeight)
    val drawableRect = getTailRect(rect, drawable)
    val drawableRotation = getTailRotation(drawableRect, drawable)

    // draw body
    canvas.drawRoundRect(bodyRect.left, bodyRect.top, bodyRect.right, bodyRect.bottom, cornerRadius, cornerRadius, paint)

    // draw tail
    drawable.bounds = drawableRect
    drawable.colorFilter = PorterDuffColorFilter(paint.color, PorterDuff.Mode.SRC_IN)
    canvas.save()
    if (drawableRotation != null) {
      canvas.rotate(drawableRotation.degree, drawableRotation.px, drawableRotation.py)
    }
    drawable.draw(canvas)
    canvas.restore()
  }

  override fun setAlpha(alpha: Int) {
    paint.alpha = alpha
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    paint.colorFilter = colorFilter
  }

  @Suppress("OVERRIDE_DEPRECATION")
  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}