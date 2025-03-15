package hnatiuk.dev.drawable.bubble

val BubbleTailSide.isVerticalSide: Boolean
  get() = this == BubbleTailSide.TOP || this == BubbleTailSide.BOTTOM

enum class BubbleTailSide {

  LEFT,
  TOP,
  RIGHT,
  BOTTOM;
}