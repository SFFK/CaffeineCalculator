package com.cookandroid.caffeinecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.caffeinecalculator.databinding.ActivityNotifyBinding

class NotifyActivity : AppCompatActivity() {
    // 바인딩 객체 선언
    private var mBinding : ActivityNotifyBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val view = binding.notifyView
        val viewItem = ArrayList<NotifyItem>()

        viewItem.add(NotifyItem("Beta Version 출시"))
        viewItem.add(NotifyItem("버그 수정"))
        viewItem.add(NotifyItem("UI 업데이트"))
        viewItem.add(NotifyItem("푸시 알림 추가"))
        viewItem.add(NotifyItem("정식 버전 출시 1.0.0 Version"))
        viewItem.add(NotifyItem("버그 수정"))

        val adapter = NotifyAdapter(viewItem)
        adapter.notifyDataSetChanged()

        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}