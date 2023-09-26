package com.cookandroid.caffeinecalculator

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.cookandroid.caffeinecalculator.databinding.FragmentHomeBinding
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.formula.functions.Today
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    // 바인딩 객체 선언
    private var mBinding : FragmentHomeBinding? = null
    // items MutableList 객체 생성
    private var items: MutableList<CoffeeData> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        mBinding = binding

        // 오늘 날짜 받아오기
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
        val formatted = current.format(formatter)

        // 첫번째 시트 엑셀 불러오기
        readExcelFileFromAssets()

        // mutableList 객체 생성, String 객체 생성
        var brand : MutableList<String> =  mutableListOf()
        var coffee : MutableList<String> = mutableListOf()
        var size : MutableList<String> = mutableListOf()
        var caffeine = "0"

        // 기록을 위한 sharedPreferences 객체 생성
        val sharedPreferences = context?.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        // 초기화 함수들
        fun clearBrand() {
            brand.clear()
            brand.add(0,"브랜드명을 선택하세요.")
        }

        fun clearCoffee() {
            coffee.clear()
            coffee.add(0,"커피를 선택하세요.")
        }

        fun clearSize() {
            size.clear()
            size.add(0,"사이즈를 선택하세요.")
        }

        // 초기화 후 브랜드명 추가
        clearBrand()
        for(i in 0 until items.size - 1) {
            brand.add(items[i].brand)
        }

        // 중복 데이터 제거
        brand = brand.distinct() as MutableList<String>

        // 어뎁터에 등록
        var adapter =
            activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, brand) }
        var spinner = binding.brand
        spinner.adapter = adapter

        var adapter2 =
            activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, coffee) }
        var spinner2 = binding.coffee
        spinner2.adapter = adapter2

        var adapter3 =
            activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, size) }
        var spinner3 = binding.size
        spinner3.adapter = adapter3

        // 스피너 시작위치 지정
        spinner.setSelection(0)
        spinner2.setSelection(0)
        spinner3.setSelection(0)

        // 스피너 이벤트
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 초기화 후 MutableList 생성
                clearCoffee()
                spinner2.setSelection(0)
                spinner3.setSelection(0)
                var coffeeList : MutableList<String> = mutableListOf()

                // 선택한 브랜드명과 해당 브랜드명이 일치하는 커피목록을 MutableList에 추가
                for(i in 0 until items.size) {
                    if(items[i].brand == brand[position]) {
                        coffeeList.add(items[i].coffee)
                    }
                }

                // 중복을 제거한 뒤 데이터 추가
                coffee.addAll(coffeeList.distinct())
                adapter2?.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 초기화 후 MutableList 생성
                clearSize()
                spinner3.setSelection(0)
                var sizeList : MutableList<String> = mutableListOf()

                // 첫번째 스피너에서 선택한 브랜드명과 선택한 메뉴와 동일한 브랜드명인 커피의 사이즈를 MutableList에 추가
                for(i in 0 until items.size) {
                    if(items[i].brand == spinner.selectedItem && items[i].coffee == coffee[position]) {
                        sizeList.add(items[i].size)
                    }
                }

                // 중복을 제거한 뒤 데이터 추가
                size.addAll(sizeList.distinct())
                adapter3?.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        var addbtn = binding.addBtn
        var resetbtn = binding.resetBtn
        var sc = binding.showCaffeine
        var sp = binding.showPercent

        // 실행시 보여지는 카페인 퍼센트와 총 함량
        caffeine = sharedPreferences?.getString("caffeine", "0")!!
        sc.text = caffeine
        sp.text = (caffeine?.toDouble()!! / 400 * 100).toInt().toString()

        // 버튼 이벤트
        addbtn.setOnClickListener {
            for(i in 0 until items.size) {
                if(spinner.selectedItem == items[i].brand && spinner2.selectedItem == items[i].coffee && spinner3.selectedItem == items[i].size) {
                    // 카페인양 누적
                    val total = caffeine.toDouble() + items[i].caffeine
                    // total 소수 첫번째 자리까지 출력
                    val Dtotal = DecimalFormat("#.#").format(total)

                    // sharedPreferences 카페인양 입력 후 다시 값 불러오기
                    editor?.putString("caffeine", Dtotal)?.apply()
                    caffeine = sharedPreferences?.getString("caffeine", "0")!!
                    addText("$formatted.txt", items[i].brand, items[i].coffee, items[i].size, items[i].caffeine)

                }
            }

            sc.text = caffeine
            sp.text = (caffeine?.toDouble()!! / 400 * 100).toInt().toString()

            // 카페인량이 50 미만일 경우
            if(sp.text.toString().toInt() < 50) {
                sp.setTextColor(Color.BLUE)
            }
            // 아닐 경우
            else {
                sp.setTextColor(Color.RED)
            }
        }

        // 초기화 버튼
        resetbtn.setOnClickListener {
            editor?.clear()?.apply()
            caffeine = sharedPreferences?.getString("caffeine", "0")!!
            sc.text = "0"
            sp.text = "0"
            removeText("$formatted.txt")
        }


        return mBinding?.root
    }

    // 캘린더에 마신 음료와 카페인 추가 하는 함수
    fun addText(day : String, brand : String, coffee : String, size : String, caffeine : Double) {
        var fileOutputStream : FileOutputStream
        try {
            fileOutputStream = activity?.openFileOutput(day, Context.MODE_APPEND)!!
            val content = "$brand / $coffee / $size / $caffeine\n"
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e : java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // 기록을 지우는 함수
    fun removeText(day : String) {
        var fileOutputStream : FileOutputStream
        try {
            fileOutputStream =  activity?.openFileOutput(day, Context.MODE_PRIVATE)!!
            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e : java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // 엑셀 데이터 가져오기
    private fun readExcelFileFromAssets() {
        try {
            // 엑셀 열기
            val myInput = context?.assets?.open("coffee.xls")
            // POI File System 객체 만들기
            val myFileSystem = POIFSFileSystem(myInput)
            // 워크 북 객체 만들기
            val myWorkBook = HSSFWorkbook(myFileSystem)
            // 워크북에서 시트 가져오기
            val sheet = myWorkBook.getSheetAt(0)

            // 행 반복 변수
            val rowIter = sheet.rowIterator()
            // 행 번호
            var rowno = 0

            //행 반복문
            while (rowIter.hasNext()) {
                val myRow = rowIter.next() as HSSFRow
                if (rowno != 0) {
                    //열을 반복할 변수 만들어주기
                    val cellIter = myRow.cellIterator()
                    //열 번호
                    var colno = 0
                    // 변수 생성
                    var brand = ""
                    var coffee = ""
                    var size = ""
                    var caffeine = 0.0

                    //열 반복문
                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next() as HSSFCell
                        if (colno == 0) {
                            brand = myCell.toString()
                        } else if (colno == 1) {
                            coffee = myCell.toString()
                        } else if (colno == 2) {
                            size = myCell.toString()
                        } else if (colno == 3) {
                            caffeine = myCell.toString().toDouble()
                        }
                        colno++
                    }
                    items.add(CoffeeData(brand, coffee, size, caffeine))
                }
                rowno++
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "에러 발생", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}