package hnatiuk.dev.drawable.bubble

import androidx.annotation.FloatRange
import hnatiuk.dev.drawable.bubble.BubbleTailPosition.Fixed

sealed interface BubbleTailPosition {

  data class Percentage(
    @FloatRange(from = .0, to = 1.0) val value: Float
  ) : BubbleTailPosition

  data class Fixed(val type: Type) : BubbleTailPosition {

    enum class Type {

      START,
      CENTER,
      END;
    }
  }
}

fun Fixed.asPercentage(): BubbleTailPosition.Percentage {
  val value =  when (type) {
    Fixed.Type.START -> 0f
    Fixed.Type.CENTER -> 0.5f
    Fixed.Type.END -> 1f
  }
  return BubbleTailPosition.Percentage(value)
}

val BubbleTailPosition.percentage: Float
  get() = when (this) {
    is BubbleTailPosition.Percentage -> value
    is BubbleTailPosition.Fixed -> asPercentage().value
  }