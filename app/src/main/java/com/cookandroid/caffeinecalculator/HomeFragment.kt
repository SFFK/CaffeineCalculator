package com.cookandroid.caffeinecalculator

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.cookandroid.caffeinecalculator.databinding.FragmentHomeBinding
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
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

        var nowDay = getDate()

        // 엑셀 불러오기
        readExcelFileFromAssets()

        // mutableList 객체 생성, String 객체 생성
        var brand : MutableList<String> =  mutableListOf()
        var coffee : MutableList<String> = mutableListOf()
        var size : MutableList<String> = mutableListOf()
        var caffeine = "0"

        // 기록을 위한 sharedPreferences 객체 생성
        val sharedPreferences = context?.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        // 브랜드 초기화
        fun clearBrand() {
            brand.clear()
            brand.add(0,"브랜드명을 선택하세요.")
        }

        // 커피 초기화
        fun clearCoffee() {
            coffee.clear()
            coffee.add(0,"커피를 선택하세요.")
        }

        // 사이즈 초기화
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
        var custombtn = binding.customBtn
        var resetbtn = binding.resetBtn
        var sc = binding.showCaffeine
        var sp = binding.showPercent

        // 퍼센트별로 텍스트 색상 구현
        fun textColor() {
            // 카페인량이 90 미만일 경우
            if(sp.text.toString().toInt() < 90) {
                sp.setTextColor(Color.parseColor("#a52a2a"))
            }
            // 아닐 경우
            else {
                sp.setTextColor(Color.RED)
            }
        }

        // 앱 구동시 초기에 보이는 화면
        fun start() {
            // 스피너 시작위치 지정
            spinner.setSelection(0)
            spinner2.setSelection(0)
            spinner3.setSelection(0)

            // 카페인 퍼센트와 총 함량
            caffeine = sharedPreferences?.getString("caffeine $nowDay", "0")!!
            sc.text = caffeine
            sp.text = (caffeine?.toDouble()!! / 400 * 100).toInt().toString()

            // 텍스트 색상
            textColor()
        }

        // 초기화 함수
        fun reset() {
            // 스피너 시작위치 지정
            spinner.setSelection(0)
            spinner2.setSelection(0)
            spinner3.setSelection(0)

            // sharedPreferences 초기화
            editor?.clear()?.apply()
            caffeine = sharedPreferences?.getString("caffeine $nowDay", "0")!!
            sc.text = "0"
            sp.text = "0"
        }

        // 추가 버튼
        addbtn.setOnClickListener {
            for(i in 0 until items.size) {
                if(spinner.selectedItem == items[i].brand && spinner2.selectedItem == items[i].coffee && spinner3.selectedItem == items[i].size) {
                    // 카페인양 누적
                    val total = caffeine.toDouble() + items[i].caffeine
                    // total 소수 첫번째 자리까지 출력
                    val Dtotal = DecimalFormat("#.#").format(total)

                    // sharedPreferences 카페인양 입력 후 다시 값 불러오기
                    editor?.putString("caffeine $nowDay", Dtotal)?.apply()
                    caffeine = sharedPreferences?.getString("caffeine $nowDay", "0")!!
                    addText("$nowDay.txt", items[i].brand, items[i].coffee, items[i].size, items[i].caffeine)

                }
            }

            sc.text = caffeine
            sp.text = (caffeine?.toDouble()!! / 400 * 100).toInt().toString()

            textColor()
        }

        // 직접추가 버튼
        custombtn.setOnClickListener {
            val dlg = activity?.let { AddDialog(it) }
            dlg!!.show()
            // 커스텀다이얼로그 값 가져오기 interface 이용
            dlg.setOnClickedListener(object : AddDialog.addClickListener {
                override fun onClicked(item: CoffeeData) {
                    // 카페인양 누적
                    val total = caffeine.toDouble() + item.caffeine
                    // total 소수 첫번째 자리까지 출력
                    val Dtotal = DecimalFormat("#.#").format(total)

                    // sharedPreferences 카페인양 입력 후 다시 값 불러오기
                    editor?.putString("caffeine $nowDay", Dtotal)?.apply()
                    caffeine = sharedPreferences?.getString("caffeine $nowDay", "0")!!
                    addText("$nowDay.txt", item.brand, item.coffee, item.size, item.caffeine)

                    sc.text = caffeine
                    sp.text = (caffeine?.toDouble()!! / 400 * 100).toInt().toString()

                    textColor()
                }
            })
        }



        // 초기화 버튼
        resetbtn.setOnClickListener {
            reset()
            removeText("$nowDay.txt")
        }

        start()

        return mBinding?.root
    }

    // 오늘 날짜 받아오기
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")

        return current.format(formatter)
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

    // 채널 생성
    private fun createNotificationChannel(channelId : String, channelName : String, channelDescription : String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // NotificationManager 객체 생성
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Notification Channel 아이디, 이름, 설명, 중요도 설정
            val channelId = channelId
            val channelName = channelName
            val channelDescription = channelDescription
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            // NotificationChannel 객체 생성
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            // 설명 설정
            notificationChannel.description = channelDescription

            // 채널에 불빛 설정
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            // 채널 진동 설정
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100L, 200L, 300L)

            // 시스템에 notificationChannel 등록
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    // 푸시 알림 생성
    private fun createNotification(channelId : String, content : String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API Level 26(O) 이상에서는 Builder 생성자에 NotificationChannel의 아이디값을 설정
            val notificationCompatBuilder = context?.let { NotificationCompat.Builder(it, channelId)
                .setSmallIcon(R.drawable.coffeebins)
                .setContentTitle("Caffeine Record")
                .setContentText()
            }

        } else {
            // 26버전 미만은 생성자에 context만 설정
            val notificationCompatBuilder = context?.let { NotificationCompat.Builder(it) }
        }

        notificationCompatBuilder?.let { it ->
            // 작은 아이콘 설정
            it.setSmallIcon(R.drawable.coffeebins)
            // 시간 설정
            it.setWhen(System.currentTimeMillis())
            // 알림 메시지 설정
            it.setContentTitle("Content Title")
            // 알림 내용 설정
            it.setContentText("Content Message")
            // 알림과 동시에 진동 설정(권한 필요(
            it.setDefaults(Notification.DEFAULT_VIBRATE)
            // 스와이프를 해도 삭제 안되게 설정
            it.setOngoing(true)
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