package com.cookandroid.caffeinecalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.cookandroid.caffeinecalculator.databinding.ActivityUseBinding
import kotlinx.android.synthetic.main.activity_use.*

class UseActivity : AppCompatActivity() {
    // 바인딩 객체 선언
    private var mBinding : ActivityUseBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    // 뷰가 몇퍼센트로 줄어들 것인지 수치
    private val MIN_SCALE = 0.85f
    // 뷰 투명도
    private val MIN_ALPHA = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뷰페이저 어댑터
        viewPager.adapter = ViewPagerAdapter(getItemList())
        // 방향을 가로로
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        // 애니메이션 적용
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        // indicator 적용
        binding.indicator.setViewPager(viewPager)
    }

    // 아이템리스트 가져오기
    private fun getItemList() : ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.usefirst , R.drawable.usesecond, R.drawable.usethird)
    }

    // 이미지를 넘길때 애니메이션 효과
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.apply {
                val pageWidth = width
                val pageHeight = height

                when {
                    position < -1 -> {
                        alpha = 0f
                    }

                    position <= 1 -> {
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> {
                        alpha = 0f
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}