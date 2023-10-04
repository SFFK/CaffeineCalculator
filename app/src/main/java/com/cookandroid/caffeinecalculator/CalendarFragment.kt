package com.cookandroid.caffeinecalculator

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.caffeinecalculator.databinding.FragmentCalendarBinding
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.io.FileInputStream

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {
    private var mBinding : FragmentCalendarBinding? = null
    private lateinit var itemList : MutableList<String>
    private lateinit var coffeeAdapter : CoffeeAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentCalendarBinding.inflate(inflater, container, false)
        mBinding = binding

        val re_view = binding.todayDrink
        itemList = mutableListOf<String>()

        val calendar = binding.calendarView
        val title = binding.today

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
            coffeeAdapter.notifyDataSetChanged()
        }

        coffeeAdapter = CoffeeAdapter(itemList)

        re_view.adapter = coffeeAdapter
        re_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return mBinding?.root
    }

    // 데이터를 보여주는 함수
    fun showText(day : String) {
        val fileInputStream : FileInputStream
        itemList.clear()
        try {
            fileInputStream = activity?.openFileInput("$day.txt")!!
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            val content = String(fileData).also {
                itemList.add(it)
            }



        } catch (e : java.lang.Exception) {
            e.printStackTrace()
        }
    }
}