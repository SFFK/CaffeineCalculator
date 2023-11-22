package com.cookandroid.caffeinecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cookandroid.caffeinecalculator.databinding.ActivityUseBinding

class UseActivity : AppCompatActivity() {
    // 바인딩 객체 선언
    private var mBinding : ActivityUseBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}