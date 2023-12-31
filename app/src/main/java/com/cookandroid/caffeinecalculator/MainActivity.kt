package com.cookandroid.caffeinecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cookandroid.caffeinecalculator.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    // 바인딩 객체 선언
    private var mBinding : ActivityMainBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    // items MutableList 객체 생성
    private var items: MutableList<CoffeeData> = mutableListOf()
    // 뒤로가기 연속 클릭 대기 시간
    private var backTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var bottom_btn = binding.navigationView
        // 네비게이션 버튼 이벤트
        bottom_btn.run { setOnItemSelectedListener {
            when(it.itemId) {
                // 홈버튼 클릭시
                R.id.home -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, homeFragment).commit()
                    
                    true
                }
                // left
                R.id.left -> {
                    val settingFragment = SettingFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, settingFragment).commit()
                    true
                }
                // right
                R.id.calendar -> {
                    val calendarFragment = CalendarFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, calendarFragment).commit()
                    true
                }
                else -> false
            }
        }
            // 처음에 나타날 화면
            selectedItemId = R.id.home
        }
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis()  - backTime >= 2000) {
            backTime = System.currentTimeMillis()
            Snackbar.make(binding.root, "앱을 종료하겠습니까?", Snackbar.LENGTH_LONG).apply {
                anchorView = binding.navigationView
            }.show()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}