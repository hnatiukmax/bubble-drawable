package hnatiuk.dev.drawable

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hnatiuk.dev.drawable.bubble.BubbleDrawable
import hnatiuk.dev.drawable.bubble.BubbleTailPosition
import hnatiuk.dev.drawable.bubble.BubbleTailSide
import hnatiuk.dev.drawable.databinding.ActivityMainBinding
import hnatiuk.dev.drawable.databinding.ItemDrawableBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.apply {
      val configList = listOf(
        BubbleConfig(
          side = BubbleTailSide.BOTTOM,
          position = BubbleTailPosition.Fixed(BubbleTailPosition.Fixed.Type.START),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.BLUE
        ),
        BubbleConfig(
          side = BubbleTailSide.BOTTOM,
          position = BubbleTailPosition.Percentage(value = 0.4f),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.DKGRAY
        ),
        BubbleConfig(
          side = BubbleTailSide.TOP,
          position = BubbleTailPosition.Fixed(BubbleTailPosition.Fixed.Type.CENTER),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.BLACK
        ),
        BubbleConfig(
          side = BubbleTailSide.TOP,
          position = BubbleTailPosition.Percentage(0.8f),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.LTGRAY
        ),
        BubbleConfig(
          side = BubbleTailSide.LEFT,
          position = BubbleTailPosition.Fixed(BubbleTailPosition.Fixed.Type.END),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.YELLOW
        ),
        BubbleConfig(
          side = BubbleTailSide.LEFT,
          position = BubbleTailPosition.Percentage(0.5f),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.GREEN
        ),
        BubbleConfig(
          side = BubbleTailSide.RIGHT,
          position = BubbleTailPosition.Fixed(BubbleTailPosition.Fixed.Type.CENTER),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.CYAN
        ),
        BubbleConfig(
          side = BubbleTailSide.RIGHT,
          position = BubbleTailPosition.Percentage(0.1f),
          marginSide = dpToPx(16f),
          cornerSide = dpToPx(12f),
          color = Color.MAGENTA
        )
      )

      configList.forEachIndexed { index, bubbleConfig ->
        val view = ItemDrawableBinding.inflate(LayoutInflater.from(this@MainActivity), null, false).apply {
          title.text = bubbleConfig.text
          bubble.background = BubbleDrawable(
            context = this@MainActivity,
            resId = R.drawable.ic_bubble_tail,
            color = bubbleConfig.color,
            side = bubbleConfig.side,
            position = bubbleConfig.position,
            sideMargin = bubbleConfig.marginSide,
            cornerRadius = bubbleConfig.cornerSide
          )
        }
        binding.container.addView(
          view.root,
          LayoutParams().apply {
            width = 0
            height = LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
          }
        )
      }
    }

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }
  }

  data class BubbleConfig(
    val side: BubbleTailSide,
    val position: BubbleTailPosition,
    val marginSide: Float,
    val cornerSide: Float,
    val color: Int
  ) {

    val text: String
      get() = "side: $side\nposition: $position"
  }

  private fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
  }
}