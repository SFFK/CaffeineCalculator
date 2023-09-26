package com.cookandroid.caffeinecalculator

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.cookandroid.caffeinecalculator.databinding.FragmentRightBinding
import kotlinx.android.synthetic.main.fragment_right.view.*
import java.io.FileInputStream

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {
    private var mBinding : FragmentRightBinding? = null
    lateinit var EditContext : TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentRightBinding.inflate(inflater, container, false)
        mBinding = binding

        EditContext = binding.content
        var calendar = binding.calendarView
        var title = binding.today

        // 오늘 날짜 받아오기
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
        val formatted = current.format(formatter)

        // 처음에 보여주는 화면
        title.text = "$formatted 섭취한 카페인 목록"
        showText(formatted)

        // 캘린더 이벤트
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDay = year.toString() + "년 " + (month + 1).toString() + "월 " + dayOfMonth.toString() + "일"
            title.text = "$selectedDay 섭취한 카페인 목록"
            showText(selectedDay)
        }

        return mBinding?.root
    }

    // 데이터를 보여주는 함수
    fun showText(day : String) {
        var fileInputStream : FileInputStream
        try {
            fileInputStream = activity?.openFileInput("$day.txt")!!
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            EditContext.text = String(fileData)
        } catch (e : java.lang.Exception) {
            e.printStackTrace()
        }
    }
}